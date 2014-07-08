/**
 * PaMDA Project
 * PACKAGE - pamda.io.d3m
 * RDFSReader.java                          
 * @author Cl���udia Antunes (claudia.antunes@ist.utl.pt)
 * 2009
 */
package d3m.span.ontologies.io;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.jrdf.graph.*;
import org.jrdf.graph.mem.*;
import org.jrdf.parser.StatementHandler;
import org.jrdf.parser.rdfxml.RdfXmlParser;
import org.jrdf.util.ClosableIterator;
import d3m.span.ontologies.*;
import d3m.span.ontologies.kb.Feature;

/**
 * Class <B>RDFSReader</B> for reading ontologies in the context of D3M.
 * @author Claudia Antunes
 * @version 1.0
 */
public class RDFSReader implements OntologyReader {
	/* RDF pre-defined properties. */
	private static String RDFS_URI = "http://www.w3.org/2000/01/rdf-schema#";

	private static String RDF_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	
	private static String A_URI = "http://protege.stanford.edu/system#";

	private final String P_SUBCLASS_OF = "subClassOf";

	private final String P_DOMAIN = "domain";

	private final String P_RANGE = "range";

	private final String P_OVERRIDDENPROP = "overriddenProperty";

	private final String P_VALUES = "values";

	/* RDF pre-defined nodes . */
	private final String N_CLASS = "Class";

	private final String N_PROPERTY = "Property";

	private final String N_LITERAL = "Literal";

	private final String N_INTEGER = "integer";
	
	private final String N_STRING = "string";
	
	/** ******* FIELDS */
	private String m_filename;
	private String m_simpleFilename;
	
	
	/**
	 * Creates a reader for the file received.
	 * @param filename the name of the file with the RDFS graph
	 */
	public RDFSReader(String filename) {
		m_filename = filename;
	}
		
	/**
	 * Updates the ontology from an RDFS file
	 * @param onto the ontology to update
	 * @throws Exception 
	 */
	@Override
	public void updateOntology(Ontology onto) throws Exception {
		final Graph graph = readFile(m_filename, RDFS_URI);
		System.out.println("ONTOLOGY --> RDFS file read.");
		ArrayList<ArrayList<Triple>> triples = processRDFSFile(graph);
		
		System.out.println("ONTOLOGY --> it is going to create elements...");
		onto.setConcepts(createConcepts(triples.get(0)));
		
		System.out.println("ONTOLOGY --> it is going to fill attributes and assign them to concepts...");
		fillAttributes(triples.get(2),triples.get(3),triples.get(4), onto);
		
		System.out.println("ONTOLOGY --> it is going to create taxonomy...");
		createTaxonomy(triples.get(1), onto);
		
		System.out.println("ONTOLOGY --> it is going to fill Overridden values...");
		fillOverriddenValues(triples.get(5), graph, onto);
		triples.clear();

	}

	/**
	 * Read a RDFS file, returning a vector of RDF triples (subject, predicate,
	 * object).
	 * @param filename the name of the file with the RDFS graph
	 * @param baseURI 
	 * @throws Exception
	 * @return the graph that represents the RDFS file
	 */
	private Graph readFile(String filename, String baseURI) throws Exception {
			// Reads the ontology from a RDF file
			FileReader file = new FileReader(filename);
			final Graph jrdfMem = new GraphImpl();
			RdfXmlParser parser = new RdfXmlParser(jrdfMem.getElementFactory());
			parser.setStatementHandler(new StatementHandler() {
				@Override
				public void handleStatement(SubjectNode subject,
						PredicateNode predicate, ObjectNode object) {
					try {
						jrdfMem.add(subject, predicate, object);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			parser.parse(file, baseURI);
			file.close();
			return jrdfMem;

	}
	
	/**
	 * Process the RDFS file, by separating the different kind of properties.
	 * @param jrdfMem the graph that represents the RDFS file
	 * @return an array with one array of triples for each kind of properties
	 */
	private ArrayList<ArrayList<Triple>> processRDFSFile(Graph jrdfMem){
		try{
			// Stores read objects in the corresponding vectors
			ArrayList<ArrayList<Triple>> allTriples = new ArrayList<ArrayList<Triple>>();

			ArrayList<Triple> conceptTriples = new ArrayList<Triple>();
			// get the Factory
			GraphElementFactory elementFactory = jrdfMem.getElementFactory();
			// 0 <-- Gets the CONCEPTS
			ObjectNode node = elementFactory.createResource(new URI(RDFS_URI + N_CLASS));
			Triple triple = elementFactory.createTriple(null, null, node);
			ClosableIterator iter = jrdfMem.find(triple);
			while (iter.hasNext()) {
				conceptTriples.add((Triple) iter.next());
			}
			allTriples.add(conceptTriples);
			// 1 <-- Gets the parents
			ArrayList<Triple> parentsTriples = new ArrayList<Triple>();	
			PredicateNode pnode = elementFactory.createResource(new URI(RDFS_URI + P_SUBCLASS_OF));
			triple = elementFactory.createTriple(null, pnode, null);
			iter = jrdfMem.find(triple);
			while (iter.hasNext()) {
				parentsTriples.add((Triple) iter.next());
			}
			allTriples.add(parentsTriples);

			// 2 <-- Gets the PROPERTIES
			ArrayList<Triple> attributeTriples = new ArrayList<Triple>();	
			node = elementFactory.createResource(new URI(RDF_URI + N_PROPERTY));
			triple = elementFactory.createTriple(null, null, node);
			iter = jrdfMem.find(triple);
			while (iter.hasNext()) {
				attributeTriples.add((Triple) iter.next());
			}
			allTriples.add(attributeTriples);

			// 3 <--  Gets the DOMAINS
			ArrayList<Triple> domainTriples = new ArrayList<Triple>();	
			pnode = elementFactory.createResource(new URI(RDFS_URI + P_DOMAIN));
			triple = elementFactory.createTriple(null, pnode, null);
			iter = jrdfMem.find(triple);
			while (iter.hasNext()) {
				domainTriples.add((Triple) iter.next());
			}
			allTriples.add(domainTriples);

			// 4 <-- Gets the RANGES
			ArrayList<Triple> rangesTriples = new ArrayList<Triple>();	
			pnode = elementFactory.createResource(new URI(RDFS_URI + P_RANGE));
			triple = elementFactory.createTriple(null, pnode, null);
			iter = jrdfMem.find(triple);
			while (iter.hasNext()) {
				rangesTriples.add((Triple) iter.next());
			}
			allTriples.add(rangesTriples);
			
			// 5 <-- Gets the OVERRIDDENVALUES
			ArrayList<Triple> overriddenTriples = new ArrayList<Triple>();	
			pnode = elementFactory.createResource(new URI(A_URI + P_OVERRIDDENPROP));
			triple = elementFactory.createTriple(null, pnode, null);
			iter = jrdfMem.find(triple);
			while (iter.hasNext()) {
				overriddenTriples.add((Triple) iter.next());
			}
			allTriples.add(overriddenTriples);
			
			return allTriples;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Create ontology concepts.
	 * @param triples The set of triples declared in the RDF file.
	 * @return an array of concepts defined in the ontology
	 */
	private HashMap<Integer, Concept> createConcepts(ArrayList<Triple> triples) {
		// Stores read concepts in the corresponding vector
		Concept THING = new Concept();
		THING.setRefreshed();
		HashMap<Integer, Concept> concepts = new HashMap<Integer, Concept>();
		concepts.put(THING.getID(), THING);
		Concept c;
		for (int i = 0; i<triples.size(); i++) {
			Triple t = triples.get(i);
			c = new Concept(((URIReferenceImpl) t.getSubject()).getURI().getFragment(), i+1, THING);
			concepts.put(c.getID(), c);
		}
		
		return concepts;
	}

	/**
	 * Create the taxonomy, modifying its concepts and storing is-a relations in
	 * m_taxonomy member.
	 * @param triples The set of triples declared in the RDF file, excluding the declarations
	 * @param onto the ontology to update
	 */
	private void createTaxonomy(ArrayList<Triple> triples, Ontology onto) {
		LinkedList<RelationIsA> taxonomy = new LinkedList<RelationIsA>();
		Concept child, parent;
		Triple t;
		int n = 0;
		do {
			int i = 0; 
			while (i<triples.size()) {
				t = triples.get(i);
				child = onto.getConcept(((URIReferenceImpl) t.getSubject()).getURI().getFragment().toLowerCase());
				parent = onto.getConcept(((URIReferenceImpl) t.getObject()).getURI().getFragment().toLowerCase());
				if ((null != child) && (null != parent)) {
					if (parent.alreadyRefreshed()) {
						child.addParent(parent);
						child.setPK(parent.getPKIndex());
						taxonomy.add(new RelationIsA(n++, child, parent));
						triples.remove(i);
					}else i++;
				}else i++;
			}
		} while (triples.size()>0);
		// Initializes the m_taxonomy array
		onto.setTaxonomy(taxonomy);		
		// Clean THING'S children
		onto.cleanThing();		
	}

	/**
	 * Fill the attributes with their characteristics (domain and range).
	 * @param attributes the attributes to fill in
	 * @param domains the domains of the attributes 
	 * @param ranges the ranges of the attributes
	 * @param onto the ontology to update
	 */
	private void fillAttributes(ArrayList<Triple> ats, ArrayList<Triple> domains, ArrayList<Triple> ranges,  Ontology onto){
		int nr_props= 0;
		HashMap<String, Attribute> attributes = new HashMap<String, Attribute>();
		HashMap<String, Relation> relations = new HashMap<String, Relation>();
		Triple tri;
		String attName;
		Attribute att;
		for (int i = 0; i<ats.size(); i++) {
			// Creates the attribute
			tri = ats.get(i);
			attName = ((URIReferenceImpl) tri.getSubject()).getURI().getFragment().toLowerCase();
			if (! attributes.containsKey(attName))
				attributes.put(attName, new Attribute(attName, nr_props++));
		}
		// Sets attributes domain
		for (int i = 0; i<domains.size(); i++) {	
			Triple d = domains.get(i);
			Concept dom = onto.getConcept(((URIReferenceImpl) d.getObject()).getURI().getFragment().toLowerCase());
			attName = ((URIReferenceImpl) d.getSubject()).getURI().getFragment().toLowerCase();
			if (attributes.containsKey(attName)){
				att = attributes.get(attName);
				att.setDomain(dom);
			}
		}
		
		int nr_relations = 0;	
		// Sets attributes range and creates corresponding relations if necessary
		for (int i = 0; i<ranges.size(); i++) {	
			Triple r = ranges.get(i);
			ObjectNode o = r.getObject();
			attName = ((URIReferenceImpl) r.getSubject()).getURI().getFragment().toLowerCase();
			if (attributes.containsKey(attName)){
				att = attributes.get(attName);				
				if (!att.isRangeSet()){
					Concept dom = att.getDomain();
					if (o instanceof URIReferenceImpl) {
						String oname = ((URIReferenceImpl) o).getURI().getFragment().toLowerCase();
						// It is a nominal attribute
						if ((0 == oname.compareToIgnoreCase(N_LITERAL)) || 
								(0 == oname.compareToIgnoreCase(N_STRING))) {
							att.setRange((new String("")).getClass());
//IF there are no RELATIONS	at.setID(nr_attributes++);
							dom.addAttribute(att);
						} 
						else if (0 == oname.compareToIgnoreCase(N_INTEGER)) {
							att.setRange((new Integer(0)).getClass());
//IF there are no RELATIONS	at.setID(nr_attributes++);
							dom.addAttribute(att);
						}
						else {
							// IT IS A RELATION
							Concept target = onto.getConcept(oname);
							att.setRange(target);
							Relation rel = new Relation(att, nr_relations++, dom, target);
							relations.put(rel.getName(), rel);
							dom.addRelation(rel);
							// REMOVE THE RELATION FROM THE SET OF ATTRIBUTES
							attributes.remove(att.getName());
						}
					}
					else {
						if (0 == o.toString().compareToIgnoreCase(N_INTEGER)) {
							att.setRange((new Integer(0)).getClass());
						}else 
							att.setRange((new String("")).getClass());
//IF there are no RELATIONS	at.setID(nr_attributes++);
					}
				}
			}
		}
		// Initializes the m_relations and m_attributes
		onto.addRelations(relations);
		onto.addAttributes(attributes);
	}

	/**
	 * Updates the default values for each concept in the ontology
	 * @param ats the attributes to update
	 * @param jrdfMem the RDFS file as a graph
	 * @param onto the ontology to update
	 */
	private void fillOverriddenValues(ArrayList<Triple> ats, Graph jrdfMem, Ontology onto){
		try{
			onto.initializeConceptDefaultValues();

			// get the Factory
			GraphElementFactory elementFactory = jrdfMem.getElementFactory();
			// 0 <-- Gets the CONCEPTS
			String domain="", overridden="", st, rangeST= "";
			HashMap<Integer, Feature> values = new HashMap<Integer, Feature>();
			HashMap<String, Relation> rels = new HashMap<String, Relation>();
			HashMap<Integer, Relation> ontorelations = onto.getRelations();
			ObjectNode obj = null;
			Triple triple, result, t;
			ClosableIterator iter;
			Concept con = null, rangeCon;
			Relation rel;
			SubjectNode sub;
			PredicateNode pnode;
			Attribute att;
			Feature f;
			int nRelations = 0;
			// Process each overridden property
			for (int i=0; i<ats.size(); i++){
				t = ats.get(i);
				sub = t.getSubject();
				overridden = ((URIReferenceImpl) t.getObject()).getURI().getFragment().toLowerCase();
				// Gets the domain
				pnode = elementFactory.createResource(new URI(A_URI + P_DOMAIN));
				triple = elementFactory.createTriple(sub, pnode, null);
				iter = jrdfMem.find(triple);
				while (iter.hasNext()) {
					result = (Triple) iter.next();
					domain = ((URIReferenceImpl) result.getObject()).getURI().getFragment().toLowerCase();
					con = onto.getConcept(domain);
				}
				// Sets the default values
				if (null!=con && null!=overridden){
					att = onto.getAttribute(overridden);
					// It is an ATTRIBUTE ==> looks for the new VALUE
					if (null != att){
						// Gets the value
						pnode = elementFactory.createResource(new URI(A_URI + P_VALUES));
						triple = elementFactory.createTriple(sub, pnode, null);
						iter = jrdfMem.find(triple);
						while (iter.hasNext()) {
							result = (Triple) iter.next();
							obj = result.getObject();
						}
						st = obj.toString().replaceAll("\"", "");
						// It is a numeric attribute
						if (st.matches("[0-9]*")){
							att.setRange(new Integer(0));
							f = new Feature(new Integer(st), Ontology.NR_DEFAULT_VALUES, att.getID(), con.getID());
							values.put(f.getID(),  f);
						}
						// It is a nominal attribute
						else{
							f = new Feature(st, Ontology.NR_DEFAULT_VALUES, att.getID(), con.getID());
							values.put(f.getID(), f);
						}
						con.setDefaultValue(values.get(Ontology.NR_DEFAULT_VALUES).getID(), att.getID());
						Ontology.NR_DEFAULT_VALUES++;
					}
					// It is a RELATION ==> looks for the new RANGE
					else {
						rel = onto.getRelation(overridden);
						if (null == rel){
							// Gets the range
							pnode = elementFactory.createResource(new URI(RDFS_URI + P_RANGE));
							triple = elementFactory.createTriple(sub, pnode, null);
							iter = jrdfMem.find(triple);
							while (iter.hasNext()) {
								result = (Triple) iter.next();
								rangeST = ((URIReferenceImpl) result.getObject()).getURI().getFragment().toLowerCase();
							}
							rel = new Relation(ontorelations.get(overridden), nRelations++);
							rangeCon = onto.getConcept(rangeST);
							rel.setRange(rangeCon);
							rels.put(overridden, rel);
							con.setRelation(rel, rel.getID());
						}
					}
				}
			}
			// Add new default values
			onto.addDefaultValues(values);
			
			// Add new relations if any
			if (nRelations > 0){
				onto.addRelations(rels);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public String getFilename() {
		return m_filename;
	}

	@Override
	public String getName() {
		return "RDFS Reader";
	}

	@Override
	public String getSimpleFilename() {
		if (m_simpleFilename == null) {
		m_simpleFilename = m_filename.substring(m_filename.lastIndexOf('\\') + 1,
				m_filename.length() - 5);
		}
		return m_simpleFilename;
	}
}

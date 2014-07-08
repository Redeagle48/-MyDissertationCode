/**
 * PaMDA Project
 * PACKAGE - io.d3m
 * OWLReader.java                          
 * @author Joao Vieira (joao.c.vieira@ist.utl.pt)
 * 2012
 */

package d3m.span.ontologies.io;

import dm.classification.ds.Item;
import d3m.span.ontologies.*;
import d3m.span.ontologies.Axiom;
//import d3m.span.ontologies.SameIndividualAxiom;
import d3m.span.ontologies.kb.Feature;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;

import java.io.File;
import java.util.*;

/**
 * Class <CODE><B>OWLReader</B></CODE> for reading ontologies in the context of <CODE><B>D3M</B></CODE>.
 * @author Joao Vieira
 * @version 0.3
 */

public class OWLReader implements OntologyReader {
	private String filename;
	private String simpleFilename;
	private final OWLOntologyManager manager;
	private final OWLOntology ontology;
	private final ConceptProcessor conceptProcessor;
	private final AttributeProcessor attributeProcessor;
	private final AxiomProcessor axiomProcessor;

	/**
	 * Creates a reader for the file received.
	 * 
	 * The reader is composed by a concept processor and an attribute
	 * processor. These will get all the information needed to fill our
	 * representation of the ontology.
	 * 
	 * @see ConceptProcessor
	 * @see AttributeProcessor
	 * @see AxiomProcessor
	 * @param filename the name of the OWL file with the ontology
	 * @throws OWLOntologyCreationException
	 */
	public OWLReader(String filename) throws OWLOntologyCreationException {
		this.filename = filename;
		manager = OWLManager.createOWLOntologyManager();
		ontology = manager.loadOntologyFromOntologyDocument(new File(filename));
		System.out.println("ONTOLOGY --> OWL file read.");
		conceptProcessor = new ConceptProcessor(ontology.getClassesInSignature());
		attributeProcessor = new AttributeProcessor();
		axiomProcessor = new AxiomProcessor();
	}
	
	/**
	 * Updates the ontology from an OWL file
	 * @param ontology the ontology to update
	 * @throws Exception 
	 */
	@Override
	public void updateOntology(Ontology ontology) throws Exception {
		
		ontology.setName(this.getSimpleFilename());
		
		//DEBUG_visitOjectProperties(this.ontology);
		System.out.println("ONTOLOGY --> it is going to create elements and infer taxonomy...");
		Concept[] concepts = conceptProcessor.getConcepts();
		System.out.println("ONTOLOGY --> it is going to fill attributes and assign them to concepts...");
		Attribute[] attributes = attributeProcessor.getAttributesFor(concepts);
		System.out.println("ONTOLOGY --> it is going to fill defaults and assign them to attributes...");
		Feature[] features = conceptProcessor.getFeaturesFor(attributes);
		System.out.println("ONTOLOGY --> it is going to get inferred taxonomy...");
		RelationIsA[] taxonomy = conceptProcessor.getTaxonomy();
		System.out.println("ONTOLOGY --> it is going to get inferred relations...");
		Relation[] relations = attributeProcessor.getRelations();
		System.out.println("ONTOLOGY --> it is going to get axioms...");
		Set<Axiom> axioms = axiomProcessor.getSameIndividualAxioms();
		
		HashMap<Integer, Concept> concepts_ = new HashMap<Integer, Concept>();
		for(Concept c : concepts) {
			concepts_.put(c.getID(), c);
		}
		
		
		HashMap<String, Attribute> attributes_ = new HashMap<String, Attribute>();
		for(Attribute c : attributes) {
			if(c != null) 
				attributes_.put(c.getName(), c);
		}
		
		HashMap<Integer, Feature> defaultValues_ = new HashMap<Integer, Feature>();
		for(Feature c : features) {
			defaultValues_.put(c.getID(), c);
		}
		
		LinkedList<RelationIsA> relationIsA = new LinkedList<RelationIsA>();
		for(RelationIsA c : taxonomy) {
			relationIsA.add(c);
		}
		
		HashMap<String, Relation> relations_ = new HashMap<String, Relation>();
		for(Relation c : relations) {
			relations_.put(c.getName(), c);
		}
		
		ontology.setConcepts(concepts_);
		
		//ontology.setAttributes(attributes_);
		ontology.addAttributes(attributes_);
		
		//ontology.setDefaultValues(features);
		ontology.addDefaultValues(defaultValues_);
		
		//ontology.setTaxonomy(taxonomy);
		ontology.setTaxonomy(relationIsA);
		
		//ontology.setRelations(relations);
		ontology.addRelations(relations_);
		
		//ontology.setAxioms(axioms);
	}
	
	@SuppressWarnings("unused")
	private void DEBUG_visitOjectProperties(OWLOntology o) {
		OWLOntologyWalker walker = new OWLOntologyWalker(Collections.singleton(o));
		OWLOntologyWalkerVisitor<Object> visitor = new OWLOntologyWalkerVisitor<Object>(walker) {
			@Override
			public Object visit(OWLObjectProperty desc) {
				System.out.println(desc);
				System.out.println("  " + getCurrentAxiom());
				return null;
			}
		};
		walker.walkStructure(visitor);
	}
	
	/**
	 * Internal class responsible for processing the axioms on the ontology.
	 * 
	 * This class contains methods to process various types of axioms that may be present
	 * explicitely on the ontology.
	 * 
	 * @author Joao Vieira
	 * @version 0.1
	 */
	private class AxiomProcessor {	
		private Set<Axiom> getSameIndividualAxioms() {
			Set<Axiom> axs = new HashSet<Axiom>();
			Set<OWLSameIndividualAxiom> owlaxs = ontology.getAxioms(AxiomType.SAME_INDIVIDUAL);
			for (OWLSameIndividualAxiom owlax : owlaxs) {
				Collection<Item<String>> items = new LinkedList<Item<String>>();
				Set<OWLIndividual> owlinds = owlax.getIndividuals();
				for (OWLIndividual owlind : owlinds) {
					items.add(new Item<String>(getObjectName((OWLNamedIndividual)owlind)));
				}
				//axs.add(new SameIndividualAxiom(items));
			}
			return axs;
		}
	}

	/**
	 * Internal class responsible for processing the concepts on the ontology.
	 * 
	 * This class contains methods to process the concepts, their features and
	 * the taxonomy of the ontology.
	 * 
	 * @author Joao Vieira
	 * @version 0.3
	 */
	private class ConceptProcessor {
		private int id = 1, relIsAId = 0;
		private final Map<String, Integer> map = new Hashtable<String, Integer>();
		private final Concept THING = new Concept();
		private final Set<OWLClass> classes;
		private final Collection<String[]> features;
		private final Collection<RelationIsA> relationsIsA;
		private Concept[] concepts;
		
		/**
		 * Creates a concept processor for the set of OWL classes received.
		 * 
		 * When allocating space checks if the concept Thing is explicitly 
		 * defined. If it isn't add an extra position in the array for it.
		 * By the W3 spec, all OWL ontologies have a root concept named Thing 
		 * even if it is not explicitly defined.
		 * 
		 * @param classes a set of OWL classes to process into concepts.
		 */
		private ConceptProcessor(Set<OWLClass> classes) {
			boolean hasThing = false;
			this.classes = classes;
			this.features = new LinkedList<String[]>();
			this.relationsIsA = new LinkedList<RelationIsA>();
			
			for (OWLClass cl : classes) {
				if (cl.isTopEntity()) {
					this.concepts = new Concept[classes.size()];
					hasThing = true;
					break;
				}
			}
			if (!hasThing) this.concepts = new Concept[classes.size() + 1];
			this.concepts[0] = THING;
			this.map.put("http://www.w3.org/2002/07/owl#Thing", 0);
		}
		
		/**
		 * Gets a list of concepts from the owl classes of the processor
		 * 
		 * While constructing the list of concepts it will also pre-process
		 * information about their features and the taxonomy of the ontology.
		 * 
		 * @return an array of concepts defined in the ontology
		 */
		private Concept[] getConcepts() {
			for (OWLClass cl : classes) {
				if (!cl.isTopEntity()) {
					Concept concept;
					String clName = getObjectName(cl);
					preProccessClassFeatures(cl);

					if (map.containsKey(clName)) {
						concept = concepts[map.get(clName)];
					} else {
						concept = new Concept(clName, id, THING);
						concepts[id] = concept;
						map.put(clName, id);
						++id;
					}

					preProccessTaxonomy(cl, concept);
				}
			}
			return concepts;
		}
		
		/**
		 * Gets the taxonomy of this ontology
		 * @return an array of <I>isA</I> relations between concepts
		 */
		private RelationIsA[] getTaxonomy() {
			RelationIsA[] taxonomy = new RelationIsA[relationsIsA.size()];
			taxonomy = relationsIsA.toArray(taxonomy);
			Arrays.sort(taxonomy);
			return taxonomy;
		}
		
		/**
		 * Gets the features for the attributes
		 * @see Feature
		 * @param attributes an array of attributes to be initialized
		 * @return an array of features
		 */
		private Feature[] getFeaturesFor(Attribute[] attributes) {
			int id = 0;
			Feature[] features = new Feature[this.features.size()];
			for (String[] attr : this.features) {
				for (Concept d : concepts) {
					if (attr[0].equalsIgnoreCase(d.getName())) {
						for (Attribute a : attributes) {
							if (attr[1].equalsIgnoreCase(a.getName())) {
								Feature feature = new Feature(attr[2], id, a.getID(), d.getID());
								try {
									d.setDefaultValue(feature.getID(), a.getID());
									features[id] = feature;
									++id;
								} catch (OntoException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
			return features;
		}

		/**
		 * Processes information about parents, forming a taxonomy
		 * @param cl the owlapi concept with information about its parents
		 * @param concept our internal representation of the concept
		 */
		private void preProccessTaxonomy(OWLClass cl, Concept concept) {
			for (OWLClassExpression scl : cl.getSuperClasses(ontology)) {
				if (!scl.isAnonymous()) {
					String sclName = getObjectName(scl.asOWLClass());
					if (!map.containsKey(sclName)) {
						concepts[id] = new Concept(sclName, id, THING);
						map.put(sclName, id);
						++id;
					}
					Concept parent = concepts[map.get(sclName)];
					//concept.addParent(concepts, parent);
					relationsIsA.add(new RelationIsA(relIsAId++, concept, parent));
				}
			}
		}
		
		// TODO: This deals only superficially with features. 
		// Relations may be initialized in a huge variety of ways given the
		// flexibility that OWL provides for doing so via axioms.
		private void preProccessClassFeatures(OWLClass cl) {
			String clName = getObjectName(cl);
			for (OWLClassExpression ec : cl.getEquivalentClasses(ontology)) {
				for (OWLObjectProperty oop : 
					ec.getObjectPropertiesInSignature()) {
					String attrName = getObjectName(oop);
					for (OWLNamedIndividual oni : ec
							.getIndividualsInSignature()) {
						String attrValue = getObjectName(oni);
						String attr[] = { clName, attrName, attrValue };
						features.add(attr);
					}
				}
			}
		}
	}

	/**
	 * Internal class responsible for processing the attributes
	 * 
	 * This class contains methods to process the attributes, their domains and
	 * ranges and relations between concepts.
	 * 
	 * @author Joao Vieira
	 * @version 0.3
	 */
	private class AttributeProcessor {
		private int id = 0, relId = 0;
		private final Set<OWLDataProperty> dataProperties;
		private final Set<OWLObjectProperty> objectProperties;
		private final Attribute[] attributes;
		private final Collection<Relation> relations;
		private Concept[] concepts;
		
		/**
		 * Creates an attribute processor for the ontology.
		 */
		private AttributeProcessor() {
			dataProperties = ontology.getDataPropertiesInSignature();
			objectProperties = ontology.getObjectPropertiesInSignature();
			relations = new LinkedList<Relation>();
			attributes = new Attribute[dataProperties.size() + objectProperties.size()];
		}
		
		/**
		 * Gets the relations between concepts.
		 * 
		 * Relations are attributes whose range is another concept.
		 * 
		 * @see Relation
		 * @return an array of relations
		 */
		private Relation[] getRelations() {
			Relation[] relations = new Relation[this.relations.size()];
			relations = this.relations.toArray(relations);
			Arrays.sort(relations);
			return relations;
		}

		/**
		 * Gets the simple attributes for these concepts.
		 * 
		 * This will fill the attributes and propagate from the root to the
		 * leafs of the taxonomy.
		 * 
		 * @param concepts the concepts whose attributes will be processed
		 * @return an array of attributes
		 */
		private Attribute[] getAttributesFor(Concept[] concepts) {
			this.concepts = concepts;
			for (Concept c : concepts) {
				fillAttributesFor(c);
				fillRelationsFor(c);
			}
			propagateAttributesAndRelations(0);
			return attributes;
		}
		
		/**
		 * Fills the domain and range of simple attributes
		 * @param c the concept whose attributed will be filled
		 */
		private void fillAttributesFor(Concept c) {
			for (OWLDataProperty at : dataProperties) {
				for (OWLClassExpression ce : at.getDomains(ontology)) {
					if (c.getName().equalsIgnoreCase(getObjectName(ce.asOWLClass()))) {
						Attribute attribute = new Attribute(getObjectName(at), id);
						attribute.setDomain(c);
						for (OWLDataRange r : at.getRanges(ontology)) {
							if (r.isDatatype()) {
								OWLDatatype type = r.asOWLDatatype();
								if (type.isInteger() || 
										 type.toString().contains("Integer"))
									attribute.setRange(Integer.class);
								else if (type.isBoolean()) 
									attribute.setRange(Boolean.class);
								else if (type.isDouble())
									attribute.setRange(Double.class);
								else if (type.isFloat())
									attribute.setRange(Float.class);
								else if (type.isString() || type.isTopDatatype())
									attribute.setRange(String.class);
								else throw new RuntimeException(type.toString());
							}
						}
						attributes[id] = attribute;
						c.addAttribute(attribute);
						++id;
					}
				}
			}
		}
		
		/**
		 * Fills the domain and range of relations
		 * @param c the concept whose relations will be filled
		 */
		private void fillRelationsFor(Concept c) {
			for (OWLObjectProperty op : objectProperties) {
				if (c.getID() == 0 && op.getDomains(ontology).size() == 0) {
					addRelation(c, op);
				} else for (OWLClassExpression ce : op.getDomains(ontology)) {
					if (c.getName().equalsIgnoreCase(getObjectName(ce.asOWLClass()))) {
						addRelation(c, op);
					}
				}
			}
		}

		/**
		 * Add a relation to the corresponding concept and array
		 * @param c the concept which is the domain of the relation
		 * @param op the owlapi representation of this relation
		 */
		private void addRelation(Concept c, OWLObjectProperty op) {
			Attribute at = new Attribute(getObjectName(op), id);
			at.setDomain(c);
			for (OWLClassExpression r : op.getRanges(ontology)) {
				for (OWLClass range : r.getClassesInSignature()) {
					for (Concept rc : concepts) {
						if (rc.getName().equalsIgnoreCase(getObjectName(range))) {
							at.setRange(rc);
							Relation rel = new Relation(at, relId++, c, rc);
							relations.add(rel);
							c.addRelation(rel);
						}
					}
				}
			}
			attributes[id] = at;
			c.addAttribute(at);
			++id;
		}
		
		/**
		 * Propagates relations and attributes from parents to children.
		 * @param conceptId the id of the concept where the propagation starts
		 */
		private void propagateAttributesAndRelations(int conceptId) {
			for (int childId : concepts[conceptId].getChildren()) {
				for (int attrId : concepts[conceptId].getAttributes()) {
					concepts[childId].addAttribute(attributes[attrId]);
				}
				for (int relId : concepts[conceptId].getRelations()) {
					concepts[childId].addRelation(getRelations()[relId]);
				}
				concepts[childId].initializeDefaultValues();
				propagateAttributesAndRelations(childId);
			}
		}
	}

    private String getObjectName(OWLNamedObject o) {
        return o.getIRI().toString();
    }
	
	
	@Override
	public String getFilename() {
		return filename;
	}

	@Override
	public String getSimpleFilename() {
		if (simpleFilename == null) {
			//simpleFilename = filename.substring(filename.lastIndexOf('\\') + 1,
					//filename.length() - 5);
			simpleFilename = filename.substring(filename.lastIndexOf('/') + 1,
					filename.length() - 4);
		}
		return simpleFilename;
	}

	@Override
	public String getName() {
		return "OWL Reader";
	}
}

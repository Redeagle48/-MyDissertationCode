/**
 * PaMDA Project
 * PACKAGE - pamda.ontologies
 * Ontology.java                         
 * @author Cl���udia Antunes (claudia.antunes@ist.utl.pt)
 * 2007
 */
package d3m.span.ontologies;

import d3m.span.ontologies.io.OntologyReader;
import d3m.span.ontologies.io.RDFSReader;
import d3m.span.ontologies.kb.Feature;
import d3m.span.ontologies.kb.KnowledgeBase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import d3m.d2pm.core.D2Item;

/**
 * Class <B>Ontology</> for handling particular ontologies, read from a RDF
 * file. RDF files have to present its concepts ordered from the most general to
 * the most specific, which means that first appears the ones that directly
 * descends from RESOURCE.
 * @author Claudia Antunes
 * @version 1.0
 */
public class Ontology {
	/** ****** CONSTANTS */
	final private String AXIOM_SEPARATOR = ":";
	
	final private String AXIOMS = "ontologies.Axiom";
	
	/** ****** FIELDS */	
	/** The name of the universal concept. */
	public static final String THING = "Resource";

	/** The identifier for the definition axiom. */
	public static final int AXIOM_DEF = 0;
	
	/** The identifier for the axiom Equal. */
	public static final int AXIOM_EQUAL = 1;
	
	/** The identifier for the axiom Valid. */
	public static final int AXIOM_VALID = 2;
	
	/** The identifier for the axiom Joinable. */
	public static final int AXIOM_JOINABLE = 3;
	
	/** The number of created default values. */
	public static int NR_DEFAULT_VALUES = 0;
	
	/** The ontology name. */
	protected String m_name;

	/** The set of concepts. */
	protected HashMap<Integer, Concept> m_concepts;
	
	/** An array with the name of all concepts. */
	protected HashMap<String, Integer> m_conceptNames;
	
	/** The set of attributes. */
	protected HashMap<Integer, Attribute> m_attributes;

	/** An array with the name of all attributes. */
	protected HashMap<String, Integer> m_attributeNames;

	/** The set of default values. */
	protected HashMap<Integer, Feature> m_attDefaultValues;
	
	/** The set of relations. */
	protected HashMap<Integer, Relation> m_relations;
	
	/** An array with the name of all attributes. */
	protected HashMap<String, Integer> m_relationNames;


	/** The set of is-a relations. The key is the concept id that IS_A*/
	protected LinkedList<RelationIsA> m_taxonomy;
	
	
	/** The set of axioms extracted from the ontology. */
//	protected Set<ontologies.axioms.Axiom> m_axioms;

	/** ***************************** CONSTRUCTORS */
	/**
	 * Default constructor
	 */
	public Ontology() {
		m_attributes = new HashMap<Integer, Attribute>();
		m_attributeNames = new HashMap<String, Integer>();
		m_attDefaultValues = new HashMap<Integer, Feature>();
		m_relations = new HashMap<Integer, Relation>();
		m_relationNames = new HashMap<String, Integer>();
	}
	
	/**
	 * Set the Ontology name
	 * @param name
	 */
	public void setName(String name){
		m_name = name;
	}
	
	/**
	 * 
	 * TODO Put here a description of what this method does.
	 *
	 * @param RDFSfilename
	 * @param axiomsFile
	 * @throws Exception
	 */
	public void acquiresOntologyFrom(String RDFSfilename, String axiomsFile) throws Exception{
		System.out.println("ONTOLOGY --> it is going to read the RDFS file "
				+ RDFSfilename);
		m_name = RDFSfilename.substring(RDFSfilename.lastIndexOf('\\') + 1,
				RDFSfilename.length() - 5);
		RDFSReader rdfs = new RDFSReader(RDFSfilename);
		rdfs.updateOntology(this);
		Iterator<Concept> iter = m_concepts.values().iterator();
		while (iter.hasNext()) 
			(iter.next()).propagateValues(this);
		
//		readAxioms(axiomsFile);
//		applyDefinitionAxioms(axiomsFile);
		
		System.gc();
		System.out.println("ONTOLOGY --> finished.");
	}
	/**
	 * Creates an initialized ontology using the given Reader.
	 * @param reader an instance of an ontology reader
	 * @throws Exception 
	 */
	public void updatesOntologyFrom(OntologyReader reader) throws Exception {
		System.out.println("ONTOLOGY --> it is going to read "
				+ reader.getFilename() + " using " + reader.getName());
		m_name = reader.getSimpleFilename();
		reader.updateOntology(this);
		
		Iterator<Concept> iter = m_concepts.values().iterator();
		while (iter.hasNext()) 
			(iter.next()).propagateValues(this);
		
		System.gc();
		System.out.println("ONTOLOGY --> finished.");
	}

	/** ***************************** CONCEPTS **************************************** */
	/**
	 * Returns the number of concepts in the ontology
	 * @return the number of concepts in the ontology
	 */
	public int getNrConcepts(){
		return m_concepts.size();
	}
	
	/**
	 * Looks for a concept in the ontology.
	 * @param name the name of the concept
	 * @return return the concept or null if it does not belong to the ontology
	 */
	public Concept getConcept(String name) {
		if (m_conceptNames.containsKey(name))
			return m_concepts.get(m_conceptNames.get(name));
		return null;
	}

	/**
	 * Returns the indexed concept in the ontology
	 * @param index the concept to return
	 * @return the ith concept
	 */
	public Concept getConcept(int index){
		if (m_concepts.containsKey(index))
			return m_concepts.get(index);
		return null;
	}

	/**
	 * Returns the array of concepts in this ontology
	 * @return the array of concepts in this ontology
	 */
	public HashMap<Integer, Concept> getConcepts(){
		return m_concepts;
	}
	
	/**
	 * Returns the most generic concept if not the THING
	 * @return the most generic concept despite THING. THING if more than a single one.
	 */
	public Concept getGenericConcept(){
		if (m_concepts.containsKey(Concept.GENERIC))
			return m_concepts.get(Concept.GENERIC);
		return m_concepts.get(0);
	}
	
	/**
	 * Updates the set of concepts
	 * @param concepts the array of concepts to set
	 */
	public void setConcepts(HashMap<Integer, Concept> concepts){
		m_concepts = concepts;
		m_conceptNames = new HashMap<String, Integer>();
		Iterator<Concept> iter = m_concepts.values().iterator();
		Concept c = null;
		while (iter.hasNext()) {
			c = iter.next();
			m_conceptNames.put(c.getName(), c.getID());
		}
	}

	
	/** ***************************** ATTRIBUTES **************************************** */

	/**
	 * Looks for an attribute in an array of attributes.
	 * @param attributes the vector of attributes
	 * @param name the name of the attribute
	 * @return return the attribute or null if it does not belong to the ontology
	 */
/*	public int getAttribute(ArrayList<Attribute> attributes, String name) {
		for (int i = 0; i < attributes.size(); i++) {
			if (null!= attributes.get(i) && (0 == name.compareToIgnoreCase(attributes.get(i).getName())))
				return i;
		}
		return -1;
	}
*/	
	/**
	 * Looks for an attribute in the ontology by its id.
	 * @param atts the vector of attributes
	 * @param name the name of the attribute
	 * @return return the attribute index or -1 if it does not belong to the ontology
	 */
/*	public int get(Attribute[] atts, String name){
		for (int i = 0; i < atts.length; i++) {
			if (0 == name.compareToIgnoreCase(atts[i].getName()))
				return i;
		}
		return -1;
	}*/
	
	/**
	 * Returns the ith attribute 
	 * @param id the attribute's identifier
	 * @return the id_th attribute
	 */
	public Attribute getAttribute(int id){
		if (m_attributes.containsKey(id))
			return m_attributes.get(id);
		return null;
	}
	
	/**
	 * Looks for an attribute in the ontology by its name.
	 * @param name the name for the attribute
	 * @return the attribute if found, null otherwise
	 */
	public Attribute getAttribute(String name){
		if (m_attributeNames.containsKey(name))
			return m_attributes.get(m_attributeNames.get(name));
		return null;
	}
	
	/**
	 * Returns the array of all attributes in the ontology
	 * @return the array of attributes in the entire ontology
	 */
	public HashMap<Integer, Attribute> getAttributes(){
		return m_attributes;
	}
	
	/**
	 * Updates the set of attributes
	 * @param ats the set of attributes
	 */
	public void addAttributes(HashMap<String, Attribute> ats){
		Iterator<String> iter = ats.keySet().iterator();
		String name = "";
		Attribute a = null;
		while (iter.hasNext()) {
			name = iter.next();
			a = ats.get(name);
			m_attributes.put(a.getID(), a);
			m_attributeNames.put(name, a.getID());
		}
	}

	/** ***************************** FEATURES **************************************** */
	/**
	 * Returns the attribute value with id
	 * @param id the attribute value's identifier
	 * @return the attribute value
	 */
	public Feature getAttributeValue(int id){
		if (m_attDefaultValues.containsKey(id))
			return m_attDefaultValues.get(id);
		return null;
	}
	
	/**
	 * Returns the array of default values in this ontology
	 * @return the array of default values in this ontology
	 */
	public HashMap<Integer, Feature> getAttributeValues(){
		return m_attDefaultValues;
	}
	
	/**
	 * Updates the set of default values
	 * @param values the set of default values
	 */
	public void addDefaultValues(HashMap<Integer, Feature> values){
		m_attDefaultValues = values;
	}

	/**
	 * Initialize the array of default values for each concept, 
	 * in accordance to their attributes
	 */
	public void initializeConceptDefaultValues(){
		Iterator<Concept> iter = m_concepts.values().iterator();
		while (iter.hasNext()) 
			(iter.next()).initializeDefaultValues();
	}

	
	/** ***************************** RELATIONS **************************************** */
	/**
	 * Returns the relation with id 
	 * @param id the relation's identifier
	 * @return the relation
	 */
	public Relation getRelation(int id){
		return m_relations.get(id);
	}

	/**
	 * Looks for an attribute in the ontology by its name.
	 * @param name the name for the attribute
	 * @return the attribute if found, null otherwise
	 */
	public Relation getRelation(String name){
		if (m_relationNames.containsKey(name))
			return m_relations.get(m_relationNames.get(name));
		return null;
	}

	/**
	 * Returns the array of relations in this ontology
	 * @return the array of relations in this ontology
	 */
	public HashMap<Integer, Relation> getRelations(){
		return m_relations;
	}
	
	/**
	 * Updates the set of relations
	 * @param rels the set of relations
	 */
	public void addRelations(HashMap<String, Relation> rels){
		Iterator<String> iter = rels.keySet().iterator();
		String name = "";
		Relation a = null;
		while (iter.hasNext()) {
			name = iter.next();
			a = rels.get(name);
			m_relations.put(a.getID(), a);
			m_relationNames.put(name, a.getID());
		}
	}

	/**
	 * Updates the taxonomy
	 * @param is_a the taxonomy
	 */
	public void setTaxonomy(LinkedList<RelationIsA> is_a){
		m_taxonomy = is_a;
	}
	

	
	
	
	
	/** ***************************** AXIOMS **************************************** */
	/**
	 * Reads the axioms from a file and updates their corresponding concepts.
	 * @param filename the name for the file with the axioms
	 * @throws Exception
	 */
/*	protected void readAxioms(String filename) throws Exception{
			BufferedReader reader = null;
			String line = null;
			String[] temp = null;
			reader = new BufferedReader(new FileReader(filename));
			Class<?> classe = Class.forName(AXIOMS);
			while ((line = reader.readLine()) != null) {
				temp = line.split(AXIOM_SEPARATOR);
				Concept concept = getConcept(temp[0]);
				if (null != concept){
					Class<?>[] params = new Class[temp.length-3];
					for (int i=3; i<temp.length; i++)
						params[i-3] = Class.forName(temp[i]);
					Method m = classe.getMethod(temp[2],params);
					if (null!=m){
						concept.setAxiom(m, new Integer(temp[1]));
					}
				}	
			}
			reader.close();	
	}*/

	/**
	 * XXX: Returns the set of axioms in this ontology
	 * @return the set of axioms in this ontology
	 */
/*	public Set<ontologies.axioms.Axiom> getAxioms() {
		return m_axioms;
	}
*/
	
	/**
	 * Updates the set of relations
	 * @param rels the set of relations
	 */
/*	public void setAxioms(Set<ontologies.axioms.Axiom> axioms){
		m_axioms = axioms;
	}
*/	

	/**
	 * Read and applies the definition axioms for each concept.
	 * @param filename
	 * @throws Exception
	 */
/*	protected void applyDefinitionAxioms(String filename) throws Exception{
			BufferedReader reader = null;
			String line = null;
			String[] temp = null;
			reader = new BufferedReader(new FileReader(filename));
			Class<?> classe = Class.forName(AXIOMS);
			while ((line = reader.readLine()) != null) {
				temp = line.split(AXIOM_SEPARATOR);
				Concept concept = getConcept(temp[0]);
				if (null != concept){
					Class<?>[] params = new Class[temp.length-4];
					for (int i=3; i<temp.length-1; i++)
						params[i-3] = Class.forName(temp[i]);
					Method m = classe.getMethod(temp[2],params);
					if (null!=m){
						concept.setAxiom(m, new Integer(temp[1]));
						Object[] args = new Object[2];
						args[0] = concept;
						args[1] = concept.getAttributeIndex(get(m_attributes,temp[5]));
						m.invoke(new Axiom(), args);
					}
				}	
			}
			reader.close();
	}*/

	/** ****** TRANSFORMERS */	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String st = "Ontology " + m_name + "\nCONCEPTS:\n";
		// Don't print the THING in m_concepts[0]
		Iterator<Concept> iter = m_concepts.values().iterator();
		while (iter.hasNext()) 
			st += (iter.next()).toString()+"\n";
		st += "\\ATTRIBUTES:\n";	
		Iterator<Attribute> iter2 = m_attributes.values().iterator();
			while (iter2.hasNext()) 
				st += (iter2.next()).toString()+"\n";
			
		st += "\nRELATIONS:\n";
		Iterator<Relation> iter3 = m_relations.values().iterator();
		while (iter3.hasNext()) 
			st += (iter3.next()).toString()+"\n";

		return st;
	}
	
	/**
	 * Returns a textual representation for this ontology completely filled
	 * @param kb the context knowledge base
	 * @return the representation of the ontology 
	 */
	public String toString(KnowledgeBase kb) {
		String st = "Ontology " + m_name + "\nCONCEPTS:\n";
		Iterator<Concept> iter = m_concepts.values().iterator();
		while (iter.hasNext()) 
			st += (iter.next()).toString(kb)+"\n";
		return st;
	}
	
	/**
	 * 
	 * @return some statistics about the ontology
	 */
	public String statistics(){
		return "NR CONCEPTS=\t" + m_concepts.size();
	}
	/**
	 * 	Remove any concept that has another parent from THING's children
	 *
	 */
	public void cleanThing(){
		LinkedList<Integer> kids = new LinkedList<Integer>();
		Concept thing = this.m_concepts.get(0);
		Iterator<Integer> iter = thing.m_children.iterator();
		while (iter.hasNext()) {
			Concept kid = m_concepts.get(iter.next());
			if (0 == kid.m_parents.size())
				kids.add(kid.getID());
		}
		thing.m_children = kids;
	}

}

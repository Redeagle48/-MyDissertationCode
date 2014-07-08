/**
 * PaMDA Project
 * PACKAGE - pamda.ontologies
 * Concept.java                         
 * @author Cl���udia Antunes (claudia.antunes@ist.utl.pt)
 * 2007
 */
package d3m.span.ontologies;

import d3m.span.ontologies.kb.KnowledgeBase;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Class <Code><B>Concept</B></Code> for handling concepts in an ontology.
 * @author Claudia Antunes
 * @version 1.0
 */
public class Concept extends Element{
	/** The tag for accessing to the most generic concept. */
	public static final String GENERIC = "_generic_"; 
	/** ****** FIELDS */
	boolean m_refreshed = false;

	private byte m_nrAxioms = 3;

	/** The set of parents. */
	protected LinkedList<Integer> m_parents;

	/** The children. */
	protected LinkedList<Integer> m_children;
	
	/** Stores the set of attributes for this concept . */
	protected LinkedList<Integer> m_attributes;
	
	/** Stores the set of default values for attributes, attribute.id is the key for the hashmap. */
	protected HashMap<Integer, Integer> m_attDefaultValues;

	/** Stores the set of concept relations. */
	protected LinkedList<Integer> m_relations;
	
	/** Stores the names for defined axioms. Each name corresponds to a function in Class AXIOM. */
	protected Method[] m_axioms; 
	
	/** Stores the position for the differentiation attribute. */
	protected int m_primaryKey = 0;
	
	/** ****** CONSTRUCTORS */
	/**
	 * Creates the THING concept
	 */
	public Concept() {
		super(0, Ontology.THING);
		m_parents = new LinkedList<Integer>();
		m_children = new LinkedList<Integer>();
		m_attributes = new LinkedList<Integer>();
		m_attDefaultValues = new HashMap<Integer, Integer>();
		m_relations = new LinkedList<Integer>();
		m_axioms = new Method[0];
	}

	/**
	 * Creates any initialized concept.
	 * @param name the name for the concept
	 * @param id the identifier for the concept
	 * @param parent the parent concept
	 */
	public Concept(String name, int id, Concept parent) {
		super(id,name);
		m_children = new LinkedList<Integer>();
		m_parents = new LinkedList<Integer>();
		m_parents.add(parent.getID());
		parent.addChild(this);
		m_attributes = new LinkedList<Integer>();
		m_attDefaultValues = new HashMap<Integer, Integer>();
		m_relations = new LinkedList<Integer>();
		m_axioms = new Method[m_nrAxioms];
		Arrays.fill(m_axioms, null);
	}
	
	/**
	 * Creates a concept equal to the received one
	 * @param c the concept to copy
	 */
	public Concept(Concept c){
		super(c);
		m_children = c.m_children;
		m_parents = c.m_parents;
		m_attributes = c.m_attributes;
		m_attDefaultValues = c.m_attDefaultValues;
		m_relations = c.m_relations;
		m_axioms = c.m_axioms;
	}

	/** ****** SELECTORS */
	/**
	 * Returns the method that implements the ith axiom for this concept
	 * @param i the identifier for the axiom
	 * @return the method that implements the ith axiom
	 */
	public Method getAxiom(int i){
		if (i<m_axioms.length)
			return m_axioms[i];
		else 
			return null;
	}

	/**
	 * Returns the position occupied by the differentiation attribute, 
	 * as set by definition axioms.
	 * @return the index of the differentiation attribute in the concept
	 */
	public int getPKIndex(){
		return m_primaryKey;
	}
	
	/**
	 * Looks for an attribute in an array of attributes, returns its index 
	 * if found, and UNKNOWN otherwise.
	 * @param attributes the array of attributes to look into
	 * @param at the attribute to look for
	 * @return the index of the attribute in this concept
	 */
/*	private int getAttributePosition(int[] attributes, int at){
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i] == at)
				return i;
		}
		return UNKNOWN;
	}
*/	
	/**
	 * Looks for an attribute in an array of attributes, returns its index 
	 * if found, and UNKNOWN otherwise.
	 * @param at the attribute to look for
	 * @return the index for the attribute at in this concept
	 */
/*	public int getAttributeIndex(int at){
		return getAttributePosition(m_attributes, at);
	}
*/	
	/**
	 * Return the identifier of the ith attribute in this concept
	 * @param i the index of the attribute to return
	 * @return the identifier for the ith attribute
	 */
/*	public int getAttributeAt(int i){
		return m_attributes.get(i);
	}
	*/
	/**
	 * Return the identifier of the ith relation in this concept
	 * @param i the index of the relation to return
	 * @return the identifier for the ith relation
	 */
/*	public int getRelationAt(int i){
		return m_relations[i];
	}
	*/
	/**
	 * Returns an array with the identifiers for the attributes of this concept
	 * @return the attributes for this concept
	 */
	public LinkedList<Integer> getAttributes() {
		return m_attributes;
	}
	
	/**
	 * Returns an array with the identifiers for the relations of this concept
	 * @return the relations for this concept
	 */
	public LinkedList<Integer> getRelations() {
		return m_relations;
	}

	/**
	 * Returns an array with the identifiers for the parents of this concept
	 * @return the parents for this concept
	 */
	public LinkedList<Integer> getParents(){
		return m_parents;
	}
	
	/**
	 * Returns an array with the identifiers for the children of this concept
	 * @return the children for this concept
	 */
	public LinkedList<Integer> getChildren(){
		return m_children;
	}
	
	/**
	 * Returns an array with the default values
	 * @return the list of defaultValues
	 */
/*	public int[] getDefaultValues(){
		return m_attDefaultValues;
	}
*/	
	/** ****** MODIFIERS */
	/**
	 * @param att the position for the differentiation attribute
	 */
	public void setPK(int att){
		m_primaryKey = att;
	}
	
	/**
	 * 
	 */
	public void initializeDefaultValues(){
		m_attDefaultValues = new HashMap<Integer, Integer>();
		for (int i  :  m_attributes)
			m_attDefaultValues.put(i, NoNameElement.UNKNOWN);
	}
	
	/**
	 * Updates one relation (identified by id) with a new one received
	 * @param r the new relation
	 * @param id the relation to update
	 */
	public void setRelation(Relation r, int id){
		m_relations.set(id, r.getID());
	}

	/**
	 * Updates the default value for the attribute identified by atID
	 * @param f the default value - the feature id
	 * @param atID the attribute to set
	 * @throws OntoException whenever adding operation fails
	 */
	public void setDefaultValue(int f, int atID) throws OntoException {
		m_attDefaultValues.put(atID, f);
	}
			
	/**
	 * Updates the refreshed flag. 
	 * It turns to TRUE whenever its parents are refreshed.
	 */
	public void setRefreshed(){
		m_refreshed = true;		
	}
	
	/**
	 * Updates the ith axiom with the method that implements it
	 * @param m the implementation method
	 * @param i the axiom to set
	 */
	public void setAxiom(Method m, int i){
		if (i<m_nrAxioms+1)
			m_axioms[i] = m;
	}
	
	/**
	 * Add another child to the concept.
	 * @param child a concept that derives from this one
	 */
	public void addChild(Concept child) {
		m_children.add(child.getID());
	}
	
	/**
	 * Add another parent to the concept, filling its parents list.
	 * @param parent a concept from it derives
	 */
	public void addParent(Concept parent) {
		parent.addChild(this);
		if (m_parents.contains(0)) //If it descends from THING then OVERRITE
			m_parents.set(0, parent.getID());
		else 
			m_parents.add(parent.getID());
		m_attributes.addAll(parent.m_attributes);
		m_relations.addAll(parent.m_relations);
		addAxioms(parent.m_axioms);
		m_refreshed = true;
	}
	
	/**
	 * Add another axiom to the concept.
	 * @param m the method that implements the axiom
	 */
	public void addAxiom(Method m){
		Method[] newAxioms = new Method[m_axioms.length+1];
		for (int i=0; i<m_axioms.length; i++)
			newAxioms[i] = m_axioms[i];
		newAxioms[m_axioms.length] = m;
		m_nrAxioms ++; 
		m_axioms = newAxioms;
	}
	
	/**
	 * Add another attribute to the concept.
	 * @param at a concept's attribute
	 */
	public void addAttribute(Attribute at) {
		m_attributes.add(at.getID());
	}

	/**
	 * Add another relation to the concept.
	 * @param r a concept's relation
	 */
	public void addRelation(Relation r) {
		m_relations.add(r.getID());
	}
	
	/**
	 * Add a set of attributes to the concept.
	 * @param myAtts the array of attributes to update
	 * @param ats a set of concept's attribute
	 * @return the updated array of attributes
	 */
/*	public void addAttributes(LinkedList<Element> myAtts, LinkedList<Element> ats){ 
		if (ats.length==0)
			return myAtts;
		int[] auxil = new int[ats.length];
		int atInd, n=0;
		for (int i = 0; i < ats.length; i++) {
			atInd = getAttributePosition(myAtts, ats[i]);
			if (UNKNOWN == atInd)
				auxil[n++] = ats[i];
		}
		if (0==n)
			return myAtts;
		
		int[] newAtts = new int[myAtts.length + n];
		System.arraycopy(myAtts, 0, newAtts, 0, myAtts.length);
		System.arraycopy(auxil, 0, newAtts, myAtts.length, auxil.length);
		return newAtts;
	}*/

	/**
	 * Add a set of axioms to this concept
	 * @param axs the set of axioms to add
	 */
	public void addAxioms(Method[] axs) {
		for (int i = 0; i < axs.length; i++)
			m_axioms[i] = axs[i];
	}

	/**
	 * Propagate default values for the children of this concept
	 * @param onto the ontology where the concept is defined
	 * @throws OntoException
	 */
	public void propagateValues(Ontology onto) throws OntoException{
		Iterator<Integer> iter = m_attDefaultValues.keySet().iterator();
		int at = -1;
		while (iter.hasNext()) {
			at = iter.next();
			if (NoNameElement.UNKNOWN != at){
				Iterator<Integer> kids = m_children.iterator();
				while (kids.hasNext())
					onto.getConcept(kids.next()).setDefaultValue(m_attDefaultValues.get(at), at);
			}
		}
	}
	/******** PREDICATES */
	/**
	 *
	 * @param c
	 * @return true, if c is equal to this concept
	 */
	public boolean equals(Concept c){
		return m_name.equalsIgnoreCase(c.m_name);
	}

	/**
	 * Verifies if this concept has been visited to inherit attributes and relations
	 * @return true, if this concept has been visited to inherit attributes and relations
	 */
	public boolean alreadyRefreshed() {
		return m_refreshed;
	}
	
	/**
	 * Verifies if this concept descends exactly from the same parents
	 * @param c the concept to compare with
	 * @return true, if this concept has the same parents as c
	 */
	public boolean haveSameParents(Concept c){
		if (m_parents.size() != c.m_parents.size())
			return false;
		Iterator<Integer> iter = m_parents.iterator();
		while (iter.hasNext())
			if (! c.m_parents.contains(iter.next()))
				return false;
		return true;
	}
	
	/**
	 * Predicate that verifies if there is an indirect relation from this concept 
	 * to the received one.
	 * @param kbRelations the set of relations to look into
	 * @param target the potential target concept
	 * @return true, if this concept is related to the argument
	 */
/*	public boolean isRelatedTo(Relation[] kbRelations, Concept target){
		Iterator<Integer> iterR = m_relations.iterator();
		int r = -1;
		Relation rel = null;
		while (iterR.hasNext()) {
			r = iterR.next();
			rel = kb.ONTOLOGY.getRelation(r).toString() + "\n\t";
		}

		
		for (int i=0; i<m_relations.length; i++){
			Concept t = (Concept)kbRelations[m_relations[i]].getRange();
			if (t.equals(target) || target.descendsFrom(t))
				return true;
		}
		return false;
	}*/
		
	/**
	 * Predicate that verifies if the concept descends from the received one
	 * @param obj a potential parent
	 * @return true, if this concept descends from the argument
	 */
	public boolean descendsFrom(Concept obj){
		return m_parents.contains(obj.getID());
	}
	
	/**
	 * Verifies if this concept satisfies a given axiom
	 * @param axiomNr the axiom to verify
	 * @param params the set of parameters to the axiom method
	 * @return true, if the concept satisfies the axiom
	 */
	public boolean verifiesAxiom(int axiomNr, Object[] params){
		if (axiomNr<m_axioms.length)
			try{
				return (Boolean) m_axioms[axiomNr].invoke(new Axiom(), params); 
			} catch (Exception ex){
				ex.printStackTrace();
				return false;
			}
		else
			return false;	
	}

	/** ****** TRANSFORMERS */	
	/* 
	 * (non-Javadoc)
	 * @see pamda.ontologies.Element#toString()
	 */
	@Override
	public String toString(){
		String st =  m_name + "(" + m_id + ")"+" << ";
		Iterator<Integer> iter = m_parents.iterator();
		while (iter.hasNext())
			st += iter.next() + ",";
		st = st.substring(0, st.length() - 1) + "\n\t";
		// Prints its attributes
		iter = m_attributes.iterator();
		int at = -1;
		int f = -1;
		while (iter.hasNext()) {
			at = iter.next();
			st += at;
			if(m_id != 0) {
				f = m_attDefaultValues.get(at);
				if (NoNameElement.UNKNOWN != f)
					st += "=" + f;
			}
			st += "\n\t";
		}
		return st;
	}

	/**
	 * Returns a string representation for the concept, including its elements. 
	 * @param kb the knowledge base where the concept is defined
	 * @return a string for representing the concept
	 */
	public String toString(KnowledgeBase kb) {
		String st = m_name + "(" + m_id + ")" +" << ";
		// Prints its parents
		Iterator<Integer> iter = m_parents.iterator();
		while (iter.hasNext())
			st += kb.ONTOLOGY.getConcept(iter.next()).getName() + ",";
		st = st.substring(0, st.length() - 1) + "\n\t";
		// Prints its attributes
		iter = m_attributes.iterator();
		int at = -1;
		int f = -1;
		while (iter.hasNext()) {
			at = iter.next();
			st += kb.ONTOLOGY.getAttribute(at).toString();
			f = m_attDefaultValues.get(at);
			if (NoNameElement.UNKNOWN != f)
				st += kb.getFeature(f); 
			
			st += "\n\t";
		}
		// Prints its relations
		Iterator<Integer> iterR = m_relations.iterator();
		int r = -1;
		while (iterR.hasNext()) {
			r = iterR.next();
			st += kb.ONTOLOGY.getRelation(r).toString() + "\n\t";
		}
		st = st.substring(0, st.length() - 1) + "\n";
		return st;
	}
	
}

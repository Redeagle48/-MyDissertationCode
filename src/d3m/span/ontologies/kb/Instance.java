/**
 * PaMDA Project
 * PACKAGE - pamda.ontologies
 * Instance.java                         
 * 2009
 */
package d3m.span.ontologies.kb;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import ontologies.*;

/**
 * Class <B>Instance</> for handling instances.
 * @author Claudia Antunes
 * @version 2.0
 */
public class Instance extends NoNameElement{
	/** ****** FIELDS */
	/** The concept implemented by the instance. */
	int m_concept;

	/** The instance features: key=att.id value=feature.id. */
	HashMap<Integer, Integer> m_features;

	/** The instance relations. */
	int[] m_relations;

	/** ****** CONSTRUCTORS */
	/**
	 * Create an empty instance.
	 */
	public Instance() {
		super();
	}

	/**
	 * Creates an empty instantiated instance.
	 * @param concept the concept to instantiate
	 */
	public Instance(Concept concept) {
		super(KnowledgeBase.KB_NR_INSTANCES ++);
		m_concept = concept.getID();
		m_features = new HashMap<Integer, Integer>();
		m_relations = new int[concept.getRelations().size()];
		Iterator<Integer> atts = concept.getAttributes().iterator();
		while (atts.hasNext())
			m_features.put(atts.next(), NoNameElement.UNKNOWN);
		
		Arrays.fill(m_relations,NoNameElement.UNKNOWN);
	}

	/**
	 * Creates a new instance from another one
	 * @param obj the instance to copy
	 */
	public Instance(Instance obj){
		super(obj.m_id);
		m_concept = obj.m_concept;
		m_features = obj.m_features;

		m_relations = Arrays.copyOf(obj.m_relations, obj.m_relations.length);
	}
	
	/** ***** SELECTORS */
	/**
	 * Returns the concept instantiated
	 * @return the concept
	 */
	public int getConcept() {
		return m_concept;
	}
	
	// daniel
	/** ***** SELECTORS */
	/**
	 * Returns the features keys
	 * @return the features keys
	 */
	public HashMap<Integer, Integer> getFeatures() {
		return m_features;
	}

	/**
	 * Returns the size of known features
	 * @return the size of known features
	 */
	public int getFeaturesLength() {
		int c = 0;
		Iterator<Integer> values = m_features.values().iterator();
		while (values.hasNext())
			if (values.next() != NoNameElement.UNKNOWN)
				c++;
		return c;
	}
	//
	
	/**
	 * 
	 * @param at the index for the attribute to check
	 * @return true, if the attribute was already set
	 */
	public boolean attributeIsSet(int at){
		return NoNameElement.UNKNOWN != m_features.get(at); 
	}
	
	/** ****** MODIFIERS */	
	/**
	 * Adds another feature to its set of features
	 * @param f the feature to add
	 * @param kb the context knowledge base, where the feature is defined
	 * @throws OntoException whenever adding operation fails
	 */
	public void setFeature(Feature f, KnowledgeBase kb) throws OntoException{
		m_features.put(f.getAttribute(), f.getID());
	}
		
	/******** PREDICATES */		
	/**
	 * Verifies if exists some RelationImpl with in as the target, or there is
	 * an reflexed relation.
	 * @param onto
	 * @param in
	 * @return true, if this instance is directly related to in
	 */
/*	public boolean isDirectlyRelatedTo(Instance in, Ontology onto) {
		boolean ok = false;
		int i = 0;
		// Verifies direct relations
		while ((!ok) && (i < this.m_relations.length)) {
			ok = (in.equals(this.m_relations[i].getValue()));
			i++;
		}
		// Verify reflexed relations
		i = 0;
		while ((!ok) && (i < in.m_relations.length)) {
			ok = (this.equals(in.m_relations[i].getValue()));
			i++;
		}
		return ok;
	}*/
	
	/** ****** TRANSFORMERS */	
	/* 
	 * (non-Javadoc)
	 * @see pamda.ontologies.NoNameElement#toString()
	 */
	@Override
	public String toString() {
		String st = "ID=" + this.m_id + "(c="+m_concept+") " + "Features[";
		// Prints its features
		Iterator<Integer> iter = m_features.values().iterator();
		while (iter.hasNext()) {
			int f = m_features.get(iter.next());
			if (NoNameElement.UNKNOWN != f)
				st += f + ", ";
			st += "\n\t";
		}
		if (st.endsWith(", "))
			st = st.substring(0, st.length() - 2);
		st += "]\tRelations[  "; 
		// Prints its relations
		for (int i = 0; i < m_relations.length; i++)
			if (NoNameElement.UNKNOWN != m_relations[i])
				st += m_relations[i] + ", ";
		if (st.endsWith(", "))
			st = st.substring(0, st.length() - 2);
		st += "]\n"; 
		return st;
	}
	
	/**
	 * Returns a textual representation of this instance, in the context of a given kb
	 * @param kb the context knowledge base
	 * @return a string representing this instance 
	 */
	public String toString(KnowledgeBase kb) {
		String st;
		Concept c = kb.ONTOLOGY.getConcept(m_concept);
		if (null!=c)
			st = "Concept=" + c.getName() + "Features {";
		else
			st = "NULL" + "Features {";
		// Prints its features
		Iterator<Integer> iter = m_features.keySet().iterator();
		while (iter.hasNext()) {
			int f = m_features.get(iter.next());
			if (NoNameElement.UNKNOWN != f)
				st +=kb.getFeature(f).toString(kb) + ", ";
			st += "\n\t";
		}
		if (st.endsWith(", "))
			st = st.substring(0, st.length() - 2);
		st += "}\tRelations{  "; 
		// Prints its relations
		for (int i = 0; i < m_relations.length; i++)
			if (NoNameElement.UNKNOWN != m_relations[i])
				st += m_relations[i] + ", ";
		if (st.endsWith(", "))
			st = st.substring(0, st.length() - 2);
		st += "}\n"; 
		return st;
	}
}

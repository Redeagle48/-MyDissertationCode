/**
 * PaMDA Project
 * PACKAGE - pamda.ontologies
 * Feature.java                         
 * 2007
 */
package d3m.span.ontologies.kb;

import ontologies.*;
/**
 * Class <B>Feature</> for handling attribute values.
 * @author Claudia Antunes
 * @version 1.0
 */
public class Feature extends NoNameElement {
	/** The value for the attribute. */
	protected Object m_value;
	
	/** The attribute instantiated. */
	protected int m_attribute;
	
	/** The concept instantiated. */
	protected int m_concept;
	
	/** ****** CONSTRUCTORS *
	/**
	 * Creates an empty feature
	 */
	public Feature() {
		super();
		m_attribute = UNKNOWN;
		m_concept = UNKNOWN;
		m_value = null;
	}

	/**
	 * Creates an initialized feature.
	 * @param value the feature value
	 * @param nrF
	 * @param atId
	 * @param conceptId
	 */
	public Feature(Object value,int nrF, int atId, int conceptId) {
		m_attribute = atId; 
		m_concept = conceptId;
		if (m_value instanceof String)
			m_value = ((String)value).toLowerCase();
		else
			m_value = value;
		m_id = nrF;
	}
	
	/** ***** SELECTORS */
	/**
	 * Returns the attribute instantiated
	 * @return the attribute
	 */
	public int getAttribute() {
		return m_attribute;
	}
	
	/**
	 * Returns the value that instantiates the feature
	 * @return the value
	 */
	public Object getValue() {
		return m_value;
	}
	
	/**
	 * 
	 * @return the concept linked to this feature
	 */
	public int getConcept(){
		return m_concept;
	}

	/******** PREDICATES */	
	/**
	 * Compares this element with the received one
	 * @param obj the element to compare with
	 * @return -1, if this element is less than the received one, 
	 *          0 if they are equal
	 *          1 if it is greater than the other
	 */
	public int compareTo(Feature obj){
		// They are both for the same attribute
		if (m_attribute == obj.m_attribute){
			if (m_value instanceof String && obj.m_value instanceof String)
				return ((String)m_value).compareTo((String)obj.m_value);
			else
				return ((Integer)m_value).compareTo((Integer)obj.m_value);
		}
		return ((Integer)m_attribute).compareTo(obj.m_attribute);
	}
		
	/** ****** TRANSFORMERS */	
	/* 
	 * (non-Javadoc)
	 * @see pamda.ontologies.NoNameElement#toString()
	 */
	@Override
	public String toString() {
		if (null != m_value)
			return "ATT "+m_attribute + "=" +m_value.toString()+"(ID="+m_id+")";
		else
			return "";
	}

	/**
	 * Returns a textual representation for this feature in the context of the given kb
	 * @param kb the context knowledge base
	 * @return the representation of the feature in the context of the knowledge base
	 */
	public String toString(KnowledgeBase kb) {
		String st = kb.ONTOLOGY.getAttribute(m_attribute).toString() + " =";
		if (null != m_value){
			return st+=m_value.toString()+"(ID="+m_id+")";
		}
		else
			return "?";
	}
}

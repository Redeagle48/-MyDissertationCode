/**
 * PaMDA Project
 * PACKAGE - pamda.ontologies
 * Attribute.java                         
 * @author Cl�udia Antunes (claudia.antunes@ist.utl.pt)
 * 2007
 */
package d3m.span.ontologies;

/**
 * Class <B>Attribute</B> for handling attributes in concepts belonging 
 * to an ontology.
 * @author Claudia Antunes
 * @version 1.0
 */
public class Attribute extends Element{
	/** ****** FIELDS */
	/** The domain of the attribute. */
	protected Concept m_domain;

	/** The range of the attribute. */
	protected Object m_range;

	/** ****** CONSTRUCTORS */
	/**
	 * Default constructor
	 */
	public Attribute() {
		super();
	}

	/**
	 * Creates a named attribute.
	 * @param name the attribute name
	 * @param id attribute's id
	 */
	public Attribute(String name, int id) {
		super(id, name);
		m_domain = null;
		m_range = null;
	}

	/**
	 * Creates a new attribute from another
	 * @param at the attribute to copy
	 */
	public Attribute(Attribute at) {
		super(at.m_id, at.m_name);
		m_domain = at.m_domain;
		m_range = at.m_range;
	}

	/** ***** SELECTORS */
	/**
	 * Returns the domain of this attribute
	 * @return the domain
	 */
	public Concept getDomain() {
		return m_domain;
	}

	/**
	 * Returns the range of this attribute
	 * @return the range
	 */
	public Object getRange() {
		return m_range;
	}

	/** ******* MODIFIERS */
	/**
	 * Updates the domain for this attribute
	 * @param domain the domain to set
	 */
	public void setDomain(Concept domain) {
		m_domain = domain;
	}

	/**
	 * Updates the range for this attribute
	 * @param range the range to set
	 */
	public void setRange(Object range) {
			m_range = range;
	}
	
	/******** PREDICATES */
	/**
	 * Verifies if the range for this attribute is already set.
	 * @return true, if the range was already set
	 */
	public boolean isRangeSet(){
		return null!=m_range;
	}
	
	/**
	 * Compares this element with the received one
	 * @param obj the element to compare with
	 * @return -1, if this element is less than the received one, 
	 *          0 if they are equal
	 *          1 if it is greater than the other
	 */
	public int compareTo(Attribute obj){
		// They are both for the same attribute
		if (m_domain == obj.m_domain)
			return ((Integer)m_id).compareTo(obj.m_id);
		return m_domain.compareTo(obj.m_domain);
	}
	
	/** ****** TRANSFORMERS */	
	/* 
	 * (non-Javadoc)
	 * @see pamda.ontologies.Element#toString()
	 */
	@Override
	public String toString() {
		String st = m_name + "(" + m_id +")"; // ":" + m_domain.getName() + " --> ";
		
		return st;
	}
	
}

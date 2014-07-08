/**
 * PaMDA Project
 * PACKAGE - pamda.ontologies
 * Element.java                         
 * @author Cl�udia Antunes (claudia.antunes@ist.utl.pt)
 * 2009
 */
package d3m.span.ontologies;

/**
 * Class <Code><B>Element</B></Code> for handling elements in an ontology.
 * @author Claudia Antunes
 * @version 1.0
 */
public abstract class Element extends NoNameElement{
	/** ****** FIELDS */
	/** The element's name. */
	protected String m_name;

	/** ****** MEMBERS */ 
	/**	 
	 * Default constructor
	 */
	public Element(){
		super();		
	}
	
	/**
	 * Creates an initialized element.
	 * @param id the identifier for this element
	 */
	public Element(int id){
		super(id);
	}

	/**
	 * Creates an initialized element with id and name.
	 * @param id the identifier for this element
	 * @param name the name for this element
	 */
	public Element(int id, String name){
		super(id);
		m_name = name.toLowerCase();
	}
	
	/**
	 * Creates an element equal to the one received
	 * @param el the element to copy
	 */
	public Element(Element el){
		super(el);
		m_name = el.m_name;
	}
	
	/** ****** SELECTORS */
	/**
	 * Returns the name of this element.
	 * @return the name
	 */
	public String getName() {
		return m_name;
	}

	/** ****** MODIFIERS */
	/**
	 * Updates the name for this element
	 * @param name the name to set
	 */
	public void setName(String name) {
		m_name = name.toLowerCase();
	}

	/******** PREDICATES */
	/**
	 * 
	 * @param el
	 * @return true, if el equals this element
	 */
	public boolean equals(Element el){
		return m_name.equalsIgnoreCase(el.m_name);
	}
	
	/** ****** TRANSFORMERS */	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return m_name + "(" + m_id +")";
	}
}

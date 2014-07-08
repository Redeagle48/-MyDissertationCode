/**
 * PaMDA Project
 * PACKAGE - pamda.ontologies
 * Element.java                         
 * @author Cl���udia Antunes (claudia.antunes@ist.utl.pt)
 * 2009
 */
package d3m.span.ontologies;

/**
 * Class <Code><B>Element</B></Code> for handling elements in an ontology.
 * @author Claudia Antunes
 * @version 1.0
 */
public abstract class NoNameElement implements Comparable<NoNameElement> {
	/** ****** FIELDS */
	static public int UNKNOWN = -1;
	
	/** Defines a unique identifier for the elements in the ontology. */
	protected int m_id;

	/** ****** MEMBERS */ 
	/**	 
	 * Default constructor
	 */
	public NoNameElement(){
		m_id = UNKNOWN;		
	}
	
	/**
	 * Creates an initialized element.
	 * @param id the identifier for this element
	 */
	public NoNameElement(int id){
		m_id = id;
	}

	/**
	 * Creates an element equal to the one received
	 * @param el the element to copy
	 */
	public NoNameElement(NoNameElement el){
		m_id = el.m_id;
	}
	
	/** ****** SELECTORS */
	/**
	 * Returns the identifier for this element
	 * @return the identifier for this element
	 */
	public int getID(){
		return m_id;
	}
	
	/** ****** MODIFIERS */
	/**
	 * Updates the identifier for this element
	 * @param key the new identifier for the element
	 */
	public void setID(int key){
		m_id = key;
	}

	/******** PREDICATES */
	/**
	 * Looks for an element in array of sorted elements, following a binary search
	 * @param elems the array of sorted elements
	 * @param el the element to look for
	 * @return the index for el in elems, based on el's ID
	 */
	public static int binarySearch(NoNameElement[] elems, int el){
		int first = 0, last = elems.length-1, mid = (first + last)/2;
		while (first < last){
			if (el == elems[mid].m_id)
				return mid;
			if (el < elems[mid].m_id)
				last = mid - 1;
			else
				first = mid + 1;
		}
		return -1;	
	}
	
	/**
	 * Compares this element with the received one
	 * @param el the element to compare with
	 * @return -1, if this element is less than the received one, 
	 *          0 if they are equal
	 *          1 if it is greater than the other
	 */
	@Override
	public int compareTo(NoNameElement el){
		return Integer.valueOf(m_id).compareTo(Integer.valueOf(el.m_id));	
	}
	
	/** ****** TRANSFORMERS */	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.valueOf(m_id);
	}
}

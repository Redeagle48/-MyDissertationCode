/**
 * PaMDA Project
 * PACKAGE - pamda.ontologies
 * Relation.java                         
 * @author Cl�udia Antunes (claudia.antunes@ist.utl.pt)
 * 2007
 */
package d3m.span.ontologies;

/**
 * Class <Code><B>Relation</B></Code> for handling relations in an ontology.
 * @author Claudia Antunes
 * @version 1.0
 */
public class Relation extends Attribute {
	/** ****** CONSTRUCTORS */ 
	/**
	 * Default constructor
	 */
	public Relation() {
		super();
	}

	/**
	 * Creates an instantiated relation.
	 * @param at the attribute from it descends
	 * @param id the identifier for this relation
	 * @param active the active element in the relation
	 * @param passive the passive element in the relation
	 */
	public Relation(Attribute at, int id, Concept active, Concept passive) {
		super(at);
		m_id = id;
		m_domain = active;
		m_range = passive;
	}
	
	/**
	 * Creates a new relation from another
	 * @param r the relation to copy
	 * @param id the unique identifier
	 */
	public Relation(Relation r, int id) {
		super(r);
		m_domain = r.m_domain;
		m_range = r.m_range;
		m_id = id;
	}
	
	/** ****** TRANSFORMERS */	
	/* 
	 * (non-Javadoc)
	 * @see pamda.ontologies.Element#toString()
	 */
	@Override
	public String toString() {
		String st = "REL "+ m_name + ": " + m_domain.getName() + " --> ";
		st += ((Concept) m_range).getName();
		return st;
	}

}

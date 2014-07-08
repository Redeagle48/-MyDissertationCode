/**
 * PaMDA Project
 * PACKAGE - pamda.ontologies
 * RelationIsA.java                         
 * @author Cl�udia Antunes (claudia.antunes@ist.utl.pt)
 * 2007
 */
package d3m.span.ontologies;

/**
 * Class <Code><B>Concept</B></Code> for handling IS-A relations in an ontology.
 * @author Claudia Antunes
 * @version 1.0
 */
public class RelationIsA extends Relation {
	/** ****** FIELDS */
	static String IS_A = "is_a";
	/** ****** CONSTRUCTORS */
	/**
	 * Creates an empty is-a relation.
	 */
	public RelationIsA() {
		super();
	}

	/**
	 * Creates an instantiated is-a relation.
	 * @param id the index for this relation
	 * @param child the concept that is a subclass
	 * @param parent the superclass
	 */
	public RelationIsA(int id, Concept child, Concept parent) {
		super();
		m_id = id;
		m_name = IS_A;
		m_domain = child;
		m_range = parent;
	}

}

/**
 * PaMDA Project
 * PACKAGE - pamda.ontologies
 * Axiom.java                          
 * @author Cl�udia Antunes (claudia.antunes@acm.org)
 * 2007
 */
package d3m.span.ontologies;

/**
 * Class <CODE><B>Axiom</B></CODE> for dealing with axioms in an ontology. 
 * Axioms are hard-coded in this java file.
 * @author Claudia Antunes
 * @version 1.0
 */
public class Axiom {

	/**
	 * Defines the implementation of simple definition axioms, that are run to set the 
	 * differentiation values.
	 * @param c
	 * @param diffAtt
	 */
	static public void definitionAxiom(Concept c, Integer diffAtt){
		c.setPK(diffAtt);
	}
/*	static public boolean equalBonds(Concept c1, Concept c2){
		int index = c1.getAttributeIndex(Bond.BOND_TYPE);
		AttributeValue defaultValue1 = c1.getAttribute(index).getDefaultValue();
		AttributeValue defaultValue2 = c2.getAttribute(index).getDefaultValue();
		if (defaultValue1.equals(defaultValue2)){
			index = c1.getRelationIndex(Bond.BOND_1ST_AT);
			Concept defaultR1 = (Concept)c1.getRelation(index).getRange();
			Concept defaultR2 = (Concept)c2.getRelation(index).getRange();
			if (defaultR1.equals(defaultR2)){
				index = c1.getRelationIndex(Bond.BOND_2ND_AT);
				defaultR1 = (Concept)c1.getRelation(index).getRange();
				defaultR2 = (Concept)c2.getRelation(index).getRange();
				return (defaultR1.equals(defaultR2));
			}
			return false;
		}
		return false;
	}*/

	
}

package d3m.span.io.rulesWriters;

import d3m.span.constraints.SeqD2Rule;

public class PrecedenceRules extends RuleWriter{
	
	public PrecedenceRules(){}
	
	@Override
	public SeqD2Rule[] writeRules(String[] item,int[] gap, int ruleCounter, int constraintCounter, boolean[] isParallel){

		int antecedent = Integer.parseInt(item[0]);
		int consequent = Integer.parseInt(item[1]);
		int rule_order = ++ruleCounter;
		int constraint_order = constraintCounter;
		int gap_before_constraint = gap[0];
		int gap_between_constraint = gap[1];
		boolean isParallel_before = isParallel[0];
		boolean isParallel_inside = isParallel[1];

		SeqD2Rule rule1 = new SeqD2Rule
				((short)rule_order,		// rule
						(short)constraint_order, 		// restriction
						(short)gap_before_constraint, 		// gap_interTransaction
						(short)0, 		// gap_intraRestriction
						(short)0, 		// itemset
						(short)antecedent,		// item
						isParallel_before);		// isParallel

		rule_order = ++ruleCounter;
		
		SeqD2Rule rule2 = new SeqD2Rule
				((short)rule_order,		// rule
						(short)constraint_order, 		// restriction
						(short)gap_between_constraint, 		// gap_interTransaction
						(short)0, 		// gap_intraRestriction
						(short)0, 		// itemset
						(short)consequent,		// item
						isParallel_inside);		// isParallel

		return new SeqD2Rule[]{rule1,rule2};
	}
}

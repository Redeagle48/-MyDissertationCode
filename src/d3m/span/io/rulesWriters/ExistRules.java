package d3m.span.io.rulesWriters;

import d3m.span.constraints.SeqD2Rule;

public class ExistRules extends RuleWriter {
	
	public ExistRules(){}

	@Override
	public SeqD2Rule[] writeRules(String[] item, int[] gap, int ruleCounter, int constraintCounter, boolean[] isParallel) {
		
		int item_int = Integer.parseInt(item[0]);
		int rule_order = ++ruleCounter;
		int constraint_order = constraintCounter;
		int gap_to_be_inserted = gap[0];
		boolean isParallel_before = isParallel[0];
		
		SeqD2Rule rule = new SeqD2Rule
				((short)rule_order,		// rule
						(short)constraint_order, 		// restriction
						(short)gap_to_be_inserted, 		// gap_interTransaction
						(short)0, 		// gap_intraRestriction
						(short)0, 		// itemset
						(short)item_int,		// item
						isParallel_before);		// isParallel
		return new SeqD2Rule[]{rule};
	}

}

package d3m.span.io.rulesWriters;

import d3m.span.constraints.SeqD2Rule;

public abstract class RuleWriter {

	/**
	 * 
	 * @param item
	 * @param gap
	 * @param ruleCounter -> rule cardinal already written
	 * @param constraintCounter -> constraint cardinal already written
	 * @return
	 */
	public abstract SeqD2Rule[] writeRules(String[] item,int[] gap, int ruleCounter, int constraintCounter, boolean[] isParallel);
	
}

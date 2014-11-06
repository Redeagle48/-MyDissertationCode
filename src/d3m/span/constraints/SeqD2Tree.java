package d3m.span.constraints;

import d3m.span.constraints.SeqD2State.State;
import d3m.span.core.SeqItemset;
import d3m.span.core.SeqSequence;
import d3m.span.core.Taxonomy.ComposedElement;


public class SeqD2Tree {

	SeqD2Rule[] rules;

	SeqD2Tree(SeqD2Rule[] rules){
		this.rules = rules;
	}

	SeqD2Tree(){
		this.rules = new SeqD2Rule[0];
	}

	SeqD2Tree(SeqD2Rule rule){
		this.rules = new SeqD2Rule[]{rule};
	}

	SeqD2Rule[] getRules(){ return this.rules; }
	SeqD2Rule getRule(int pos) {
		return rules.length > pos ? rules[pos] : null;
	}

	void addRule(SeqD2Rule rule){
		SeqD2Rule[] newRules = new SeqD2Rule[1+this.rules.length];
		System.arraycopy(this.rules, 0, newRules, 0, this.rules.length);
		newRules[newRules.length-1] = rule;
		this.rules = newRules;
	}

	void addRule(SeqD2Rule[] rules) {
		SeqD2Rule[] newRules = new SeqD2Rule[rules.length+this.rules.length];
		System.arraycopy(this.rules, 0, newRules, 0, this.rules.length);
		System.arraycopy(rules, 0, newRules, this.rules.length, rules.length);
		this.rules = newRules;
	}

	/**
	 * Validates the state according to the rules
	 * @param sequence
	 */
	boolean validate(short item, SeqD2State sequence_state, SeqSequence sequence, ComposedElement taxonomy) {

		if(this.rules.length != 0 && this.rules.length >= sequence_state.getRule()) { // Protect when there are no rules or all the rules where passed

			// actual size of the sequence to be analyzed
			int sequence_size = sequence.size();
			sequence_state.setCurrentItemset((short)sequence_size);
			
			SeqItemset itemset = sequence.getItemsetAt(sequence_size-1);
			
			int res = this.rules[sequence_state.getRule()-1].validate(item, sequence_state, sequence_size, itemset, taxonomy);
			if (res == -1) return false; // rejecting the sequence

			// when is inside the gap but not the item
			if(res == 0) {

			}

			if (res == 1){
				if(sequence_state.getRule() < rules.length ) {

					SeqD2Rule newRule = rules[(sequence_state.getRule()-1)+1]; // rule nr. is +1 than in this.rules position
					
					short currentItemset = sequence_state.getCurrentItemset();
					int startRuleItemset = (newRule.getIsParallel() ? sequence_size : sequence_size+1);
					short max_allowed_itemset = 
							(newRule.isParallel? currentItemset : (short) (newRule.getMax_Gap() + startRuleItemset));
					short rule = newRule.getRule();
					short restriction = newRule.getRestriction();
					
					State new_RuleState = 
							(sequence_state.getRuleState() == State.PASS ? State.IN : sequence_state.getRuleState());
					
					SeqD2State new_sequence_state = new SeqD2State(
							max_allowed_itemset,		//max_allowed_itemset
							rule,						//rule
							restriction,				//restriction
							currentItemset,				//itemset
							(short)startRuleItemset,	//startRuleItemset
							//false,					//insideRestriction --> MODIFICAR
							new_RuleState				//ruleState
							//new_RestrictionState,		//restrictionState
							);

					sequence.setState(new_sequence_state);
					
				} 
				// when all the rules were passed
				else {
					sequence_state.setRuleState(State.ALL_PASSED);
				}
			}

		}
		return true;
	}
	
	public void printRules(){
		//System.out.println("\n=====>>> Printing rules of tree");
		//for (SeqD2Rule seqD2Rule : this.rules) {
		//	System.out.println(seqD2Rule.toString());
		//}
		//System.out.println("===============================\n");
	}
}
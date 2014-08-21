package d3m.span.constraints;

import d3m.span.constraints.SeqD2State.State;
import d3m.span.core.SeqSequence;


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
	boolean validate(short item, SeqD2State sequence_state, SeqSequence sequence) {

		/*
		if(this.rules.length != 0 && this.rules.length >= sequence_state.getRule()) { // Protect when there are no rules or all the rules where passed
			int sequence_size = sequence.size();
			int res = this.rules[sequence_state.getRule()-1].validate(item, sequence_state, sequence_size);
			if (res == -1) return false;

			if(res == 1) {

				if(sequence_state.getRule() < rules.length ) {

					SeqD2Rule newRule = rules[(sequence_state.getRule()-1)+1]; // rule nr. is +1 than in this.rules position

					short currentItemset = sequence_state.getCurrentItemset();
					short max_allowed_itemset = (short) (newRule.getMax_Gap() + sequence_state.getCurrentItemset());
					short rule = newRule.getRule();
					short restriction = newRule.getRestriction();

					State new_RestrictionState = 
							(sequence_state.getRestrictionState() == State.PASS ? State.OUT : sequence_state.getRestrictionState());

					SeqD2State new_sequence_state = new SeqD2State(
							max_allowed_itemset,		//max_allowed_itemset
							rule,						//rule
							restriction,				//restriction
							currentItemset,				//itemset
							false,						//insideRestriction --> MODIFICAR
							State.IN,					//ruleState
							new_RestrictionState);		//restrictionState

					sequence.setState(new_sequence_state);

				}

				else if (sequence_state.getRule() == rules.length) {
					sequence_state.setAllPassedRestrictionState();
				}
			}
		}
		return true;
		 */

		
	}

}

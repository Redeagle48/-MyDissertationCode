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
		if(this.rules.length != 0) { // Protect when there are no rules
			int res = this.rules[sequence_state.getRule()-1].validate(item, sequence_state);
			if (res == -1) return false;

			if(res == 1) {
				if(sequence_state.getRule() <= rules.length ) {

					SeqD2Rule newRule = rules[(sequence_state.getRule()-1)+1]; // rule nr. is +1 than in this.rules

					short itemset = newRule.getItemset();
					short rule = newRule.getRule();
					short restriction = newRule.getRestriction();

					State new_RestrictionState = 
							(sequence_state.getRestrictionState() == State.PASS ? State.OUT : sequence_state.getRestrictionState());

					SeqD2State new_sequence_state = new SeqD2State(
							(short)0,					//gap_interTransaction
							(short)0,					//gap_intraRestriction
							rule,						//rule
							restriction,				//restriction
							itemset,					//itemset
							false,						//insideRestriction
							State.IN,					//ruleState
							new_RestrictionState);		//restrictionState
					
					sequence.setState(new_sequence_state);

				}
				else{
					//JA PASSOU TODAS AS RESTRICOES
				}
			}
		}
		return true; // Esta funcao tem de retornar o novo estado para ser atribuido ao SeqSequence
	}

}

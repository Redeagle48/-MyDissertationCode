package d3m.span.constraints;

import d3m.span.constraints.SeqD2State.State;


public class SeqD2Rule {
	
	final short k = 1;
	short rule;
	short restriction;
	short gap_interTransaction, gap_intraRestriction;
	short itemset;
	short item; // item to be discovered
	
	short pos; // position in the analyzed sequence
	
	SeqD2Rule(short posAcumulated, short rule, short restriction, 
			short gap_interTransaction, short gap_intraRestriction, short itemset, short item){
		this.rule = rule;
		this.restriction = restriction;
		this.gap_interTransaction = gap_interTransaction;
		this.gap_intraRestriction = gap_intraRestriction;
		this.itemset = itemset;
		this.item = item;
		this.pos = posAcumulated;
	}
	
	short getRestriction() { return this.restriction; }
	short getRule() { return this.rule; }
	short getItemset() { return this.itemset; }
	short getGap_interTransaction() { return this.gap_interTransaction; }
	short getGap_intraRestriction() { return this.gap_intraRestriction; }
	short getItem() { return this.item; }

	
	/**
	 * 
	 * @param item
	 * @param sequence_state
	 * @return
	 * 	-1 if the sequence is not allowed
	 * 	0 if the sequence is allowed but not pass the rule
	 *  1 if the rule is passed
	 */
	public int validate(short item, SeqD2State sequence_state) {
		if(item == this.item) {
			sequence_state.setPassRuleState();
			sequence_state.resetVariable(gap_interTransaction);
			//if(verify_if_the_restriction_is_passed_too){
				//sequence_state.resetVariable(gap_intraRestriction);
			//}
			return 1; // CARREGA NOVO RULE
		}
		
		if(sequence_state.getGap_interTransaction() < this.gap_interTransaction &&
				sequence_state.getRestrictionState() == State.OUT){
			sequence_state.incVariable(sequence_state.gap_interTransaction);
			return 0;
		} else if(sequence_state.getGap_intraRestriction() < this.gap_intraRestriction &&
				sequence_state.getRestrictionState() == State.IN){
			sequence_state.incVariable(sequence_state.gap_intraRestriction);
			return 0;
		} // TEORICAMENTE JA VIU A SITUACAO EM QUE A RESTRICAO FOI PASSADA
		
		return -1;
	}
}

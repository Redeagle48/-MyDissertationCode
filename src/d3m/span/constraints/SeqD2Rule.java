package d3m.span.constraints;

import d3m.span.constraints.SeqD2State.State;


public class SeqD2Rule { // Dividir em rules para intra e inter restricoes

	short rule;
	short restriction;
	short max_gap;
	short itemset;
	short item; // item to be discovered
	boolean isParallel;

	short pos; // position in the analyzed sequence

	public SeqD2Rule(short rule, short restriction, 
			short gap_interTransaction, short gap_intraRestriction, short itemset, short item, boolean isParallel){
		this.rule = rule;
		this.restriction = restriction;
		this.max_gap = gap_interTransaction;
		this.itemset = itemset;
		this.item = item;
		this.isParallel = isParallel;
	}

	short getRestriction() { return this.restriction; }
	short getRule() { return this.rule; }
	short getItemset() { return this.itemset; }
	short getMax_Gap() { return this.max_gap; }
	short getItem() { return this.item; }
	boolean getIsParallel() { return this.isParallel; }


	/**
	 * 
	 * @param item
	 * @param sequence_state
	 * @return
	 * 	-1 if the sequence is not allowed
	 * 	0 if the sequence is allowed but not pass the rule
	 *  1 if the rule is passed
	 */
	public int validate(short item, SeqD2State sequence_state, int currentItemset) {

		if(sequence_state.getMax_Allowed_Itemset() >= currentItemset) {
			
			if(sequence_state.getRuleState() == State.ALL_PASSED) {
				if(sequence_state.getItemFoundItemset() == currentItemset) {
					return 1;
				} else{
					return -1;
				}
			}
			
			// when we found the item we were looking for
			if(item == this.item && sequence_state.getRuleState() == State.IN
					&& sequence_state.getStartRuleItemset() <= currentItemset) {
				sequence_state.setPassRuleState(); // Change rule state to pass
				sequence_state.setItemFoundItemset((short)currentItemset);
			
				return 1; // CASO EM QUE CARREGA NOVO RULE
			}
			else {
				return 0;
			}
			
		}
		return -1;
	}
	
	@Override
	public String toString(){
		String output = "Rule nr: " + this.rule + "\n"
						+ "Restriction nr: " + this.restriction + "\n"
						+ "Gap Allowed: " + this.max_gap + "\n"
						+ "Item: " + this.item + "\n"
						+ "Is Parallel? " + this.isParallel;
		return output;
	}
}

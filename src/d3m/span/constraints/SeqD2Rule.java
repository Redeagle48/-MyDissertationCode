package d3m.span.constraints;

import d3m.span.constraints.SeqD2State.State;
import d3m.span.core.SeqItemset;


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

	public short getRestriction() { return this.restriction; }
	public short getRule() { return this.rule; }
	public short getItemset() { return this.itemset; }
	public short getMax_Gap() { return this.max_gap; }
	public short getItem() { return this.item; }
	public boolean getIsParallel() { return this.isParallel; }


	/**
	 * 
	 * @param item
	 * @param sequence_state
	 * @return
	 * 	-1 if the sequence is not allowed
	 * 	0 if the sequence is allowed but not pass the rule
	 *  1 if the rule is passed
	 */
	public int validate(short item, SeqD2State sequence_state, int currentItemset, SeqItemset itemset) {

		if(sequence_state.getMax_Allowed_Itemset() >= currentItemset) {
			
			if(sequence_state.getRuleState() == State.ALL_PASSED) {
				if(sequence_state.getItemFoundItemset() == currentItemset) {
					return 1;
				} else{
					return -1;
				}
			}
			
			// when we found the item we were looking for TODO ESTA A VER O INDEX E NAO O ITEM EM SI
			if((item == this.item /*|| itemset.contains(this.item)*/) // verify if the item is the current to be analyzed or is in the current itemset
					&& sequence_state.getRuleState() == State.IN
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
						+ "Gap Allowed: " + (!this.isParallel? this.max_gap : "(no applied here)") + "\n"
						+ "Item: " + this.item + "\n"
						+ "Is Parallel? " + this.isParallel;
		return output;
	}
}

package d3m.span.constraints;

import d3m.span.constraints.SeqD2State.State;


public class SeqD2Rule { // Dividir em rules para intra e inter restricoes

	short rule;
	short restriction;
	short max_gap;
	short itemset;
	short item; // item to be discovered

	short pos; // position in the analyzed sequence

	SeqD2Rule(short rule, short restriction, 
			short gap_interTransaction, short gap_intraRestriction, short itemset, short item){
		this.rule = rule;
		this.restriction = restriction;
		this.max_gap = gap_interTransaction;
		this.itemset = itemset;
		this.item = item;
	}

	short getRestriction() { return this.restriction; }
	short getRule() { return this.rule; }
	short getItemset() { return this.itemset; }
	short getMax_Gap() { return this.max_gap; }
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
	public int validate(short item, SeqD2State sequence_state, int currentItemset) {

		// Se passou todas as restricoes rejeita tudo o resto que vier a seguir (itemsets para a frente)
		if(sequence_state.getRestrictionState() == State.ALL_PASSED) {
			if(currentItemset == sequence_state.getMax_Allowed_Itemset()){  // --> TEM DE ACEITAR OS ITEMS DO MESMO ITEMSET 
				return 0;
			}
			return -1;
		}

		int currentItemset_tmp = currentItemset;

		if(sequence_state.getMax_Allowed_Itemset() >= currentItemset_tmp) {
			if(item == this.item) {
				sequence_state.setPassRuleState();
				//if(verify_if_the_restriction_is_passed_too){
				//sequence_state.resetVariable(gap_intraRestriction);
				//}

				if(!sequence_state.getIsToCount()) // DISCOVER '3' --> (1) NO PATTERN --> (1 2) NO PATTERN --> (1 2 3) PATTERN ---> (1 2 3 ...) PATTERN
					sequence_state.setIsToCount(true);

				return 1; // CASO EM QUE CARREGA NOVO RULE
			}


			// Escrever isto melhor
			if(currentItemset_tmp != sequence_state.getLastVisitedItemset() // para items paralelos
					/*&& sequence_state.getRestrictionState() == State.OUT*/)
			{
				sequence_state.setCurrentItemset((short)currentItemset);
				sequence_state.setLastVisitedItemset((short)currentItemset);

				//if(sequence_state.getMax_Allowed_Itemset() == currentItemset_tmp){
				//	if(sequence_state.getIsToCount())
				//		sequence_state.setIsToCount(false);
				//}
				//return 0;

			} //else if (sequence_state.getMax_Allowed_Itemset() >= currentItemset_tmp){
				if(sequence_state.getMax_Allowed_Itemset() == currentItemset_tmp){
					if(sequence_state.getIsToCount())
						sequence_state.setIsToCount(false);
				}
				return 0;
			//}
		}
		return -1;

	}
}

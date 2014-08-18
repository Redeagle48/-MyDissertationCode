package d3m.span.constraints;


public class SeqD2State {
	
	public enum State {
	    OUT,IN,PASS // OUT -> outside rule/restriction; IN -> inside ...; PASS -> validated rule/restriction
	}
	
	short gap_interTransaction, gap_intraRestriction; // current gap inter and intra restrictions
	short rule; // rule number
	short restriction; // restriction number (can be composed by 1 or more rules)
	short itemset; // current itemset
	boolean insideRestriction; // validating inside the restriction
	State ruleState, restrictionState;
	
	public SeqD2State () {
		gap_interTransaction = 0;
		gap_intraRestriction = 0;
		rule = 1; // first rule to be validated
		restriction = 1; // first restriction to be validated
		itemset = 1; // first itemset
		insideRestriction = false;
		ruleState = State.OUT;
		restrictionState = State.OUT;
	}
	
	public SeqD2State(short gap_interTransaction, short gap_intraRestriction, short rule,
			short restriction, short itemset, boolean insideRestriction, State ruleState, 
			State restrictionState) {
		this.gap_interTransaction = gap_interTransaction;
		this.gap_intraRestriction = gap_intraRestriction;
		this.rule = rule;
		this.restriction = restriction;
		this.itemset = itemset;
		this.insideRestriction = insideRestriction;
		this.ruleState = ruleState;
		this.restrictionState = restrictionState;
	}
	
	short getRestriction() { return this.restriction; }
	short getRule() { return this.rule; }
	short getItemset() { return this.itemset; }
	boolean isInsideRestriction() { return this.insideRestriction; }
	State getRuleState() { return this.ruleState; }
	State getRestrictionState() { return this.restrictionState; }
	short getGap_interTransaction() { return this.gap_interTransaction; }
	short getGap_intraRestriction() { return this.gap_intraRestriction; }
	
	void incVariable(short var) { var++; }
	void resetVariable(short var) { var = 0; }
	
	void setInsideRestriction() { this.insideRestriction = true; }
	void setOutsideRestriction() { this.insideRestriction = false; }
	void changeInsideRestriction() { this.insideRestriction = !this.insideRestriction; }
	
	void setInsideRuleState() { this.ruleState = State.IN; }
	void setPassRuleState() { this.ruleState = State.PASS; }
	
	void setOusideRestrictionState() { this.restrictionState = State.OUT; }
	void setInsideRestrictionState() { this.restrictionState = State.IN; }
	void setPassRestrictionState() { this.restrictionState = State.PASS; }
}

package d3m.span.constraints;


public class SeqD2State {
	
	public enum State {
	    OUT,IN,PASS // OUT -> outside rule/restriction; IN -> inside ...; PASS -> validated rule/restriction
	    , ALL_PASSED // ALL RULES/RESTRICTIONS WERE ACCEPTED
	}
	
	short max_allowed_itemset;
	short rule; // rule number
	short restriction; // restriction number (can be composed by 1 or more rules)
	short currentItemset; // current itemset
	boolean insideRestriction; // validating inside the restriction
	State ruleState, restrictionState;
	
	short lastVisitedItemset;
	boolean isToCount;
	
	short lastAcceptedVisitedItemset; // last itemset number of the longest pattern accepted
	
	public SeqD2State () {
		this.max_allowed_itemset = 1;
		rule = 1; // first rule to be validated
		restriction = 1; // first restriction to be validated
		currentItemset = 1;
		insideRestriction = false;
		ruleState = State.OUT;
		restrictionState = State.OUT;
		lastVisitedItemset = -1;
		isToCount = true;
		lastAcceptedVisitedItemset = -1;
	}
	
	public SeqD2State(short max_allowed_itemset, short rule,
			short restriction, short itemset, boolean insideRestriction, State ruleState, 
			State restrictionState) {
		this.max_allowed_itemset = max_allowed_itemset;
		this.rule = rule;
		this.restriction = restriction;
		this.currentItemset = itemset;
		this.insideRestriction = insideRestriction;
		this.ruleState = ruleState;
		this.restrictionState = restrictionState;
		this.isToCount = true;
		this.lastAcceptedVisitedItemset = -1;
	}
	
	short getRestriction() { return this.restriction; }
	short getRule() { return this.rule; }
	short getCurrentItemset() { return this.currentItemset; }
	boolean isInsideRestriction() { return this.insideRestriction; }
	State getRuleState() { return this.ruleState; }
	State getRestrictionState() { return this.restrictionState; }
	short getMax_Allowed_Itemset() { return this.max_allowed_itemset; }
	short getLastVisitedItemset() { return this.lastVisitedItemset; }
	public boolean getIsToCount() { return this.isToCount; }
	short getLastAcceptedVisitedItemset() { return this.lastAcceptedVisitedItemset; }
	
	void incVariable(short val) { val++; }
	void resetVariable(short val) { val = 0; }
	
	void setCurrentItemset(short val) { this.currentItemset = val; }
	void setMax_Allowed_Itemset(short val) { this.max_allowed_itemset = val; }
	
	void setInsideRestriction() { this.insideRestriction = true; }
	void setOutsideRestriction() { this.insideRestriction = false; }
	void changeInsideRestriction() { this.insideRestriction = !this.insideRestriction; }
	
	void setInsideRuleState() { this.ruleState = State.IN; }
	void setPassRuleState() { this.ruleState = State.PASS; }
	
	void setOusideRestrictionState() { this.restrictionState = State.OUT; }
	void setInsideRestrictionState() { this.restrictionState = State.IN; }
	void setPassRestrictionState() { this.restrictionState = State.PASS; }
	void setAllPassedRestrictionState() { this.restrictionState = State.ALL_PASSED; }
	
	void setLastVisitedItemset(short val) { this.lastVisitedItemset = val; }
	
	void setIsToCount(boolean val) { this.isToCount = val; }
	
	void setLastAcceptedVisitedItemset(short val) { this.lastAcceptedVisitedItemset = val; }
	
	public SeqD2State cloneState(){
		SeqD2State clone = new SeqD2State(); //better to explicitly assign the values
		
		clone.max_allowed_itemset = this.max_allowed_itemset;
		clone.rule = this.rule;
		clone.restriction = this.restriction;
		clone.currentItemset = this.currentItemset;
		clone.insideRestriction = this.insideRestriction;
		clone.ruleState = this.ruleState;
		clone.restrictionState = this.restrictionState;
		clone.lastVisitedItemset = this.lastAcceptedVisitedItemset;
		clone.isToCount = this.isToCount;
		clone.lastAcceptedVisitedItemset = this.lastAcceptedVisitedItemset;
		
		
		return clone;
	}
	
	public String toString(){
		String output = "Max Allowed Itemset: " + this.max_allowed_itemset + "\n"
				+ "Current Itemset: " + this.currentItemset + "\n"
				+ "Is to count? " + (this.isToCount? "yes" : "no") + "\n"
				+ "Rule: " + this.rule + "\n"
				+ "Restriction: " + this.restriction + "\n"
				+ "Rule State: " + this.ruleState + "\n"
				+ "Inside Restriction? " + this.insideRestriction + "\n"
				+ "Restriction State: " + this.restrictionState + "\n"
				+ "All Passed? " + (this.restrictionState == State.ALL_PASSED? "yes" : "no");
		
		return output;
	}
}

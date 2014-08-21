package d3m.span.constraints;


public class SeqD2State {
	
	public enum State {
		NO_RULES,
	    OUT,IN,PASS // OUT -> outside rule/restriction; IN -> inside ...; PASS -> validated rule/restriction
	    , ALL_PASSED // ALL RULES/RESTRICTIONS WERE ACCEPTED
	}
	
	short max_allowed_itemset;
	short rule; // rule number
	short restriction; // restriction number (can be composed by 1 or more rules)
	short currentItemset; // current itemset
	short startRuleItemset; // first itemset to consider when veryfing the constraint
	short itemfoundItemset;
	//boolean insideRestriction; // validating inside the restriction
	State ruleState;
	//State restrictionState;
	
	//short lastVisitedItemset;
	//boolean isToCount;
	
	//short lastAcceptedVisitedItemset; // last itemset number of the longest pattern accepted
	
	public SeqD2State () {
		this.max_allowed_itemset = 1;
		rule = 1; // first rule to be validated
		restriction = 1; // first restriction to be validated
		currentItemset = 1;
		startRuleItemset = 1;
		itemfoundItemset = -1;
		//insideRestriction = false;
		ruleState = State.IN;
		//restrictionState = State.OUT;
		//lastVisitedItemset = -1;
		//isToCount = true;
		//lastAcceptedVisitedItemset = -1;
	}
	
	public SeqD2State(short max_allowed_itemset, short rule,
			short restriction, short itemset, short startRuleItemset, /*boolean insideRestriction,*/ State ruleState/*, 
			State restrictionState*/) {
		this.max_allowed_itemset = max_allowed_itemset;
		this.rule = rule;
		this.restriction = restriction;
		this.currentItemset = itemset;
		this.startRuleItemset = startRuleItemset;
		itemfoundItemset = -1;
		//this.insideRestriction = insideRestriction;
		this.ruleState = ruleState;
		//this.restrictionState = restrictionState;
		//this.isToCount = true;
		//this.lastAcceptedVisitedItemset = -1;
	}
	
	short getRestriction() { return this.restriction; }
	short getRule() { return this.rule; }
	short getCurrentItemset() { return this.currentItemset; }
	//boolean isInsideRestriction() { return this.insideRestriction; }
	State getRuleState() { return this.ruleState; }
	public boolean isAllPassTheRuleState() { return this.ruleState == State.ALL_PASSED; }
	public boolean isNoRulesTheRuleState() { return this.ruleState == State.NO_RULES; } 
	//State getRestrictionState() { return this.restrictionState; }
	short getMax_Allowed_Itemset() { return this.max_allowed_itemset; }
	//short getLastVisitedItemset() { return this.lastVisitedItemset; }
	//public boolean getIsToCount() { return this.isToCount; }
	//short getLastAcceptedVisitedItemset() { return this.lastAcceptedVisitedItemset; }
	
	short getStartRuleItemset() { return this.startRuleItemset; }
	short getItemFoundItemset() { return this.itemfoundItemset; }
	
	void incVariable(short val) { val++; }
	void resetVariable(short val) { val = 0; }
	
	void setCurrentItemset(short val) { this.currentItemset = val; }
	void setMax_Allowed_Itemset(short val) { this.max_allowed_itemset = val; }
	
	//void setInsideRestriction() { this.insideRestriction = true; }
	//void setOutsideRestriction() { this.insideRestriction = false; }
	//void changeInsideRestriction() { this.insideRestriction = !this.insideRestriction; }
	
	void setInsideRuleState() { this.ruleState = State.IN; }
	void setPassRuleState() { this.ruleState = State.PASS; }
	
	//void setOusideRestrictionState() { this.restrictionState = State.OUT; }
	//void setInsideRestrictionState() { this.restrictionState = State.IN; }
	//void setPassRestrictionState() { this.restrictionState = State.PASS; }
	//void setAllPassedRestrictionState() { this.restrictionState = State.ALL_PASSED; }
	
	void setRuleState(State new_state) { this.ruleState = new_state; }
	
	//void setLastVisitedItemset(short val) { this.lastVisitedItemset = val; }
	
	//void setIsToCount(boolean val) { this.isToCount = val; }
	
	//void setLastAcceptedVisitedItemset(short val) { this.lastAcceptedVisitedItemset = val; }
	
	void setStartRuleItemset(short val) { this.startRuleItemset = val; }
	void setItemFoundItemset(short val) { this.itemfoundItemset = val; }
	
	public SeqD2State cloneState(){
		SeqD2State clone = new SeqD2State(); //better to explicitly assign the values
		
		clone.max_allowed_itemset = this.max_allowed_itemset;
		clone.rule = this.rule;
		clone.restriction = this.restriction;
		clone.currentItemset = this.currentItemset;
		clone.startRuleItemset = this.startRuleItemset;
		clone.itemfoundItemset = this.itemfoundItemset;
		//clone.insideRestriction = this.insideRestriction;
		clone.ruleState = this.ruleState;
		//clone.restrictionState = this.restrictionState;
		//clone.lastVisitedItemset = this.lastAcceptedVisitedItemset;
		//clone.isToCount = this.isToCount;
		//clone.lastAcceptedVisitedItemset = this.lastAcceptedVisitedItemset;
		
		
		return clone;
	}
	
	public String toString(){
		String output = "Max Allowed Itemset: " + this.max_allowed_itemset + "\n"
				+ "Start Rule Itemset: " + this.startRuleItemset + "\n"
				+ "Current Itemset: " + this.currentItemset + "\n"
				+ "Found Item's Itemset: " + this.itemfoundItemset + "\n"
				//+ "Is to count? " + (this.isToCount? "yes" : "no") + "\n"
				+ "Rule: " + this.rule + "\n"
				+ "Restriction: " + this.restriction + "\n"
				+ "Rule State: " + this.ruleState + "\n"
				//+ "Inside Restriction? " + this.insideRestriction + "\n"
				//+ "Restriction State: " + this.restrictionState + "\n"
				+ "All Rules Passed? " + (this.ruleState == State.ALL_PASSED? "yes" : "no");
		
		return output;
	}
}

package semanticRestrictions;

import java.util.ArrayList;

import logicprocess.RestrictionSequence;

public class Restriction_hasFirst extends RestrictionSemantic {
	
	String item;
	//RestrictionSequence restrictionSequence;
	
	public Restriction_hasFirst(String item, RestrictionSequence restrictionSequence) {
		this.item = item;
		//this.restrictionSequence = restrictionSequence;
		super.restrictionSequence = restrictionSequence;
	}
	
	public String getItem() {
		return item;
	}
	
	public RestrictionSequence getRestrictionSequence() {
		return super.restrictionSequence;
	}
	
	@Override
	public boolean execute(ArrayList<String> sequence){
		return sequence.get(0).equals(item);
	}

	@Override
	public String getRelationName() {
		return "hasFirst";
	}
}

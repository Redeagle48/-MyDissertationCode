package semanticRestrictions;

import java.util.ArrayList;

import logicprocess.RestrictionSequence;

public class Restriction_hasFirstItem extends RestrictionSemantic {
	
	String item;
	
	public Restriction_hasFirstItem(String item, ConstraintSequence restrictionSequence) {
		this.item = item;
		super.restrictionSequence = restrictionSequence;
	}

	@Override
	public ConstraintSequence getRestrictionSequence() {
		return restrictionSequence;
	}

	@Override
	public String getItem() {
		return item;
	}

	@Override
	public boolean execute(ArrayList<String> sequence){
		return true;
	}

	@Override
	public String getRelationName() {
		return "hasFirstItem";
	}
	
}

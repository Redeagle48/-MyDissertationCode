package semanticRestrictions;

import java.util.ArrayList;

import logicprocess.RestrictionSequence;

public class Restriction_hasPrecedent extends RestrictionSemantic {
	
	String item;

	public Restriction_hasPrecedent(String item, ConstraintSequence restrictionSequence) {
		this.item = item;
		//this.restrictionSequence = restrictionSequence;
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
	public String getRelationName() {
		return "precedes";
	}
	
	@Override
	public boolean execute(ArrayList<String> sequence){
		return true;
	}

}

package semanticRestrictions;

import java.util.ArrayList;

import logicprocess.RestrictionSequence;

public abstract class RestrictionSemantic {
	
	ConstraintSequence restrictionSequence;
	
	public abstract ConstraintSequence getRestrictionSequence();
	public abstract String getItem();
	
	public abstract String getRelationName();
	
	public boolean execute(ArrayList<String> sequence){
		return false;
	}
}

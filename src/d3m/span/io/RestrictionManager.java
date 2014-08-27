package d3m.span.io;

import java.util.ArrayList;

public class RestrictionManager {
	ArrayList<ConstraintSequence> restrictions;
	
	public RestrictionManager() {
		restrictions = new ArrayList<>();
	}
	
	public void addRestriction(ConstraintSequence restriction){
		restrictions.add(restriction);
	}
	
	public ArrayList<ConstraintSequence> getRestrictionSet(){
		return restrictions;
	}
}

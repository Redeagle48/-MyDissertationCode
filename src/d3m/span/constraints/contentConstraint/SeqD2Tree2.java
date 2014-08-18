package d3m.span.constraints.contentConstraint;

import java.util.Vector;

import d3m.span.core.SeqItem;
import d3m.span.core.SeqSequence;

public class SeqD2Tree2 {

	protected Vector<Vector<SeqD2Relation>> constraints = new Vector<Vector<SeqD2Relation>>();

	/*
	public SeqD2Tree() {

		//To Test 
		/////////////////////////////////////
		/////////////////////////////////////
		SeqD2Begin begin_1 = new SeqD2Begin(new SeqItem("1"));
		SeqD2Precedence precedence_1 = new SeqD2Precedence(new SeqItem("2"),new SeqItem("3"));

		Vector<SeqD2Relation> constraint1 = new Vector<SeqD2Relation>();
		constraint1.add(begin_1);
		constraint1.add(precedence_1);

		constraints.add(constraint1);
		/////////////////////////////////////
		/////////////////////////////////////

	}
	*/

	public boolean verify(SeqSequence b, SeqItem[] alphabet){
		//Vector<SeqD2Relation> constraint = constraints.get(0);
		//SeqD2Relation relation = constraint.get(0);
		//return relation.accept(b,alphabet);

		/// NEW NEW NEW NEW NEW
		for(int i = 0; i < constraints.size(); i++) {
			if(achievesStateWithSequence(b, b.size(), constraints.get(i), alphabet) == 1)
				return true;
		}
		
		return false;
	}

	protected int achievesStateWithSequence(SeqSequence s, int size, Vector<SeqD2Relation> constraint,SeqItem[] alphabet)
	{
		for (int i = 0; i < constraint.size(); i++) {
			if(!constraint.get(i).accept(s, alphabet, i))
				return -1;
		}
		return 1;
	}

// --------------------------NEW STUFF -------------------------------- //
	
	/*
	Vector<SeqD2StateTransition> statesTransictions = new Vector<SeqD2StateTransition>();
	SeqD2StateTransition transiction0 = new SeqD2StateTransition(new SeqD2Begin(new SeqItem("1")),0);
	SeqD2StateTransition transiction1 = new SeqD2StateTransition(new SeqD2Precedence(new SeqItem("2"), new SeqItem("3")),3);
	
	public SeqD2Tree(){

		statesTransictions.add(transiction0);
		statesTransictions.add(transiction1);

	}
	*/
}

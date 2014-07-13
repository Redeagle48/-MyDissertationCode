package d3m.span.constraints.contentConstraint;

import java.util.Vector;

import d3m.spam.core.EventSequence;
import d3m.spam.core.ItemSet;
import d3m.span.core.SeqItem;
import d3m.span.core.SeqItemset;
import d3m.span.core.SeqSequence;

public class SeqD2Tree {

	protected Vector<Vector<SeqD2Relation>> constraints = new Vector<Vector<SeqD2Relation>>();

	//To Test
	public SeqD2Tree() {

		SeqD2Begin begin_1 = new SeqD2Begin(new SeqItem("1"));
		SeqD2Precedence precedence_1 = new SeqD2Precedence(new SeqItem("2"),new SeqItem("3"));

		Vector<SeqD2Relation> constraint1 = new Vector<SeqD2Relation>();
		constraint1.add(begin_1);
		constraint1.add(precedence_1);

		constraints.add(constraint1);

	}

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




}

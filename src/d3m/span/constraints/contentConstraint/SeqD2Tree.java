package d3m.span.constraints.contentConstraint;

import java.util.Vector;

import d3m.span.core.SeqItem;
import d3m.span.core.SeqSequence;

public class SeqD2Tree {
	
	protected Vector<Vector<SeqD2Relation>> constraints = new Vector<Vector<SeqD2Relation>>();

	//To Test
	public SeqD2Tree() {
		
		SeqD2Begin begin_1 = new SeqD2Begin(new SeqItem("1"));
		
		Vector<SeqD2Relation> constraint1 = new Vector<SeqD2Relation>();
		constraint1.add(begin_1);
		
		constraints.add(constraint1);
	
	}
	
	public boolean verify(SeqSequence b, SeqItem[] alphabet){
		Vector<SeqD2Relation> constraint = constraints.get(0);
		SeqD2Relation relation = constraint.get(0);
		return relation.accept(b,alphabet);
	}
	
	
	
	

}

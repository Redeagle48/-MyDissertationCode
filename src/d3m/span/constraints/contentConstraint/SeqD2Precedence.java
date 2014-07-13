package d3m.span.constraints.contentConstraint;

import d3m.span.core.SeqItem;
import d3m.span.core.SeqSequence;

public class SeqD2Precedence extends SeqD2Relation{

	SeqItem precedent, consequent;
	
	public SeqD2Precedence(){
		
	}
	
	public SeqD2Precedence(SeqItem precedent, SeqItem consequent){
		this.precedent = precedent;
		this.consequent = consequent;
	}
	
	public SeqItem getPrecedent(){
		return precedent;
	}
	
	public SeqItem getConsequent(){
		return consequent;
	}
	
	public String toString(){
		return "Seq2Precedence";
	}

	@Override
	public boolean accept(SeqSequence b, SeqItem[] alphabet) {
		// TODO Auto-generated method stub
		return false;
	}
	
}

package d3m.span.constraints.contentConstraint;

import d3m.span.core.SeqItem;
import d3m.span.core.SeqSequence;

public class SeqD2End extends SeqD2Relation {

	SeqItem item;

	public SeqD2End(){

	}

	public SeqD2End(SeqItem item){
		this.item = item;
	}
	
	public SeqItem getItem(){
		return item;
	}
	
	public String toString(){
		return "Seq2End";
	}

	@Override
	public boolean accept(SeqSequence b, SeqItem[] alphabet, int until) {
		// TODO Auto-generated method stub
		return false;
	}
	
}

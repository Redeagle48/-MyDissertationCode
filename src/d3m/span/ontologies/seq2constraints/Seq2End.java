package d3m.span.ontologies.seq2constraints;

import d3m.span.core.SeqItem;

public class Seq2End extends Seq2Constraint {

	SeqItem item;

	public Seq2End(){

	}

	public Seq2End(SeqItem item){
		this.item = item;
	}
	
	public SeqItem getItem(){
		return item;
	}
	
	public String toString(){
		return "Seq2End";
	}
	
}

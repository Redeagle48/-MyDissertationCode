package d3m.span.ontologies.seq2constraints;

import d3m.span.core.SeqItem;

public class Seq2Begin extends Seq2Constraint{

	SeqItem item;

	public Seq2Begin(){

	}

	public Seq2Begin(SeqItem item){
		this.item = item;
	}
	
	public SeqItem getItem(){
		return item;
	}
	
	public String toString(){
		return "Seq2Begin";
	}

}

package d3m.span.ontologies.seq2constraints;

import d3m.span.core.SeqItem;

public class Seq2Precedence extends Seq2Constraint{

	SeqItem precedent, consequent;
	
	public Seq2Precedence(){
		
	}
	
	public Seq2Precedence(SeqItem precedent, SeqItem consequent){
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
	
}

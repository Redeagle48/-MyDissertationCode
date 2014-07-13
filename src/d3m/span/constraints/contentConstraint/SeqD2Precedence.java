package d3m.span.constraints.contentConstraint;

import d3m.span.core.SeqItem;
import d3m.span.core.SeqItemset;
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
		return "SeqD2Precedence";
	}

	@Override
	public boolean accept(SeqSequence b, SeqItem[] alphabet, int until) {
		
		SeqItemset itemset = b.getItemsetAt(until);
		//short item = itemset.elementIdAt(0); //retorna o id n o item mas pronto
		
		//return this.item.getElement().equals(alphabet[item].getElement());
		
		
		return true;
	}
	
}

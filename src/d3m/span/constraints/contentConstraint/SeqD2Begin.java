package d3m.span.constraints.contentConstraint;

import d3m.span.core.SeqItem;
import d3m.span.core.SeqItemset;
import d3m.span.core.SeqSequence;

public class SeqD2Begin extends SeqD2Relation{

	SeqItem item;

	public SeqD2Begin(){

	}

	public SeqD2Begin(SeqItem item){
		this.item = item;
	}
	
	public SeqItem getItem(){
		return item;
	}
	
	public String toString(){
		return "Seq2Begin";
	}

	@Override
	public boolean accept(SeqSequence b,SeqItem[] alphabet) {
		
		SeqItemset itemset = b.getFirstItemset();
		short item = itemset.elementIdAt(0); //retorna o id n o item mas pronto
		
		return this.item.getElement().equals(alphabet[item].getElement());
	}

}

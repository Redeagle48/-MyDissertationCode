package d3m.span.constraints.contentConstraint;

import d3m.span.core.SeqItem;
import d3m.span.core.SeqSequence;

public abstract class SeqD2Relation {

	public SeqD2Relation() {
	}

	public abstract boolean accept(SeqSequence b,SeqItem[] alphabet);
	
}

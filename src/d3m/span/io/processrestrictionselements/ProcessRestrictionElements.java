package d3m.span.io.processrestrictionselements;

import d3m.span.io.OntologyHolder;
import d3m.span.io.ConstraintSequence;

import org.w3c.dom.Node;

public abstract class ProcessRestrictionElements {
	
	OntologyHolder ontologyHolder;
	int constraint_id;
	
	public abstract void proceed(Node node, ConstraintSequence restrictionSequence);
}

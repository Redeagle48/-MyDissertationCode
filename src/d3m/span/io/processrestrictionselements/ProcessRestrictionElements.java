package d3m.span.io.processrestrictionselements;

import d3m.span.io.OntologyHolder;
import d3m.span.io.RestrictionSequence;

import org.w3c.dom.Node;

public abstract class ProcessRestrictionElements {
	
	OntologyHolder ontologyHolder;
	
	public abstract void proceed(Node node, RestrictionSequence restrictionSequence);
}

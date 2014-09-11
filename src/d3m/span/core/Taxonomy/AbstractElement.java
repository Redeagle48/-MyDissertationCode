package d3m.span.core.Taxonomy;

public abstract class AbstractElement {

	String elem;
	
	public abstract String getThisElement();
	public abstract AbstractElement getElement(String ae_name);
	public abstract ComposedElement getParent();
	public abstract boolean alreadyExistsElement(String el);
	public abstract boolean isTheSame(String item);
	public abstract void print();

}

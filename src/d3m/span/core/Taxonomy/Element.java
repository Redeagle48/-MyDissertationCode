package d3m.span.core.Taxonomy;

public class Element extends AbstractElement{
	
	ComposedElement parentElement;
	
	public Element(ComposedElement parent, String el) {
		super.elem = el;
		this.parentElement = parent;
	}
	
	@Override
	public ComposedElement getParent() {
		// TODO Auto-generated method stub
		return this.parentElement;
	}

	@Override
	public AbstractElement getElement(String ae_name) {
		if(this.elem.equals(ae_name))
			return this;
		return null;
	}
	
	@Override
	public boolean alreadyExistsElement(String el) {
		return this.elem.equals(el);
	}

	@Override
	public void print() {
		System.out.println(super.elem);
	}
}

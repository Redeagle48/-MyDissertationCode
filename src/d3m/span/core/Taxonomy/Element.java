package d3m.span.core.Taxonomy;

public class Element extends AbstractElement{
	
	public Element(String el) {
		super.elem = el;
	}

	@Override
	public boolean visitElement(String el) {
		return this.elem.equals(el);
	}

	@Override
	public void print() {
		System.out.println(super.elem);
	}
	
}

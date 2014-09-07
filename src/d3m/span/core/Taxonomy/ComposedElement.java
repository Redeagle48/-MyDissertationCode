package d3m.span.core.Taxonomy;

import java.util.Vector;

public class ComposedElement extends AbstractElement{
	
	Vector<AbstractElement> elements;
	
	public ComposedElement(String el){
		elements = new Vector<AbstractElement>();
		super.elem = el;
	}
	
	public void addElement(AbstractElement el){
		elements.add(el);
	}
	
	public boolean visitElement(String elem){
		for (AbstractElement el : elements) {
			if(el.visitElement(elem)) return true;
		}
		return false;
	}

	@Override
	public void print() {
		System.out.println("Elements belonging to the concept: " + super.elem);
		for (AbstractElement el : elements) {
			el.print();
		}
	}

	
	
}

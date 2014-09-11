package d3m.span.core.Taxonomy;

import java.util.Vector;

public class ComposedElement extends AbstractElement{
	
	Vector<AbstractElement> elements; // Elements contained by this one
	ComposedElement parentElement; // Element that contains this one
	
	public ComposedElement(ComposedElement parent, String el){
		elements = new Vector<AbstractElement>();
		super.elem = el;
		this.parentElement = parent;
	}
	
	public ComposedElement getParent(){
		return this.parentElement;
	}
	
	public void addElement(AbstractElement el){
		elements.add(el);
	}
	
	public void removeElement(AbstractElement el){
		this.elements.remove(el);
	}
	
	public void removeElement(String el_name){
		this.elements.remove(getElement(el_name));
	}
	
	public AbstractElement getElement(String el_name){
		
		AbstractElement out = null;
		
		for (AbstractElement ae : elements) {
			if(ae.elem.equals(el_name)){
				out = ae;
				return out;
			} else {
				out = ae.getElement(el_name);
				if(out!=null){
					return out;
				}
			}
		}
		return out;
	}
	
	public boolean alreadyExistsElement(String elem){
		
		if(this.elem.equals(elem))
			return true;
		
		for (AbstractElement el : elements) {
			if(el.alreadyExistsElement(elem)) return true;
		}
		return false;
	}
	
	public boolean isTheSame(String item){
		if(elem.equals(item))
			return true;
		
		for (AbstractElement el : elements) {
			if(el.isTheSame(item))
				return true;
		}
		
		return false;
	}
	
	public boolean isSameFamily(String item){
		for (AbstractElement el : elements) {
			el.isTheSame(item);
		}
		return false;
	}

	@Override
	public void print() {
		
		System.out.println("Start ComposedElement:" + this.elem);
		
		for (AbstractElement el : elements) {
			el.print();
		}
		
		System.out.println("End ComposedElement:" + this.elem);
	}

	@Override
	public String getThisElement() {
		return elem;
	}

	
	
}

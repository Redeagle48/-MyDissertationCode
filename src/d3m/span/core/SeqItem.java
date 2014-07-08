package d3m.span.core;

import java.util.Vector;

import dm.pam.core.Item;
import dm.uspam.core.Element;

public class SeqItem extends Item{
	
	String element = null;
	
	/** The element index in the alphabet. */
    protected short m_index=-1;
	
	/**
	 * Default constructor
	 */
	public SeqItem() {}
	
	public SeqItem(String element){
		super(Integer.parseInt(element));
		this.element = element;
	}
	
	public SeqItem(String element, short id) {
		super(Integer.parseInt(element));
		this.element = element;
		m_index = id;
	}
	
	public String getElement(){
		return element;
	}
	
	public short getIndex(){
		return m_index;
	}
	
	//________________________________________________________________
    public boolean isGreaterThan(SeqItem el)
    {   return (m_index>el.m_index);    }
	
	//________________________________________________________________
	/**
	 * Check if the element is already present in the alphabet
	 * @param alphabet
	 * @return
	 */
    public short isAlreadyDiscovered(Vector<SeqItem> alphabet)
    {
        short i = 0;
        while (i<alphabet.size() && 
             (0!=((SeqItem)alphabet.elementAt(i)).element.compareTo(this.element)))
            i++;
        if (i<alphabet.size())
            return i;
        else 
            return -1;
    }
    
    public String toString() {
    	return element;
    }
	
}

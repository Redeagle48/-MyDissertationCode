package d3m.span.core;

import java.util.Arrays;
import java.util.Vector;

import d3m.spam.core.Element;
import dm.pam.core.Item;
import dm.pam.core.Itemset;
import dm.uspam.core.ItemSet;

public class SeqItemset extends Itemset {

	/** The number of element*/
	protected short m_size=0;

	/** array containing the index of corresponding SeqItem's */ 
	protected short[] m_elems = null; 

	//________________________________________________________________
	/** Creates a new instance of ItemSet with elements in v.
	 * @param v The set of elements.
	 */
	public SeqItemset(SeqItemset s)
	{
		m_size = s.m_size;
		m_elems = new short[m_size];
		//m_itemset = new SeqItem[m_size];
		for (int i=0; i<m_size; i++)
			m_elems[i] = s.m_elems[i];
	}

	/**
	 * Creates a new itemset for n items
	 * @param n the itemset's size
	 */
	public SeqItemset(int n){
		m_elems = new short[n];
		//m_itemset = new SeqItem[n];
		m_frequency = 0;
		m_size = (short) m_elems.length;
	}

	public SeqItemset(SeqItem seqItem)
	{
		m_elems = new short[1];
		m_elems[0] = seqItem.getIndex();
		m_size = (short)m_elems.length;
	}

	public SeqItemset(String itemset, Vector<SeqItem> alphabet){

		char comma = ','; // separator
		int ind_first = itemset.indexOf("(", 0);
		int ind;
		String st_elem;
		Vector<SeqItem> elems = new Vector<SeqItem>(); 

		while (-1!=(ind = itemset.indexOf(comma, ind_first+1)))
		{
			st_elem = itemset.substring(ind_first+1, ind);         
			SeqItem elem = new SeqItem (st_elem);
			elem = restoreAlphabet(elem, alphabet);
			ind_first = ind;
			elems.addElement(elem);
		}

		st_elem = itemset.substring(ind_first+1, itemset.indexOf(')', ind_first+1)/*itemset.length()-3*/);
		SeqItem elem = new SeqItem (st_elem);
		elem = restoreAlphabet(elem, alphabet);
		elems.addElement(elem);

		// Create the ItemSet itself

		m_size = (short)elems.size();
		m_elems = new short[m_size];
		for (int i=0; i<m_size; i++)
			//m_itemset[i] = ((SeqItem)elems.elementAt(i));
			m_elems[i] = ((SeqItem)elems.elementAt(i)).getIndex();

		Arrays.sort(m_elems);
	}

	//________________________________________________________________
	/**
	 * Return the index of the SeqItem in the position i of the SeqItemset
	 * @param position i
	 * @return value of the index of the SeqItem in the position i
	 */
	public short elementIdAt(int i)
	{
		return m_elems[i];
	}

	//________________________________________________________________
	/**
	 * Verifies if this itemset contains another by its index in the alphabet.
	 * @param The itemset to look for.
	 */
	public boolean contains(short el)
	{
		int n = indexOf(el); //DEBUG
		if (-1==indexOf(el)) 
			return false;
		else 
			return true;
	}

	public int size(){
		return m_elems.length;
	}

	//________________________________________________________________
	/**
	 * Returns the array index where element occurs. Returns -1 if it not occur.
	 */
	private int indexOf(SeqItem el)
	{
		return indexOf((short)el.getIndex());
	}

	private int indexOf(short id)
	{
		int i = 0;
		boolean ok = true;
		/*
	    while ((i<m_size) && (ok))
	    {
	        if (id<m_elems[i]) return -1;
	        if (id==m_elems[i]) return i;
	        i++;
	    }
	    return -1;
		 */
		//if(contains(id)){
		while ((i<m_size) && (ok)){
			if(id == m_elems[i])
				return i;
			i++;
		}
		//}
		return -1;
	}

	//________________________________________________________________
	/**
	 * Verifies if this itemset contains another.
	 * @param The itemset to look for.
	 */
	public boolean contains(SeqItemset set)
	{
		if (m_size<set.m_size) return false;
		int ind = 0;
		int i = 0;
		while ((i<set.m_size) && (-1!=ind))
		{
			ind = indexOf(set.elementIdAt(i));
			i++;
		}
		if (-1==ind) return false;
		else return true;
	}

	//________________________________________________________________
	public void addElement(SeqItem el)
	{
		addElement(el.getIndex());
	}
	
	//________________________________________________________________
	public void addElement(short id)
	{
		if (0>=Arrays.binarySearch(m_elems, id))
		{
			short[] a = new short[m_size+1];
			int i=0;
			while (i<m_size && (id>m_elems[i]))
				a[i]= m_elems[i++];
			a[i++] = id;
			for (int j=i; j<m_size; j++)
				a[j]= m_elems[j];
			m_elems = a;
			m_size ++;
		}
	}

	//________________________________________________________________
	private SeqItem restoreAlphabet(SeqItem elem, Vector<SeqItem> alphabet)
	{
		// If element doesn't exist on m_seqAlphabet
		short exists = elem.isAlreadyDiscovered(alphabet);
		// It is created a new element with a new index
		if (-1==exists)
		{
			elem = new SeqItem(elem.getElement(), (short)alphabet.size());
			alphabet.addElement(elem);

		}	
		// It is created a new element with the same index
		else
			elem = new SeqItem(elem.getElement(), exists);
		return elem;
	}

	/**
	 * Creates a new itemset equal to this one, but with one more item
	 * @param it the new item
	 * @return a new itemset equal to this one, but with one more item
	 */
	public SeqItemset createExtendedWith(SeqItem it){
		SeqItemset newSet = new SeqItemset(m_itemset.length+1);
		System.arraycopy(m_itemset,0,newSet.m_itemset, 0, m_itemset.length);
		newSet.m_itemset[m_itemset.length] = it;
		return newSet;
	}

	//________________________________________________________________
	public Object clone()
	{
		SeqItemset set = new SeqItemset(this);
		return set;
	}

	/** ****** TRANSFORMERS */
	/**
	 * Returns a string representation of the itemset, including its items and its frequency. 
	 * @return a string representation of this itemset
	 */
	@Override
	public String toString() {
		String st = "(";
		for (int i = 0; i < m_elems.length; i++){
			if (i>0)
				st+=",";
			st += m_elems[i];
		}
		st += ")"; //"):" + String.valueOf(m_frequency);
		return st;
	}
}
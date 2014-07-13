package d3m.span.core;

import java.util.Vector;

import dm.pam.core.Itemset;
import dm.uspam.core.Element;
import dm.uspam.core.ItemSet;

public class SeqSequence extends Itemset {

	protected SeqItemset[] m_itemsets;

	/** The number of events. */
	protected int m_size;
	/** Sequence id. */
	protected int m_id;
	/** Sequence support in a given database. */
	protected int m_support;
	protected double m_conf;
	static protected int m_nrOfSequences = 0;

	/** ****** CONSTRUCTORS */
	/**
	 * Default constructor
	 */
	public SeqSequence() {
		super();
		m_itemsets = new SeqItemset[0];
	}

	/**
	 * Creates a new sequence for n itemsets
	 * @param n the sequence's size
	 */
	public SeqSequence(int n){
		super();
		m_itemsets = new SeqItemset[n];
		m_size = m_itemsets.length;
	}

	/**
	 * Creates a new sequence from an array of itemsets, read from a dataset. 
	 * @param itemsets The itemsets to include in the dataset
	 */
	public SeqSequence(SeqItemset[] itemsets) {
		super();
		m_itemsets = itemsets;
		m_size = m_itemsets.length;
	}

	/**
	 * Creates a new sequence equal to another sequence
	 * @param s
	 */
	public SeqSequence(SeqSequence s) {
		super();
		m_itemsets = s.m_itemsets;
		m_size = m_itemsets.length;
	}

	/**
	 * Creates a new sequence with one itemset
	 * @param itemset the itemset to include in the sequence
	 */
	public SeqSequence(SeqItemset itemset) {
		//super(itemset);
		m_itemsets = new SeqItemset[1];
		m_itemsets[0] = itemset;
		m_size = m_itemsets.length;
	}

	//________________________________________________________________
	/**
	 * Creates a new instance of EventSequence, with several elements created from
	 * the received string.
	 * @param st The EventSequence of pairs (element, time) that composes the sequence.
	 */
	public SeqSequence(String st, Vector<SeqItem> alphabet)
	{
		char i_init = '['; //TODO alphabet not working right
		char i_end = ']';
		Vector<SeqItemset> elems = new Vector<SeqItemset>();
		String st_set;
		int i_end_ind = 0;
		int i_init_ind = 0;
		// For each itemset (indicated by ( and ))
		while ((-1!=(i_init_ind=st.indexOf(i_init, i_end_ind)))&&(i_end_ind<=st.length()))
		{
			// Looks for the next event, finding a ] parentesis
			i_end_ind = st.indexOf(i_end, i_init_ind);
			st_set = st.substring(i_init_ind+1, i_end_ind);
			// Creates the itemset for this instant
			SeqItemset set = new SeqItemset(st_set, alphabet);
			elems.addElement(set);
		}
		initializeMembers(elems);
		elems.toArray(m_itemsets); // Change the structure that has the itemsets of the sequence
	}

	/**
	 * Creates a new sequence equal to this one, but with one more itemset
	 * @param it the new itemset
	 * @return a new sequence equal to this one, but with one more itemset
	 */
	public SeqSequence createExtendedWith(SeqItemset itemset){
		SeqSequence newSeq = new SeqSequence(m_itemsets.length+1);
		System.arraycopy(m_itemsets,0,newSeq.m_itemsets, 0, m_itemsets.length);
		newSeq.m_itemsets[m_itemsets.length] = itemset;
		return newSeq;
	}

	//________________________________________________________________
	protected void initializeMembers(Vector<SeqItemset> elements)
	{
		// Initialize member arrays
		m_itemsets = new SeqItemset[elements.size()];
		m_size = elements.size();
		// Initialize events
		for (int j=0; j<elements.size(); j++)
			m_itemsets[j]= (SeqItemset)elements.elementAt(j);
		m_id = m_nrOfSequences++;
		m_support = 0;
		m_conf = 0.0;    
	}

	/** ****** GETTERS */
	/**
	 * Returns the number of items in this sequence
	 * @return its number of itemsets
	 */
	public int size(){
		return m_itemsets.length;
	}

	/**
	 * Gets the ith itemset in the sequence
	 * @param i the index for the desirable itemset
	 * @return the ith itemset
	 */
	public SeqItemset getItemsetAt(int i) {
		return m_itemsets[i];
	}

	/**
	 * Gets the last itemset in the sequence
	 * @return the last item in the sequence
	 */
	public SeqItemset getLastItemset(){
		return m_itemsets[m_itemsets.length-1];
	}

	/**
	 * Gets the first item in the itemset
	 * @return the first item in the itemset
	 */
	public SeqItemset getFirstItemset(){
		return m_itemsets[0];
	}

	//________________________________________________________________
	/**
	 * subsequence: int x int -> Sequence
	 * Creates a subsequence with end-init+1 elements.
	 * @param init -> the first element's index of the new sequence
	 * @param end -> the last element's index of the new sequence
	 * Sequence s = Sequence("abcd");
	 * s.subsequence(0,2); ==> "abc"
	 */
	public SeqSequence subsequence(int init, int end)
	{
		int n = end-init+1;
		if ((0==m_size)||(n<1))
			return new SeqSequence(0);
		SeqSequence s = new SeqSequence(n);
		for (int i=0; i<n; i++)
		{
			SeqItemset e = (SeqItemset) m_itemsets[i+init];
			s.m_itemsets[i] = e;
		}
		s.m_id = m_id;
		return s;
	}

	//________________________________________________________________
	/**
	 * addElement: Event -> EventSequence
	 * Creates a new sequence with a new element at the end of the sequence.
	 * @param el The new element to add.
	 * @return The new sequence.
	 */
	public SeqSequence addElement(SeqItemset set)
	{
		SeqSequence s = new SeqSequence(m_size+1);
		for (int j=0; j<m_size; j++)
			s.m_itemsets[j]= m_itemsets[j];
		s.m_itemsets[m_size] = set;	
		s.m_size = m_size+1;	
		return s;
	}

	/**
	 * concatenateWith: Sequence -> Sequence
	 * Creates a new sequence from two others: the received and the original ones.
	 * @param seq The received sequence, which will be the postfix.
	 * @return A sequence composed by this|seq.
	 */
	public SeqSequence concatenateWith (SeqSequence seq)
	{
		SeqSequence new_seq = new SeqSequence(this.m_size + seq.m_size);
		for (int i=0; i<this.m_size; i++)
			new_seq.m_itemsets[i] = this.getItemsetAt(i);
		for (int i=0; i<seq.m_size; i++)
			new_seq.m_itemsets[i+m_size] = seq.getItemsetAt(i);
		m_id = -1;
		return new_seq;
	}

	/**
	 * Represents this sequence in the context of the dataset received
	 * @param dataset the dataset where the sequence was created
	 * @return this sequence representation in the context of its dataset
	 */
	public String toString(SeqDataset dataset) {
		String st = "[";
		for(int i = 0; i < size(); i++){
			st += m_itemsets[i].toString();
		}
		st += "]";
		return st;
	}

	public void setSupport(int sup)
	{   
		m_support = sup;
	}

	//________________________________________________________________
	public Object clone()
	{
		SeqSequence copy = new SeqSequence(m_size);
		for (int i=0; i<m_size; i++)
			copy.m_itemsets[i] = (SeqItemset)m_itemsets[i].clone();
		copy.m_size = m_size;
		copy.m_id = m_id;
		copy.m_support = m_support;
		copy.m_conf = m_conf;
		return copy;
	}

	//________________________________________________________________
	/**
	 * countSupportForEachElement: ->
	 * It discovers the different elements of a sequence and counts their
	 * support
	 * @param vecItems A vector with different elements to look for.
	 * @param vecItemsCount A vector with the number of occurrences for each different
	 *		       element
	 * @param first The index of the first element of the subsequence
	 * @param last The index of the last element to consider
	 */
	public void countSupportForEachElement (Vector<SeqItem> alphabet, long[] arSup, int first, int last)
	{
		boolean[] found = new boolean[alphabet.size()];
		// Look for serial elements
		for (int i=first; i<last; i++)   {
			SeqItemset set = getItemsetAt(i);
			for (short j=0; j<set.size(); j++){
				int id = set.elementIdAt(j);
				if (!found[id])                {
					found[id] = true;
					(arSup[id])++;     
				}       
			}    
		}
	}

	//________________________________________________________________
	/**
	 * findFrequentElements: ->
	 * It discovers the different elements of a sequence and counts their
	 * support, when this sequence doesn't already support a particular element
	 * @param alphabet A vector with different elements to look for.
	 * @param arSerialSup An array with the number of occurrences for each different
	 *		      element that occurs serially
	 * @param arParalelSup An array with the number of occurrences for each different
	 *		      element that occurs in paralel
	 * @param arLastSeq An array with the last sequence id that supports each element
	 * @param first The index of the first element of the subsequence
	 * @param last The index of the last element to consider
	 */
	public void findFrequentSerialElements(SeqDataset db, Vector<SeqItem> alphabet, long[] arSerialSup, 
			long[] arLastSeq, int first, int last)
	{
		// Look for serial elements
		for (int i=first; i<=last; i++)
		{
			SeqItemset set = getItemsetAt(i);
			for (short j=0; j<set.size(); j++)
			{
				SeqItem elem = (SeqItem)db.getAlphabet()[set.elementIdAt(j)];

				int ind = alphabet.indexOf(elem);		
				// Even positions mantains the seq ids for serial events
				if (-1!=ind && m_id!=arLastSeq[2*ind])
				{
					(arSerialSup[ind])++;
					arLastSeq[2*ind] = m_id;
				}
			}
		}
	}
	public void findFrequentParallelElements(SeqDataset db, Vector<SeqItem> alphabet, long[] arParalelSup, 
			long[] arLastSeq, int pos)
	{
		// Look for parallel elements
		SeqItemset set = getItemsetAt(pos);
		for (short j=0; j<set.size(); j++)
		{
			SeqItem elem = (SeqItem)db.getAlphabet()[set.elementIdAt(j)];
			int ind = alphabet.indexOf(elem);
			
			//int ind = alphabet.indexOf(set.elementIdAt(j));
			
			// Odd positions mantains the seq ids for baskets support
			if (-1!=ind && m_id!=arLastSeq[2*ind+1])
			{
				(arParalelSup[ind])++; 
				arLastSeq[2*ind+1] = m_id;
			}
		}
	}

	/**
	 * Returns the position index of the first occurrence of el in this sequence.
	 * @param el The element to find.
	 * @return The position index.
	 */
	public int indexOf(SeqItem el, int n)
	{
		while ((n<m_size) && !((m_itemsets[n]).contains(el.getIndex())))
			n++;
		if (n>=m_size) 
			return -1;
		else 
			return n;
	}

	/** ****** MODIFIERS */

	/**
	 * Adds an item to the end of the itemset
	 * @param it The item to insert
	 */
	public void addItem(SeqItemset itemset){
		int size = m_itemsets.length;
		SeqItemset[] newSequence = new SeqItemset[size+1];
		System.arraycopy(m_itemsets, 0, newSequence, 0, size);
		newSequence[size] = itemset;
		m_itemsets = newSequence;
	}

	/**
	 * Transform this sequence into a string, representing each element separeted by a comma.
	 * @return The string representing this sequence
	 */
	public String toString()
	{
		String st ="<";
		if (0<m_size)
		{
			for (int i=0; i<m_size; i++)
				st+=((SeqItemset)m_itemsets[i]).toString() + " ";
		}
		st += ">:"+ String.valueOf(m_support)
				+" c:"+ String.valueOf(m_conf*100)+"%";
		return st;
	}

}

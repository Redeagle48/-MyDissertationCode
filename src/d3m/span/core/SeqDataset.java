package d3m.span.core;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

import dm.pam.core.Dataset;
import dm.pam.core.Item;
import dm.pam.core.Itemset;
import dm.uspam.core.Sequence;

public class SeqDataset extends Dataset{
	
	/** ****** FIELDS */
	/** Defines the set of existing sequences. */
	protected SeqSequence[] m_data;
	
	/** Defines the number of existing sequences. */
	protected int m_size;

	/** Defines the set of items - the alphabet. */
	protected SeqItem[] m_alphabet;
	
	/** Defines the values for each item in the alphabet. */
	protected String[] m_alphabetValues;
	
	/** Defines the dataset name */
	protected String m_name;

	/** ****** CONSTRUCTORS */
	/**
	 * Default constructor. 
	 */
	public SeqDataset() {
		m_data = new SeqSequence[0];
		m_alphabet = new SeqItem[0];
		m_alphabetValues = new String[0];
		m_size = 0;
		m_name = "";
	}

	/** ****** GETTERS */
	/**
	 * Gets the dataset name
	 * @return the dataset name
	 */
	public String getName() {
		return m_name;
	}

	/**
	 * Gets the set of itemsets
	 * @return the m_data
	 */
	public SeqSequence[] getData() {
		return m_data;
	}

	/**
	 * Gets the number of sequences in the dataset.
	 * @return the m_size
	 */
	public int getSize() {
		return m_size;
	}

	/**
	 * Gets the nth itemset
	 * @param n the nth itemset in the dataset
	 * @return the nth itemset in the dataset, null if not possible
	 */
	public SeqSequence getSequence(int n) {
		try {
			return m_data[n];
		} catch (ArrayIndexOutOfBoundsException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the dataset alphabet
	 * @return the set of items
	 */
	public SeqItem[] getAlphabet() {
		return m_alphabet;
	}
	
	/**
	 * Returns the value for an item
	 * @param it the item to look for
	 * @return the value for the received item
	 */
	public String getValueForItem(Item it){
		return m_alphabetValues[it.key()];
	}
	
	/**
	 * Returns the values for a set of items
	 * @param its the set of items to look for
	 * @return the value for the received items
	 */
	public String[] getValueForItems(Item[] its){
		String[] values = new String[its.length];
		for(int i = 0; i < its.length; i++) {
			values[i] = getValueForItem(its[i]);
		}
		return values;
	}
	
	/**
	 * Return the values associated to each item in the alphabet
	 * @return the alphabet values
	 */
	public String[] getAlphabetValues(){
		return m_alphabetValues;
	}


	/** ****** MODIFIERS */
	/**
	 * Updates the dataset name
	 * @param name The name for the dataset
	 */
	public void setName(String name) {
		m_name = name;
	}

	/**
	 * Add an sequence to the set of sequences
	 * @param set The sequence to add to the dataset.
	 */
	public void addSequence(SeqSequence seq) {
		m_size++;
		SeqSequence[] data = new SeqSequence[m_size];
		for (int i=0; i<m_size-1; i++)
			data[i] = m_data[i];
		data[m_size-1] = seq;
		m_data = data;
	}
	
	/**
	 * Updates the dataset set of sequence
	 * @param itemsets the sequences to set
	 */
	public void setSequences(SeqSequence[] sequences) {
		m_data = sequences;
		m_size = Array.getLength(sequences);
	}
	/**
	 * Updates the dataset set of sequence
	 * @param itemsets the sequences to set
	 */
	public void setSequences(ArrayList<SeqSequence> sequences) {
		m_data = sequences.toArray(m_data);
		m_size = sequences.size();
	}

	/**
	 * Updates dataset's alphabet
	 * @param items the alphabet
	 */
	public void setAlphabet(Vector<SeqItem> items) {
		SeqItem[] tmp = items.toArray(m_alphabet);
		m_alphabet = tmp; // Don't know if needed
		//m_alphabet = items;
	}
	
	/**
	 * Updates dataset's alphabet and their corresponding names
	 * @param items the alphabet
	 * @param names the names for items
	 */
	public void setAlphabet(ArrayList<Item> items, ArrayList<String> names) {
		m_alphabet = new SeqItem[items.size()];
		items.toArray(m_alphabet);
		m_alphabetValues = new String[m_alphabet.length];
		names.toArray(m_alphabetValues);
	}
	
	/**
	 * 
	 * @param size
	 */
	public void setSize(int size) {
		m_size = size;
	}
	
	/** ****** TRANSFORMERS */	
	/**
	 * Returns a string representation of the dataset. 
	 * @return a string representation of this dataset
	 */
	@Override
	public String toString(){
		String st = "Dataset="+m_name+"\n";
		for (int i=0; i<m_size; i++)
			st+=m_data[i].toString()+"\n";
		return st;
	}
	
}

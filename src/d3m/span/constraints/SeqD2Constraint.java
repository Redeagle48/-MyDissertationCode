package d3m.span.constraints;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import ontologies.OntoException;
import d3m.d2pm.constraints.D2Constraint;
import d3m.d2pm.core.D2Item;
import d3m.span.constraints.contentConstraint.SeqD2Tree;
import d3m.span.core.SeqDataset;
import d3m.span.core.SeqItem;
import d3m.span.core.SeqItemset;
import d3m.span.core.SeqProjectedDB;
import d3m.span.core.SeqSequence;

public class SeqD2Constraint extends D2Constraint {
	
	/** The large distance allowed between two elements in a sequence. */
    public int m_distance = 0;
    
    /** The global number of occurrences to consider a sequence frequent. */
    public long m_globalNr = 0;
    /** The min support. */
    public float minSup = 0;
    
    /** The tree containing the constraints. */
    public SeqD2Tree tree = new SeqD2Tree();

	public SeqD2Constraint() {}

	/**
	 * Reads the file in name of the constraint, according to the received KB.
	 * @param fileIn
	 * @param separator
	 * @return the dataset read
	 * @throws IOException
	 * @throws OntoException
	 */
	public SeqSequence readFileToSequence(String fileIn, String separator) throws IOException, OntoException{
		SeqSequence data = null;
		//Sequence data = m_mapping.readFile(KB, fileIn, separator);
		//m_acceptance.update(KB, m_mapping.m_alphabet, m_mapping.m_genericConcept); // Set to protected
		HashMap<Integer, D2Item> alph = m_acceptance.getAcceptedAlphabet();
		Iterator<D2Item> iter = alph.values().iterator();
		m_accItems = new D2Item[alph.values().size()];
		int i = 0;
		while (iter.hasNext()) 
			m_accItems[i++] = iter.next();

		return data;
	}

	//________________________________________________________________
	/**
	 * Generates the set C1, <I>ie</I>, the set of 1-itemset candidates.
	 *
	 * @param db A vector corresponding to the database.
	 * @param arrayItemsCount An array with the count support for each element in the database
	 */
	public void generateC1 (SeqDataset db, long[] arrayItemsCount) throws Exception 
	// Equal to Algorithm.java dm.uspam.algorithms
	{
		//long time = 0;
		//if (m_profileOn) time = System.currentTimeMillis();
		SeqItem[] alphabet = db.getAlphabet();
		Vector<SeqItem> alphabetV = new Vector<SeqItem>();
		for(SeqItem s : alphabet)
			alphabetV.add(s);

		// Collects each 1-itemset
		for (int i=0; i<db.getSize(); i++)
		{
			SeqSequence seq = db.getSequence(i);
			seq.countSupportForEachElement(alphabetV, arrayItemsCount,
					0, seq.size());
		}
		/*
	    if (m_profileOn)
	    {
	        Long old = (Long) m_membersProfiling.elementAt(GENERATE_C1);
	        m_membersProfiling.setElementAt(new Long(old.longValue()+System.currentTimeMillis()-time),
	                            					  GENERATE_C1);
	    }
		 */
	}

	//________________________________________________________________
	/**
	 * Generates the set C1, <I>ie</I>, the set of 1-itemset candidates.
	 * @param db The projected database.
	 * @param alphabet A vector with the elements existent in the read sequences
	 * @param arSerialElems A vector with the count support for each element that can be appended to alpha
	 * @param arParalelElems A vector with the count support for each element that can be appended to last basket of alpha
	 */
	public void generateC1 (SeqProjectedDB db, Vector<SeqItem> alphabet, long[] arSerialElems, 
			long[] arParalelElems, long[] arLastSup, SeqDataset m_dataset)
					throws Exception
					{
		long time = 0;
		//if (m_profileOn) time = System.currentTimeMillis();

		for (int i=0; i<db.size(); i++)
		{
			// pDB <-- the index of s in the original DB
			int pDB = db.indexOfSequenceAt(i);
			// pSub <-- the index of the start of subsequence
			int pSub = db.indexOfStartSubsequenceAt(i);
			// Getting the referenced sequence from the original DB
			SeqSequence seq = m_dataset.getSequence(pDB);
			seq.findFrequentParallelElements(m_dataset, alphabet, arParalelElems, arLastSup, pSub);
			if (pSub<seq.size()-1)
				seq.findFrequentSerialElements(m_dataset, alphabet, arSerialElems, arLastSup, 
						pSub+1, Math.min(pSub+1+m_distance,seq.size()-1));
		}
		/*
		if (m_profileOn)
		{
			Long old = (Long) m_membersProfiling.elementAt(GENERATE_C1);
			m_membersProfiling.setElementAt(new Long(old.longValue()+System.currentTimeMillis()-time), 
					GENERATE_C1);
		}
		 */	
	}

	public SeqProjectedDB createProjectedDB(SeqSequence alfa, SeqDataset m_db){

		long time = 0;
		//if (m_profileOn) time = System.currentTimeMillis();
		Vector<Integer> vecDB = new Vector<Integer>();
		Vector<Integer> vecInd = new Vector<Integer>();
		SeqItemset set = alfa.getItemsetAt(alfa.size()-1);
		SeqItem b = (SeqItem)m_db.getAlphabet()[set.elementIdAt(set.size()-1)];
		//SeqItem b = set.elementIdAt(set.size()-1);
		for (int i=0; i<m_db.getSize(); i++)
		{
			SeqSequence s = m_db.getSequence(i);
			// Searches for all occurrences of b
			int ind = -1;
			while ((-1!= (ind=s.indexOf(b, ind+1)))&&(ind<s.size()))
			{
				vecDB.addElement(new Integer(i));
				// ind+1 for using only postfixes 
				vecInd.addElement(new Integer(ind));
			}
		}

		//m_maxUsedMemory = Math.max(m_maxUsedMemory, Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());				

		SeqProjectedDB proj = new SeqProjectedDB(vecDB, vecInd);

		/*
		if (m_profileOn)
		{
			Long old = (Long) m_membersProfiling.elementAt(CREATE_PROJECTED_DB);
			m_membersProfiling.setElementAt(new Long(old.longValue()+System.currentTimeMillis()-time), 
					CREATE_PROJECTED_DB);
		}
		 */		
		return proj;

	}

	/**
	 * Creates a vector with all of the projected databases, one for each frequent 
	 * element that is near to the preeceding sequence.
	 * @param alfa The prefix of sequences to be mined.
	 * @param db The projected database to be partitioned.
	 * @param gap The gap constraint
	 * @return The projected database.
	 */
	public SeqProjectedDB createProjectedDB(SeqItemset set, SeqProjectedDB db, SeqDataset m_dataset) throws Exception
	{
		long time = 0;
		//if (m_profileOn) time = System.currentTimeMillis();
		Vector<Integer> vecDB = new Vector<Integer>();
		Vector<Integer> vecInd = new Vector<Integer>();
		for (int i=0; i<db.size(); i++)
		{
			// pDB <-- the index of s in the original DB
			int pDB = db.indexOfSequenceAt(i);
			// pSub <-- the index of the start of subsequence
			int pSub = db.indexOfStartSubsequenceAt(i);
			// Getting the referenced sequence from the original DB
			SeqSequence s = m_dataset.getSequence(pDB);
			// If the set is present in the previous position
			if (s.getItemsetAt(pSub).contains(set))
			{
				// The index to store is the sequence index at original DB
				vecDB.addElement(new Integer(pDB));
				// ind+1 for using only postfixes 
				vecInd.addElement(new Integer(pSub));
			}
		}

		//m_maxUsedMemory = Math.max(m_maxUsedMemory, Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());				
		SeqProjectedDB proj = new SeqProjectedDB(vecDB, vecInd);
		/*
	    if (m_profileOn)
	    {
	        Long old = (Long) m_membersProfiling.elementAt(CREATE_PROJECTED_DB);
	        m_membersProfiling.setElementAt(new Long(old.longValue()+System.currentTimeMillis()-time), 
	                            					  CREATE_PROJECTED_DB);
	    }	
		 */	
		return proj;
	}
	
	/**
	 * Creates a vector with all of the projected databases, one for each frequent 
	 * element that is near to the preeceding sequence.
	 * @param alfa The prefix of sequences to be mined.
	 * @param db The projected database to be partitioned.
	 * @param gap The gap constraint
	 * @return The projected database.
	 */
	public SeqProjectedDB createProjectedDB(SeqSequence alfa, SeqProjectedDB db, SeqDataset m_dataset) throws Exception
	{
	    long time = 0;
	    //if (m_profileOn) time = System.currentTimeMillis();
	    Vector<Integer> vecDB = new Vector<Integer>();
	    Vector<Integer> vecInd = new Vector<Integer>();
	    // b <-- the element to look for
	    SeqItemset set = alfa.getItemsetAt(alfa.size()-1);
	    SeqItem b = (SeqItem)m_dataset.getAlphabet()[set.elementIdAt(set.size()-1)];
	    
	    for (int i=0; i<db.size(); i++)
	    {
	        // pDB <-- the index of s in the original DB
	        int pDB = db.indexOfSequenceAt(i);
	        // pSub <-- the index of the start of subsequence
	        int pSub = db.indexOfStartSubsequenceAt(i);
	        // Getting the referenced sequence from the original DB
	        SeqSequence s = m_dataset.getSequence(pDB);
	        //If the element is sufficiently near
	        // Searches for all occurrences of b
	        int last = pSub+1;
	        int ind;
	        while ((-1!=(ind=s.indexOf(b, last))) && (ind-pSub-1<=m_distance))
	        {
	            // The index to store is the sequence index at original DB
	            vecDB.addElement(new Integer(pDB));
	            // ind+1 for using only postfixes 
	            vecInd.addElement(new Integer(ind));
	            last = ind+1;
	        }
	    }
	//m_maxUsedMemory = Math.max(m_maxUsedMemory, Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());				
	    SeqProjectedDB proj = new SeqProjectedDB(vecDB, vecInd);
	    /*
	    if (m_profileOn)
	    {
	        Long old = (Long) m_membersProfiling.elementAt(CREATE_PROJECTED_DB);
	        m_membersProfiling.setElementAt(new Long(old.longValue()+System.currentTimeMillis()-time), 
	                            					  CREATE_PROJECTED_DB);
	    }
	    */
	    return proj;
	}
	
	public boolean accept(SeqSequence b, SeqItem[] alphabet){
		boolean accepted = tree.verify(b,alphabet);
		return accepted;
		
	}
}

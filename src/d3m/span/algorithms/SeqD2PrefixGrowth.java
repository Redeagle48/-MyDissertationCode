package d3m.span.algorithms;

import java.util.Vector;

import d3m.span.constraints.SeqD2Constraint;
import d3m.span.core.SeqDataset;
import d3m.span.core.SeqItem;
import d3m.span.core.SeqItemset;
import d3m.span.core.SeqProjectedDB;
import d3m.span.core.SeqSequence;
import dm.pam.algorithms.PrefixPam;
import dm.pam.core.Results;
import dm.uspam.core.Sequence;
import dm.uspam.io.SequencesReader;

public class SeqD2PrefixGrowth extends PrefixPam {

	protected final int SATISFIES = 2;
	protected final int SORT_FREQUENT_LIST = 3;
	protected final int CREATE_PROJECTED_DB = 4;

	protected SeqDataset m_dataset = null;

	/** The large itemsets or frequent sequences. */
	protected Vector<Vector<SeqSequence>> m_litemsets = new Vector<Vector<SeqSequence>>();
	/** The number of discovered patterns. */
	protected int m_nrOfPatterns;
	/** The list of frequent items in the dataset */
	protected SeqItem[] flist;

	/** Constraint guiding the algorithm. */
	SeqD2Constraint d2seqconstraint = new SeqD2Constraint();

	/** Creates a new empty instance of GenPrefixSpan */	
	public SeqD2PrefixGrowth()
	{
		super();
	}

	//________________________________________________________________

	/** 
	 * Creates a new instance of GenPrefixSpan and initializes it with user parameters.
	 *
	 * @param minSup The minimum support accepted
	 * @param reader The reader created to read a file with sequences
	 * @param distance The large distance accepted to consider subsequences
	 */
	public SeqD2PrefixGrowth(double minSup, SequencesReader reader, int distance, boolean on)
	{
		//super(minSup, reader, distance, on);
	}

	//________________________________________________________________

	/** 
	 * Creates a new instance of PrefixSpan and initializes it with user parameters.
	 *
	 * @param minSup The minimum support accepted
	 * @param reader The reader created to read a file with sequences
	 * @param distance The large distance accepted to consider subsequences
	 */
	public SeqD2PrefixGrowth(double minSup, SeqDataset db, int distance, boolean on)
	{
		m_dataset = db;
		d2seqconstraint.minSup = (float)minSup;
		d2seqconstraint.m_globalNr = (long) Math.round(Math.ceil(m_dataset.getSize()*minSup));
		d2seqconstraint.m_distance = distance;
		//super(minSup, db, distance, on);
	}

	public SeqD2PrefixGrowth(SeqDataset db, float minSup)
	{
		m_dataset = db;
		d2seqconstraint.minSup = minSup;
		d2seqconstraint.m_globalNr = (long) Math.round(Math.ceil(m_dataset.getSize()*minSup));
		//super(minSup, db, distance, on);
	}


	//________________________________________________________________
	/**
	 * return the index of the maximal element of a vector with positive integers, 
	 * -1 if it is empty
	 */
	protected int getIndexMaxElement(long[] vec, int size)
	{
		long max = -1;
		int maxIndex = -1;
		for (int i=0; i<size; i++)
		{
			if (vec[i] > max)
			{
				max = vec[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	//________________________________________________________________

	/** 
	 * Sorts the vector of items and its counterpart arrayItemsCount
	 * by descending support.
	 */
	protected void sortFrequentList(Vector<SeqItem> vecItems, long[] arrayItemsCount)
	{
		//long time = 0;
		//if (m_profileOn) time = System.currentTimeMillis();
		int n = vecItems.size();
		// Sorts the frequent list
		Vector<SeqItem> f_list = new Vector<SeqItem>(0);
		long[] f_listCount = new long[n];
		System.arraycopy(arrayItemsCount, 0, f_listCount, 0, n);
		int f_listSize = 0;
		boolean ok = true;
		int i = 0;
		while (ok && (i<n))
		{
			// Sorts vecItems based on the elements of vecItemsCount
			int indMax = getIndexMaxElement(f_listCount, n);
			long d = f_listCount[indMax];
			SeqItem elem = vecItems.elementAt(indMax);
			if (ok==satisfies(d))
			{
				f_list.addElement(elem);
				arrayItemsCount[f_listSize]=d;
				f_listSize ++;
			}
			f_listCount[indMax] = 0;
			i++;
		}
		vecItems.removeAllElements();
		vecItems.addAll(f_list);

		/*
		if (m_profileOn)
		{
			Long old = m_membersProfiling.elementAt(SORT_FREQUENT_LIST);
			m_membersProfiling.setElementAt(new Long(old.longValue()+System.currentTimeMillis()-time), 
										  SORT_FREQUENT_LIST);
		}
		 */	
	}

	//________________________________________________________________
	/**
	 * Verifies if the discovered sequence may be accepted as a pattern.
	 * @param s The discovered sequence
	 * @param sup The sequence support, the number of sequence's occurrences
	 * @return True if the sequence support is enough (greater or equal to m_minSup)
	 */
	protected boolean satisfies(Sequence s, long sup)
	{
		long time = 0;
		//if (m_profileOn) time = System.currentTimeMillis();
		boolean ok = (d2seqconstraint.m_globalNr <= (double)sup);
		
		/*
		if (m_profileOn)
		{
			Long old = (Long) m_membersProfiling.elementAt(SATISFIES);
			m_membersProfiling.setElementAt(new Long(old.longValue()+System.currentTimeMillis()-time), 
										  SATISFIES);
		}*/	
		return ok;
	}

	//________________________________________________________________
	/**
	 * Computes the complete set of sequential patterns with some prefix.
	 *
	 * @param alfa The sequential pattern (empty in the first step of recursion).
	 * @param alfaSize The size of alfa.
	 * @param alphabet The set of different elements in the database.
	 * @param db The alfa-projected database. Initially it corresponds to the DB.
	 * @return A vector with the complete set of sequential patterns with prefix alfa.
	 */
	protected Vector<SeqSequence> runRecursively(SeqSequence alfa, int alfaSize, Vector<SeqItem> alphabet, SeqProjectedDB db) throws Exception
	{
		Vector<SeqSequence> frequent = new Vector<SeqSequence>(0);

		// 1. Scan the projected database once, find the set of frequent items b 
		// such that
		//(a) b can be assembled to the last element of alfa to form a sequential 
		// (b) <b> can be appended to alfa to form a sequential pattern
		// Creates 1-itemsets candidates
		int n = alphabet.size();
		long[] arSerialSup = new long[n];
		long[] arParalelSup = new long[n];
		long[] arLastSup = new long[2*n];
		for (int i=0; i<n; i++)
		{    
			arSerialSup[i] = 0; 
			arParalelSup[i] = 0; 
			arLastSup[2*i] = -1; 
			arLastSup[2*i+1] = -1; 
		}

		// For generating the 1-candidates at each step
		d2seqconstraint.generateC1 (db, alphabet, arSerialSup, arParalelSup, arLastSup, m_dataset);
		m_maxUsedMemory = Math.max(m_maxUsedMemory, Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());				
		// el can be assembled to the last element of alfa
		SeqItemset sk = alfa.getItemsetAt(alfa.size()-1);
		// 2. For each frequent item b, append it to alfa to form a sequential 
		// pattern alfa', and output alfa'
		for (int i=0; i<alphabet.size(); i++)
		{
			SeqItem el = (SeqItem)alphabet.elementAt(i);
			SeqItem sk_last = (SeqItem)m_dataset.getAlphabet()[sk.elementIdAt(sk.size()-1)];
			
	        //Element sk_last = (Element)m_db.getAlphabet().elementAt(sk.elementIdAt(sk.size()-1));
			
			// If el can be assembled to the last event of alfa
			if (satisfies(arParalelSup[i]) && (el.isGreaterThan(sk_last)))
			{
				SeqItemset set = new SeqItemset(sk);
				set.addElement(el);
				SeqSequence alfa2 = alfa.subsequence(0, alfa.size()-2);
				alfa2 = alfa2.addElement(set);  
				alfa2.setSupport((int)arParalelSup[i]);
				frequent.addElement(alfa2);
				//System.out.println(alfa2.toString());
				// 3. For each alfa', construct alfa'-projected database, and call 
				// the procedure again with (alfa', alfaSize+1, alfa'-projectedDB) 
				frequent.addAll(runRecursively(alfa2, alfaSize, alphabet, 
						d2seqconstraint.createProjectedDB(set, db, m_dataset))); 
			}
			// el can be appended to alfa
			SeqSequence b = new SeqSequence(new SeqItemset(el));
			SeqSequence alfa2 = alfa.concatenateWith(b);
			//System.out.println(alfa2.toString());
			if (satisfies(alfa2, arSerialSup[i]))
			{
				alfa2.setSupport((int)arSerialSup[i]);
				frequent.addElement(alfa2); //DEFINIR SUPPORTE
				//System.out.println(alfa2.toString());
				// 3. For each alfa', construct alfa'-projected database, and call 
				// the procedure again with (alfa', alfaSize+1, alfa'-projectedDB) 
				frequent.addAll(runRecursively(alfa2, alfaSize+1, alphabet, 
						d2seqconstraint.createProjectedDB(alfa2, db, m_dataset)));
			}
		}
		return frequent;
	}
	
	//________________________________________________________________
	/**
	 * Verifies if the discovered sequence may be accepted as a pattern.
	 * @param s The discovered sequence
	 * @param sup The sequence support, the number of sequence's occurrences
	 * @return True if the sequence support is enough (greater or equal to m_minSup)
	 */
	protected boolean satisfies(SeqSequence s, long sup)
	{
		long time = 0;
		//if (m_profileOn) time = System.currentTimeMillis();
		boolean ok = (d2seqconstraint.m_globalNr <= (double)sup);
		
		/*
		if (m_profileOn)
		{
			Long old = (Long) m_membersProfiling.elementAt(SATISFIES);
			m_membersProfiling.setElementAt(new Long(old.longValue()+System.currentTimeMillis()-time), 
										  SATISFIES);
		}
		*/	
	    return ok;
	}
	
	//________________________________________________________________		
	/**
	 * Verifies if the sequence support is enough (greater or equal to m_minSup).
	 * @param sup The sequence support, the number of sequence's occurrences
	 * @return True if the sequence support is enough (greater or equal to m_minSup)
	 */
	public boolean satisfies(long sup)
	{
		boolean ok = (d2seqconstraint.m_globalNr <= (double)sup);
		
	    return ok;
	}

	//________________________________________________________________

	/**
	 * Runs the algorithm to find the large itemsets.
	 * @return return a vector with frequent patterns in the first element, 
	 *          and the execution time in the second element.
	 */
	public Results run()
	{
		long time = 0;
		//if (m_profileOn) time = System.currentTimeMillis();

		// Creates 1-itemsets candidates
		int n = m_dataset.getAlphabet().length; //m_itemsets do sequence esta vazio
		System.out.println("alphabet size=" + n);
		long[] arrayItemsCount = new long[n];
		for (int i=0; i<n; i++)
			arrayItemsCount[i] = 0;

		// For finding the 1-litemsets
		try {
			d2seqconstraint.generateC1(m_dataset, arrayItemsCount);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//-- generateC1 (m_dataset, arrayItemsCount);

		@SuppressWarnings("unchecked")
		SeqItem[] f_list_array = m_dataset.getAlphabet().clone();
		Vector<SeqItem> f_list = new Vector<SeqItem>(); // sortFrequentList accepts Vector<SeqItem>
		for(SeqItem si : f_list_array)
			f_list.addElement(si);

		sortFrequentList(f_list, arrayItemsCount); 

		// Calls procedure runRecursevely for each of frequent items

		Vector<SeqSequence> patterns = new Vector<SeqSequence>(); 
		Vector<SeqSequence> b_patterns;
		for (int i=0; i<f_list.size(); i++)
		{
			SeqItem el = (SeqItem)f_list.elementAt(i);
			// <el> originates a new alfa
			SeqSequence b = new SeqSequence(new SeqItemset(el));
			//System.out.println(b.toString());
			b.setSupport((int)arrayItemsCount[i]);
			// f_list only contains frequent items
			if (satisfies(b, arrayItemsCount[i]) && d2seqconstraint.accept(b,m_dataset.getAlphabet()))
			{
				patterns.addElement(b);
				//System.out.println("\n\n\n"+b.toString());
				try {
					b_patterns = runRecursively(b, 1, f_list, d2seqconstraint.createProjectedDB(b,m_dataset));
					patterns.addAll(b_patterns);
					System.out.println(b.toString()+"# _PATTERNS = "+ b_patterns.size());
					System.out.println(b.toString()+"===>>>"+b_patterns.toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}    
		}
		m_litemsets.addElement(patterns);
		m_nrOfPatterns = patterns.size();			

		System.out.println("# PATTERNS = "+ m_nrOfPatterns);

		/*
		if (m_profileOn)
		{
			Long old = (Long) m_membersProfiling.elementAt(RUN);
			m_membersProfiling.setElementAt(new Long(old.longValue()+System.currentTimeMillis()-time), 
					RUN);
		}
		 */	

		return null;
	}

	//________________________________________________________________		
	/**
	 * Calls the run method and measures the time and memory wasted.
	 */
	public Vector<String> exec()
	{
		/*	System.out.println("Algoritmo -> "+getClass().getName()
				+" DBsize="+String.valueOf(m_db.getSize())
				+" SUP="+String.valueOf(m_minSupport)
				+" GAP="+String.valueOf(m_distance));
		long initialTime = System.currentTimeMillis();
		 */
		try{
			run();
			/*
			String st_alg = getClass().getName()+"\t\t";
			String st_resume = "\ttime\tLk\tMemory";
			String st_results = "\t"+String.valueOf((System.currentTimeMillis()-initialTime))
					+ "\t" + String.valueOf(m_nrOfPatterns)
					+ "\t" + String.valueOf(m_maxUsedMemory);
			if (m_profileOn)
			{
				for (int i=0; i<m_membersNameProfiling.size(); i++)
				{
					st_alg += "\t";
					st_resume += "\t" +m_membersNameProfiling.elementAt(i);
					st_results += "\t" +((Long)m_membersProfiling.elementAt(i)).toString();

				}
			} */
			Vector<String> vec = new Vector<String>(3);
			//vec.addElement(st_alg);
			//vec.addElement(st_resume);
			//vec.addElement(st_results);
			//	    vec.addElement(L.toString());  
			//	    System.out.println(String.valueOf(L.size())+"->"+L.toString());
			return vec;
		}
		catch (Exception ex){   
			ex.printStackTrace();  
			return null;
		}
	}

}

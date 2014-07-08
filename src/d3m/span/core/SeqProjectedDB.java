package d3m.span.core;

import java.util.Vector;

//--------------------------------------------------------------------
//CLASS ProjectedDB
//--------------------------------------------------------------------
/** Class <CODE>ProjectedDB</CODE> implements projected Databases described 
* in <B>"PrefixSpan: Mining Sequential Patterns Efficiently by Prefix-Projected 
* Pattern Growth"</B> by Jian Pei, Jiawei Han et al [ICDE 2001].
* @version 1.0
* @author Claudia Antunes
*/

public class SeqProjectedDB {

	/** An array with the indexes of sequences in the original DB. */
	int m_pDB[];
	/** An array with the indexes of the start of each subsequence. */
	int m_pSeq[];
	/** The number of sequences in the projected db. */
	int m_size;
    /** The number of different original sequences in the projected db. */
    int m_nrDiffSeqs;
	
	/** Creates a new instance of ProjectedDB */
	public SeqProjectedDB() 
	{
		m_size = 0;
	}
	//________________________________________________________________
	public SeqProjectedDB(Vector<Integer> pBD, Vector<Integer> pSeq, int nrDiff) 
	{
		m_size = pBD.size();
		m_pDB = new int[m_size];
		m_pSeq = new int[m_size];
        m_nrDiffSeqs = nrDiff;
		for (int i=0;i<m_size; i++)
        {	
            m_pDB[i] = ((Integer)pBD.elementAt(i)).intValue();
			m_pSeq[i] = ((Integer)pSeq.elementAt(i)).intValue();		
        }
		
//System.out.println("projDB->"+pBD.toString());		
	}
	public SeqProjectedDB(Vector<Integer> pBD, Vector<Integer> pSeq) 
	{
		m_size = pBD.size();
		m_pDB = new int[m_size];
		m_pSeq = new int[m_size];
        m_nrDiffSeqs = 0;
		for (int i=0;i<m_size; i++)
        {	
            m_pDB[i] = ((Integer)pBD.elementAt(i)).intValue();
			m_pSeq[i] = ((Integer)pSeq.elementAt(i)).intValue();		
        }
//System.out.println("projDB->"+pBD.toString());		
	}	//________________________________________________________________
	/** 
	 * Returns the index of the sequence (in the original database) at position i. 
	 * @param i The sequence index at m_pBD.
	 * @return The index of the sequence at position i.
	 */
	public int indexOfSequenceAt(int i)
	{	return m_pDB[i];	}
	//________________________________________________________________
	/** 
	 * Returns the index of the first symbol of subsequence at position i. 
	 * @param i The sequence index at m_pSeq.
	 * @return The index of the first symbol of subsequence at position i.
	 */	
	public int indexOfStartSubsequenceAt(int i)
	{	return m_pSeq[i];	}
	//________________________________________________________________
	/**
	 * Return the number of sequences in the projected db. 
	 */
	public int size()
	{
		return m_size;
	}
    public int getNrOfDifferentSequences()
    {   
        return m_nrDiffSeqs;
    }
	//________________________________________________________________
    public String toString()
	{
		String st = "[";
		for (int i=0; i<m_size; i++)
			st += "("+ String.valueOf(m_pDB[i]) + "," + String.valueOf(m_pSeq[i])+ ")";
		st += "]";
		return st;
	}
 
	
}

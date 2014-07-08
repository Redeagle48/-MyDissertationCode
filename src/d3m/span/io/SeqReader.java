package d3m.span.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import dm.pam.core.Item;
import dm.pam.io.*;
import d3m.d2pm.core.*;
import d3m.d2pm.io.D2Reader;
import d3m.spam.core.Element;
import d3m.span.core.SeqDataset;
import d3m.span.core.SeqItem;
import d3m.span.core.SeqSequence;

public class SeqReader { //abstract public class D2Reader extends Reader {
	
	/** The dataset */
    protected SeqDataset m_db = null;
    //________________________________________________________________
    /**
     * Creates a new instance of SequencesReader.
     */
    public SeqReader()
    {
        m_db = new SeqDataset();
    }
    /**
     * Reads a file into a Vector of Sequences.
     * @param filename the filename
     * @exception Exception if the file is not read successfully
     */
    public SeqReader(String filename) throws Exception
    {
        FileReader in = new FileReader(filename);
        BufferedReader dataIn = new BufferedReader(in);
        m_db = new SeqDataset();
        String line = new String("");
        Vector<SeqItem> alphabet = new Vector<SeqItem>();
        while (null != (line=dataIn.readLine())){
			SeqSequence s = new SeqSequence(line, alphabet);
			m_db.addSequence(s);
		}
        m_db.setAlphabet(alphabet);
        in.close();
        dataIn.close();
        //System.out.println("ALPHABET="+alphabet.toString());        
    }
    //________________________________________________________________
    /**
     * Returns a vector with read sequences.
     */
    public SeqDataset getSequences()
	{   return m_db;	}
    //________________________________________________________________
}

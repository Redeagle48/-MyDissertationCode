package d3m.span;

import java.io.File;
import java.util.Vector;

import d3m.span.algorithms.SeqD2PrefixGrowth;
import d3m.span.core.SeqDataset;
import d3m.span.io.SeqReader;
import d3m.span.ontologies.Ontology;
import d3m.span.ontologies.io.OWLReader;


public class Main {

	private int m_alg = 3;
	private double m_sup = 0.50;
	private int m_gap = 2;
	private boolean m_profile = true;
	//private String m_file = new File("").getAbsolutePath() + "/" + "data/tagus_consumptions/output_weekdays.txt";
	//private String m_file = new File("").getAbsolutePath() + "/" + "src/d3m/span/N10Ns5000Ni10000DB10C10T2S4I2.txt";
	private String m_file = new File("").getAbsolutePath() + "/" + "src/d3m/span/sample.txt"; //

	private String m_constraint = null;

	private String m_fileOntology = new File("").getAbsolutePath() + "/" + "src/d3m/span/ontologyV3.owl";

	private void parseCommandLine(String args[])
	{
		for(int i=0;i<args.length;i++)
		{
			if(args[i].charAt(0) != '-') break;
			++i;
			switch(args[i-1].charAt(1))
			{   
			// Algorithm
			//case 'a': m_alg =  (new Integer(args[i])).intValue();	break;
			// Support
			case 's': m_sup = (new Double(args[i])).doubleValue(); break;
			// Sequence Constraints
			case 'o': m_constraint = new String(args[i]); break;
			// Gap
			case 'g': m_gap = (new Integer(args[i])).intValue();	break;
			// Filenames
			case 'f': m_file = args[i]; break;
			// Profiling
			case 'p': int ok=(new Integer(args[i])).intValue();
			if (0==ok) m_profile = false;
			else m_profile = true;
			break;
			default: System.err.print("unknown option\n");
			}
		}
	}	
	/**
	 * Runner of the D2SeqConstraint
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		Main runner = new Main();
		
		// Parse the commands in the CommandLine
		runner.parseCommandLine(args);
		
		// Read the database to a SeqDataset object
		SeqDataset db = null;
		try {
			db = (new SeqReader(runner.m_file)).getSequences();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Read the sequential ontology
		OWLReader reader = null; 
		try {
			reader = new OWLReader(runner.m_fileOntology);
			Ontology ont = new Ontology();
			reader.updateOntology(ont);
			
			System.out.println("==> RESULT: " + ont.toString());
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		// Instatiate the sequential ontology
		// TODO
		
		// Run the algorithm
		SeqD2PrefixGrowth alg = new SeqD2PrefixGrowth((float)runner.m_sup,db,runner.m_gap, true);
		System.gc();
		Vector<String> result = alg.exec();


	}

}

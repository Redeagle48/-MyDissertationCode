package d3m.span;

import java.io.File;
import java.util.Vector;

import d3m.span.algorithms.SeqD2PrefixGrowth;
import d3m.span.constraints.SeqD2Rule;
import d3m.span.core.SeqDataset;
import d3m.span.core.Taxonomy.ComposedElement;
import d3m.span.io.LogicProcess;
import d3m.span.io.OntologyHolder;
import d3m.span.io.ProcessExtractOntology;
import d3m.span.io.ProcessXMLTaxonomy;
import d3m.span.io.SeqReader;


public class Main {

	private int m_alg = 3;
	private double m_sup = 0.5;
	private int m_gap = 0;


	private boolean m_profile = true;
	//private String m_file = new File("").getAbsolutePath() + "/" + "data/tagus_consumptions/output_weekdays.txt";
	//private String m_file = new File("").getAbsolutePath() + "/" + "src/d3m/span/N10Ns5000Ni10000DB10C10T2S4I2.txt"; //
	private String m_file = new File("").getAbsolutePath() + "/" + "src/d3m/span/full_N10Ns5000Ni10000DB10C10T2S4I2.txt";
	//private String m_file = new File("").getAbsolutePath() + "/" + "src/d3m/span/sample.txt"; //

	private String m_constraint = null;

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
		
		GlobalVariables.init();

		//////////////////////////////////////////////
		// Parse the commands in the CommandLine
		runner.parseCommandLine(args);
		//////////////////////////////////////////////

		//////////////////////////////////////////////
		// Read the database to a SeqDataset object
		//////////////////////////////////////////////
		SeqDataset db = null;
		try {
			db = (new SeqReader(runner.m_file)).getSequences();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//////////////////////////////////////////////
		// Instatiate the sequential ontology
		//////////////////////////////////////////////
		
		GlobalVariables.Timer.set(GlobalVariables.XML_TO_ONTOLOGY, System.currentTimeMillis());
		
	
		//LogicProcess logicProcess = new LogicProcess();
		//logicProcess.execute();
		
		
		GlobalVariables.Timer.set(GlobalVariables.XML_TO_ONTOLOGY, 
				System.currentTimeMillis()-GlobalVariables.Timer.get(GlobalVariables.XML_TO_ONTOLOGY));
		
		//////////////////////////////////////////////
		// Correspond the instantiated ontology to rules
		//////////////////////////////////////////////
		
		GlobalVariables.Timer.set(GlobalVariables.ONTOLOGY_TO_RULES, System.currentTimeMillis());
		
		
		//ProcessExtractOntology processExtractOntology = new ProcessExtractOntology(logicProcess);
		//Vector<SeqD2Rule> ruleVector =  processExtractOntology.execute();
		//ComposedElement topElement = processExtractOntology.getTaxonomy();
		
		
		GlobalVariables.Timer.set(GlobalVariables.ONTOLOGY_TO_RULES, 
				System.currentTimeMillis()-GlobalVariables.Timer.get(GlobalVariables.ONTOLOGY_TO_RULES));
		
		//System.out.println("\nExpliciting the resulting rules:\n");
		//for (SeqD2Rule seqD2Rule : ruleVector) {
		//	System.out.println("Rule: " + seqD2Rule.getRule() + " from Restriction: " + seqD2Rule.getRestriction());
		//	System.out.println(seqD2Rule+"\n");
		//}
		//System.out.println("===============================\n");
		
		//System.out.println("\n=====>>> Printing the taxonomy");
		//topElement.print();

		//////////////////////////////////////////////
		// Run the algorithm
		//////////////////////////////////////////////
		
		GlobalVariables.Timer.set(GlobalVariables.MINING, System.currentTimeMillis());
		
		//SeqD2PrefixGrowth alg = new SeqD2PrefixGrowth(runner.m_sup,db,runner.m_gap, true,ruleVector,topElement);
		
		// without rules
		SeqD2PrefixGrowth alg = new SeqD2PrefixGrowth(runner.m_sup,db,runner.m_gap, true,null,null);
		
		System.gc();
		
		Runtime obj = Runtime.getRuntime();
		obj.gc();
		
		Vector<String> result = alg.exec();
		
		GlobalVariables.Timer.set(GlobalVariables.MINING, 
				System.currentTimeMillis()-GlobalVariables.Timer.get(GlobalVariables.MINING));

		//System.out.println("Free memory: " + Runtime.getRuntime().freeMemory());

		System.out.println("Total memory: " + (obj.totalMemory()-obj.freeMemory()));
		
		//System.out.println("Total memory: " + Runtime.getRuntime().maxMemory());

		//System.out.println("XML TO Ontology: " + GlobalVariables.Timer.get(GlobalVariables.XML_TO_ONTOLOGY));
		//System.out.println("Ontology to rules: " + GlobalVariables.Timer.get(GlobalVariables.ONTOLOGY_TO_RULES));
		//System.out.println("Mining: " + GlobalVariables.Timer.get(GlobalVariables.MINING));
		System.out.println("project db: " + GlobalVariables.Timer.get(GlobalVariables.PROJECTDB));
		System.out.println("generate candidates: " + GlobalVariables.Timer.get(GlobalVariables.GENERATECANDIDATES));
		System.out.println("verify constraint: " + GlobalVariables.Timer.get(GlobalVariables.VERIFYCONSTRAINT));

	}
}

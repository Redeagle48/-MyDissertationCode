package d3m.span;

import java.io.File;
import java.util.Set;
import java.util.Vector;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

import d3m.span.algorithms.SeqD2PrefixGrowth;
import d3m.span.constraints.SeqD2Rule;
import d3m.span.core.SeqDataset;
import d3m.span.io.LogicProcess;
import d3m.span.io.OntologyHolder;
import d3m.span.io.ProcessExtractOntology;
import d3m.span.io.ProcessXMLTaxonomy;
import d3m.span.io.SeqReader;
import d3m.span.ontologies.Ontology;
import d3m.span.ontologies.io.OWLReader;


public class Main {

	private int m_alg = 3;
	private double m_sup = 0.30;
	private int m_gap = 2;


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
		// Read the taxonomy relation of the items to be analyzed
		//////////////////////////////////////////////
		//ProcessXMLTaxonomy processXMLTaxonomy = new ProcessXMLTaxonomy();
		//processXMLTaxonomy.execute();

		//////////////////////////////////////////////
		// Read the sequential ontology
		//////////////////////////////////////////////
		//OWLReader reader = null; 
		//try {
		//	reader = new OWLReader(runner.m_fileOntology);
		//	Ontology ont = new Ontology();
			//reader.updateOntology(ont);

			//System.out.println("==> RESULT: " + ont.toString());

		//} catch(Exception e) {
		//	e.printStackTrace();
		//}

		//////////////////////////////////////////////
		// Instatiate the sequential ontology
		//////////////////////////////////////////////
		LogicProcess logicProcess = new LogicProcess();
		logicProcess.execute();

		//////////////////////////////////////////////
		// Correspond the instantiated ontology to rules
		//////////////////////////////////////////////
		//System.out.println("\n=====>>> Printing rules of tree");
		ProcessExtractOntology processExtractOntology = new ProcessExtractOntology();
		Vector<SeqD2Rule> ruleVector =  processExtractOntology.readFromOntology(logicProcess);
		
		System.out.println("\nExpliciting the resulting rules:\n");
		for (SeqD2Rule seqD2Rule : ruleVector) {
			System.out.println("Rule: " + seqD2Rule.getRule() + " from Restriction: " + seqD2Rule.getRestriction());
			System.out.println(seqD2Rule+"\n");
		}
		System.out.println("===============================\n");

		//////////////////////////////////////////////
		// Run the algorithm
		//////////////////////////////////////////////
		//SeqD2PrefixGrowth alg = new SeqD2PrefixGrowth(runner.m_sup,db,runner.m_gap, true,ruleVector);
		System.gc();
		//Vector<String> result = alg.exec();


	}
}

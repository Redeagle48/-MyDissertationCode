package d3m.span;

import java.io.File;
import java.util.Set;
import java.util.Vector;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

import d3m.span.algorithms.SeqD2PrefixGrowth;
import d3m.span.constraints.SeqD2Rule;
import d3m.span.core.SeqDataset;
import d3m.span.io.LogicProcess;
import d3m.span.io.OntologyHolder;
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

	private String m_input = new File("").getAbsolutePath() + "/" + "src/d3m/span/assets/input3.xml";
	private String m_inputSchema = new File("").getAbsolutePath() + "/" + "src/d3m/span/assets/inputSchema.xsd";


	private String m_constraint = null;

	private String m_fileOntology = new File("").getAbsolutePath() + "/" + "src/d3m/span/assets/ontologyV4.owl";

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
		// TODO

		//////////////////////////////////////////////
		// Read the sequential ontology
		//////////////////////////////////////////////
		OWLReader reader = null; 
		try {
			reader = new OWLReader(runner.m_fileOntology);
			Ontology ont = new Ontology();
			reader.updateOntology(ont);

			System.out.println("==> RESULT: " + ont.toString());

		} catch(Exception e) {
			e.printStackTrace();
		}

		//////////////////////////////////////////////
		// Instatiate the sequential ontology
		//////////////////////////////////////////////
		LogicProcess logicProcess = new LogicProcess();
		logicProcess.execute();
		Vector<SeqD2Rule> ruleVector = readFromOntology(logicProcess);

		//////////////////////////////////////////////
		// Run the algorithm
		//////////////////////////////////////////////
		SeqD2PrefixGrowth alg = new SeqD2PrefixGrowth(runner.m_sup,db,runner.m_gap, true,ruleVector);
		System.gc();
		Vector<String> result = alg.exec();


	}

	static Vector<SeqD2Rule> readFromOntology(LogicProcess logicProcess){
		OntologyHolder ontologyHolder = logicProcess.getOntologyHolder();
		
		// Vector with the rules to be applied to the algorithm
		Vector<SeqD2Rule> ruleVec = new Vector<SeqD2Rule>();
		
		// Read the ontology
		OWLOntology ont = ontologyHolder.getOWLOntology();
		OWLDataFactory factory = ontologyHolder.getOWLDataFactory();

		OWLClass constraintClass = factory.getOWLClass(IRI.create(ont.getOntologyID()
				.getOntologyIRI().toString() + "#ConstraintComposition"));

		System.out.println("------->>>>> List Individuals from Constraint Composition");

		Set<OWLIndividual> individuals = constraintClass.getIndividuals(ont);

		for (OWLIndividual owlIndividual : individuals) {
			System.out.println(owlIndividual.toString());

			OWLObjectProperty hasConstraint = factory.getOWLObjectProperty(":hasConstraint",ontologyHolder.getPrefixOWLOntologyFormat());

			Set<OWLIndividual> individualsNamed = owlIndividual.getObjectPropertyValues(hasConstraint, ont);
			
			
			for (OWLIndividual individualNamed : individualsNamed) {
				System.out.println(individualNamed.toString());

				System.out.println("Constraint's Individual class: " + ontologyHolder.individual_Asserted_listClasses(individualNamed.asOWLNamedIndividual()));

				String begin = ontologyHolder.individual_Asserted_listClasses(individualNamed.asOWLNamedIndividual());
				begin = begin.split("#")[1];

				OWLObjectProperty hasBegin = factory.getOWLObjectProperty(":hasBegin",ontologyHolder.getPrefixOWLOntologyFormat());

				Set<OWLIndividual> item = individualNamed.getObjectPropertyValues(hasBegin, ont);

				for (OWLIndividual owlIndividual2 : item) {
					System.out.println("Item: " + owlIndividual2.toStringID());
					begin = owlIndividual2.toStringID();
					begin = begin.split("#")[1];
					begin = begin.split("_")[0];
					System.out.println(begin);
					
					SeqD2Rule[] res = readFromOntology_begin(begin);
					for (SeqD2Rule seqD2Rule : res) {
						ruleVec.add(seqD2Rule);
					}
				}
			}
		}
		return ruleVec;
	}

	static SeqD2Rule[] readFromOntology_begin(String item){
		SeqD2Rule rule = new SeqD2Rule
						((short)1,		// rule
						(short)1, 		// restriction
						(short)0, 		// gap_interTransaction
						(short)0, 		// gap_intraRestriction
						(short)0, 		// itemset
						(short)2,		// item
						false);		// isParallel
		return new SeqD2Rule[]{rule};
	}
	
	static SeqD2Rule[] readFromOntology_exists(String item, int gap){
		SeqD2Rule rule = new SeqD2Rule
						((short)1,		// rule
						(short)1, 		// restriction
						(short)gap, 		// gap_interTransaction
						(short)0, 		// gap_intraRestriction
						(short)0, 		// itemset
						(short)2,		// item
						false);		// isParallel
		return new SeqD2Rule[]{rule};
	}
	
	static SeqD2Rule[] readFromOntology_end(String item, int gap){
		SeqD2Rule rule = new SeqD2Rule
						((short)1,		// rule
						(short)1, 		// restriction
						(short)gap, 		// gap_interTransaction
						(short)0, 		// gap_intraRestriction
						(short)0, 		// itemset
						(short)2,		// item
						false);		// isParallel
		return new SeqD2Rule[]{rule};
	}
	
	static SeqD2Rule[] readFromOntology_precedence(String item1, String item2, int gap){
		SeqD2Rule rule1 = new SeqD2Rule
						((short)1,		// rule
						(short)1, 		// restriction
						(short)gap, 		// gap_interTransaction
						(short)0, 		// gap_intraRestriction
						(short)0, 		// itemset
						(short)2,		// item
						false);		// isParallel
		
		SeqD2Rule rule2 = new SeqD2Rule
				((short)1,		// rule
				(short)1, 		// restriction
				(short)gap, 		// gap_interTransaction
				(short)0, 		// gap_intraRestriction
				(short)0, 		// itemset
				(short)2,		// item
				false);		// isParallel
		
		return new SeqD2Rule[]{rule1,rule2};
	}
}

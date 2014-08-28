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
		ProcessXMLTaxonomy processXMLTaxonomy = new ProcessXMLTaxonomy();
		processXMLTaxonomy.execute();

		//////////////////////////////////////////////
		// Read the sequential ontology
		//////////////////////////////////////////////
		OWLReader reader = null; 
		try {
			reader = new OWLReader(runner.m_fileOntology);
			Ontology ont = new Ontology();
			//reader.updateOntology(ont);

			//System.out.println("==> RESULT: " + ont.toString());

		} catch(Exception e) {
			e.printStackTrace();
		}

		//////////////////////////////////////////////
		// Instatiate the sequential ontology
		//////////////////////////////////////////////
		LogicProcess logicProcess = new LogicProcess();
		logicProcess.execute();

		//////////////////////////////////////////////
		// Correspond the instantiated ontology to rules
		//////////////////////////////////////////////
		System.out.println("\n=====>>> Printing rules of tree");
		Vector<SeqD2Rule> ruleVector = readFromOntology(logicProcess);
		for (SeqD2Rule seqD2Rule : ruleVector) {
			System.out.println(seqD2Rule);
		}
		System.out.println("===============================\n");

		//////////////////////////////////////////////
		// Run the algorithm
		//////////////////////////////////////////////
		SeqD2PrefixGrowth alg = new SeqD2PrefixGrowth(runner.m_sup,db,runner.m_gap, true,ruleVector);
		System.gc();
		//Vector<String> result = alg.exec();


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

				String constraint = ontologyHolder.individual_Asserted_listClasses(individualNamed.asOWLNamedIndividual());
				constraint = constraint.split("#")[1];

				// TODO To refactor
				if(constraint.equals("Begin")){

					OWLObjectProperty hasBegin = factory.getOWLObjectProperty(":hasBegin",ontologyHolder.getPrefixOWLOntologyFormat());

					Set<OWLIndividual> item = individualNamed.getObjectPropertyValues(hasBegin, ont);

					for (OWLIndividual owlIndividual2 : item) {
						System.out.println("Item: " + owlIndividual2.toStringID());
						String item_begin = owlIndividual2.toStringID();
						item_begin = item_begin.split("#")[1];
						item_begin = item_begin.split("_")[0];
						System.out.println(item_begin);

						SeqD2Rule[] res = readFromOntology_begin(item_begin,0);
						for (SeqD2Rule seqD2Rule : res) {
							ruleVec.add(seqD2Rule);
						}
					}
				}

				else if(constraint.equals("Exists")){
					System.out.println("Exists");
					OWLObjectProperty hasExists = factory.getOWLObjectProperty(":hasExists",ontologyHolder.getPrefixOWLOntologyFormat());
					Set<OWLIndividual> item = individualNamed.getObjectPropertyValues(hasExists, ont);


					for (OWLIndividual owlIndividual2 : item) {
						System.out.println("Item: " + owlIndividual2.toStringID());
						String item_exists = owlIndividual2.toStringID();
						item_exists = item_exists.split("#")[1];
						item_exists = item_exists.split("_")[0];


						SeqD2Rule[] res = readFromOntology_exists(item_exists,0);
						for (SeqD2Rule seqD2Rule : res) {
							ruleVec.add(seqD2Rule);
						}
					}

				}

				else if(constraint.equals("End")){
					System.out.println("End");
					OWLObjectProperty hasEnd = factory.getOWLObjectProperty(":hasEnd",ontologyHolder.getPrefixOWLOntologyFormat());
					Set<OWLIndividual> item = individualNamed.getObjectPropertyValues(hasEnd, ont);


					for (OWLIndividual owlIndividual2 : item) {
						System.out.println("Item: " + owlIndividual2.toStringID());
						String item_end = owlIndividual2.toStringID();
						item_end = item_end.split("#")[1];
						item_end = item_end.split("_")[0];


						SeqD2Rule[] res = readFromOntology_end(item_end,0);
						for (SeqD2Rule seqD2Rule : res) {
							ruleVec.add(seqD2Rule);
						}
					}
				}

				else if(constraint.equals("Precedence")){
					System.out.println("Precedence");
					OWLObjectProperty hasAntecedent = factory.getOWLObjectProperty(":antecedent",ontologyHolder.getPrefixOWLOntologyFormat());
					Set<OWLIndividual> itemAntecedent_set = individualNamed.getObjectPropertyValues(hasAntecedent, ont);
					
					String item_antecedent = null, item_consequent = null;
					for (OWLIndividual owlIndividual2 : itemAntecedent_set) {
						System.out.println("Antecedent Item: " + owlIndividual2.toStringID());
						item_antecedent = owlIndividual2.toStringID();
						item_antecedent = item_antecedent.split("#")[1];
						item_antecedent = item_antecedent.split("_")[0];
						
					}
					
					OWLObjectProperty hasConsequent = factory.getOWLObjectProperty(":consequent",ontologyHolder.getPrefixOWLOntologyFormat());
					Set<OWLIndividual> itemConsequent_set = individualNamed.getObjectPropertyValues(hasConsequent, ont);
					
					for (OWLIndividual owlIndividual2 : itemConsequent_set) {
						System.out.println("Consequent Item: " + owlIndividual2.toStringID());
						item_consequent = owlIndividual2.toStringID();
						item_consequent = item_consequent.split("#")[1];
						item_consequent = item_consequent.split("_")[0];
						
					}
					
					SeqD2Rule[] res = readFromOntology_precedence(item_antecedent, item_consequent, 0);
					for (SeqD2Rule seqD2Rule : res) {
						ruleVec.add(seqD2Rule);
					}
				}
			}
		}
		return ruleVec;
	}

	static SeqD2Rule[] readFromOntology_begin(String item,int gap){
		
		int item_int = Integer.parseInt(item);
		
		SeqD2Rule rule = new SeqD2Rule
				((short)1,		// rule
						(short)1, 		// restriction
						(short)0, 		// gap_interTransaction
						(short)0, 		// gap_intraRestriction
						(short)0, 		// itemset
						(short)item_int,		// item
						false);		// isParallel
		return new SeqD2Rule[]{rule};
	}

	static SeqD2Rule[] readFromOntology_exists(String item, int gap){
		
		int item_int = Integer.parseInt(item);
		
		SeqD2Rule rule = new SeqD2Rule
				((short)1,		// rule
						(short)1, 		// restriction
						(short)gap, 		// gap_interTransaction
						(short)0, 		// gap_intraRestriction
						(short)0, 		// itemset
						(short)item_int,		// item
						false);		// isParallel
		return new SeqD2Rule[]{rule};
	}

	static SeqD2Rule[] readFromOntology_end(String item, int gap){
		
		int item_int = Integer.parseInt(item);
		
		SeqD2Rule rule = new SeqD2Rule
				((short)1,		// rule
						(short)1, 		// restriction
						(short)gap, 		// gap_interTransaction
						(short)0, 		// gap_intraRestriction
						(short)0, 		// itemset
						(short)item_int,		// item
						false);		// isParallel
		return new SeqD2Rule[]{rule};
	}

	static SeqD2Rule[] readFromOntology_precedence(String item1, String item2, int gap){
		
		int antecedent = Integer.parseInt(item1);
		int consequent = Integer.parseInt(item2);
		
		SeqD2Rule rule1 = new SeqD2Rule
				((short)1,		// rule
						(short)1, 		// restriction
						(short)gap, 		// gap_interTransaction
						(short)0, 		// gap_intraRestriction
						(short)0, 		// itemset
						(short)antecedent,		// item
						false);		// isParallel

		SeqD2Rule rule2 = new SeqD2Rule
				((short)1,		// rule
						(short)1, 		// restriction
						(short)gap, 		// gap_interTransaction
						(short)0, 		// gap_intraRestriction
						(short)0, 		// itemset
						(short)consequent,		// item
						false);		// isParallel

		return new SeqD2Rule[]{rule1,rule2};
	}
}

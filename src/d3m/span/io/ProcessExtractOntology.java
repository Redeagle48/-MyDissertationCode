package d3m.span.io;

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

import d3m.span.constraints.SeqD2Rule;

public class ProcessExtractOntology {

	public ProcessExtractOntology(){}

	public Vector<SeqD2Rule> readFromOntology(LogicProcess logicProcess){
		OntologyHolder ontologyHolder = logicProcess.getOntologyHolder();

		// Vector with the rules to be applied to the algorithm
		Vector<SeqD2Rule> ruleVec = new Vector<SeqD2Rule>();

		// Read the ontology
		OWLOntology ont = ontologyHolder.getOWLOntology();
		OWLDataFactory factory = ontologyHolder.getOWLDataFactory();

		// Get ConstraintComposition instance
		OWLClass constraintClass = factory.getOWLClass(IRI.create(ont.getOntologyID()
				.getOntologyIRI().toString() + "#ConstraintComposition"));

		Set<OWLIndividual> individualsNamed = constraintClass.getIndividuals(ont);
		Object[] arr = individualsNamed.toArray();
		
		while(!individualsNamed.isEmpty()){
			
			OWLIndividual owlIndividual = (OWLIndividual)arr[0];
			System.out.println(owlIndividual.toString());

			OWLObjectProperty hasConstraint = factory.getOWLObjectProperty(":nextConstraint",ontologyHolder.getPrefixOWLOntologyFormat());

			individualsNamed = owlIndividual.getObjectPropertyValues(hasConstraint, ont);
			Object[] arr2 = individualsNamed.toArray();
			OWLIndividual individualNamed = (OWLIndividual)arr2[0];
			System.out.println(individualNamed.toString());

			System.out.println("Constraint's Individual class: " + ontologyHolder.individual_Asserted_listClasses(individualNamed.asOWLNamedIndividual()));

			String constraint = ontologyHolder.individual_Asserted_listClasses(individualNamed.asOWLNamedIndividual());
			constraint = constraint.split("#")[1];

			// TODO To refactor
			if(constraint.equals("Begin")){

				System.out.println("--> In the Begin");

				// Obtain item
				OWLObjectProperty hasBegin = factory.getOWLObjectProperty(":hasItem",ontologyHolder.getPrefixOWLOntologyFormat());
				Set<OWLIndividual> item = individualNamed.getObjectPropertyValues(hasBegin, ont);

				// Obtain gap
				OWLDataProperty hasGap = factory.getOWLDataProperty(":hasGap",ontologyHolder.getPrefixOWLOntologyFormat());
				Set<OWLLiteral> item_set = owlIndividual.getDataPropertyValues(hasGap, ont);

				int hasGap_int = -1;
				for(OWLLiteral hasGap_value : item_set){
					hasGap_int = Integer.parseInt(hasGap_value.getLiteral());
					System.out.println("OLAOLAOLAODADQ$: hasGap: "  + hasGap_int);
				}

				for (OWLIndividual owlIndividual2 : item) {
					System.out.println("Item: " + owlIndividual2.toStringID());
					String item_begin = owlIndividual2.toStringID();
					item_begin = item_begin.split("#")[1];
					item_begin = item_begin.split("_")[0];
					System.out.println(item_begin);

					SeqD2Rule[] res = readFromOntology_begin(item_begin,hasGap_int);
					for (SeqD2Rule seqD2Rule : res) {
						ruleVec.add(seqD2Rule);
					}
				}
			}

			else if(constraint.equals("Exists")){
				System.out.println("Exists");

				// Obtain item
				OWLObjectProperty hasExists = factory.getOWLObjectProperty(":hasItem",ontologyHolder.getPrefixOWLOntologyFormat());
				Set<OWLIndividual> item = individualNamed.getObjectPropertyValues(hasExists, ont);

				// Obtain gap
				OWLDataProperty hasGap = factory.getOWLDataProperty(":hasGap",ontologyHolder.getPrefixOWLOntologyFormat());
				Set<OWLLiteral> item_set = owlIndividual.getDataPropertyValues(hasGap, ont);

				int hasGap_int = -1;
				for(OWLLiteral hasGap_value : item_set){
					hasGap_int = Integer.parseInt(hasGap_value.getLiteral());
					System.out.println("OLAOLAOLAODADQ$: hasGap: "  + hasGap_int);
				}

				for (OWLIndividual owlIndividual2 : item) {
					System.out.println("Item: " + owlIndividual2.toStringID());
					String item_exists = owlIndividual2.toStringID();
					item_exists = item_exists.split("#")[1];
					item_exists = item_exists.split("_")[0];

					SeqD2Rule[] res = readFromOntology_exists(item_exists,hasGap_int);
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
			
			OWLObjectProperty nextConstraintSequence = factory.getOWLObjectProperty(":nextConstraintSequence",ontologyHolder.getPrefixOWLOntologyFormat());

			individualsNamed = individualNamed.getObjectPropertyValues(nextConstraintSequence, ont);
			arr = individualsNamed.toArray();
		}

		return ruleVec;
	}

	SeqD2Rule[] readFromOntology_begin(String item,int gap){

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

	SeqD2Rule[] readFromOntology_exists(String item, int gap){

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

	SeqD2Rule[] readFromOntology_end(String item, int gap){

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

	SeqD2Rule[] readFromOntology_precedence(String item1, String item2, int gap){

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

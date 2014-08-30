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
import d3m.span.io.rulesWriters.BeginRules;
import d3m.span.io.rulesWriters.EndRules;
import d3m.span.io.rulesWriters.ExistRules;
import d3m.span.io.rulesWriters.PrecedenceRules;

public class ProcessExtractOntology {

	public ProcessExtractOntology(){}

	public Vector<SeqD2Rule> readFromOntology(LogicProcess logicProcess){
		
		int constraintsCounter = 0; // Constraints already processed
		
		System.out.println("\n=============WRITING RULES FROM CONSTRAINTS=============");
		
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
			//System.out.println(owlIndividual.toString());

			OWLObjectProperty hasConstraint = factory.getOWLObjectProperty(":nextConstraint",ontologyHolder.getPrefixOWLOntologyFormat());

			individualsNamed = owlIndividual.getObjectPropertyValues(hasConstraint, ont);
			Object[] arr2 = individualsNamed.toArray();
			OWLIndividual individualNamed = (OWLIndividual)arr2[0];
			//System.out.println(individualNamed.toString());

			//System.out.println("Constraint's Individual class: " + ontologyHolder.individual_Asserted_listClasses(individualNamed.asOWLNamedIndividual()));

			String constraint = ontologyHolder.individual_Asserted_listClasses(individualNamed.asOWLNamedIndividual());
			constraint = constraint.split("#")[1];

			// TODO To refactor
			if(constraint.equals("Begin")){

				constraintsCounter++;
				
				System.out.println("\n==> Writing a Begin's rule");

				// Obtain item
				OWLObjectProperty hasBegin = factory.getOWLObjectProperty(":hasItem",ontologyHolder.getPrefixOWLOntologyFormat());
				Set<OWLIndividual> item = individualNamed.getObjectPropertyValues(hasBegin, ont);

				// Obtain gap
				OWLDataProperty hasGap = factory.getOWLDataProperty(":hasGap",ontologyHolder.getPrefixOWLOntologyFormat());
				Set<OWLLiteral> item_set = owlIndividual.getDataPropertyValues(hasGap, ont);

				int hasGap_int = -1;
				for(OWLLiteral hasGap_value : item_set){
					hasGap_int = Integer.parseInt(hasGap_value.getLiteral());
					System.out.println("Gap: "  + hasGap_int);
				}

				for (OWLIndividual owlIndividual2 : item) {
					//System.out.println("Item: " + owlIndividual2.toStringID());
					String item_begin = owlIndividual2.toStringID();
					item_begin = item_begin.split("#")[1];
					item_begin = item_begin.split("_")[0];
					System.out.println("Item: " + item_begin);

					SeqD2Rule[] res = (new BeginRules().writeRules(new String[]{item_begin}, new int[]{hasGap_int}, ruleVec.size(), constraintsCounter));
					for (SeqD2Rule seqD2Rule : res) {
						ruleVec.add(seqD2Rule);
					}
				}
			}

			else if(constraint.equals("Exists")){
				
				constraintsCounter++;
				
				System.out.println("\n==> Writing a Exists' rule");

				// Obtain item
				OWLObjectProperty hasExists = factory.getOWLObjectProperty(":hasItem",ontologyHolder.getPrefixOWLOntologyFormat());
				Set<OWLIndividual> item = individualNamed.getObjectPropertyValues(hasExists, ont);

				// Obtain gap
				OWLDataProperty hasGap = factory.getOWLDataProperty(":hasGap",ontologyHolder.getPrefixOWLOntologyFormat());
				Set<OWLLiteral> item_set = owlIndividual.getDataPropertyValues(hasGap, ont);

				int hasGap_int = -1;
				for(OWLLiteral hasGap_value : item_set){
					hasGap_int = Integer.parseInt(hasGap_value.getLiteral());
					System.out.println("Gap: "  + hasGap_int);
				}

				for (OWLIndividual owlIndividual2 : item) {
					System.out.println("Item: " + owlIndividual2.toStringID());
					String item_exists = owlIndividual2.toStringID();
					item_exists = item_exists.split("#")[1];
					item_exists = item_exists.split("_")[0];
					System.out.println("Item: " + item_exists);

					SeqD2Rule[] res = (new ExistRules().writeRules(new String[]{item_exists}, new int[]{hasGap_int},  ruleVec.size(), constraintsCounter));
					for (SeqD2Rule seqD2Rule : res) {
						ruleVec.add(seqD2Rule);
					}
				}

			}

			else if(constraint.equals("End")){
				
				constraintsCounter++;
				
				System.out.println("\n==> Writing a End's rule");
				OWLObjectProperty hasEnd = factory.getOWLObjectProperty(":hasEnd",ontologyHolder.getPrefixOWLOntologyFormat());
				Set<OWLIndividual> item = individualNamed.getObjectPropertyValues(hasEnd, ont);


				for (OWLIndividual owlIndividual2 : item) {
					System.out.println("Item: " + owlIndividual2.toStringID());
					String item_end = owlIndividual2.toStringID();
					item_end = item_end.split("#")[1];
					item_end = item_end.split("_")[0];
					System.out.println("Item: " + item_end);

					SeqD2Rule[] res = (new EndRules().writeRules(new String[]{item_end}, new int[]{0},  ruleVec.size(), constraintsCounter));
					for (SeqD2Rule seqD2Rule : res) {
						ruleVec.add(seqD2Rule);
					}
				}
			}

			else if(constraint.equals("Precedence")){
				
				constraintsCounter++;
				
				System.out.println("\n==> Writing a Precedence's rule");
				OWLObjectProperty hasAntecedent = factory.getOWLObjectProperty(":antecedent",ontologyHolder.getPrefixOWLOntologyFormat());
				Set<OWLIndividual> itemAntecedent_set = individualNamed.getObjectPropertyValues(hasAntecedent, ont);

				String item_antecedent = null, item_consequent = null;
				for (OWLIndividual owlIndividual2 : itemAntecedent_set) {
					//System.out.println("Antecedent Item: " + owlIndividual2.toStringID());
					item_antecedent = owlIndividual2.toStringID();
					item_antecedent = item_antecedent.split("#")[1];
					item_antecedent = item_antecedent.split("_")[0];
					System.out.println("Antecedent Item: " + item_antecedent);
				}

				OWLObjectProperty hasConsequent = factory.getOWLObjectProperty(":consequent",ontologyHolder.getPrefixOWLOntologyFormat());
				Set<OWLIndividual> itemConsequent_set = individualNamed.getObjectPropertyValues(hasConsequent, ont);

				for (OWLIndividual owlIndividual2 : itemConsequent_set) {
					//System.out.println("Consequent Item: " + owlIndividual2.toStringID());
					item_consequent = owlIndividual2.toStringID();
					item_consequent = item_consequent.split("#")[1];
					item_consequent = item_consequent.split("_")[0];
					System.out.println("Consequent Item: " + item_consequent);
				}

				SeqD2Rule[] res = (new PrecedenceRules().writeRules(new String[]{item_antecedent,item_consequent}, new int[]{0,0},  ruleVec.size(), constraintsCounter));
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
}

package d3m.span.io.processrestrictionselements;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import d3m.span.io.OntologyHolder;
import d3m.span.io.ConstraintSequence;
import d3m.span.io.relations.RelationExists;

public class Process_exists extends ProcessRestrictionElements{

	public Process_exists(OntologyHolder ontologyHolder) {
		// TODO Auto-generated constructor stub
		super.ontologyHolder = ontologyHolder;
	}

	@Override
	public void proceed(Node node, ConstraintSequence restrictionSequence) {
		System.out.println("========> Processing Exists constraint");
		NodeList nodeElements = node.getChildNodes();
		System.out.println("Item name to be inserted: " + nodeElements.item(0).getNodeValue());

		String itemValue = nodeElements.item(0).getNodeValue();

		OWLOntology ont = ontologyHolder.getOWLOntology();
		OWLDataFactory factory = ontologyHolder.getOWLDataFactory();
		OWLOntologyManager manager = ontologyHolder.getOWLOntologyManager();

		System.out.println("%%%%%%%%%%% Processando");

		/* Get some new classes. */
		OWLClass item = factory.getOWLClass(IRI.create(ont.getOntologyID()
				.getOntologyIRI().toString() + "#Exists"));

		String individualName = "Exists_" + restrictionSequence.getSequenceName();

		// Add individual
		OWLIndividual relationIndividual = factory.getOWLNamedIndividual(":"+individualName, ontologyHolder.getPrefixOWLOntologyFormat());

		//Create an individual of Root
		OWLClassAssertionAxiom classAssertionAx = factory.getOWLClassAssertionAxiom(
				item, relationIndividual);

		manager.addAxiom(ont, classAssertionAx);

		OWLIndividual itemIndividual = factory.getOWLNamedIndividual(":"+itemValue, ontologyHolder.getPrefixOWLOntologyFormat());

		OWLClass itemClass = factory.getOWLClass(IRI.create(ont.getOntologyID()
				.getOntologyIRI().toString() + "#Item"));

		//Create an individual of Item
		OWLClassAssertionAxiom classAssertionAx2 = factory.getOWLClassAssertionAxiom(itemClass,
				itemIndividual);

		manager.addAxiom(ont, classAssertionAx2);

		//OWLObjectProperty hasRelation = factory.getOWLObjectProperty(":hasRelation", ontologyHolder.getPrefixOWLOntologyFormat());

		OWLObjectProperty hasItem = factory.getOWLObjectProperty(":hasItem",ontologyHolder.getPrefixOWLOntologyFormat());


		//OWLObjectPropertyAssertionAxiom axiom1 = factory
		//		.getOWLObjectPropertyAssertionAxiom(hasRelation, restrictionIndividual, relationIndividual);

		OWLObjectPropertyAssertionAxiom axiom2 = factory
				.getOWLObjectPropertyAssertionAxiom(hasItem, relationIndividual, itemIndividual);

		//AddAxiom addAxiom1 = new AddAxiom(ont, axiom1);
		AddAxiom addAxiom2 = new AddAxiom(ont,axiom2);
		// Now we apply the change using the manager.
		//manager.applyChange(addAxiom1);
		manager.applyChange(addAxiom2);

		//ontologyHolder.printProperties(item);

		//ontologyHolder.listIndividualsFromClass(itemset);
		//ontologyHolder.listIndividualsFromClass(item);

		//ontologyHolder.processReasoner();
		restrictionSequence.addConstraint(new RelationExists(individualName,itemValue));

		try {
			manager.saveOntology(ont);
			restrictionSequence.insertItem(itemValue);
			restrictionSequence.insertRoot();
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}

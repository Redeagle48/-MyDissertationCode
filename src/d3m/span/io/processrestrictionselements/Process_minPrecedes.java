package d3m.span.io.processrestrictionselements;

import d3m.span.io.OntologyHolder;
import d3m.span.io.RestrictionSequence;

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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Process_minPrecedes extends ProcessRestrictionElements{

	public Process_minPrecedes(OntologyHolder ontologyHolder) {
		// TODO Auto-generated constructor stub
		super.ontologyHolder = ontologyHolder;
	}

	@Override
	public void proceed(Node node, RestrictionSequence restrictionSequence) {

		System.out.println("========> Processing Precedence restriction");
		NodeList nodeElements = node.getChildNodes();

		String precedesValue, procedesValue;

		Element eElement = (Element)node;

		Element precedesElement = (Element) eElement.getElementsByTagName("antecedent").item(0);
		Element procedesElement = (Element) eElement.getElementsByTagName("consequent").item(0);

		System.out.println("Antecedent: " + precedesElement.getTextContent());
		System.out.println("Consequent: " + procedesElement.getTextContent());

		precedesValue = precedesElement.getTextContent();
		procedesValue = procedesElement.getTextContent();

		OWLOntology ont = ontologyHolder.getOWLOntology();
		OWLDataFactory factory = ontologyHolder.getOWLDataFactory();
		OWLOntologyManager manager = ontologyHolder.getOWLOntologyManager();

		String restrictionName = restrictionSequence.getSequenceName();

		//Sequence from which this items belong
		OWLIndividual RestrictionIndividual = factory.getOWLNamedIndividual(":"+restrictionSequence.getSequenceName(),
				ontologyHolder.getPrefixOWLOntologyFormat());

		//OWLIndividual RelationIndividual = factory.getOWLNamedIndividual(":",ontologyHolder.getPrefixOWLOntologyFormat());

		OWLObjectProperty hasRelation = factory.getOWLObjectProperty(":hasRelation",ontologyHolder.getPrefixOWLOntologyFormat());

		OWLIndividual minPrecedesIndividual = factory.getOWLNamedIndividual(":Precedence_"+restrictionName, ontologyHolder.getPrefixOWLOntologyFormat());

		OWLClass minPrecedesClass = factory.getOWLClass(":Precedence", ontologyHolder.getPrefixOWLOntologyFormat());

		OWLClassAssertionAxiom classAssertionAx = factory.getOWLClassAssertionAxiom(
				minPrecedesClass, minPrecedesIndividual);

		OWLObjectPropertyAssertionAxiom addaxiom2 = factory
				.getOWLObjectPropertyAssertionAxiom(hasRelation, RestrictionIndividual, minPrecedesIndividual);
		
		manager.addAxiom(ont, classAssertionAx);
		manager.addAxiom(ont,addaxiom2);
		
		OWLIndividual precedesIndividual = factory.getOWLNamedIndividual(":"+precedesValue, ontologyHolder.getPrefixOWLOntologyFormat());

		OWLClass itemClass = factory.getOWLClass(IRI.create(ont.getOntologyID()
				.getOntologyIRI().toString() + "#Item"));
		
		//Create an individual of Item
		OWLClassAssertionAxiom classAssertionAx2 = factory.getOWLClassAssertionAxiom(itemClass,
				precedesIndividual);
		
		manager.addAxiom(ont, classAssertionAx2);
		
		OWLIndividual procedesIndividual = factory.getOWLNamedIndividual(":"+procedesValue, ontologyHolder.getPrefixOWLOntologyFormat());

		//Create an individual of Item
		OWLClassAssertionAxiom classAssertionAx3 = factory.getOWLClassAssertionAxiom(itemClass,
				procedesIndividual);
		
		manager.addAxiom(ont, classAssertionAx3);
		
		OWLObjectProperty precedence = factory.getOWLObjectProperty(":A",ontologyHolder.getPrefixOWLOntologyFormat());

		OWLObjectProperty procedence = factory.getOWLObjectProperty(":Procedence",ontologyHolder.getPrefixOWLOntologyFormat());

		OWLObjectPropertyAssertionAxiom addaxiom3 = factory
				.getOWLObjectPropertyAssertionAxiom(precedence, minPrecedesIndividual, precedesIndividual);
		
		OWLObjectPropertyAssertionAxiom addaxiom4 = factory
				.getOWLObjectPropertyAssertionAxiom(procedence, minPrecedesIndividual, procedesIndividual);
		
		manager.addAxiom(ont,addaxiom3);
		manager.addAxiom(ont,addaxiom4);
		
		//ontologyHolder.processReasoner();

		try {
			manager.saveOntology(ont);
			//restrictionSequence.insertItem(itemValue);
			//restrictionSequence.insertRoot();
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

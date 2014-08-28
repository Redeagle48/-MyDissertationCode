package d3m.span.io.processrestrictionselements;

import d3m.span.io.OntologyHolder;
import d3m.span.io.ConstraintSequence;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import d3m.span.io.relations.RelationPrecedence;

public class Process_precedence extends ProcessRestrictionElements{

	public Process_precedence(OntologyHolder ontologyHolder) {
		// TODO Auto-generated constructor stub
		super.ontologyHolder = ontologyHolder;
	}

	@Override
	public void proceed(Node node, ConstraintSequence restrictionSequence) {

		System.out.println("========> Processing Precedence restriction");

		//antecedent and consequent items
		String antecedentValue, consequentValue;
		String gap;

		Element eElement = (Element)node;

		gap = eElement.getElementsByTagName("precedence").item(0).getAttributes().item(0).getNodeValue();

		Element precedesElement = (Element) eElement.getElementsByTagName("antecedent").item(0);
		Element procedesElement = (Element) eElement.getElementsByTagName("consequent").item(0);

		System.out.println("Antecedent: " + precedesElement.getTextContent());
		System.out.println("Consequent: " + procedesElement.getTextContent());
		System.out.println("Gap: " + gap);

		antecedentValue = precedesElement.getTextContent();
		consequentValue = procedesElement.getTextContent();

		OWLOntology ont = ontologyHolder.getOWLOntology();
		OWLDataFactory factory = ontologyHolder.getOWLDataFactory();
		OWLOntologyManager manager = ontologyHolder.getOWLOntologyManager();

		String restrictionName = restrictionSequence.getSequenceName();

		//Sequence from which this items belong
		//OWLIndividual RestrictionIndividual = factory.getOWLNamedIndividual(":"+restrictionSequence.getSequenceName(),
		//	ontologyHolder.getPrefixOWLOntologyFormat());

		//OWLIndividual RelationIndividual = factory.getOWLNamedIndividual(":",ontologyHolder.getPrefixOWLOntologyFormat());

		//OWLObjectProperty hasRelation = factory.getOWLObjectProperty(":hasRelation",ontologyHolder.getPrefixOWLOntologyFormat());

		OWLIndividual precedenceIndividual = factory.getOWLNamedIndividual(":Precedence_"+restrictionName, ontologyHolder.getPrefixOWLOntologyFormat());

		OWLClass precedenceClass = factory.getOWLClass(":Precedence", ontologyHolder.getPrefixOWLOntologyFormat());

		OWLClassAssertionAxiom classAssertionAx = factory.getOWLClassAssertionAxiom(
				precedenceClass, precedenceIndividual);

		//OWLObjectPropertyAssertionAxiom addaxiom2 = factory
		//	.getOWLObjectPropertyAssertionAxiom(hasRelation, RestrictionIndividual, minPrecedesIndividual);

		manager.addAxiom(ont, classAssertionAx);

		//******Add Gap
		OWLDataProperty hasGap = factory.getOWLDataProperty(":hasGap",ontologyHolder.getPrefixOWLOntologyFormat());
		OWLDatatype integerDatatype = factory
                .getOWLDatatype(OWL2Datatype.XSD_INT.getIRI());
		OWLDataPropertyAssertionAxiom addaxiom_precedenceGap = factory
				.getOWLDataPropertyAssertionAxiom(hasGap, precedenceIndividual, factory.getOWLLiteral(gap, integerDatatype));
		
		manager.addAxiom(ont, addaxiom_precedenceGap);
		//******

		OWLIndividual precedesIndividual = factory.getOWLNamedIndividual(":"+antecedentValue, ontologyHolder.getPrefixOWLOntologyFormat());

		OWLClass itemClass = factory.getOWLClass(IRI.create(ont.getOntologyID()
				.getOntologyIRI().toString() + "#Item"));

		//Create an individual of Item
		OWLClassAssertionAxiom classAssertionAx2 = factory.getOWLClassAssertionAxiom(itemClass,
				precedesIndividual);

		manager.addAxiom(ont, classAssertionAx2);

		OWLIndividual procedesIndividual = factory.getOWLNamedIndividual(":"+consequentValue, ontologyHolder.getPrefixOWLOntologyFormat());

		//Create an individual of Item
		OWLClassAssertionAxiom classAssertionAx3 = factory.getOWLClassAssertionAxiom(itemClass,
				procedesIndividual);

		manager.addAxiom(ont, classAssertionAx3);

		OWLObjectProperty antecedent = factory.getOWLObjectProperty(":antecedent",ontologyHolder.getPrefixOWLOntologyFormat());

		OWLObjectProperty consequent = factory.getOWLObjectProperty(":consequent",ontologyHolder.getPrefixOWLOntologyFormat());

		OWLObjectPropertyAssertionAxiom addaxiom3 = factory
				.getOWLObjectPropertyAssertionAxiom(antecedent, precedenceIndividual, precedesIndividual);

		OWLObjectPropertyAssertionAxiom addaxiom4 = factory
				.getOWLObjectPropertyAssertionAxiom(consequent, precedenceIndividual, procedesIndividual);

		manager.addAxiom(ont,addaxiom3);
		manager.addAxiom(ont,addaxiom4);

		//ontologyHolder.processReasoner();

		//COLOCAR A RESTRICAO NA CLASSE
		restrictionSequence.addConstraint(new RelationPrecedence("Precedence_"+restrictionName, antecedentValue, consequentValue));

		try {
			manager.saveOntology(ont);
			//restrictionSequence.insertItem(itemValue);
			//restrictionSequence.insertRoot();
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}

	}

}

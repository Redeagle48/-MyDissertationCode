package d3m.span.io;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
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
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import d3m.span.io.processrestrictionselements.Process_begin;
import d3m.span.io.processrestrictionselements.Process_end;
import d3m.span.io.processrestrictionselements.Process_exists;
import d3m.span.io.processrestrictionselements.Process_precedence;
import d3m.span.io.relations.Relation;

/**
 * Class that reads the XML with the constraint
 * and create the instances in the ontology
 * @author antoniopereira
 *
 */
public class ProcessXML {
	boolean debug_parserXML = false;
	OWLOntologyManager manager;
	OWLOntology ont;

	// Count the number of each type of constraint
	int constraint_id = 0;

	public ProcessXML() {}

	public static void main(String [] args) throws Exception
	{
		//ProcessXMLv2 processxmlv2 = new ProcessXMLv2();
		//System.out.println("isValid?: " + processxml.isValidXML());
		//readXML();
		//processxml.owlApiExplorer();
	}

	public void execute(OntologyHolder ontologyHolder) {
		//System.out.println("Analyzing restriction's XML..........isValid?: " + isValidXML());
		try {
			readTaxonomyXML(ontologyHolder);
			readConstraintsXML(ontologyHolder);

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	public void readTaxonomyXML(OntologyHolder ontologyHolder) throws ParserConfigurationException, SAXException, IOException{
		OWLOntology ont = ontologyHolder.getOWLOntology();
		OWLDataFactory factoryOnt = ontologyHolder.getOWLDataFactory();
		OWLOntologyManager manager = ontologyHolder.getOWLOntologyManager();

		File xmlFile = new File(FilesLocation.TAXONOMY_XML);

		HashMap<String,Boolean> elements_state = new HashMap<String,Boolean>();

		//Get the DOM Builder Factory
		DocumentBuilderFactory factory = 
				DocumentBuilderFactory.newInstance();

		//Get the DOM Builder
		DocumentBuilder dbuilder = factory.newDocumentBuilder();

		//Load and Parse the XML document
		//document contains the complete XML as a Tree.
		Document document = dbuilder.parse(xmlFile);

		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		document.getDocumentElement().normalize();

		NodeList element_List  = document.getElementsByTagName("element");
		for(int i = 0; i < element_List.getLength(); i++){
			Node element_node = element_List.item(i);
			String element_name = element_node.getTextContent();
			OWLClass element_class = factoryOnt.getOWLClass(IRI.create(ont.getOntologyID()
					.getOntologyIRI().toString() + "#Element"));
			OWLIndividual element_individual = factoryOnt.getOWLNamedIndividual(":"+element_name,
					ontologyHolder.getPrefixOWLOntologyFormat());
			OWLClassAssertionAxiom classAssertion_ComposedElement = factoryOnt.getOWLClassAssertionAxiom(
					element_class, element_individual);
			manager.addAxiom(ont, classAssertion_ComposedElement);

			elements_state.put(":"+element_name, true);
		}

		NodeList composedElement_List  = document.getElementsByTagName("composedElement");

		OWLClass composedElement_class = factoryOnt.getOWLClass(IRI.create(ont.getOntologyID()
				.getOntologyIRI().toString() + "#ComposedElement"));
		OWLIndividual composedElement_individual_topparent = factoryOnt.getOWLNamedIndividual(":TopElement",
				ontologyHolder.getPrefixOWLOntologyFormat());
		OWLClassAssertionAxiom classAssertion_ComposedElement = factoryOnt.getOWLClassAssertionAxiom(
				composedElement_class, composedElement_individual_topparent);
		manager.addAxiom(ont, classAssertion_ComposedElement);

		OWLObjectProperty containsElement = factoryOnt.getOWLObjectProperty(":containsElement",ontologyHolder.getPrefixOWLOntologyFormat());
		OWLObjectProperty isContainedByElement = factoryOnt.getOWLObjectProperty(":isContainedBy",ontologyHolder.getPrefixOWLOntologyFormat());

		for(int i = 0; i < composedElement_List.getLength(); i++){
			//System.out.println("ComposedElement nr."+(i+1));
			Node node = composedElement_List.item(i);

			NamedNodeMap attr = node.getAttributes();
			Node id = attr.getNamedItem("id");
			//System.out.println("--> ID: " + id.getNodeValue());

			OWLIndividual composedElement_individual_parent = factoryOnt.getOWLNamedIndividual(":"+id.getNodeValue(),
					ontologyHolder.getPrefixOWLOntologyFormat());
			classAssertion_ComposedElement = factoryOnt.getOWLClassAssertionAxiom(
					composedElement_class, composedElement_individual_parent);
			manager.addAxiom(ont, classAssertion_ComposedElement);

			elements_state.put(":"+id.getNodeValue(), true);

			NodeList childList = node.getChildNodes();
			for (int j = 0; j < childList.getLength(); j++) {
				Node childNode = childList.item(j);

				if(childNode.getNodeName().equals("value")){

					String childValue = childNode.getTextContent();
					//System.out.println("---> Item: " + childValue);

					OWLClass element_class = factoryOnt.getOWLClass(IRI.create(ont.getOntologyID()
							.getOntologyIRI().toString() + "#Element"));
					OWLIndividual element_individual = factoryOnt.getOWLNamedIndividual(":"+childValue,
							ontologyHolder.getPrefixOWLOntologyFormat());
					OWLClassAssertionAxiom classAssertion_Element = factoryOnt.getOWLClassAssertionAxiom(
							element_class, element_individual);
					manager.addAxiom(ont, classAssertion_Element);

					OWLAxiom addaxiom3 = factoryOnt
							.getOWLObjectPropertyAssertionAxiom(containsElement, composedElement_individual_parent, element_individual);
					manager.addAxiom(ont, addaxiom3);
					
					OWLAxiom addaxiom4 = factoryOnt
							.getOWLObjectPropertyAssertionAxiom(isContainedByElement, element_individual, composedElement_individual_parent);
					manager.addAxiom(ont, addaxiom4);

					elements_state.put(":"+childValue, false);


				} else if(childNode.getNodeName().equals("composedValue")){

					String childValue = childNode.getTextContent();
					//System.out.println("---> Composed Value: " + childValue);

					OWLIndividual composedValue_individual = factoryOnt.getOWLNamedIndividual(":"+childValue,
							ontologyHolder.getPrefixOWLOntologyFormat());

					OWLAxiom addaxiom3 = factoryOnt
							.getOWLObjectPropertyAssertionAxiom(containsElement, composedElement_individual_parent, composedValue_individual);
					manager.addAxiom(ont, addaxiom3);
					
					OWLAxiom addaxiom4 = factoryOnt
							.getOWLObjectPropertyAssertionAxiom(isContainedByElement, composedValue_individual, composedElement_individual_parent);
					manager.addAxiom(ont, addaxiom4);

					elements_state.put(":"+childValue, false);

				}
			}
		}

		Set<Entry<String, Boolean>> set = elements_state.entrySet();
		containsElement = factoryOnt.getOWLObjectProperty(":containsElement",ontologyHolder.getPrefixOWLOntologyFormat());
		for (Entry<String, Boolean> entry : set) {
			if(entry.getValue()) {
				OWLIndividual composedElement_individual = factoryOnt.getOWLNamedIndividual(entry.getKey(),
						ontologyHolder.getPrefixOWLOntologyFormat());
				OWLAxiom addaxiom3 = factoryOnt
						.getOWLObjectPropertyAssertionAxiom(containsElement, composedElement_individual_topparent, composedElement_individual);
				manager.addAxiom(ont, addaxiom3);
				
				OWLAxiom addaxiom4 = factoryOnt
						.getOWLObjectPropertyAssertionAxiom(isContainedByElement, composedElement_individual, composedElement_individual_topparent);
				manager.addAxiom(ont, addaxiom4);
				
			}
		}

		try {
			manager.saveOntology(ont);
			//restrictionSequence.insertItem(itemValue);
			//restrictionSequence.insertRoot();
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}

	public void readConstraintsXML(OntologyHolder ontologyHolder) throws ParserConfigurationException, SAXException, IOException {

		OWLOntology ont = ontologyHolder.getOWLOntology();
		OWLDataFactory factoryOnt = ontologyHolder.getOWLDataFactory();
		OWLOntologyManager manager = ontologyHolder.getOWLOntologyManager();

		File xmlFile = new File(FilesLocation.RESTRICTIONS_XML);

		//Get the DOM Builder Factory
		DocumentBuilderFactory factory = 
				DocumentBuilderFactory.newInstance();

		//Get the DOM Builder
		DocumentBuilder dbuilder = factory.newDocumentBuilder();

		//Load and Parse the XML document
		//document contains the complete XML as a Tree.
		Document document = dbuilder.parse(xmlFile);

		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		document.getDocumentElement().normalize();

		if(false)
			System.out.println("Restrictions' XML -> Root element: " + document.getDocumentElement().getNodeName());

		//Iterating through the nodes and extracting the data.
		NodeList nodeList = document.getDocumentElement().getChildNodes();

		//System.out.println("Restrictions' XML -> Length: " + nodeList.getLength());

		for(int i = 0; i < nodeList.getLength(); i++){

			Node node = nodeList.item(i);

			//String s = nodeList.item(i).getNodeName();

			if (node instanceof Element && nodeList.item(1).getNodeName().equals("constraintSet")) {

				//System.out.println("======================= Analyzing a constraint =======================");

				Element restrictionElement = (Element) node;
				//System.out.println("Constraint's name: " + restrictionElement.getElementsByTagName("title").item(0).getTextContent());
				//System.out.println("Note: " + restrictionElement.getElementsByTagName("note").item(0).getTextContent());

				//Object that represents the present restriction
				ConstraintSequence restrictionSequence = new ConstraintSequence(restrictionElement.getElementsByTagName("title").item(0).getTextContent());

				// Add the restriction to the resrictionManager
				GlobalVariables.restrictonManager.addRestriction(restrictionSequence);

				// Instantiate ConstraintDefinition in the ontology
				OWLClass restriction = factoryOnt.getOWLClass(IRI.create(ont.getOntologyID()
						.getOntologyIRI().toString() + "#ConstraintDefinition"));
				OWLIndividual restrictionIndividual = factoryOnt.getOWLNamedIndividual(":"+restrictionSequence.getSequenceName(),
						ontologyHolder.getPrefixOWLOntologyFormat());
				OWLClassAssertionAxiom classAssertionBx = factoryOnt.getOWLClassAssertionAxiom(
						restriction, restrictionIndividual);
				manager.addAxiom(ont, classAssertionBx);

				NodeList restrictions = node.getChildNodes();

				//handle restriction node
				for(int j = 0; j < restrictions.getLength(); j++) {
					Node restrictionElements = restrictions.item(j);
					//System.out.println("Name Element: " + restrictionElements.getNodeName());

					if (node instanceof Element && restrictionElements.getNodeName().equals("constraint")) {

						//System.out.println("======================= Constraint =======================");

						NodeList restrictions2 = restrictionElements.getChildNodes();
						Node restriction2 = restrictions2.item(1);

						//System.out.println("Node: " + restriction2.getNodeName());

						// TODO TO REFACTOR

						// Unary constraints
						if(restriction2.getNodeName().equals("begin")) {
							new Process_begin(ontologyHolder,++constraint_id).proceed(restriction2,restrictionSequence);
						}
						else if(restriction2.getNodeName().equals("exists")){
							new Process_exists(ontologyHolder,++constraint_id).proceed(restriction2, restrictionSequence);
						}
						else if(restriction2.getNodeName().equals("end")) {
							new Process_end(ontologyHolder,++constraint_id).proceed(restriction2,restrictionSequence);
						}
						// Binary constraints
						else if(restriction2.getNodeName().equals("precedence")) {
							new Process_precedence(ontologyHolder,++constraint_id).proceed(restrictionElements,restrictionSequence);
						}
						// Complex constraints
						else if(restriction2.getNodeName().equals("palindrome")){

						}
						else if(restriction2.getNodeName().equals("cardinalExistence")){

						}


					}

					else if (restrictionElements instanceof Element && restrictionElements.getNodeName().equals("constraintProperties")) {

						//System.out.println("======================= Restriction's Property =======================");

						//Insert the properties of the relations

						NodeList relationProperties = restrictionElements.getChildNodes();

						for(int k = 0; k < relationProperties.getLength(); k++) {

							Node relationProperty = relationProperties.item(k);

							if(node instanceof Element && relationProperty.getNodeName().equals("constraintSequence")) {

								String type = relationProperty.getAttributes().getNamedItem("type").getNodeValue();

								if(type.equals("start")){

									//System.out.println("\nSTART");

									// Instantiate ConstraintComposition in the ontology
									OWLClass restrictionComposition = factoryOnt.getOWLClass(IRI.create(ont.getOntologyID()
											.getOntologyIRI().toString() + "#ConstraintComposition"));
									OWLIndividual restrictionCompositionIndividual = factoryOnt.getOWLNamedIndividual(":ConstraintComposition_"+restrictionSequence.getSequenceName(),
											ontologyHolder.getPrefixOWLOntologyFormat());
									OWLClassAssertionAxiom classAssertionCx = factoryOnt.getOWLClassAssertionAxiom(
											restrictionComposition, restrictionCompositionIndividual);
									manager.addAxiom(ont, classAssertionCx);

									// Connect ConstraintDefinition instance to the ConstraintComposition
									OWLObjectProperty hasConstraint = factoryOnt.getOWLObjectProperty(":hasConstraint",ontologyHolder.getPrefixOWLOntologyFormat());
									OWLObjectPropertyAssertionAxiom addaxiom3 = factoryOnt
											.getOWLObjectPropertyAssertionAxiom(hasConstraint, restrictionIndividual, restrictionCompositionIndividual);
									manager.addAxiom(ont, addaxiom3);

									String initial_gap = relationProperty.getAttributes().getNamedItem("gap").getNodeValue();
									// NAO ESTA A APANHAR O GAP
									OWLDataProperty hasGap = factoryOnt.getOWLDataProperty(":hasGap",ontologyHolder.getPrefixOWLOntologyFormat());
									OWLDatatype integerDatatype = factoryOnt
											.getOWLDatatype(OWL2Datatype.XSD_INT.getIRI());
									OWLDataPropertyAssertionAxiom addaxiom_precedenceGap = factoryOnt
											.getOWLDataPropertyAssertionAxiom(hasGap, restrictionCompositionIndividual, factoryOnt.getOWLLiteral(initial_gap, integerDatatype));
									manager.addAxiom(ont, addaxiom_precedenceGap);

								} else {

									//System.out.println("========> Encontrei esta relation: " + relationProperty.getNodeName());
									//System.out.println("Type: " + relationProperty.getAttributes().getNamedItem("type").getNodeValue());
									//System.out.println("Relations involved: " +
									//		relationProperty.getAttributes().getNamedItem("rel1").getNodeValue() +
									//		" -> " + relationProperty.getAttributes().getNamedItem("rel2").getNodeValue());
									//System.out.println("Gap: " + relationProperty.getAttributes().getNamedItem("gap").getNodeValue());

									int rel1ID = Integer.parseInt(relationProperty.getAttributes().getNamedItem("rel1").getNodeValue());
									int rel2ID = Integer.parseInt(relationProperty.getAttributes().getNamedItem("rel2").getNodeValue());
									String gap = relationProperty.getAttributes().getNamedItem("gap").getNodeValue();

									//IR buscar o nome das relations envolvidas
									String rel1 = restrictionSequence.getConstraints().get(rel1ID-1).getInstanceName();
									String rel2 = restrictionSequence.getConstraints().get(rel2ID-1).getInstanceName();

									//System.out.println("\n----------------CONSTRAINTS PROPERTIES-----------------");
									//System.out.println("Relation to insert: " + rel1);
									//System.out.println("Relation to insert: " + rel2);

									OWLIndividual Relation1Individual = factoryOnt.getOWLNamedIndividual(":"+rel1,
											ontologyHolder.getPrefixOWLOntologyFormat());

									OWLIndividual Relation2Individual = factoryOnt.getOWLNamedIndividual(":"+rel2,
											ontologyHolder.getPrefixOWLOntologyFormat());

									/* Get some new classes. */
									OWLClass relationSequenceClass = null;
									if(type.equals("sequential")){

										relationSequenceClass = factoryOnt.getOWLClass(IRI.create(ont.getOntologyID()
												.getOntologyIRI().toString() + "#SequentialConstraint"));

										OWLIndividual relationIndividual = factoryOnt.getOWLNamedIndividual(":" +
												"SequentialProperty" + restrictionSequence.getcountSequentialProperties() + "_" + restrictionSequence.getSequenceName(),ontologyHolder.getPrefixOWLOntologyFormat());

										OWLClassAssertionAxiom classAssertionAx = factoryOnt.getOWLClassAssertionAxiom(
												relationSequenceClass, relationIndividual);

										manager.addAxiom(ont, classAssertionAx);

										OWLObjectProperty nextRelationSequence = factoryOnt.getOWLObjectProperty(":nextConstraintSequence",ontologyHolder.getPrefixOWLOntologyFormat());

										OWLObjectPropertyAssertionAxiom axiom2 = factoryOnt
												.getOWLObjectPropertyAssertionAxiom(nextRelationSequence, Relation1Individual, relationIndividual);

										manager.addAxiom(ont, axiom2);

										OWLObjectProperty nextRelation = factoryOnt.getOWLObjectProperty(":nextConstraint",ontologyHolder.getPrefixOWLOntologyFormat());

										OWLObjectPropertyAssertionAxiom axiom3 = factoryOnt
												.getOWLObjectPropertyAssertionAxiom(nextRelation, relationIndividual, Relation2Individual);

										manager.addAxiom(ont, axiom3);

										//******Add Gap
										OWLDataProperty hasGap = factoryOnt.getOWLDataProperty(":hasGap",ontologyHolder.getPrefixOWLOntologyFormat());
										OWLDatatype integerDatatype = factoryOnt
												.getOWLDatatype(OWL2Datatype.XSD_INT.getIRI());
										OWLDataPropertyAssertionAxiom addaxiom_precedenceGap = factoryOnt
												.getOWLDataPropertyAssertionAxiom(hasGap, relationIndividual, factoryOnt.getOWLLiteral(gap, integerDatatype));
										manager.addAxiom(ont, addaxiom_precedenceGap);
										//******

									} else if(type.equals("concurrential")){

										relationSequenceClass = factoryOnt.getOWLClass(IRI.create(ont.getOntologyID()
												.getOntologyIRI().toString() + "#ConcurrentialConstraint"));

										OWLIndividual relationIndividual = factoryOnt.getOWLNamedIndividual(":" +
												"ConcurrentialProperty" + restrictionSequence.getcountConcurrentialProperties() + "_" + restrictionSequence.getSequenceName(),ontologyHolder.getPrefixOWLOntologyFormat());

										OWLClassAssertionAxiom classAssertionAx = factoryOnt.getOWLClassAssertionAxiom(
												relationSequenceClass, relationIndividual);

										manager.addAxiom(ont, classAssertionAx);

										OWLObjectProperty nextRelationSequence = factoryOnt.getOWLObjectProperty(":nextConstraintSequence",ontologyHolder.getPrefixOWLOntologyFormat());

										OWLObjectPropertyAssertionAxiom axiom2 = factoryOnt
												.getOWLObjectPropertyAssertionAxiom(nextRelationSequence, Relation1Individual, relationIndividual);

										manager.addAxiom(ont, axiom2);

										OWLObjectProperty nextRelation = factoryOnt.getOWLObjectProperty(":nextConstraint",ontologyHolder.getPrefixOWLOntologyFormat());

										OWLObjectPropertyAssertionAxiom axiom3 = factoryOnt
												.getOWLObjectPropertyAssertionAxiom(nextRelation, relationIndividual, Relation2Individual);

										manager.addAxiom(ont, axiom3);

									}  
								}
							}
						}
					}

				}

				//Insert the relation Properties
				//System.out.println("========================================================");
				//System.out.println("Relations in this constraint:");
				//for (Relation relation : restrictionSequence.getConstraints()) {
				//	System.out.println(relation.getRelationName());
				//}


				// Get first constraint
				Relation relation = restrictionSequence.getConstraints().get(0);

				//Sequence from which this items belong
				OWLIndividual contraintComposition_Individual = factoryOnt.getOWLNamedIndividual(":ConstraintComposition_"+restrictionSequence.getSequenceName(),
						ontologyHolder.getPrefixOWLOntologyFormat());

				OWLIndividual ConstraintIndividual = factoryOnt.getOWLNamedIndividual(":"+relation.getInstanceName(),ontologyHolder.getPrefixOWLOntologyFormat());

				OWLObjectProperty hasRelation = factoryOnt.getOWLObjectProperty(":nextConstraint",ontologyHolder.getPrefixOWLOntologyFormat());

				OWLObjectPropertyAssertionAxiom addaxiom2 = factoryOnt
						.getOWLObjectPropertyAssertionAxiom(hasRelation, contraintComposition_Individual, ConstraintIndividual);

				manager.addAxiom(ont, addaxiom2);

				try {
					manager.saveOntology(ont);
					//restrictionSequence.insertItem(itemValue);
					//restrictionSequence.insertRoot();
				} catch (OWLOntologyStorageException e) {
					e.printStackTrace();
				}

				ontologyHolder.processReasoner(); // to ensure the consistency of the intantiate ontology
			}
		}
	}
}

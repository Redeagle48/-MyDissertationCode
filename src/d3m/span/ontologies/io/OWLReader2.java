package d3m.span.ontologies.io;

import dm.classification.ds.Item;
import d3m.span.ontologies.*;
//import d3m.span.ontologies.SameIndividualAxiom;
import d3m.span.ontologies.kb.Feature;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;

import java.io.File;
import java.util.*;



public class OWLReader2 implements OntologyReader {
	private String filename;
	private String simpleFilename;
	private final OWLOntologyManager manager;
	private final OWLOntology ontology;
	private final ConceptProcessor conceptProcessor;
	//private final AttributeProcessor attributeProcessor;
	//private final AxiomProcessor axiomProcessor;

	/**
	 * Creates a reader for the file received.
	 * 
	 * The reader is composed by a concept processor and an attribute
	 * processor. These will get all the information needed to fill our
	 * representation of the ontology.
	 * 
	 * @see ConceptProcessor
	 * @see AttributeProcessor
	 * @see AxiomProcessor
	 * @param filename the name of the OWL file with the ontology
	 * @throws OWLOntologyCreationException
	 */
	public OWLReader2(String filename) throws OWLOntologyCreationException {
		this.filename = filename;
		manager = OWLManager.createOWLOntologyManager();
		ontology = manager.loadOntologyFromOntologyDocument(new File(filename));
		System.out.println("ONTOLOGY --> OWL file read.");
		conceptProcessor = new ConceptProcessor(ontology.getClassesInSignature());
		//attributeProcessor = new AttributeProcessor();
		//axiomProcessor = new AxiomProcessor();
	}
	
	/**
	 * Updates the ontology from an OWL file
	 * @param ontology the ontology to update
	 * @throws Exception 
	 */
	@Override
	public void updateOntology(Ontology ontology) throws Exception {
		//DEBUG_visitOjectProperties(this.ontology);
		System.out.println("ONTOLOGY --> it is going to create elements and infer taxonomy...");
		Concept[] concepts = conceptProcessor.getConcepts();
		System.out.println("ONTOLOGY --> it is going to fill attributes and assign them to concepts...");
		//Attribute[] attributes = attributeProcessor.getAttributesFor(concepts);
		System.out.println("ONTOLOGY --> it is going to fill defaults and assign them to attributes...");
		//Feature[] features = conceptProcessor.getFeaturesFor(attributes);
		System.out.println("ONTOLOGY --> it is going to get inferred taxonomy...");
		//RelationIsA[] taxonomy = conceptProcessor.getTaxonomy();
		System.out.println("ONTOLOGY --> it is going to get inferred relations...");
		//Relation[] relations = attributeProcessor.getRelations();
		System.out.println("ONTOLOGY --> it is going to get axioms...");
		//Set<Axiom> axioms = axiomProcessor.getSameIndividualAxioms();
		
		HashMap<Integer, Concept> concepts_ = new HashMap<Integer, Concept>();
		for(Concept c : concepts) {
			concepts_.put(c.getID(), c);
		}
		
		/*
		HashMap<Integer, Attribute> attributes_ = new HashMap<Integer, Attribute>();
		for(Attribute a : attributes) {
			attributes_.put(a.getID(), a);
		}
		*/
		ontology.setConcepts(concepts_);
		//ontology.setAttributes(attributes_);
		//ontology.setDefaultValues(features);
		//ontology.setTaxonomy(taxonomy);
		//ontology.setRelations(relations);
		//ontology.setAxioms(axioms);
	}

	/**
	 * Internal class responsible for processing the concepts on the ontology.
	 * 
	 * This class contains methods to process the concepts, their features and
	 * the taxonomy of the ontology.
	 * 
	 * @author Joao Vieira
	 * @version 0.3
	 */
	private class ConceptProcessor {
		private int id = 1, relIsAId = 0;
		private final Map<String, Integer> map = new Hashtable<String, Integer>();
		private final Concept THING = new Concept();
		private final Set<OWLClass> classes;
		private final Collection<String[]> features;
		private final Collection<RelationIsA> relationsIsA;
		private Concept[] concepts;
		
		/**
		 * Creates a concept processor for the set of OWL classes received.
		 * 
		 * When allocating space checks if the concept Thing is explicitly 
		 * defined. If it isn't add an extra position in the array for it.
		 * By the W3 spec, all OWL ontologies have a root concept named Thing 
		 * even if it is not explicitly defined.
		 * 
		 * @param classes a set of OWL classes to process into concepts.
		 */
		private ConceptProcessor(Set<OWLClass> classes) {
			boolean hasThing = false;
			this.classes = classes;
			this.features = new LinkedList<String[]>();
			this.relationsIsA = new LinkedList<RelationIsA>();
			
			for (OWLClass cl : classes) {
				if (cl.isTopEntity()) {
					this.concepts = new Concept[classes.size()];
					hasThing = true;
					break;
				}
			}
			if (!hasThing) this.concepts = new Concept[classes.size() + 1];
			this.concepts[0] = THING;
			this.map.put("http://www.w3.org/2002/07/owl#Thing", 0);
		}
		
		/**
		 * Gets a list of concepts from the owl classes of the processor
		 * 
		 * While constructing the list of concepts it will also pre-process
		 * information about their features and the taxonomy of the ontology.
		 * 
		 * @return an array of concepts defined in the ontology
		 */
		private Concept[] getConcepts() {
			for (OWLClass cl : classes) {
				if (!cl.isTopEntity()) {
					Concept concept;
					String clName = getObjectName(cl);
					//preProccessClassFeatures(cl);

					if (map.containsKey(clName)) {
						concept = concepts[map.get(clName)];
					} else {
						concept = new Concept(clName, id, THING);
						concepts[id] = concept;
						map.put(clName, id);
						++id;
					}

					//preProccessTaxonomy(cl, concept);
				}
			}
			return concepts;
		}
	}

    private String getObjectName(OWLNamedObject o) {
        return o.getIRI().toString();
    }
	
	
	@Override
	public String getFilename() {
		return filename;
	}

	@Override
	public String getSimpleFilename() {
		if (simpleFilename == null) {
			simpleFilename = filename.substring(filename.lastIndexOf('\\') + 1,
					filename.length() - 5);
		}
		return simpleFilename;
	}

	@Override
	public String getName() {
		return "OWL Reader";
	}
}

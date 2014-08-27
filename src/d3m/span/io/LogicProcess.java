package d3m.span.io;

import java.io.File;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

public class LogicProcess {

	ProcessXML processxmlv2;
	OntologyHolder ontologyHolder;
	ProcessSequences processSequences;

	public LogicProcess(){}

	public OntologyHolder getOntologyHolder() {
		return ontologyHolder;
	}

	public void init() {
		processxmlv2 = new ProcessXML();
		ontologyHolder = new OntologyHolder();
		//processSequences = new ProcessSequences();
		
		GlobalVariables.restrictonManager = new RestrictionManager();

		// The OWLOntologyManager is at the heart of the OWL API, we can create
		// an instance of this using the OWLManager class, which will set up
		// commonly used options (such as which parsers are registered etc.
		// etc.)
		ontologyHolder.setOWLOntologyManager(OWLManager.createOWLOntologyManager());
		// Load the ontology
		try {
			ontologyHolder.setOWLOntology(ontologyHolder.getOWLOntologyManager().loadOntologyFromOntologyDocument(
					new File(FilesLocation.ONTOLOGY)));
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ontologyHolder.setOWLDataFactory(ontologyHolder.getOWLOntologyManager().getOWLDataFactory());
		
		//OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory(); //Standard reasoner
		OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory(); //Hermit reasoner
		
		//ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
		//OWLReasonerConfiguration config = new SimpleConfiguration(
		//		progressMonitor);
		
		ontologyHolder.setOWLReasoner(reasonerFactory.createNonBufferingReasoner(ontologyHolder.getOWLOntology()/*,config*/));
		
		ontologyHolder.setPrefixOWLOntologyFormat(ontologyHolder.getOWLOntology().getOntologyID().getOntologyIRI().toString());
		System.out.println("Ontology IRI: " + ontologyHolder.getOWLOntology().getOntologyID().getOntologyIRI().toString());
	}
	
	public void execute() {
		init();
		processxmlv2.execute(ontologyHolder);
	}
	
	// To test
	public static void main(String[] args) {
		new LogicProcess().execute();
	}

}

package d3m.span;

import java.io.File;

import d3m.span.ontologies.Ontology;
import d3m.span.ontologies.io.OWLReader;

public class OWLReaderMain {

	private String m_file = new File("").getAbsolutePath() + "/" + "src/d3m/span/ontologyV3.owl";

	public void run() {
		OWLReader reader = null; 
		try {
			reader = new OWLReader(m_file);
			Ontology ont = new Ontology();
			reader.updateOntology(ont);
			
			System.out.println("==> RESULT: " + ont.toString());
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		OWLReaderMain main = new OWLReaderMain();
		main.run();
	}
}

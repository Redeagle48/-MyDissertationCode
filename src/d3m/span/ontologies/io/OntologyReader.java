/**
 * PaMDA Project
 * PACKAGE - pamda.io.d3m
 * OWLReader.java                          
 * @author Joao Vieira (joao.c.vieira@ist.utl.pt)
 * 2012
 */

package d3m.span.ontologies.io;

import d3m.span.ontologies.Ontology;

/**
 * Interface <B>OntologyReader</B> that classes reading ontologies in the
 * context of D3M should implement.
 * @author Joao Vieira
 * @version 0.1
 */
public interface OntologyReader {
	/**
	 * Updates the ontology from a file.
	 * @param ontology to update
	 * @throws Exception 
	 */
	public void updateOntology(Ontology ontology) throws Exception;

	/**
	 * Returns a string of the filename of the current opened file
	 * @return the name of the file currently being read
	 */
	public String getFilename();
	
	/**
	 * Returns a string of the simple name of the current opened file
	 * @return the simple name of the file currently being read
	 */
	public String getSimpleFilename();
	
	/**
	 * Returns a string with the name of the current reader
	 * @return the name of the reader currently being used
	 */
	public String getName();
}

/**
 * PaMDA Project
 * PACKAGE - pamda.ontologies
 * OntoException.java                         
 * 2009
 */
package d3m.span.ontologies;

/**
 * Class <B>OntoException</> for handling exceptions occurring during ontologies' updating.
 * @author Claudia Antunes
 * @version 1.0
 */
public class OntoException extends Exception {

	/**	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 */
	public OntoException() {
		super();
	}

	/**
	 * @param arg0
	 */
	public OntoException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public OntoException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public OntoException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}

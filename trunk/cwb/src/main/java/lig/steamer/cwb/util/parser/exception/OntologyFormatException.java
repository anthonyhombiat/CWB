package lig.steamer.cwb.util.parser.exception;

public class OntologyFormatException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public OntologyFormatException(Throwable e){
		super("Unable to parse the given ontology.", e);
	}

}

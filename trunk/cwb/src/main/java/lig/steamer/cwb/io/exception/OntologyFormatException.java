package lig.steamer.cwb.io.exception;

public class OntologyFormatException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public OntologyFormatException(Throwable e){
		super("Unable to parse the given ontology.", e);
	}

}

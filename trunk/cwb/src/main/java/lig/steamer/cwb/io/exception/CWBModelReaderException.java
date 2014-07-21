package lig.steamer.cwb.io.exception;

public class CWBModelReaderException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CWBModelReaderException(Throwable e){
		super("Unable to parse the given ontology.", e);
	}

}

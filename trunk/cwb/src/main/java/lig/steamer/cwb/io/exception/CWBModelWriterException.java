package lig.steamer.cwb.io.exception;

public class CWBModelWriterException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CWBModelWriterException(Throwable e){
		super("Unable to parse the given ontology.", e);
	}

}

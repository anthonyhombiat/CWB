package lig.steamer.cwb.io.read.exception;

public class CWBDataModelReaderException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CWBDataModelReaderException(Throwable e){
		super("Unable to parse the given data model.", e);
	}

}

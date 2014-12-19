package lig.steamer.cwb.io.read.impl.exception;

public class CWBFolksoReaderException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CWBFolksoReaderException(Throwable e){
		super("Unable to parse the given folksonomy.", e);
	}

}

package lig.steamer.cwb.io.read.impl.exception;

public class CWBNomenReaderException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CWBNomenReaderException(Throwable e){
		super("Unable to parse the given folksonomy.", e);
	}

}

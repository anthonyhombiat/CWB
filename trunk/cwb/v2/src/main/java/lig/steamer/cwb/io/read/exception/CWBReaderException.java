package lig.steamer.cwb.io.read.exception;

public class CWBReaderException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CWBReaderException(Throwable e){
		super("Unable to read the given CWB project.", e);
	}

}

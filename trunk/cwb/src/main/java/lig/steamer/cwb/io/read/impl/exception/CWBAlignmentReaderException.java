package lig.steamer.cwb.io.read.impl.exception;

public class CWBAlignmentReaderException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CWBAlignmentReaderException(Throwable e){
		super("Unable to parse the given alignment.", e);
	}

}

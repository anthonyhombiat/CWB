package lig.steamer.cwb.io.write.impl.exception;

public class CWBFolksoWriterException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CWBFolksoWriterException(Throwable e){
		super("Unable to write the given folksonomy.", e);
	}

}

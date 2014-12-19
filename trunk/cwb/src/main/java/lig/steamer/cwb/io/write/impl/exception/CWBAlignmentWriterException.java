package lig.steamer.cwb.io.write.impl.exception;

public class CWBAlignmentWriterException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CWBAlignmentWriterException(Throwable e){
		super("Unable to write the given alignment.", e);
	}

}

package lig.steamer.cwb.io.write.exception;

public class CWBWriterException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CWBWriterException(Throwable e){
		super("Unable to write the CWB project.", e);
	}

}

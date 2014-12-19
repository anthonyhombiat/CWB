package lig.steamer.cwb.io.write.impl.exception;

public class CWBProjectWriterException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CWBProjectWriterException(Throwable e){
		super("Unable to write the given CWB project.", e);
	}

}

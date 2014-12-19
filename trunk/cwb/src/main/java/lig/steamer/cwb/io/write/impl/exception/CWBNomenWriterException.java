package lig.steamer.cwb.io.write.impl.exception;

public class CWBNomenWriterException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CWBNomenWriterException(Throwable e){
		super("Unable to write the given nomenclature.", e);
	}

}

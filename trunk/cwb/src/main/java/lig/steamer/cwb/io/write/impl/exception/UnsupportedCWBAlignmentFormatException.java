package lig.steamer.cwb.io.write.impl.exception;

public class UnsupportedCWBAlignmentFormatException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public UnsupportedCWBAlignmentFormatException(){
		super("Unsupported format for rendering the alignment.");
	}
	
	public UnsupportedCWBAlignmentFormatException(Throwable e){
		super("Unsupported format for rendering the alignment.", e);
	}

}

package lig.steamer.cwb.util.wsclient.taginfo.exception;

public class TaginfoResponseException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public TaginfoResponseException(Throwable e){
		super("The Taginfo web service response could not be interpreted.", e);
	}

}

package lig.steamer.cwb.util.wsclient.taginfo.exception;

public class TaginfoWSMalformedURLException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public TaginfoWSMalformedURLException(Throwable e){
		super("Malformed Taginfo web service URL.", e);
	}

}

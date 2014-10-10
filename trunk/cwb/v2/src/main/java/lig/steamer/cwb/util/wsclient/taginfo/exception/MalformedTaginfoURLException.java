package lig.steamer.cwb.util.wsclient.taginfo.exception;

public class MalformedTaginfoURLException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public MalformedTaginfoURLException(Throwable e){
		super("Malformed Taginfo web service URL.", e);
	}

}

package lig.steamer.cwb.util.wsclient.taginfo.exception;

public class TaginfoURISyntaxException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public TaginfoURISyntaxException(Throwable e){
		super("Malformed Taginfo web service URI.", e);
	}

}

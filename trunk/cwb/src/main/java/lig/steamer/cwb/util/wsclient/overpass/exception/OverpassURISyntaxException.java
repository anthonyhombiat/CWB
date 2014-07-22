package lig.steamer.cwb.util.wsclient.overpass.exception;

public class OverpassURISyntaxException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public OverpassURISyntaxException(Throwable e){
		super("Malformed Overpass web service URI.", e);
	}

}

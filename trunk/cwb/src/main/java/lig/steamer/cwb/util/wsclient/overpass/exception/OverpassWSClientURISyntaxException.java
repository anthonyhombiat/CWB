package lig.steamer.cwb.util.wsclient.overpass.exception;

public class OverpassWSClientURISyntaxException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public OverpassWSClientURISyntaxException(Throwable e){
		super("Malformed Overpass web service URI.", e);
	}

}

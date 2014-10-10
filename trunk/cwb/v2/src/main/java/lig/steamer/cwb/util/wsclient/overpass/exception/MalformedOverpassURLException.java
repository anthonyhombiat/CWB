package lig.steamer.cwb.util.wsclient.overpass.exception;

public class MalformedOverpassURLException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public MalformedOverpassURLException(Throwable e){
		super("Malformed Overpass web service URL.", e);
	}

}

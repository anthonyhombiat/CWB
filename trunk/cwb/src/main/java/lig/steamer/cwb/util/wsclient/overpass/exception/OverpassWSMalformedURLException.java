package lig.steamer.cwb.util.wsclient.overpass.exception;

public class OverpassWSMalformedURLException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public OverpassWSMalformedURLException(Throwable e){
		super("Malformed Overpass web service URL.", e);
	}

}

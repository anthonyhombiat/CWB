package lig.steamer.cwb.util.wsclient.overpass.exception;

public class OverpassWSServerResponseException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public OverpassWSServerResponseException(Throwable e){
		super("The Overpass web service response could not be interpreted.", e);
	}

}

package lig.steamer.cwb.util.wsclient.overpass.exception;

public class OverpassResponseException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public OverpassResponseException(Throwable e){
		super("The Overpass web service response could not be interpreted.", e);
	}

}

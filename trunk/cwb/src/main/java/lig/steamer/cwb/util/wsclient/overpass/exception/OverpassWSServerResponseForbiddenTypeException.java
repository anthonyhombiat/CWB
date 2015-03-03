package lig.steamer.cwb.util.wsclient.overpass.exception;

public class OverpassWSServerResponseForbiddenTypeException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public OverpassWSServerResponseForbiddenTypeException(){
		super("Elements type in Overpass WS Server Response must be either a node or a way.");
	}

}

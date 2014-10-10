package lig.steamer.cwb.util.wsclient.overpass.exception;

import lig.steamer.cwb.util.wsclient.exception.WSClientException;

public class OverpassWSClientException extends WSClientException {

	private static final long serialVersionUID = 1L;
	
	public OverpassWSClientException(Throwable e){
		super("An error occurred while calling Overpass web service.", e);
	}

}

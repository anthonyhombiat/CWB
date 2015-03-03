package lig.steamer.cwb.util.wsclient.overpass.exception;

import lig.steamer.cwb.util.wsclient.exception.WSDatasetFolksoClientException;

public class OverpassWSClientException extends WSDatasetFolksoClientException {

	private static final long serialVersionUID = 1L;
	
	public OverpassWSClientException(Throwable e){
		super("An error occurred while calling Overpass web service.", e);
	}

}

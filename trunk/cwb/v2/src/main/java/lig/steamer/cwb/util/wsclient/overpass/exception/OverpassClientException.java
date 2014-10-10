package lig.steamer.cwb.util.wsclient.overpass.exception;

import lig.steamer.cwb.util.wsclient.exception.FolksoProviderWSClientException;

public class OverpassClientException extends FolksoProviderWSClientException {

	private static final long serialVersionUID = 1L;
	
	public OverpassClientException(Throwable e){
		super("An error occurred while calling Overpass web service.", e);
	}

}

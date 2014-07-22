package lig.steamer.cwb.util.wsclient.overpass.exception;

import lig.steamer.cwb.util.wsclient.exception.FolksonomyWSClientException;

public class OverpassClientException extends FolksonomyWSClientException {

	private static final long serialVersionUID = 1L;
	
	public OverpassClientException(Throwable e){
		super("An error occurred while calling Overpass web service.", e);
	}

}

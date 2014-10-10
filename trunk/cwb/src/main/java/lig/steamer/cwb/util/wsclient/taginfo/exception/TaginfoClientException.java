package lig.steamer.cwb.util.wsclient.taginfo.exception;

import lig.steamer.cwb.util.wsclient.exception.WSClientException;

public class TaginfoClientException extends WSClientException {

	private static final long serialVersionUID = 1L;
	
	public TaginfoClientException(Throwable e){
		super("An error occurred while calling Taginfo web service.", e);
	}

}

package lig.steamer.cwb.util.wsclient.taginfo.exception;

import lig.steamer.cwb.util.wsclient.exception.WSDataModelFolksoClientException;

public class TaginfoClientException extends WSDataModelFolksoClientException {

	private static final long serialVersionUID = 1L;
	
	public TaginfoClientException(Throwable e){
		super("An error occurred while calling Taginfo web service.", e);
	}

}

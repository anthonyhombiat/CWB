package lig.steamer.cwb.util.wsclient.taginfo.exception;

import lig.steamer.cwb.util.wsclient.exception.WSDataModelFolksoClientException;

public class TaginfoResponseException extends WSDataModelFolksoClientException {

	private static final long serialVersionUID = 1L;
	
	public TaginfoResponseException(Throwable e){
		super("The Taginfo web service response could not be interpreted.", e);
	}

}

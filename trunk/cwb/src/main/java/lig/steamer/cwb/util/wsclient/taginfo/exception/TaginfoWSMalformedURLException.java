package lig.steamer.cwb.util.wsclient.taginfo.exception;

import lig.steamer.cwb.util.wsclient.exception.WSDataModelFolksoClientException;

public class TaginfoWSMalformedURLException extends WSDataModelFolksoClientException {

	private static final long serialVersionUID = 1L;
	
	public TaginfoWSMalformedURLException(Throwable e){
		super("Malformed Taginfo web service URL.", e);
	}

}

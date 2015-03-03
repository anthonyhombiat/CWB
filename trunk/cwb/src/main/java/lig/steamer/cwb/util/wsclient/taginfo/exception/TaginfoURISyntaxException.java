package lig.steamer.cwb.util.wsclient.taginfo.exception;

import lig.steamer.cwb.util.wsclient.exception.WSDataModelFolksoClientException;

public class TaginfoURISyntaxException extends WSDataModelFolksoClientException {

	private static final long serialVersionUID = 1L;
	
	public TaginfoURISyntaxException(Throwable e){
		super("Malformed Taginfo web service URI.", e);
	}

}

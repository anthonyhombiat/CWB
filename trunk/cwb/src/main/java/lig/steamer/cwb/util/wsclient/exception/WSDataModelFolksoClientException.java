package lig.steamer.cwb.util.wsclient.exception;

public class WSDataModelFolksoClientException extends WSClientException {

	private static final long serialVersionUID = 1L;

	public WSDataModelFolksoClientException(String message, Throwable e){
		super("An error occured while requesting the folksonomy provider Web Service.", e);
	}
	
}

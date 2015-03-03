package lig.steamer.cwb.util.wsclient.exception;

public class WSDatasetFolksoClientException extends WSClientException {

	private static final long serialVersionUID = 1L;

	public WSDatasetFolksoClientException(String message, Throwable e){
		super("An error occured while requesting the folksonomy data provider Web Service.", e);
	}
	
}

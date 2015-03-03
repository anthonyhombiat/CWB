package lig.steamer.cwb.util.wsclient.exception;

public class WSDataModelNomenClientException extends WSClientException {

	private static final long serialVersionUID = 1L;

	public WSDataModelNomenClientException(String message, Throwable e){
		super("An error occured while requesting the nomenclature provider Web Service.", e);
	}
	
}

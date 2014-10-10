package lig.steamer.cwb.util.wsclient.exception;

public class WSClientException extends Exception{

	private static final long serialVersionUID = 1L;

	public WSClientException(String message, Throwable e){
		super(message, e);
	}
	
}

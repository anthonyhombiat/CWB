package lig.steamer.cwb.util.wsclient.exception;

public class WSDatasetNomenClientException extends Exception{

	private static final long serialVersionUID = 1L;

	public WSDatasetNomenClientException(String message, Throwable e){
		super("An error occured while requesting the nomenclature data provider Web Service.", e);
	}
	
}

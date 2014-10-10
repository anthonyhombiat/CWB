package lig.steamer.cwb.util.wsclient.bdtopo.exception;

public class BDTopoWSServerResponseException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public BDTopoWSServerResponseException(Throwable e){
		super("The IGN TOPO database web service response could not be interpreted.", e);
	}

}

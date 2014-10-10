package lig.steamer.cwb.util.wsclient.bdtopo.exception;

import lig.steamer.cwb.util.wsclient.exception.WSClientException;

public class BDTopoWSClientException extends WSClientException {

	private static final long serialVersionUID = 1L;
	
	public BDTopoWSClientException(Throwable e){
		super("An error occurred while calling IGN TOPO database web service.", e);
	}

}

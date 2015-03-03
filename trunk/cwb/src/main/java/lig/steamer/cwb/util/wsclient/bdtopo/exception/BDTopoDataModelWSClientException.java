package lig.steamer.cwb.util.wsclient.bdtopo.exception;

import lig.steamer.cwb.util.wsclient.exception.WSDataModelNomenClientException;


public class BDTopoDataModelWSClientException extends WSDataModelNomenClientException {

	private static final long serialVersionUID = 1L;
	
	public BDTopoDataModelWSClientException(Throwable e){
		super("An error occurred while calling IGN TOPO database web service.", e);
	}

}

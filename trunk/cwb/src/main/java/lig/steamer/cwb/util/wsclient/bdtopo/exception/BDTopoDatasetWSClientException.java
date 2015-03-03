package lig.steamer.cwb.util.wsclient.bdtopo.exception;

import lig.steamer.cwb.util.wsclient.exception.WSDatasetNomenClientException;

public class BDTopoDatasetWSClientException extends WSDatasetNomenClientException {

	private static final long serialVersionUID = 1L;
	
	public BDTopoDatasetWSClientException(Throwable e){
		super("An error occurred while calling IGN TOPO database web service.", e);
	}

}

package lig.steamer.cwb.util.wsclient.bdtopo.exception;

public class BDTopoWSMalformedURLException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public BDTopoWSMalformedURLException(Throwable e){
		super("Malformed IGN TOPO database web service URL.", e);
	}

}

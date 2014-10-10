package lig.steamer.cwb.util.wsclient.bdtopo.exception;

public class BDTopoWSClientURISyntaxException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public BDTopoWSClientURISyntaxException(Throwable e){
		super("Malformed IGN TOPO database web service URI.", e);
	}

}

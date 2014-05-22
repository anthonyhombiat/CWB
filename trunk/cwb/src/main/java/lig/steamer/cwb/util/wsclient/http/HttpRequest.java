package lig.steamer.cwb.util.wsclient.http;


/**
 * @author Anthony Hombiat
 * Enumeration that gathers HTTP request vocabulary.
 */
public enum HttpRequest {

	ACCEPT("Accept"), 
	APPLICATION_JSON("application/json");

	private String name = "";

	HttpRequest(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}

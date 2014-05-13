package lig.steamer.cwb.http;

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

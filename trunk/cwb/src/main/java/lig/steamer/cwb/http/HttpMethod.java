package lig.steamer.cwb.http;

/**
 * @author Anthony Hombiat
 * Enumeration that gathers all the HTTP methods name.
 */
public enum HttpMethod {

	DELETE("DELETE"), 
	GET("GET"), 
	HEAD("HEAD"), 
	OPTIONS("OPTIONS"), 
	POST("POST"), 
	PUT("PUT"), 
	TRACE("TRACE");

	private String name = "";

	HttpMethod(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}

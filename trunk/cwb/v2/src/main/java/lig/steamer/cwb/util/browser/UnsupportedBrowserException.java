package lig.steamer.cwb.util.browser;

public class UnsupportedBrowserException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public UnsupportedBrowserException() {
		super("Unsupported browser (supported browsers: Chrome, Internet Explorer, Firefox, Opera and Safari)");
	}

}

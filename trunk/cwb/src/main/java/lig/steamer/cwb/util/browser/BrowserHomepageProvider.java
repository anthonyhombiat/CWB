package lig.steamer.cwb.util.browser;

import com.vaadin.server.Page;
import com.vaadin.server.WebBrowser;

public class BrowserHomepageProvider {

	public static final String CHROME = "chrome://newtab";
	public static final String FIREFOX = "about:home";
	public static final String IE = "about:home";
	public static final String OPERA = "opera:blank";
	public static final String SAFARI = "about:home";
	
	private BrowserHomepageProvider(){
		
	}
	
	public static String getUrl() throws UnsupportedBrowserException{
		WebBrowser browser = Page.getCurrent().getWebBrowser();
		if(browser.isChrome()){
			return CHROME;
		}
		if(browser.isFirefox()){
			return FIREFOX;
		}
		if(browser.isIE()){
			return IE;
		}
		if(browser.isOpera()){
			return OPERA;
		}
		if(browser.isSafari()){
			return SAFARI;
		}
		throw new UnsupportedBrowserException();
	}
	
}

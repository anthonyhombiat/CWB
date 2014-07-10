package lig.steamer.cwb.util.wsclient.taginfo;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

import org.apache.http.client.utils.URIBuilder;

public class TagInfoRequest {

	private static String TAGINFO_QUERY_BY_KEY_URL = "http://taginfo.openstreetmap.org/api/4/key/values";
	private static String TAGINFO_QUERY_PARAM_NAME_LANG = "lang";
	private static String TAGINFO_QUERY_PARAM_NAME_SORT_BY = "sortname";
	private static String TAGINFO_QUERY_PARAM_NAME_SORT_ORDER = "sortorder";
	private static String TAGINFO_QUERY_PARAM_NAME_PAGE_NB = "page";
	private static String TAGINFO_QUERY_PARAM_NAME_RESULTS_PER_PAGE = "rp";
	private static String TAGINFO_QUERY_PARAM_NAME_KEY = "key";

	private static String TAGINFO_QUERY_DEFAULT_PAGE_NB_VALUE = "1";
	private static String TAGINFO_QUERY_DEFAULT_RESULTS_PER_PAGE_VALUE = "100";
	private static String TAGINFO_QUERY_DEFAULT_SORT_BY_VALUE = "count_all";
	private static String TAGINFO_QUERY_DEFAULT_SORT_ORDER_VALUE = "desc";

	public URL url;

	public TagInfoRequest(String key, Locale locale) {

		try {

			URIBuilder uriBuilder = new URIBuilder(TAGINFO_QUERY_BY_KEY_URL);

			uriBuilder
					.setParameter(TAGINFO_QUERY_PARAM_NAME_LANG, locale.toString())
					.setParameter(TAGINFO_QUERY_PARAM_NAME_SORT_BY,
							TAGINFO_QUERY_DEFAULT_SORT_BY_VALUE)
					.setParameter(TAGINFO_QUERY_PARAM_NAME_SORT_ORDER,
							TAGINFO_QUERY_DEFAULT_SORT_ORDER_VALUE)
					.setParameter(TAGINFO_QUERY_PARAM_NAME_PAGE_NB,
							TAGINFO_QUERY_DEFAULT_PAGE_NB_VALUE)
					.setParameter(TAGINFO_QUERY_PARAM_NAME_RESULTS_PER_PAGE,
							TAGINFO_QUERY_DEFAULT_RESULTS_PER_PAGE_VALUE)
					.setParameter(TAGINFO_QUERY_PARAM_NAME_KEY, key);
			
			url = uriBuilder.build().toURL();
		} catch (URISyntaxException e) { e.printStackTrace(); 
		} catch (MalformedURLException e) { e.printStackTrace();
		}

	}

	/**
	 * @return the url
	 */
	public URL getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(URL url) {
		this.url = url;
	}

}

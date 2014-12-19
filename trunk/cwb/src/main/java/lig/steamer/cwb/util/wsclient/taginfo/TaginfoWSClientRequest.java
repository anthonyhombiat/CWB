package lig.steamer.cwb.util.wsclient.taginfo;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

import lig.steamer.cwb.util.wsclient.taginfo.exception.TaginfoWSMalformedURLException;
import lig.steamer.cwb.util.wsclient.taginfo.exception.TaginfoURISyntaxException;

import org.apache.http.client.utils.URIBuilder;

public class TaginfoWSClientRequest {

	private static final String QUERY_BY_KEY_URL = "http://taginfo.openstreetmap.org/api/4/key/values";
	
	private static final String PARAM_NAME_LANG = "lang";
	private static final String PARAM_NAME_SORT_BY = "sortname";
	private static final String PARAM_NAME_SORT_ORDER = "sortorder";
	private static final String PARAM_NAME_PAGE_NB = "page";
	private static final String PARAM_NAME_RESULTS_PER_PAGE = "rp";
	private static final String PARAM_NAME_KEY = "key";

	private static final String DEFAULT_PAGE_NB_VALUE = "1";
	private static final String DEFAULT_RESULTS_PER_PAGE_VALUE = "100";
	private static final String DEFAULT_SORT_BY_VALUE = "count_all";
	private static final String DEFAULT_SORT_ORDER_VALUE = "desc";

	public URL url;

	public TaginfoWSClientRequest(String key, Locale locale)
			throws TaginfoWSMalformedURLException, TaginfoURISyntaxException {

		try {

			URIBuilder uriBuilder = new URIBuilder(QUERY_BY_KEY_URL);

			uriBuilder
					.setParameter(PARAM_NAME_LANG, locale.toString())
					.setParameter(PARAM_NAME_SORT_BY,
							DEFAULT_SORT_BY_VALUE)
					.setParameter(PARAM_NAME_SORT_ORDER,
							DEFAULT_SORT_ORDER_VALUE)
					.setParameter(PARAM_NAME_PAGE_NB,
							DEFAULT_PAGE_NB_VALUE)
					.setParameter(PARAM_NAME_RESULTS_PER_PAGE,
							DEFAULT_RESULTS_PER_PAGE_VALUE)
					.setParameter(PARAM_NAME_KEY, key);
			
			url = uriBuilder.build().toURL();
		} catch (URISyntaxException e) { 
			throw new TaginfoURISyntaxException(e); 
		} catch (MalformedURLException e) { 
			throw new TaginfoWSMalformedURLException(e);
		}

	}

	/**
	 * @return the url
	 */
	public URL getUrl() {
		return url;
	}

}

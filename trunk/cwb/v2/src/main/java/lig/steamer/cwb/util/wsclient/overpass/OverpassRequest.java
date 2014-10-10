package lig.steamer.cwb.util.wsclient.overpass;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

import lig.steamer.cwb.model.BBox;
import lig.steamer.cwb.util.wsclient.overpass.exception.MalformedOverpassURLException;
import lig.steamer.cwb.util.wsclient.overpass.exception.OverpassURISyntaxException;

import org.apache.http.client.utils.URIBuilder;

public class OverpassRequest {

	private static final String QUERY_BY_KEY_URL = "http://overpass-api.de/api/interpreter";

	private static final String PARAM_NAME_DATA = "data";

	private URL url;
	private Locale locale;
	private BBox bbox;

	public OverpassRequest(String key, Locale locale, BBox bbox,
			String outputFormat) throws OverpassURISyntaxException,
			MalformedOverpassURLException {

		try {

			URIBuilder uriBuilder = new URIBuilder(QUERY_BY_KEY_URL);

			uriBuilder.setParameter(PARAM_NAME_DATA,
					buildURLParameters(key, bbox, outputFormat));

			url = uriBuilder.build().toURL();
		} catch (URISyntaxException e) {
			throw new OverpassURISyntaxException(e);
		} catch (MalformedURLException e) {
			throw new MalformedOverpassURLException(e);
		}

	}

	public String buildURLParameters(String key, BBox bbox, String outputFormat) {
		return String.format("[out:%s];node[\"%s\"](%s,%s,%s,%s);out;",
				outputFormat, key, bbox.getSouth(), bbox.getWest(),
				bbox.getNorth(), bbox.getEast());
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

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the bbox
	 */
	public BBox getBbox() {
		return bbox;
	}

	/**
	 * @param bbox the bbox to set
	 */
	public void setBbox(BBox bbox) {
		this.bbox = bbox;
	}

}

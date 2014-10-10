package lig.steamer.cwb.util.wsclient.overpass;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;

import lig.steamer.cwb.model.CWBBBox;
import lig.steamer.cwb.util.wsclient.WSClientRequest;
import lig.steamer.cwb.util.wsclient.overpass.exception.OverpassWSClientURISyntaxException;
import lig.steamer.cwb.util.wsclient.overpass.exception.OverpassWSMalformedURLException;

import org.apache.http.client.utils.URIBuilder;

public class OverpassWSClientRequest implements WSClientRequest {

	private static final String QUERY_URL = "http://overpass-api.de/api/interpreter";

	private static final String PARAM_NAME_DATA = "data";

	private URL url;

	public OverpassWSClientRequest(String key, String value, CWBBBox bbox,
			Locale locale, String outputFormat)
			throws OverpassWSClientURISyntaxException,
			OverpassWSMalformedURLException {

		try {

			URIBuilder uriBuilder = new URIBuilder(QUERY_URL);

			uriBuilder.setParameter(PARAM_NAME_DATA,
					buildURLParameters(key, value, bbox, outputFormat));

			url = uriBuilder.build().toURL();

		} catch (URISyntaxException e) {
			throw new OverpassWSClientURISyntaxException(e);
		} catch (MalformedURLException e) {
			throw new OverpassWSMalformedURLException(e);
		}

	}

	public OverpassWSClientRequest(String key, CWBBBox bbox, Locale locale,
			String outputFormat) throws OverpassWSClientURISyntaxException,
			OverpassWSMalformedURLException {

		this(key, "", bbox, locale, outputFormat);

	}

	private String buildURLParameters(String key, String value, CWBBBox bbox,
			String outputFormat) {

		if (value == "") {
			return buildGetByKeyURLParameters(key, bbox, outputFormat);
		}

		return buildGetByKeyValueURLParameters(key, value, bbox, outputFormat);
	}

	private String buildGetByKeyURLParameters(String key, CWBBBox bbox,
			String outputFormat) {
		return String.format("[out:%s];node[\"%s\"](%s,%s,%s,%s);out;",
				outputFormat, key, bbox.getSouth(), bbox.getWest(),
				bbox.getNorth(), bbox.getEast());
	}

	private String buildGetByKeyValueURLParameters(String key, String value,
			CWBBBox bbox, String outputFormat) {
		return String.format("[out:%s];node[\"%s\"=\"%s\"](%s,%s,%s,%s);out;",
				outputFormat, key, value, bbox.getSouth(), bbox.getWest(),
				bbox.getNorth(), bbox.getEast());
	}

	/**
	 * @return the url
	 */
	public URL getUrl() {
		return url;
	}

}

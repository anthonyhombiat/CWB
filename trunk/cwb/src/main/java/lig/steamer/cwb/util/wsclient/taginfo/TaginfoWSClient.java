package lig.steamer.cwb.util.wsclient.taginfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBDataModelFolkso;
import lig.steamer.cwb.util.wsclient.DataModelFolksoProviderWSClient;
import lig.steamer.cwb.util.wsclient.exception.WSClientException;
import lig.steamer.cwb.util.wsclient.http.HttpMethod;
import lig.steamer.cwb.util.wsclient.http.HttpRequest;
import lig.steamer.cwb.util.wsclient.taginfo.exception.TaginfoClientException;
import lig.steamer.cwb.util.wsclient.taginfo.exception.TaginfoResponseException;
import lig.steamer.cwb.util.wsclient.taginfo.exception.TaginfoURISyntaxException;
import lig.steamer.cwb.util.wsclient.taginfo.exception.TaginfoWSMalformedURLException;

import org.semanticweb.owlapi.model.IRI;

/**
 * @author Anthony Hombiat OSM Taginfo client that provides methods to consume
 * data from the restful Web Service OSM Taginfo API.
 */
public class TaginfoWSClient implements DataModelFolksoProviderWSClient {

	private static Logger LOGGER = Logger.getLogger(TaginfoWSClient.class
			.getName());

	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	private static final double DEFAULT_THRESHOLD = 0;

	public static final String DEFAULT_TAG_KEY = "amenity";
	public static final String OSM_TAG_INFO_IRI = "http://taginfo.openstreetmap.fr";

	public TaginfoWSClient() {

	}

	/**
	 * Returns the Folksonomy that gathers OSM tags corresponding to the given
	 * key for the given locale and for the given frequency of occurrence
	 * threshold.
	 * @param key , the key
	 * @param locale , the locale
	 * @param threshold , the threshold
	 * @return the CWBDataModelFolkso
	 * @throws TaginfoClientException
	 */
	public CWBDataModelFolkso getTagsByKey(String key, Locale locale,
			double threshold) throws TaginfoClientException {

		LOGGER.log(Level.INFO, "Querying the Taginfo web service...");

		try {

			TaginfoWSClientRequest tagInfoRequest = new TaginfoWSClientRequest(
					key, locale);
			URL url = tagInfoRequest.getUrl();

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod(HttpMethod.GET.toString());
			connection.setRequestProperty(HttpRequest.ACCEPT.toString(),
					HttpRequest.APPLICATION_JSON.toString());

			TaginfoWSServerResponse response = new TaginfoWSServerResponse(
					connection);

			CWBDataModelFolkso folkso = new CWBDataModelFolkso(
					IRI.create(OSM_TAG_INFO_IRI));

			for (Entry<String, String> entry : response.getTags().entrySet()) {

				CWBConcept concept = new CWBConcept(IRI.create(OSM_TAG_INFO_IRI
						+ "#" + entry.getKey()));
				concept.addLabel(entry.getKey(), locale);
				concept.addDescription(entry.getValue(), locale);

				folkso.addConcept(concept);

				LOGGER.log(Level.INFO, concept.getFragment());

			}

			connection.disconnect();

			LOGGER.log(Level.INFO,
					"Connection to the Taginfo web service is close.");

			return folkso;

		} catch (IOException | TaginfoResponseException
				| TaginfoURISyntaxException | TaginfoWSMalformedURLException e) {
			throw new TaginfoClientException(e);
		}

	}

	/**
	 * Returns the Folksonomy that gathers OSM tags corresponding to the given
	 * key.
	 * @param key
	 * @return the CWBDataModelFolkso
	 * @throws TaginfoClientException
	 */
	public CWBDataModelFolkso getTagsByKey(String key) throws TaginfoClientException {
		return this.getTagsByKey(key, DEFAULT_LOCALE, DEFAULT_THRESHOLD);
	}

	/**
	 * Returns the Folksonomy that gathers OSM tags corresponding to the given
	 * key for the given locale.
	 * @param key
	 * @return the CWBDataModelFolkso
	 * @throws TaginfoClientException
	 */
	public CWBDataModelFolkso getTagsByKey(String key, Locale locale)
			throws TaginfoClientException {
		return this.getTagsByKey(key, locale, DEFAULT_THRESHOLD);
	}

	@Override
	public CWBDataModelFolkso getDataModelFolkso() throws WSClientException {
		try {
			return getTagsByKey(DEFAULT_TAG_KEY);
		} catch (TaginfoClientException e) {
			throw new WSClientException(e.getMessage(), e);
		}
	}

}

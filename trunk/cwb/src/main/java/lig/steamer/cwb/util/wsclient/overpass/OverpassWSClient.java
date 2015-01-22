package lig.steamer.cwb.util.wsclient.overpass;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import lig.steamer.cwb.Prop;
import lig.steamer.cwb.model.CWBBBox;
import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBDataModelFolkso;
import lig.steamer.cwb.model.CWBInstanceFolkso;
import lig.steamer.cwb.util.wsclient.DataModelFolksoProviderWSClient;
import lig.steamer.cwb.util.wsclient.InstancesFolksoProviderWSClient;
import lig.steamer.cwb.util.wsclient.WSNodeFolkso;
import lig.steamer.cwb.util.wsclient.http.HttpMethod;
import lig.steamer.cwb.util.wsclient.http.HttpRequest;
import lig.steamer.cwb.util.wsclient.overpass.exception.OverpassWSClientException;
import lig.steamer.cwb.util.wsclient.overpass.exception.OverpassWSClientURISyntaxException;
import lig.steamer.cwb.util.wsclient.overpass.exception.OverpassWSMalformedURLException;
import lig.steamer.cwb.util.wsclient.overpass.exception.OverpassWSServerResponseException;

import org.semanticweb.owlapi.model.IRI;
import org.vaadin.addon.leaflet.shared.Point;

/**
 * @author Anthony Hombiat OSM Overpass client that provides methods to consume
 * data from the restful Web Service OSM Overpass API.
 */
public class OverpassWSClient implements DataModelFolksoProviderWSClient,
		InstancesFolksoProviderWSClient {

	private static Logger LOGGER = Logger.getLogger(OverpassWSClient.class
			.getName());

	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	private static final String DEFAULT_OUTPUT_FMT = "json";
	private static final double DEFAULT_THRESHOLD = 0;
	private static final String DEFAULT_TAG_KEY = "amenity";

	public static final String OVERPASS_URI = "http://overpass-api.de";

	public OverpassWSClient() {

	}

	/**
	 * Returns the Folksonomy that gathers OSM tags corresponding to the given
	 * key for the given locale, for the given frequency of occurrence
	 * threshold, in the given bounding box and in the given output format.
	 * @param key , the key
	 * @param bbox, the bounding box
	 * @param locale , the locale
	 * @param threshold , the threshold
	 * @param outputFormat, the output format
	 * @return the CWBDataModelFolkso
	 * @throws OverpassWSClientException
	 */
	public CWBDataModelFolkso getTagsByKey(String key, CWBBBox bbox, Locale locale,
			double threshold, String outputFormat)
			throws OverpassWSClientException {

		LOGGER.log(Level.INFO, "Querying the Overpass web service...");

		try {

			OverpassWSClientRequest request = new OverpassWSClientRequest(key,
					bbox, locale, outputFormat);
			URL url = request.getUrl();

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod(HttpMethod.GET.toString());
			connection.setRequestProperty(HttpRequest.ACCEPT.toString(),
					HttpRequest.APPLICATION_JSON.toString());

			OverpassWSServerResponse response = new OverpassWSServerResponse(
					connection);

			
			CWBDataModelFolkso folkso = new CWBDataModelFolkso(IRI.create(OVERPASS_URI));

			for (WSNodeFolkso node : response.getNodes()) {

				CWBConcept concept = new CWBConcept(IRI.create(OVERPASS_URI + "/" + node.getTags()
						.iterator().next())); 

				folkso.addConcept(concept);
				
				LOGGER.log(Level.INFO, "Concept retrieved: " + concept.getFragment());

			}

			connection.disconnect();

			LOGGER.log(Level.INFO,
					"Connection to the Overpass web service is close.");

			return folkso;

		} catch (IOException | OverpassWSServerResponseException
				| OverpassWSClientURISyntaxException
				| OverpassWSMalformedURLException e) {
			throw new OverpassWSClientException(e);
		}

	}

	/**
	 * Returns the Folksonomy that gathers OSM tags corresponding to the given
	 * key.
	 * @param key
	 * @return the CWBDataModelFolkso
	 * @throws OverpassWSClientException
	 */
	public CWBDataModelFolkso getTagsByKey(String key)
			throws OverpassWSClientException {
		return this.getTagsByKey(key, Prop.DEFAULT_MAP_BBOX, DEFAULT_LOCALE,
				DEFAULT_THRESHOLD, DEFAULT_OUTPUT_FMT);
	}

	/**
	 * Returns the Folksonomy that gathers OSM tags corresponding to the given
	 * key for the given locale.
	 * @param key
	 * @param locale
	 * @return the CWBDataModelFolkso
	 * @throws OverpassWSClientException
	 */
	public CWBDataModelFolkso getTagsByKey(String key, Locale locale)
			throws OverpassWSClientException {
		return this.getTagsByKey(key, Prop.DEFAULT_MAP_BBOX, locale, DEFAULT_THRESHOLD,
				DEFAULT_OUTPUT_FMT);
	}

	/**
	 * Returns the Folksonomy that gathers OSM tags corresponding to the given
	 * key for the given locale.
	 * @param key
	 * @param locale
	 * @param threshold
	 * @return the CWBDataModelFolkso
	 * @throws OverpassWSClientException
	 */
	public CWBDataModelFolkso getTagsByKey(String key, Locale locale, double threshold)
			throws OverpassWSClientException {
		return this.getTagsByKey(key, Prop.DEFAULT_MAP_BBOX, locale, threshold,
				DEFAULT_OUTPUT_FMT);
	}

	/**
	 * Returns the instances that gathers OSM tags corresponding to the given
	 * key for the given locale.
	 * @param key
	 * @param bbox
	 * @param locale
	 * @param threshold
	 * @return the CWBDataModelFolkso
	 * @throws OverpassWSClientException
	 */
	public CWBDataModelFolkso getTagsByKey(String key, Locale locale,
			double threshold, CWBBBox bbox) throws OverpassWSClientException {
		return this.getTagsByKey(key, bbox, locale, threshold,
				DEFAULT_OUTPUT_FMT);
	}

	@Override
	public CWBDataModelFolkso getDataModelFolkso() throws OverpassWSClientException {
		try {
			return getTagsByKey(DEFAULT_TAG_KEY);
		} catch (OverpassWSClientException e) {
			throw new OverpassWSClientException(e);
		}
	}

	@Override
	public Collection<CWBInstanceFolkso> getFolksoInstances(CWBConcept concept,
			CWBBBox bbox, Locale locale, double threshold, String outputFormat)
			throws OverpassWSClientException {

		LOGGER.log(Level.INFO, "Querying the Overpass web service...");

		try {

			List<CWBInstanceFolkso> instances = new ArrayList<CWBInstanceFolkso>();

			OverpassWSClientRequest request = new OverpassWSClientRequest(DEFAULT_TAG_KEY,
					concept.getFragment().toString(), bbox, locale, outputFormat);
			URL url = request.getUrl();

			System.out.println(url.toString());

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod(HttpMethod.GET.toString());
			connection.setRequestProperty(HttpRequest.ACCEPT.toString(),
					HttpRequest.APPLICATION_JSON.toString());

			OverpassWSServerResponse response = new OverpassWSServerResponse(
					connection);

			for (WSNodeFolkso node : response.getNodes()) {

				instances.add(new CWBInstanceFolkso(new Point(node.getLat(),
						node.getLon()), node.getName(), node.getTags()));

				LOGGER.log(Level.INFO, node.getTags().iterator().next()
						.toString()
						+ ": " + node.getName());

			}

			connection.disconnect();

			LOGGER.log(Level.INFO,
					"Connection to the Overpass web service is close.");

			return instances;

		} catch (IOException | OverpassWSServerResponseException
				| OverpassWSClientURISyntaxException
				| OverpassWSMalformedURLException e) {
			throw new OverpassWSClientException(e);
		}
	}

	@Override
	public Collection<CWBInstanceFolkso> getFolksoInstances(CWBConcept concept)
			throws OverpassWSClientException {
		return this.getFolksoInstances(concept, Prop.DEFAULT_MAP_BBOX, DEFAULT_LOCALE,
				DEFAULT_THRESHOLD, DEFAULT_OUTPUT_FMT);
	}

	@Override
	public Collection<CWBInstanceFolkso> getFolksoInstances(CWBConcept concept,
			CWBBBox bbox) throws OverpassWSClientException {
		return this.getFolksoInstances(concept, bbox, DEFAULT_LOCALE,
				DEFAULT_THRESHOLD, DEFAULT_OUTPUT_FMT);
	}

	@Override
	public Collection<CWBInstanceFolkso> getFolksoInstances(CWBConcept concept,
			CWBBBox bbox, Locale locale) throws OverpassWSClientException {
		return this.getFolksoInstances(concept, bbox, locale, DEFAULT_THRESHOLD,
				DEFAULT_OUTPUT_FMT);
	}

	@Override
	public Collection<CWBInstanceFolkso> getFolksoInstances(CWBConcept concept,
			CWBBBox bbox, Locale locale, double threshold)
			throws OverpassWSClientException {
		return this.getFolksoInstances(concept, bbox, locale, threshold,
				DEFAULT_OUTPUT_FMT);
	}
}

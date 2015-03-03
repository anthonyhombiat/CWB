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

import lig.steamer.cwb.model.CWBBBox;
import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBInstanceFolkso;
import lig.steamer.cwb.util.wsclient.CWBDatasetFolksoProviderWSClient;
import lig.steamer.cwb.util.wsclient.WSNodeFolkso;
import lig.steamer.cwb.util.wsclient.http.HttpMethod;
import lig.steamer.cwb.util.wsclient.http.HttpRequest;
import lig.steamer.cwb.util.wsclient.overpass.exception.OverpassWSClientException;
import lig.steamer.cwb.util.wsclient.overpass.exception.OverpassWSClientURISyntaxException;
import lig.steamer.cwb.util.wsclient.overpass.exception.OverpassWSMalformedURLException;
import lig.steamer.cwb.util.wsclient.overpass.exception.OverpassWSServerResponseException;

import org.vaadin.addon.leaflet.shared.Point;

/**
 * @author Anthony Hombiat OSM Overpass client that provides methods to consume
 * data from the restful Web Service OSM Overpass API.
 */
public class OverpassWSClient implements
		CWBDatasetFolksoProviderWSClient {

	private static Logger LOGGER = Logger.getLogger(OverpassWSClient.class
			.getName());

	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	private static final String DEFAULT_OUTPUT_FMT = "json";
	private static final double DEFAULT_THRESHOLD = 0;
	private static final String DEFAULT_TAG_KEY = "amenity";

	public static final String OVERPASS_URI = "http://overpass-api.de";

	public OverpassWSClient() {

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

				instances.add(new CWBInstanceFolkso(node.getId(), new Point(node.getLat(),
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

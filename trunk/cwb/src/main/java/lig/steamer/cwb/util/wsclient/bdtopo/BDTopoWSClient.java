package lig.steamer.cwb.util.wsclient.bdtopo;

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
import lig.steamer.cwb.model.CWBDataModelNomen;
import lig.steamer.cwb.model.CWBInstanceNomen;
import lig.steamer.cwb.util.wsclient.CWBDatasetNomenProviderWSClient;
import lig.steamer.cwb.util.wsclient.DataModelNomenProviderWSClient;
import lig.steamer.cwb.util.wsclient.WSNodeNomen;
import lig.steamer.cwb.util.wsclient.bdtopo.exception.BDTopoDataModelWSClientException;
import lig.steamer.cwb.util.wsclient.bdtopo.exception.BDTopoDatasetWSClientException;
import lig.steamer.cwb.util.wsclient.bdtopo.exception.BDTopoWSClientURISyntaxException;
import lig.steamer.cwb.util.wsclient.bdtopo.exception.BDTopoWSMalformedURLException;
import lig.steamer.cwb.util.wsclient.bdtopo.exception.BDTopoWSServerResponseException;
import lig.steamer.cwb.util.wsclient.http.HttpMethod;
import lig.steamer.cwb.util.wsclient.http.HttpRequest;

import org.semanticweb.owlapi.model.IRI;
import org.vaadin.addon.leaflet.shared.Point;

public class BDTopoWSClient implements DataModelNomenProviderWSClient,
		CWBDatasetNomenProviderWSClient {

	private static Logger LOGGER = Logger.getLogger(BDTopoWSClient.class
			.getName());

	private static final String DEFAULT_OUTPUT_FMT = "json";
	private static final double DEFAULT_THRESHOLD = 0;
	private static final String DEFAULT_PROP = "nature";

	public static final String BD_TOPO_URI = "http://ign.topo.fr";

	public BDTopoWSClient() {

	}

	public CWBDataModelNomen getDataModelNomen(String outputFormat) throws BDTopoDataModelWSClientException {

		LOGGER.log(Level.INFO, "Querying the IGN BD TOPO web service...");

		try {

			BDTopoWSClientRequest request = new BDTopoWSClientRequest(DEFAULT_PROP, null,
					null, outputFormat);
			URL url = request.getUrl();

			System.out.println(url);
			
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod(HttpMethod.GET.toString());
			connection.setRequestProperty(HttpRequest.ACCEPT.toString(),
					HttpRequest.APPLICATION_JSON.toString());

			BDTopoWSServerResponse response = new BDTopoWSServerResponse(
					connection);

			CWBDataModelNomen nomen = new CWBDataModelNomen(
					IRI.create(BD_TOPO_URI));

			for (WSNodeNomen node : response.getNodes()) {

				CWBConcept concept = new CWBConcept(IRI.create(BD_TOPO_URI
						+ "/" + node.getCategory()));

				if(!nomen.getConcepts().contains(concept)){
					nomen.addConcept(concept);
				}

				LOGGER.log(Level.INFO, "Concept retrieved: " + concept.getFragment());

			}

			connection.disconnect();

			LOGGER.log(Level.INFO,
					"Connection to the Overpass web service is close.");

			return nomen;

		} catch (IOException | BDTopoWSServerResponseException
				| BDTopoWSClientURISyntaxException
				| BDTopoWSMalformedURLException e) {
			throw new BDTopoDataModelWSClientException(e);
		}
	}

	@Override
	public CWBDataModelNomen getDataModelNomen() throws BDTopoDataModelWSClientException {
		return getDataModelNomen(DEFAULT_OUTPUT_FMT);
	}

	@Override
	public Collection<CWBInstanceNomen> getNomenInstances(CWBConcept concept,
			CWBBBox bbox, double threshold, String outputFormat)
			throws BDTopoDatasetWSClientException {

		LOGGER.log(Level.INFO, "Querying the IGN BD TOPO web service...");
		
		try {
			
			List<CWBInstanceNomen> instances = new ArrayList<CWBInstanceNomen>();

			BDTopoWSClientRequest request = new BDTopoWSClientRequest(DEFAULT_PROP, concept.getLabelByLanguage(Locale.FRENCH), bbox, outputFormat);
			URL url = request.getUrl();

			System.out.println(url.toString());

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod(HttpMethod.GET.toString());
			connection.setRequestProperty(HttpRequest.ACCEPT.toString(),
					HttpRequest.APPLICATION_JSON.toString());

			BDTopoWSServerResponse response = new BDTopoWSServerResponse(
					connection);

			for (WSNodeNomen node : response.getNodes()) {

				instances.add(new CWBInstanceNomen(node.getId(), new Point(node.getLat(),
						node.getLon()), node.getName(), new ArrayList<String>()));

				LOGGER.log(Level.INFO, node.getCategory()
						+ ": " + node.getName());

			} 

			connection.disconnect();

			LOGGER.log(Level.INFO,
					"Connection to the Overpass web service is close.");

			return instances;
			
		} catch (IOException | BDTopoWSServerResponseException
				| BDTopoWSClientURISyntaxException
				| BDTopoWSMalformedURLException e) {
			throw new BDTopoDatasetWSClientException(e);
		}
		
	}

	@Override
	public Collection<CWBInstanceNomen> getNomenInstances(CWBConcept concept,
			CWBBBox bbox) throws BDTopoDatasetWSClientException {
		return getNomenInstances(concept, bbox, DEFAULT_THRESHOLD,
				DEFAULT_OUTPUT_FMT);
	}

	@Override
	public Collection<CWBInstanceNomen> getNomenInstances(CWBConcept concept,
			CWBBBox bbox, double threshold)
			throws BDTopoDatasetWSClientException {
		return getNomenInstances(concept, bbox, threshold,
				DEFAULT_OUTPUT_FMT);
	}

}

package lig.steamer.cwb.util.wsclient.overpass;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import lig.steamer.cwb.core.tagging.IFolksonomy;
import lig.steamer.cwb.core.tagging.ILocalizedString;
import lig.steamer.cwb.core.tagging.ITag;
import lig.steamer.cwb.core.tagging.ITagSet;
import lig.steamer.cwb.core.tagging.impl.Folksonomy;
import lig.steamer.cwb.core.tagging.impl.LocalizedString;
import lig.steamer.cwb.core.tagging.impl.Source;
import lig.steamer.cwb.core.tagging.impl.Tag;
import lig.steamer.cwb.core.tagging.impl.TagSet;
import lig.steamer.cwb.model.BBox;
import lig.steamer.cwb.util.wsclient.FolksoProviderWSClient;
import lig.steamer.cwb.util.wsclient.exception.FolksoProviderWSClientException;
import lig.steamer.cwb.util.wsclient.http.HttpMethod;
import lig.steamer.cwb.util.wsclient.http.HttpRequest;
import lig.steamer.cwb.util.wsclient.overpass.exception.MalformedOverpassURLException;
import lig.steamer.cwb.util.wsclient.overpass.exception.OverpassClientException;
import lig.steamer.cwb.util.wsclient.overpass.exception.OverpassResponseException;
import lig.steamer.cwb.util.wsclient.overpass.exception.OverpassURISyntaxException;

import org.semanticweb.owlapi.model.IRI;

/**
 * @author Anthony Hombiat OSM Overpass client that provides methods to consume
 * data from the restful Web Service OSM Overpass API.
 */
public class OverpassClient implements FolksoProviderWSClient{

	private static Logger LOGGER = Logger.getLogger(OverpassClient.class
			.getName());

	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	private static final String DEFAULT_OUTPUT_FMT = "json";
	private static final double DEFAULT_THRESHOLD = 0;
	public static final String DEFAULT_TAG_KEY = "amenity";
	private static final BBox DEFAULT_BBOX = new BBox(45.154121, 5.678540,
			45.213760, 5.753120); // Grenoble bbox

	public static final String OVERPASS_URI = "http://overpass-api.de";

	public OverpassClient() {

	}

	/**
	 * Returns the Folksonomy that gathers OSM tags corresponding to the given
	 * key for the given locale, for the given frequency of occurrence
	 * threshold, in the given bounding box and in the given output format.
	 * @param key , the key
	 * @param locale , the locale
	 * @param threshold , the threshold
	 * @param bbox, the bounding box
	 * @param outputFormat, the output format
	 * @return the IFolksonomy
	 * @throws OverpassClientException
	 */
	public IFolksonomy getTagsByKey(String key, Locale locale,
			double threshold, BBox bbox, String outputFormat)
			throws OverpassClientException {

		LOGGER.log(Level.INFO, "Querying the Overpass web service...");

		try {

			OverpassRequest overpassRequest = new OverpassRequest(key, locale, bbox, outputFormat);
			URL url = overpassRequest.getUrl();

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod(HttpMethod.GET.toString());
			connection.setRequestProperty(HttpRequest.ACCEPT.toString(),
					HttpRequest.APPLICATION_JSON.toString());

			OverpassResponse response = new OverpassResponse(connection);

			ITagSet tagset = new TagSet();

			for (Entry<String, String> entry : response.getTags().entrySet()) {

				ILocalizedString tagKey = new LocalizedString(key,
						locale.toString());
				ILocalizedString tagValue = new LocalizedString(entry.getKey(),
						locale.toString());
				ILocalizedString tagDescription = new LocalizedString(
						entry.getValue(), locale.toString());

				ITag currentTag = new Tag(tagKey, tagValue);
				currentTag.setDescription(tagDescription);

				tagset.addTag(currentTag);
				LOGGER.log(Level.INFO, currentTag.toString());

			}

			connection.disconnect();

			LOGGER.log(Level.INFO,
					"Connection to the Overpass web service is close.");

			return new Folksonomy(tagset, null, new Source(
					IRI.create(OverpassClient.OVERPASS_URI)), null);

		} catch (IOException | OverpassResponseException
				| OverpassURISyntaxException | MalformedOverpassURLException e) {
			throw new OverpassClientException(e);
		}

	}

	/**
	 * Returns the Folksonomy that gathers OSM tags corresponding to the given
	 * key.
	 * @param key
	 * @return the IFolksonomy
	 * @throws OverpassClientException 
	 */
	public IFolksonomy getTagsByKey(String key) throws OverpassClientException {
		return this.getTagsByKey(key, DEFAULT_LOCALE, DEFAULT_THRESHOLD,
				DEFAULT_BBOX, DEFAULT_OUTPUT_FMT);
	}

	/**
	 * Returns the Folksonomy that gathers OSM tags corresponding to the given
	 * key for the given locale.
	 * @param key
	 * @param locale
	 * @return the IFolksonomy
	 * @throws OverpassClientException 
	 */
	public IFolksonomy getTagsByKey(String key, Locale locale) throws OverpassClientException {
		return this.getTagsByKey(key, locale, DEFAULT_THRESHOLD, DEFAULT_BBOX,
				DEFAULT_OUTPUT_FMT);
	}

	/**
	 * Returns the Folksonomy that gathers OSM tags corresponding to the given
	 * key for the given locale.
	 * @param key
	 * @param locale
	 * @param threshold
	 * @return the IFolksonomy
	 * @throws OverpassClientException 
	 */
	public IFolksonomy getTagsByKey(String key, Locale locale, double threshold) throws OverpassClientException {
		return this.getTagsByKey(key, locale, threshold, DEFAULT_BBOX,
				DEFAULT_OUTPUT_FMT);
	}

	/**
	 * Returns the Folksonomy that gathers OSM tags corresponding to the given
	 * key for the given locale.
	 * @param key
	 * @param locale
	 * @param threshold
	 * @param bbox
	 * @return the IFolksonomy
	 * @throws OverpassClientException 
	 */
	public IFolksonomy getTagsByKey(String key, Locale locale,
			double threshold, BBox bbox) throws OverpassClientException {
		return this.getTagsByKey(key, locale, threshold, bbox,
				DEFAULT_OUTPUT_FMT);
	}

	@Override
	public IFolksonomy getTags() throws FolksoProviderWSClientException {
		try {
			return getTagsByKey(DEFAULT_TAG_KEY);
		} catch (OverpassClientException e) {
			throw new FolksoProviderWSClientException(e.getMessage(), e);
		}
	}

}

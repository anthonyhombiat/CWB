package lig.steamer.cwb.util.wsclient.taginfo;

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
import lig.steamer.cwb.util.wsclient.FolksonomyWSClient;
import lig.steamer.cwb.util.wsclient.exception.FolksonomyWSClientException;
import lig.steamer.cwb.util.wsclient.http.HttpMethod;
import lig.steamer.cwb.util.wsclient.http.HttpRequest;
import lig.steamer.cwb.util.wsclient.taginfo.exception.MalformedTaginfoURLException;
import lig.steamer.cwb.util.wsclient.taginfo.exception.TaginfoClientException;
import lig.steamer.cwb.util.wsclient.taginfo.exception.TaginfoResponseException;
import lig.steamer.cwb.util.wsclient.taginfo.exception.TaginfoURISyntaxException;

import org.semanticweb.owlapi.model.IRI;

/**
 * @author Anthony Hombiat OSM Taginfo client that provides methods to consume
 * data from the restful Web Service OSM Taginfo API.
 */
public class TaginfoClient implements FolksonomyWSClient {

	private static Logger LOGGER = Logger.getLogger(TaginfoClient.class
			.getName());

	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	private static final double DEFAULT_THRESHOLD = 0;

	public static final String DEFAULT_TAG_KEY = "amenity";
	public static final String OSM_TAG_INFO_IRI = "http://taginfo.openstreetmap.fr";

	public TaginfoClient() {

	}

	/**
	 * Returns the Folksonomy that gathers OSM tags corresponding to the given
	 * key for the given locale and for the given frequency of occurrence
	 * threshold.
	 * @param key , the key
	 * @param locale , the locale
	 * @param threshold , the threshold
	 * @return the IFolksonomy
	 * @throws TaginfoClientException
	 */
	public IFolksonomy getTagsByKey(String key, Locale locale, double threshold)
			throws TaginfoClientException {

		LOGGER.log(Level.INFO, "Querying the Taginfo web service...");

		try {

			TaginfoRequest tagInfoRequest = new TaginfoRequest(key, locale);
			URL url = tagInfoRequest.getUrl();

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod(HttpMethod.GET.toString());
			connection.setRequestProperty(HttpRequest.ACCEPT.toString(),
					HttpRequest.APPLICATION_JSON.toString());

			TaginfoResponse response = new TaginfoResponse(connection);

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
					"Connection to the Taginfo web service is close.");

			return new Folksonomy(tagset, null, new Source(
					IRI.create(TaginfoClient.OSM_TAG_INFO_IRI)), null);

		} catch (IOException | TaginfoResponseException
				| TaginfoURISyntaxException | MalformedTaginfoURLException e) {
			throw new TaginfoClientException(e);
		}

	}

	/**
	 * Returns the Folksonomy that gathers OSM tags corresponding to the given
	 * key.
	 * @param key
	 * @return the IFolksonomy
	 * @throws TaginfoClientException
	 */
	public IFolksonomy getTagsByKey(String key) throws TaginfoClientException {
		return this.getTagsByKey(key, DEFAULT_LOCALE, DEFAULT_THRESHOLD);
	}

	/**
	 * Returns the Folksonomy that gathers OSM tags corresponding to the given
	 * key for the given locale.
	 * @param key
	 * @return the IFolksonomy
	 * @throws TaginfoClientException
	 */
	public IFolksonomy getTagsByKey(String key, Locale locale)
			throws TaginfoClientException {
		return this.getTagsByKey(key, locale, DEFAULT_THRESHOLD);
	}

	@Override
	public IFolksonomy getFolksonomy() throws FolksonomyWSClientException {
		try {
			return getTagsByKey(DEFAULT_TAG_KEY);
		} catch (TaginfoClientException e) {
			throw new FolksonomyWSClientException(e.getMessage(), e);
		}
	}

}

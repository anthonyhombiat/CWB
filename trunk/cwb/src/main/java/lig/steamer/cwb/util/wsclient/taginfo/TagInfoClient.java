package lig.steamer.cwb.util.wsclient.taginfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
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
import lig.steamer.cwb.util.wsclient.http.HttpMethod;
import lig.steamer.cwb.util.wsclient.http.HttpRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * @author Anthony Hombiat 
 * OSM TagInfo client that provides methods to consume
 * data from the restful Web Service OSM TagInfo API.
 */
public class TagInfoClient {

	private static Logger LOGGER = Logger
			.getLogger(TagInfoClient.class.getName());

	private static final String TAGINFO_RESULT_ARRAY_KEY = "data";
	private static final String TAGINFO_RESULT_VALUE_KEY = "value";
	private static final String TAGINFO_RESULT_DESCRIPTION_KEY = "description";

	private static final String TAGINFO_RESPONSE_FAILURE_MSG = "Failed: HTTP error code ";

	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	private static final double DEFAULT_THRESHOLD = 0;

	public static final String DEFAULT_TAG_KEY = "amenity";
	public static final String OSM_TAG_INFO_URI = "http://osmtaginfo";

	public TagInfoClient() {

	}

	/**
	 * Returns the Folksonomy that gathers OSM tags corresponding to the given key
	 * for the given locale and for the given frequency of occurence threshold.
	 * @param key , the key
	 * @param locale , the locale
	 * @param threshold , the threshold
	 * @return the IFolksonomy
	 */
	public IFolksonomy getTagsByKey(String key, Locale locale, double threshold) {

		LOGGER.log(Level.INFO, "Querying the TagInfo database...");

		try {

			TagInfoRequest tagInfoRequest = new TagInfoRequest(key, locale);
			URL url = tagInfoRequest.getUrl();

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod(HttpMethod.GET.toString());
			connection.setRequestProperty(HttpRequest.ACCEPT.toString(),
					HttpRequest.APPLICATION_JSON.toString());

			if (connection.getResponseCode() != 200) {
				throw new RuntimeException(TAGINFO_RESPONSE_FAILURE_MSG
						+ connection.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(connection.getInputStream())));

			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				stringBuilder.append(line);
			}

			System.out.println(stringBuilder.toString());

			JSONObject obj = (JSONObject) JSONValue.parse(stringBuilder
					.toString());
			JSONArray array = (JSONArray) obj.get(TAGINFO_RESULT_ARRAY_KEY);

			ITagSet tagset = new TagSet();

			for (int i = 0; i < array.size(); i++) {
				JSONObject currentJSONObject = (JSONObject) ((JSONObject) array
						.get(i));

				ILocalizedString tagKey = new LocalizedString(key,
						locale.toString());
				ILocalizedString tagValue = new LocalizedString(
						currentJSONObject.get(TAGINFO_RESULT_VALUE_KEY)
								.toString(), locale.toString());
				ILocalizedString tagDescription = new LocalizedString(
						currentJSONObject.get(TAGINFO_RESULT_DESCRIPTION_KEY)
								.toString(), locale.toString());

				ITag currentTag = new Tag(tagKey, tagValue);
				currentTag.setDescription(tagDescription);

				tagset.addTag(currentTag);
				LOGGER.log(Level.INFO, currentTag.toString());

			}

			br.close();
			connection.disconnect();

			LOGGER.log(Level.INFO,
					"Connection to the TagInfo database is close.");

			return new Folksonomy(tagset, null, new Source(TagInfoClient.OSM_TAG_INFO_URI), null);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * Returns the Folksonomy that gathers OSM tags corresponding to the given key.
	 * @param key
	 * @return the IFolksonomy
	 */
	public IFolksonomy getTagsByKey(String key) {
		return this.getTagsByKey(key, DEFAULT_LOCALE, DEFAULT_THRESHOLD);
	}

	/**
	 * Returns the Folksonomy that gathers OSM tags corresponding to the given key
	 * for the given locale.
	 * @param key
	 * @return the IFolksonomy
	 */
	public IFolksonomy getTagsByKey(String key, Locale locale) {
		return this.getTagsByKey(key, locale, DEFAULT_THRESHOLD);
	}

}

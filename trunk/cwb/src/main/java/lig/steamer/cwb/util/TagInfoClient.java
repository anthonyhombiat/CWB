package lig.steamer.cwb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import lig.steamer.cwb.http.HttpMethod;
import lig.steamer.cwb.http.HttpRequest;
import lig.steamer.cwb.tagging.model.ILocalizedString;
import lig.steamer.cwb.tagging.model.ITag;
import lig.steamer.cwb.tagging.model.ITagSet;
import lig.steamer.cwb.tagging.model.impl.LocalizedString;
import lig.steamer.cwb.tagging.model.impl.Tag;
import lig.steamer.cwb.tagging.model.impl.TagSet;

import org.apache.http.client.utils.URIBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class TagInfoClient {

	private static Logger LOGGER = Logger
			.getLogger("lig.steamer.cwb.io.taginfo");

	private static String TAGINFO_QUERY_BY_KEY_URL = "http://taginfo.openstreetmap.org/api/4/key/values";
	private static String TAGINFO_QUERY_PARAMETER_LANG = "lang";
	private static String TAGINFO_QUERY_PARAMETER_SORT_BY = "sortname";
	private static String TAGINFO_QUERY_PARAMETER_SORT_ORDER = "sortorder";
	private static String TAGINFO_QUERY_PARAMETER_PAGE_NB = "page";
	private static String TAGINFO_QUERY_PARAMETER_RESULTS_PER_PAGE = "rp";
	private static String TAGINFO_QUERY_PARAMETER_KEY = "key";

	private static String TAGINFO_RESULT_ARRAY_KEY = "data";
	private static String TAGINFO_RESULT_VALUE_KEY = "value";
	private static String TAGINFO_RESULT_DESCRIPTION_KEY = "description";

	private static String TAGINFO_RESPONSE_FAILURE_MSG = "Failed: HTTP error code ";

	private static String DEFAULT_LOCALE = Locale.ENGLISH.toString();
	private static double DEFAULT_THRESHOLD = 0;
	
	public static String OSM_TAG_INFO_URI = "http://osmtaginfo";

	public TagInfoClient() {

	}

	public ITagSet getTagsByKey(String key, String locale, double d) {

		LOGGER.log(Level.INFO, "Querying the TagInfo database...");

		try {

			URIBuilder uriBuilder = new URIBuilder(TAGINFO_QUERY_BY_KEY_URL);
			uriBuilder
					.setParameter(TAGINFO_QUERY_PARAMETER_LANG, locale)
					.setParameter(TAGINFO_QUERY_PARAMETER_SORT_BY,
							"count_all")
					.setParameter(TAGINFO_QUERY_PARAMETER_SORT_ORDER, "desc")
					.setParameter(TAGINFO_QUERY_PARAMETER_PAGE_NB, "1")
					.setParameter(TAGINFO_QUERY_PARAMETER_RESULTS_PER_PAGE,
							"10")
					.setParameter(TAGINFO_QUERY_PARAMETER_KEY, key);

			URL tagInforURL = uriBuilder.build().toURL();
			System.out.println(tagInforURL.toString());
			HttpURLConnection connection = (HttpURLConnection) tagInforURL
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

			ITagSet tagSet = new TagSet();

			for (int i = 0; i < array.size(); i++) {
				JSONObject currentJSONObject = (JSONObject) ((JSONObject) array
						.get(i));

				ILocalizedString tagKey = new LocalizedString(key, locale);
				ILocalizedString tagValue = new LocalizedString(
						currentJSONObject.get(TAGINFO_RESULT_VALUE_KEY)
								.toString(), locale);
				ILocalizedString tagDescription = new LocalizedString(
						currentJSONObject.get(TAGINFO_RESULT_DESCRIPTION_KEY)
								.toString(), locale);

				ITag currentTag = new Tag(tagKey, tagValue);
				currentTag.setDescription(tagDescription);

				tagSet.addTag(currentTag);
				LOGGER.log(Level.INFO, currentTag.toString());

			}

			br.close();
			connection.disconnect();

			LOGGER.log(Level.INFO,
					"Connection to the TagInfo database is close.");

			return tagSet;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return null;

	}

	public ITagSet getTagsByKey(String key) {
		return this.getTagsByKey(key, DEFAULT_LOCALE, DEFAULT_THRESHOLD);
	}

	public ITagSet getTagsByKey(String key, String locale) {
		return this.getTagsByKey(key, locale, DEFAULT_THRESHOLD);
	}
	
	public ITagSet getTagsByKeys(String [] keys, String locale, double threshold){
		ITagSet tagSet = new TagSet();
		for(String key:keys){
			tagSet.mergeTagSet(getTagsByKey(key, locale, threshold));
		}
		return tagSet;
	}
	
	public ITagSet getTagsByKeys(String [] keys, String locale){
		return getTagsByKeys(keys, locale, DEFAULT_THRESHOLD);
	}
	
	public ITagSet getTagsByKeys(String [] keys){
		return getTagsByKeys(keys, DEFAULT_LOCALE, DEFAULT_THRESHOLD);
	}

}
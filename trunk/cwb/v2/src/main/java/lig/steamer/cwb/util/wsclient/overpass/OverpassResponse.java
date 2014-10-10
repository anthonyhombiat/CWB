package lig.steamer.cwb.util.wsclient.overpass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import lig.steamer.cwb.util.wsclient.overpass.exception.OverpassResponseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OverpassResponse {

	private static final String KEY_RESPONSE_ARRAY = "elements";
	private static final String KEY_RESPONSE_TAGS = "tags";
	private static final String KEY_RESPONSE_AMENITY = "amenity";

	private Map<String, String> tags;

	public OverpassResponse(HttpURLConnection connection)
			throws OverpassResponseException {

		tags = new HashMap<String, String>();

		try {

			if (connection.getResponseCode() != 200) {
				throw new OverpassResponseException(new Throwable());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				stringBuilder.append(line);
			}

			// System.out.println(stringBuilder.toString());

			JSONObject obj = new JSONObject(stringBuilder.toString());
			JSONArray data = (JSONArray) obj.get(KEY_RESPONSE_ARRAY);

			for (int i = 0; i < data.length(); i++) {

				JSONObject current = (JSONObject) ((JSONObject) data.get(i));
				JSONObject currentTags = (JSONObject) ((JSONObject) current
						.get(KEY_RESPONSE_TAGS));
				tags.put(currentTags.get(KEY_RESPONSE_AMENITY).toString(), "");
			}

			br.close();

		} catch (IOException | JSONException e) {
			throw new OverpassResponseException(e);
		}

	}

	public Map<String, String> getTags() {
		return tags;
	}

}

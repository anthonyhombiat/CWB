package lig.steamer.cwb.util.wsclient.taginfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import lig.steamer.cwb.util.wsclient.taginfo.exception.TaginfoResponseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TaginfoResponse {

	private static final String KEY_RESPONSE_ARRAY = "data";
	private static final String KEY_RESPONSE_VALUE = "value";
	private static final String KEY_RESPONSE_DESCR = "description";

	private Map<String, String> tags;

	public TaginfoResponse(HttpURLConnection connection)
			throws TaginfoResponseException {

		tags = new HashMap<String, String>();
		
		try {

			if (connection.getResponseCode() != 200) {
				throw new TaginfoResponseException(new Throwable());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				stringBuilder.append(line);
			}

//			System.out.println(stringBuilder.toString());

			JSONObject obj = new JSONObject(stringBuilder.toString());
			JSONArray data = (JSONArray) obj.get(KEY_RESPONSE_ARRAY);

			for (int i = 0; i < data.length(); i++) {
				JSONObject current = (JSONObject) ((JSONObject) data.get(i));

				tags.put(current.get(KEY_RESPONSE_VALUE).toString(),
						current.get(KEY_RESPONSE_DESCR).toString());

			}

			br.close();

		} catch (IOException | JSONException e) {
			throw new TaginfoResponseException(e);
		}

	}

	public Map<String, String> getTags() {
		return tags;
	}

}

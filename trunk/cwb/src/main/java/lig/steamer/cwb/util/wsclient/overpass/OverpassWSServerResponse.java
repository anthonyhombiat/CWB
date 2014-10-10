package lig.steamer.cwb.util.wsclient.overpass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;

import lig.steamer.cwb.util.wsclient.WSNodeFolkso;
import lig.steamer.cwb.util.wsclient.WSServerResponse;
import lig.steamer.cwb.util.wsclient.overpass.exception.OverpassWSServerResponseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OverpassWSServerResponse implements WSServerResponse<WSNodeFolkso> {

	private static final String KEY_RESPONSE_ARRAY = "elements";
	private static final String KEY_RESPONSE_TAGS = "tags";
	private static final String KEY_RESPONSE_AMENITY = "amenity";
	private static final String KEY_RESPONSE_NAME = "name";
	private static final String KEY_RESPONSE_LAT = "lat";
	private static final String KEY_RESPONSE_LON = "lon";

	private final Collection<WSNodeFolkso> nodes = new ArrayList<WSNodeFolkso>();

	public OverpassWSServerResponse(HttpURLConnection connection)
			throws OverpassWSServerResponseException {

		try {

			if (connection.getResponseCode() != 200) {
				throw new OverpassWSServerResponseException(new Throwable());
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

				JSONObject current = data.getJSONObject(i);
				JSONObject currentTags = current
						.getJSONObject(KEY_RESPONSE_TAGS);

				String name = NAME_UNDEFINED;
				try{
					name = currentTags.getString(KEY_RESPONSE_NAME);
				}catch(Exception e){}
				
				WSNodeFolkso node = new WSNodeFolkso(
						current.getDouble(KEY_RESPONSE_LAT),
						current.getDouble(KEY_RESPONSE_LON),
						name);

				node.addTag(currentTags.getString(KEY_RESPONSE_AMENITY));

				nodes.add(node);
			}

			br.close();

		} catch (IOException | JSONException e) {
			throw new OverpassWSServerResponseException(e);
		}

	}

	/**
	 * @return the nodes
	 */
	public Collection<WSNodeFolkso> getNodes() {
		return nodes;
	}

}

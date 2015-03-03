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
import lig.steamer.cwb.util.wsclient.overpass.exception.OverpassWSServerResponseForbiddenTypeException;

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
	private static final String KEY_RESPONSE_ID = "id";
	private static final String KEY_RESPONSE_TYPE = "type";
	private static final String KEY_RESPONSE_CENTER = "center";
	
	private static final String VALUE_RESPONSE_TYPE_NODE = "node";
	private static final String VALUE_RESPONSE_TYPE_WAY = "way";

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
				
				WSNodeFolkso node;
				
				String type = current.getString(KEY_RESPONSE_TYPE);
				switch(type){
					case VALUE_RESPONSE_TYPE_NODE:
						node = new WSNodeFolkso(
								current.getString(KEY_RESPONSE_ID),
								current.getDouble(KEY_RESPONSE_LAT),
								current.getDouble(KEY_RESPONSE_LON),
								name);
						break;
					case VALUE_RESPONSE_TYPE_WAY:
						JSONObject center = current
						.getJSONObject(KEY_RESPONSE_CENTER);
						
						node = new WSNodeFolkso(
								current.getString(KEY_RESPONSE_ID),
								center.getDouble(KEY_RESPONSE_LAT),
								center.getDouble(KEY_RESPONSE_LON),
								name);
								break;
					default: throw new OverpassWSServerResponseForbiddenTypeException();
				}

				node.addTag(currentTags.getString(KEY_RESPONSE_AMENITY));

				nodes.add(node);
			}

			br.close();

		} catch (IOException | JSONException | OverpassWSServerResponseForbiddenTypeException e) {
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

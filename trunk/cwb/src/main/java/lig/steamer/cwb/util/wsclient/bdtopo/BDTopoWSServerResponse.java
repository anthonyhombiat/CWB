package lig.steamer.cwb.util.wsclient.bdtopo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;

import lig.steamer.cwb.util.wsclient.WSNodeNomen;
import lig.steamer.cwb.util.wsclient.WSServerResponse;
import lig.steamer.cwb.util.wsclient.bdtopo.exception.BDTopoWSServerResponseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BDTopoWSServerResponse implements WSServerResponse<WSNodeNomen> {

	private static final String KEY_RESPONSE_ARRAY = "features";
	private static final String KEY_RESPONSE_PROP = "properties";
	private static final String KEY_RESPONSE_TOPO = "toponyme";
	private static final String KEY_RESPONSE_GEOM = "geometry";
	private static final String KEY_RESPONSE_COOR = "coordinates";
	private static final String KEY_RESPONSE_TAG = "nature";
	private static final String KEY_RESPONSE_ID = "id";

	private final Collection<WSNodeNomen> nodes = new ArrayList<WSNodeNomen>();

	public BDTopoWSServerResponse(HttpURLConnection connection)
			throws BDTopoWSServerResponseException {

		try {

			if (connection.getResponseCode() != 200) {
				throw new BDTopoWSServerResponseException(new Throwable());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				stringBuilder.append(line);
			}

			System.out.println(stringBuilder.toString());

			JSONObject obj = new JSONObject(stringBuilder.toString());
			JSONArray data = (JSONArray) obj.get(KEY_RESPONSE_ARRAY);

			for (int i = 0; i < data.length(); i++) {

				JSONObject current = data.getJSONObject(i);

				JSONObject geom = current.getJSONObject(KEY_RESPONSE_GEOM);
				JSONArray coordinate = geom.getJSONArray(KEY_RESPONSE_COOR);

				JSONObject prop = current.getJSONObject(KEY_RESPONSE_PROP);

				WSNodeNomen node = new WSNodeNomen(
						prop.getString(KEY_RESPONSE_ID), coordinate.getDouble(0),
						coordinate.getDouble(1),
						prop.getString(KEY_RESPONSE_TOPO),
						prop.getString(KEY_RESPONSE_TAG));

				nodes.add(node);
			}

			br.close();

		} catch (IOException | JSONException e) {
			throw new BDTopoWSServerResponseException(e);
		}

	}

	/**
	 * @return the nodes
	 */
	public Collection<WSNodeNomen> getNodes() {
		return nodes;
	}

}

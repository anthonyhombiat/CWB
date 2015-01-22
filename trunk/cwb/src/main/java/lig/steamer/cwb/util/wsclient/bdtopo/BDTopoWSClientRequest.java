package lig.steamer.cwb.util.wsclient.bdtopo;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lig.steamer.cwb.model.CWBBBox;
import lig.steamer.cwb.util.wsclient.WSClientRequest;
import lig.steamer.cwb.util.wsclient.bdtopo.exception.BDTopoWSClientURISyntaxException;
import lig.steamer.cwb.util.wsclient.bdtopo.exception.BDTopoWSMalformedURLException;

import org.apache.http.client.utils.URIBuilder;

public class BDTopoWSClientRequest implements WSClientRequest {

	private static final String COLUMN_GEOM = "geom";

	private static final String QUERY_URL = "http://localhost:8080/geoserver/cwb";

	private static final String WMS = "wms";
	private static final String WFS = "wfs";

	private static final String PARAM_SERVICE = "service";
	private static final String PARAM_VERSION = "version";
	private static final String PARAM_REQUEST = "request";
	private static final String PARAM_VALREF = "valueReference";
	private static final String PARAM_LAYERS = "layers";
	private static final String PARAM_TYPENAME = "typeName";
	private static final String PARAM_PROP = "propertyName";
	private static final String PARAM_OUTPUT = "outputFormat";
	private static final String PARAM_FORMAT = "format";
	private static final String PARAM_SRS = "srs";
	private static final String PARAM_BBOX = "bbox";
	private static final String PARAM_CQL = "CQL_FILTER";
	private static final String PARAM_FILTER = "FILTER";

	private static final String VERSION_1 = "1.1.0";
	private static final String VERSION_2 = "2.0.0";

	private static final String LAYERS_AGREGATED = "cwb:bd_topo_za";
	private static final String LAYERS_ALL = "cwb:pai_administratif_militaire,"
			+ "cwb:pai_culture_loisirs," + "cwb:pai_espace_naturel,"
			+ "cwb:pai_gestion_eaux," + "cwb:pai_hydrographie,"
			+ "cwb:pai_industriel_commercial," + "cwb:pai_orographie,"
			+ "cwb:pai_religieux," + "cwb:pai_sante,"
			+ "cwb:pai_science_enseignement," + "cwb:pai_sport,"
			+ "cwb:pai_transport," + "cwb:pai_zone_habitation";

	private static final String SERVICE_WFS = "wfs";
	private static final String SERVICE_WMS = "wms";

	private static final String REQUEST_GETMAP = "GetMap";
	private static final String REQUEST_GETFEATURE = "GetFeature";
	private static final String REQUEST_GETPROPVAL = "GetPropertyValue";

	private static final String DEFAULT_OUTPUT = "application/json";
	private static final String DEFAULT_SRS = "EPSG:4326";

	private URL url;

	public BDTopoWSClientRequest(String property, String value, CWBBBox bbox,
			String outputFormat) throws BDTopoWSMalformedURLException,
			BDTopoWSClientURISyntaxException {

		try {

			URIBuilder uriBuilder = new URIBuilder(QUERY_URL + "/" + WFS);

			uriBuilder.setParameter(PARAM_SERVICE, SERVICE_WFS);
			uriBuilder.setParameter(PARAM_VERSION, VERSION_2);
			uriBuilder.setParameter(PARAM_REQUEST, REQUEST_GETFEATURE);
			uriBuilder.setParameter(PARAM_TYPENAME, LAYERS_ALL);
			uriBuilder.setParameter(PARAM_SRS, DEFAULT_SRS);

			if (value == null) {
				uriBuilder.setParameter(PARAM_PROP, property);
			} else {
				uriBuilder.setParameter(PARAM_FILTER,
						buildOGCFilter(property, value, bbox));
			}

			uriBuilder.setParameter(PARAM_OUTPUT, DEFAULT_OUTPUT);

			url = uriBuilder.build().toURL();

		} catch (URISyntaxException e) {
			throw new BDTopoWSClientURISyntaxException(e);
		} catch (MalformedURLException e) {
			throw new BDTopoWSMalformedURLException(e);
		}

	}

	private static String buildOGCFilter(String property, String value,
			CWBBBox bbox) {
		String filter = "<?xml version='1.0' encoding='UTF-8'?><Filter xmlns:gml='http://www.opengis.net/gml'><And>";

		/** workaround Geoserver WFS request bug with special chars **/

		String ogcPropertyFilter;

		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(value);

		if (m.find()) {
			ogcPropertyFilter = buildOGCPopertyIsLikeFilter(property,
					value.replaceAll("[^a-zA-Z0-9 ]", "*"));
		} else {
			ogcPropertyFilter = buildOGCPopertyIsEqualToFilter(property, value);
		}

		filter += ogcPropertyFilter;

		filter += buildOGCBBOXFilter(bbox);
		filter += "</And></Filter>";
		return filter;
	}

	private static String buildOGCPopertyIsEqualToFilter(String property,
			String value) {
		return "<PropertyIsEqualTo><PropertyName>" + property
				+ "</PropertyName><Literal>" + value
				+ "</Literal></PropertyIsEqualTo>";
	}

	private static String buildOGCPopertyIsLikeFilter(String property,
			String value) {
		return "<PropertyIsLike wildCard='*' singleChar='_' escapeChar='!'><PropertyName>"
				+ property
				+ "</PropertyName><Literal>"
				+ value
				+ "</Literal></PropertyIsLike>";
	}

	private static String buildOGCBBOXFilter(CWBBBox bbox) {
		return "<BBOX><PropertyName>" + COLUMN_GEOM
				+ "</PropertyName><gml:Box srsName='" + DEFAULT_SRS
				+ "'><gml:coordinates>" + bbox.getSouth() + ","
				+ bbox.getWest() + " " + bbox.getNorth() + "," + bbox.getEast()
				+ "</gml:coordinates></gml:Box></BBOX>";
	}

	@Override
	public URL getUrl() {
		return url;
	}

}

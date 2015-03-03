package lig.steamer.cwb.ui.panel;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.model.CWBBuffer;
import lig.steamer.cwb.model.CWBDataSetFolkso;
import lig.steamer.cwb.model.CWBDataSetNomen;
import lig.steamer.cwb.model.CWBInstanceFolkso;
import lig.steamer.cwb.model.CWBInstanceNomen;
import lig.steamer.cwb.model.CWBStudyArea;
import lig.steamer.cwb.model.Layer;
import lig.steamer.cwb.model.LeafletBBox;

import org.vaadin.addon.leaflet.LLayerGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.LeafletLayer;
import org.vaadin.addon.leaflet.control.LLayers;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;

public class CWBMapPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_ZOOM_LEVEL = 13;
	public static final String OSM_LAYER_NAME = "OpenStreetMap";

	private final LeafletLayer baseLayer;

	private final LLayerGroup folksoGroup;
	private final LLayerGroup folksoMarkersGroup;
	private final LLayerGroup folksoBuffersGroup;

	private final LLayerGroup nomenGroup;
	private final LLayerGroup nomenMarkersGroup;
	private final LLayerGroup nomenBuffersGroup;

	private final TextField nbInstancesNomen;
	private final TextField nbInstancesFolkso;
	private final TextField nbIntersections;
	private final TextField geospatialMatchCoeff;

	private final LLayers controls = new LLayers();

	private CWBBuffer buffer;

	private final LMap map = new LMap();

	public CWBMapPanel(CWBStudyArea studyArea, CWBBuffer buffer) {

		super();

		baseLayer = new LOpenStreetMapLayer();

		nomenGroup = new LLayerGroup();
		nomenMarkersGroup = new LLayerGroup();
		nomenBuffersGroup = new LLayerGroup();

		nomenGroup.addComponent(nomenMarkersGroup);
		nomenGroup.addComponent(nomenBuffersGroup);

		folksoGroup = new LLayerGroup();
		folksoMarkersGroup = new LLayerGroup();
		folksoBuffersGroup = new LLayerGroup();

		folksoGroup.addComponent(folksoMarkersGroup);
		folksoGroup.addComponent(folksoBuffersGroup);

		map.addBaseLayer(baseLayer, OSM_LAYER_NAME);

		map.addLayer(nomenGroup);
		map.addLayer(folksoGroup);

		controls.addOverlay(folksoGroup,
				MessageFormat.format(Msg.get("map.layer.folkso.capt"), 0));
		controls.addOverlay(nomenGroup,
				MessageFormat.format(Msg.get("map.layer.nomen.capt"), 0));

		map.addControl(controls);
		map.setAttributionPrefix(Msg.get("map.attribution"));
		map.setZoomLevel(DEFAULT_ZOOM_LEVEL);
		map.setSizeFull();
		map.setWidth("100%");
		map.setHeight("100%");

		// geospatial layout
		final Panel instanceInfoPanel = new Panel();
		final FormLayout instanceInfoLayout = new FormLayout();
		
		nbInstancesNomen = new TextField("Number of nomenclature instances: ");
		nbInstancesNomen.setValue("0");
		nbInstancesNomen.setReadOnly(true);
		
		nbInstancesFolkso = new TextField("Number of folksonomy instances: ");
		nbInstancesFolkso.setValue("0");
		nbInstancesFolkso.setReadOnly(true);
		
		nbIntersections = new TextField("Number of intersections: ");
		nbIntersections.setValue("0");
		nbIntersections.setReadOnly(true);
		
		geospatialMatchCoeff = new TextField(
				"Geospatial intersection coefficient: ");
		geospatialMatchCoeff.setValue("0");
		geospatialMatchCoeff.setReadOnly(true);
		
		instanceInfoLayout.addComponent(nbInstancesNomen);
		instanceInfoLayout.addComponent(nbInstancesFolkso);
		instanceInfoLayout.addComponent(nbIntersections);
		instanceInfoLayout.addComponent(geospatialMatchCoeff);
		instanceInfoPanel.setContent(instanceInfoLayout);
		instanceInfoPanel.setStyleName("geospatial-coeff-panel");

		final AbsoluteLayout layout = new AbsoluteLayout();
		layout.addComponent(map);
		layout.addComponent(instanceInfoPanel,
				"bottom: 20px; right: 6px; left: 6px");
		layout.setSizeFull();

		setBuffer(buffer);
		setStudyArea(studyArea);

		setContent(layout);
		setSizeFull();
	}

	public boolean addMarkersFolkso(CWBDataSetFolkso dataset) {
		LLayerGroup folksoMarkersGroupCopy = folksoMarkersGroup;
		controls.removeLayer(folksoGroup);
		for (CWBInstanceFolkso instance : dataset.getInstances()) {
			addMarkerFolkso(instance);
		}
		controls.addOverlay(folksoGroup,
				MessageFormat.format(Msg.get("map.layer.folkso.capt"), dataset
						.getInstances().size()));

		return folksoMarkersGroupCopy != folksoBuffersGroup;
	}

	public boolean addMarkerFolkso(CWBInstanceFolkso instance) {

		LMarker marker = new LMarker(instance.getPoint());
		marker.setId(String.valueOf(instance.getId()));
		marker.setPopup(instance.getLabel());
		marker.setIconAnchor(new Point(24, 46));
		marker.setIconSize(new Point(48, 48));

		try {
			URL iconUrl = this.getClass().getResource(
					"/lig/steamer/cwb/icon/marker-48x48-"
							+ Layer.FOLKSO.getColor() + ".png");
			File f = new File(iconUrl.toURI());
			Resource icon = new FileResource(f);
			marker.setIcon(icon);
		} catch (URISyntaxException e) {
			return false;
		}

		marker.setCaption(instance.getLabel());
		folksoMarkersGroup.addComponent(marker);

		return true;
	}

	public boolean addMarkersNomen(CWBDataSetNomen dataset) {
		LLayerGroup nomenMarkersGroupCopy = nomenMarkersGroup;
		controls.removeLayer(nomenGroup);
		for (CWBInstanceNomen instance : dataset.getInstances()) {
			addMarkerNomen(instance);
		}
		controls.addOverlay(nomenGroup, MessageFormat.format(
				Msg.get("map.layer.nomen.capt"), dataset.getInstances().size()));
		return nomenMarkersGroupCopy != nomenBuffersGroup;
	}

	public boolean addMarkerNomen(CWBInstanceNomen instance) {

		LMarker marker = new LMarker(instance.getPoint());
		marker.setId(String.valueOf(instance.getId()));
		marker.setPopup(instance.getLabel());
		marker.setIconAnchor(new Point(24, 46));
		marker.setIconSize(new Point(48, 48));

		try {
			URL iconUrl = this.getClass().getResource(
					"/lig/steamer/cwb/icon/marker-48x48-"
							+ Layer.NOMEN.getColor() + ".png");
			File f = new File(iconUrl.toURI());
			Resource icon = new FileResource(f);
			marker.setIcon(icon);
		} catch (URISyntaxException e) {
			return false;
		}

		marker.setCaption(instance.getLabel());
		nomenMarkersGroup.addComponent(marker);

		return true;
	}

	public void drawNomenBufferPolygons() {

		nomenBuffersGroup.removeAllComponents();

		Iterator<Component> nomenMarkers = nomenMarkersGroup.iterator();
		while (nomenMarkers.hasNext()) {

			LMarker nomenMarker = (LMarker) nomenMarkers.next();

			LPolygon polygon = getBufferPolygonFromMarker(nomenMarker,
					buffer.getSize(), Layer.NOMEN.getColorHexa(),
					buffer.getFillOpacity());

			nomenBuffersGroup.addComponent(polygon);
		}

		nomenBuffersGroup.setVisible(buffer.isVisible());

	}

	public void drawFolksoBufferPolygons() {

		folksoBuffersGroup.removeAllComponents();

		Iterator<Component> folksoMarkers = folksoMarkersGroup.iterator();
		while (folksoMarkers.hasNext()) {

			LMarker folksoMarker = (LMarker) folksoMarkers.next();

			LPolygon polygon = getBufferPolygonFromMarker(folksoMarker,
					buffer.getSize(), Layer.FOLKSO.getColorHexa(),
					buffer.getFillOpacity());
			folksoBuffersGroup.addComponent(polygon);
		}

		folksoBuffersGroup.setVisible(buffer.isVisible());
	}

	public void removeAllFolksoMarkers() {
		folksoMarkersGroup.removeAllComponents();
		folksoBuffersGroup.removeAllComponents();
	}

	public void removeAllNomenMarkers() {
		nomenMarkersGroup.removeAllComponents();
		nomenBuffersGroup.removeAllComponents();
	}

	public static LPolygon getBufferPolygonFromMarker(LMarker marker,
			double bufferSize, String color, double opacity) {

		Geometry geom = marker.getGeometry().buffer(
				MetersToDecimalDegrees(bufferSize, marker.getPoint().getLat()));

		Coordinate[] coordinates = geom.getCoordinates();
		GeometryFactory factory = geom.getFactory();

		LinearRing lr = new LinearRing(factory.getCoordinateSequenceFactory()
				.create(coordinates), factory);

		LPolygon polygon = new LPolygon(lr);
		polygon.setId(marker.getId());

		polygon.setColor(color);
		polygon.setOpacity(opacity);
		polygon.setFillOpacity(opacity);

		return polygon;
	}

	public LMarker getMarkerFromBuffer(LPolygon buffer) {

		Iterator<Component> nomenMarkers = nomenMarkersGroup.iterator();

		while (nomenMarkers.hasNext()) {
			LMarker nomenMarker = (LMarker) nomenMarkers.next();
			if (nomenMarker.getId() == buffer.getId()) {
				return nomenMarker;
			}
		}

		Iterator<Component> folksoMarkers = folksoMarkersGroup.iterator();

		while (folksoMarkers.hasNext()) {
			LMarker folksoMarker = (LMarker) folksoMarkers.next();
			if (folksoMarker.getId() == buffer.getId()) {
				return folksoMarker;
			}
		}

		return null;
	}

	public LPolygon getBufferFromMarker(LMarker marker) {

		Iterator<Component> nomenBuffers = nomenBuffersGroup.iterator();

		while (nomenBuffers.hasNext()) {
			LPolygon nomenBuffer = (LPolygon) nomenBuffers.next();
			if (nomenBuffer.getId() == marker.getId()) {
				return nomenBuffer;
			}
		}

		Iterator<Component> folksoBuffers = folksoBuffersGroup.iterator();

		while (folksoBuffers.hasNext()) {
			LPolygon folksoBuffer = (LPolygon) folksoBuffers.next();
			if (folksoBuffer.getId() == marker.getId()) {
				return folksoBuffer;
			}
		}

		return null;
	}

	private static double MetersToDecimalDegrees(double meters, double latitude) {
		return meters / (111.32 * 1000 * Math.cos(latitude * (Math.PI / 180)));
	}

	public void setStudyArea(CWBStudyArea studyArea) {
		LeafletBBox bbox = new LeafletBBox(studyArea.getBBox());
		map.setCenter(bbox.getBounds());
	}
	
	public void updateInstanceInfoPanel(){
		nbInstancesNomen.setReadOnly(false);
		nbInstancesNomen.setValue("" + nomenMarkersGroup.getComponentCount());
		nbInstancesNomen.setReadOnly(true);
		
		nbInstancesFolkso.setReadOnly(false);
		nbInstancesFolkso.setValue("" + folksoMarkersGroup.getComponentCount());
		nbInstancesFolkso.setReadOnly(true);
		
		nbIntersections.setReadOnly(false);
		nbIntersections.setValue(String.valueOf(getIntersectedInstances().size()));
		nbIntersections.setReadOnly(true);
		
		geospatialMatchCoeff.setReadOnly(false);
		geospatialMatchCoeff.setValue(String.valueOf(getInstanceMatchCoeff()));
		geospatialMatchCoeff.setReadOnly(true);
	}

	public double getInstanceMatchCoeff() {

		if (nomenBuffersGroup.getComponentCount() > 0
				&& folksoBuffersGroup.getComponentCount() > 0) {

			int maxIntersections = nomenBuffersGroup.getComponentCount()
					* folksoBuffersGroup.getComponentCount();
			int actualIntersection = getIntersectedInstances().size();

			return ((double) actualIntersection) / ((double) maxIntersections);

		} 

		return (double)0;

	}

	public HashMap<LMarker, LMarker> getIntersectedInstances() {

		HashMap<LMarker, LMarker> res = new HashMap<LMarker, LMarker>();

		if (nomenBuffersGroup.getComponentCount() > 0
				&& folksoBuffersGroup.getComponentCount() > 0) {

			Iterator<Component> bufferNomenIterator = nomenBuffersGroup
					.iterator();
			while (bufferNomenIterator.hasNext()) {

				LPolygon bufferNomen = (LPolygon) bufferNomenIterator.next();
				Geometry nomenBufferGeom = bufferNomen.getGeometry();

				Iterator<Component> bufferFolksoIterator = folksoBuffersGroup
						.iterator();
				while (bufferFolksoIterator.hasNext()) {

					LPolygon bufferFolkso = (LPolygon) bufferFolksoIterator
							.next();
					Geometry folksoBufferGeom = bufferFolkso.getGeometry();

					if (nomenBufferGeom.intersects(folksoBufferGeom)) {
						res.put(getMarkerFromBuffer(bufferNomen),
								getMarkerFromBuffer(bufferFolkso));
					}

				}
			}
		}

		return res;
	}

	/**
	 * @return the folksoMarkersGroup
	 */
	public LLayerGroup getFolksoMarkersGroup() {
		return folksoMarkersGroup;
	}

	/**
	 * @return the folksoBuffersGroup
	 */
	public LLayerGroup getFolksoBuffersGroup() {
		return folksoBuffersGroup;
	}

	/**
	 * @return the nomenMarkersGroup
	 */
	public LLayerGroup getNomenMarkersGroup() {
		return nomenMarkersGroup;
	}

	/**
	 * @return the nomenBuffersGroup
	 */
	public LLayerGroup getNomenBuffersGroup() {
		return nomenBuffersGroup;
	}

	/**
	 * @return the buffer
	 */
	public CWBBuffer getBuffer() {
		return buffer;
	}

	/**
	 * @param buffer the buffer to set
	 */
	public void setBuffer(CWBBuffer buffer) {
		this.buffer = buffer;
	}

	/**
	 * @return the map
	 */
	public LMap getMap() {
		return map;
	}

}

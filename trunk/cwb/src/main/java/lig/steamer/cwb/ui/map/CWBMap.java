package lig.steamer.cwb.ui.map;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import lig.steamer.cwb.Msg;
import lig.steamer.cwb.Prop;
import lig.steamer.cwb.model.CWBInstanceFolkso;
import lig.steamer.cwb.model.CWBInstanceNomen;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LeafletLayer;
import org.vaadin.addon.leaflet.markercluster.LMarkerClusterGroup;

import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vividsolutions.jts.geom.Coordinate;

public class CWBMap extends LMap {

	private static final long serialVersionUID = 1L;

	public static final Coordinate DEFAULT_CENTER = new Coordinate(45.1841656,
			5.7155425); // Grenoble
	public static final int DEFAULT_ZOOM_LEVEL = 13;
	public static final String OSM_LAYER_NAME = "OpenStreetMap";
	
	private final LeafletLayer baseLayer;

	private final LMarkerClusterGroup folksoCluster = new LMarkerClusterGroup();
	private final LMarkerClusterGroup nomenCluster = new LMarkerClusterGroup();
	
	private final Collection <LMarker> folksoMarkers = new ArrayList<LMarker>();
	private final Collection <LMarker> nomenMarkers = new ArrayList<LMarker>();
	
	public CWBMap() {

		super();
		
		baseLayer = new LOpenStreetMapLayer();
		
		this.setAttributionPrefix(Msg.get("map.attribution"));
		this.addBaseLayer(baseLayer, OSM_LAYER_NAME);
		this.setZoomLevel(DEFAULT_ZOOM_LEVEL);
		this.setCenter(DEFAULT_CENTER.x, DEFAULT_CENTER.y);
		this.setMaxBounds(Prop.DEFAULT_MAP_BBOX.getBounds());
		this.setSizeFull();
		
		System.out.println("NELat: " + getBounds());
		
//		this.addComponent(folksoCluster);
//		this.addComponent(nomenCluster);
	}

	public boolean addMarkerFolkso(CWBInstanceFolkso instance) {

		LMarker marker = new LMarker(instance.getPoint());
		marker.setId(instance.toString());
		marker.setPopup(instance.getLabel());

		try {
			URL iconUrl = this.getClass().getResource(
					"/lig/steamer/cwb/icon/marker-48x48-"
							+ Layer.FOLKSO.getColor() + ".png");
			File f = new File(iconUrl.toURI());
			Resource icon = new FileResource(f);
			marker.setIcon(icon);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return false;
		}

		marker.setCaption(instance.getLabel());
		folksoMarkers.add(marker);
		this.addComponent(marker);

		return true;
	}

	public boolean addMarkerNomen(CWBInstanceNomen instance) {

		LMarker marker = new LMarker(instance.getPoint());
		marker.setId(instance.toString());
		marker.setPopup(instance.getLabel());

		try {
			URL iconUrl = this.getClass().getResource(
					"/lig/steamer/cwb/icon/marker-48x48-"
							+ Layer.NOMEN.getColor() + ".png");
			File f = new File(iconUrl.toURI());
			Resource icon = new FileResource(f);
			marker.setIcon(icon);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return false;
		}
		marker.setCaption(instance.getLabel());
		nomenMarkers.add(marker);
		this.addComponent(marker);

		return true;
	}
	
	public void removeFolksoMarkers(){
		Iterator<Component> it = this.getComponentIterator();
		Collection<Component> toBeDeleted = new ArrayList<Component>();
		while(it.hasNext()){
			Component currentComponent = it.next();
			if(folksoMarkers.contains(currentComponent)){
				toBeDeleted.add(currentComponent);
			}
		}
		for(Component c : toBeDeleted){
			folksoMarkers.remove(c);
			this.removeComponent(c);
		}
	}
	
	public void removeNomenMarkers(){
		Iterator<Component> it = this.getComponentIterator();
		Collection<Component> toBeDeleted = new ArrayList<Component>();
		while(it.hasNext()){
			Component currentComponent = it.next();
			if(nomenMarkers.contains(currentComponent)){
				toBeDeleted.add(currentComponent);
			}
		}
		for(Component c : toBeDeleted){
			nomenMarkers.remove(c);
			this.removeComponent(c);
		}
	}

	/**
	 * @return the cluster for the folksonomy's concepts
	 */
	public LMarkerClusterGroup getFolksoCluster() {
		return folksoCluster;
	}

	/**
	 * @return the cluster for the nomenclature's concepts
	 */
	public LMarkerClusterGroup getNomenCluster() {
		return nomenCluster;
	}

}

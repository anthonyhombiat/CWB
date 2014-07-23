package lig.steamer.cwb.ui.panel;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;

import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vividsolutions.jts.geom.Coordinate;

public class CWBMapPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	public static final Coordinate DEFAULT_CENTER = new Coordinate(45.1841656,5.7155425);
	public static final int DEFAULT_ZOOM_LEVEL = 12;
	public static final String ATTRIBUTION_PREFIX = "Powered by Leaflet with v-leaflet";
	
	private final LMap map;
	
	public CWBMapPanel(){
		
		super();
		
		map = new LMap();
		map.setAttributionPrefix(ATTRIBUTION_PREFIX);
		map.addBaseLayer(new LOpenStreetMapLayer(), "OpenStreetMap");
		map.setZoomLevel(DEFAULT_ZOOM_LEVEL);
		map.setCenter(DEFAULT_CENTER.x, DEFAULT_CENTER.y);
		map.setSizeFull();
		
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.addComponent(map);
		
		this.setContent(layout);
	}
	
	public void clear(){
	
		
		
	}

}

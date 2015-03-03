package lig.steamer.cwb.model;

import org.vaadin.addon.leaflet.shared.Bounds;

public class LeafletBBox extends CWBBBox {

	public LeafletBBox(double south, double west, double north, double east) {
		super(south, west, north, east);
	}

	public LeafletBBox(Bounds bounds) {
		super(bounds.getSouthWestLat(), bounds.getSouthWestLon(), bounds
				.getNorthEastLat(), bounds.getNorthEastLon());
	}

	public LeafletBBox(CWBBBox bbox) {
		super(bbox.getSouth(), bbox.getWest(), bbox.getNorth(), bbox.getEast());
	}

	public static LeafletBBox create(double south, double west, double north,
			double east) {
		return new LeafletBBox(south, west, north, east);
	}

	public Bounds getBounds() {
		return new Bounds(String.valueOf(getWest()) + ","
				+ String.valueOf(getSouth()) + "," + String.valueOf(getEast())
				+ "," + String.valueOf(getNorth()));
	}

}

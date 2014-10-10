package lig.steamer.cwb.model;

import org.vaadin.addon.leaflet.shared.Bounds;

public class LeafletBBox implements CWBBBox {

	private double south;
	private double west;
	private double north;
	private double east;

	public LeafletBBox(double south, double west, double north, double east) {
		this.south = south;
		this.west = west;
		this.north = north;
		this.east = east;
	}

	public LeafletBBox(Bounds bounds) {
		this.south = bounds.getSouthWestLat();
		this.west = bounds.getSouthWestLon();
		this.north = bounds.getNorthEastLat();
		this.east = bounds.getNorthEastLon();
	}

	/**
	 * @return the south
	 */
	public double getSouth() {
		return south;
	}

	/**
	 * @param south the south to set
	 */
	public void setSouth(double south) {
		this.south = south;
	}

	/**
	 * @return the west
	 */
	public double getWest() {
		return west;
	}

	/**
	 * @param west the west to set
	 */
	public void setWest(double west) {
		this.west = west;
	}

	/**
	 * @return the north
	 */
	public double getNorth() {
		return north;
	}

	/**
	 * @param north the north to set
	 */
	public void setNorth(double north) {
		this.north = north;
	}

	/**
	 * @return the east
	 */
	public double getEast() {
		return east;
	}

	/**
	 * @param east the east to set
	 */
	public void setEast(double east) {
		this.east = east;
	}

	public Bounds getBounds() {
		return new Bounds(String.valueOf(this.west) + ","
				+ String.valueOf(this.south) + "," + String.valueOf(this.east)
				+ "," + String.valueOf(this.north));
	}
	
	@Override
	public String toString(){
		return south + "," + west + "," + north + "," + east;
	}

}

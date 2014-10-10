package lig.steamer.cwb.util.wsclient;


public abstract class WSNode {

	private final String name;
	private final double lat;
	private final double lon;
	
	public WSNode(double lat, double lon, String name){
		this.lat = lat;
		this.lon = lon;
		this.name = name;
	}
	
	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * @return the lon
	 */
	public double getLon() {
		return lon;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
}

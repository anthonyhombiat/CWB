package lig.steamer.cwb.util.wsclient;


public abstract class WSNode {

	private final String id;
	private final String name;
	private final double lat;
	private final double lon;
	
	public WSNode(String id, double lat, double lon, String name){
		this.id = id;
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

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
}

package lig.steamer.cwb.util.wsclient;


public class WSNodeNomen extends WSNode {
	
	private final String category;
	
	public WSNodeNomen(double lat, double lon, String name, String category){
		super(lat, lon, name);
		this.category = category;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	
}

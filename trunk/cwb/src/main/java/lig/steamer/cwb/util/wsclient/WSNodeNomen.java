package lig.steamer.cwb.util.wsclient;


public class WSNodeNomen extends WSNode {
	
	private final String category;
	
	public WSNodeNomen(String id, double lat, double lon, String name, String category){
		super(id, lat, lon, name);
		this.category = category;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	
}

package lig.steamer.cwb.util.wsclient;

public enum WSDataModelFolksoProvider {
	
	TAGINFO("OSM Taginfo"),
	OVERPASS("OSM Overpass");
	
	private String name = "";

	WSDataModelFolksoProvider(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}

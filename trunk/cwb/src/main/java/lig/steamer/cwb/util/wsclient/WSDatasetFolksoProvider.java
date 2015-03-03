package lig.steamer.cwb.util.wsclient;

public enum WSDatasetFolksoProvider {
	
	OVERPASS("OSM Overpass");
	
	private String name = "";

	WSDatasetFolksoProvider(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}

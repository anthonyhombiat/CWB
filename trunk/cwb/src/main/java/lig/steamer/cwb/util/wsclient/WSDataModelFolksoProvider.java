package lig.steamer.cwb.util.wsclient;

public enum WSDataModelFolksoProvider {
	
	TAGINFO("OSM Taginfo");
	
	private String name = "";

	WSDataModelFolksoProvider(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}

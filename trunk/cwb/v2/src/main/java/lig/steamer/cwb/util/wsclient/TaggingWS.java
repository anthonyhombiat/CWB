package lig.steamer.cwb.util.wsclient;

public enum TaggingWS {
	
	TAGINFO("OpenStreetMap Taginfo"),
	OVERPASS("OpenStreetMap Overpass");
	
	private String name = "";

	TaggingWS(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}

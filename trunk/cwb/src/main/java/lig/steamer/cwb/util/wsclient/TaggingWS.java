package lig.steamer.cwb.util.wsclient;

public enum TaggingWS {
	
	TAG_INFO("OpenStreetMap TagInfo");
	
	private String name = "";

	TaggingWS(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}

package lig.steamer.cwb.util.wsclient;

public enum TaggingWebService {
	
	TAG_INFO("OpenStreetMap TagInfo");
	
	private String name = "";

	TaggingWebService(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}

package lig.steamer.cwb.util.wsclient;

public enum MappingWebService {

	OPEN_STREET_MAP("OpenStreetMap");
	
	private String name = "";

	MappingWebService(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}

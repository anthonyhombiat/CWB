package lig.steamer.cwb.util.wsclient;

public enum MappingWS {

	OPEN_STREET_MAP("OpenStreetMap");
	
	private String name = "";

	MappingWS(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}

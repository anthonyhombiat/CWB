package lig.steamer.cwb.util.wsclient;

public enum WSDatasetNomenProvider {
	
	BDTOPO("IGN TOPO DB");
	
	private String name = "";

	WSDatasetNomenProvider(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}

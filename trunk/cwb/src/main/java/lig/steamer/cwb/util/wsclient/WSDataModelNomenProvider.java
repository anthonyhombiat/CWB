package lig.steamer.cwb.util.wsclient;

public enum WSDataModelNomenProvider {
	
	BDTOPO("IGN TOPO DB");
	
	private String name = "";

	WSDataModelNomenProvider(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}

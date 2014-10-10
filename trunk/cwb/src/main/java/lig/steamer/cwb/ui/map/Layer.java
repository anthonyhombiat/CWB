package lig.steamer.cwb.ui.map;

public enum Layer {

	FOLKSO("marker-cluster-folkso", "green"), NOMEN("marker-cluster-nomen", "blue");
	
	private final String name;
	private final String color;
	
	private Layer(String name, String color){
		this.name = name;
		this.color = color;
	}
	
	public String toString(){
		return this.name;
	}
	
	public String getColor(){
		return this.color;
	}
	
	public String getName(){
		return this.name;
	}
	
}

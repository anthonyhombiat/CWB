package lig.steamer.cwb.model;

public enum Layer {

	FOLKSO("marker-cluster-folkso", "green", "#9C1"), NOMEN("marker-cluster-nomen", "blue", "#5CE");
	
	private final String name;
	private final String color;
	private final String colorHexa;
	
	private Layer(String name, String color, String colorHexa){
		this.name = name;
		this.color = color;
		this.colorHexa = colorHexa;
	}
	
	public String toString(){
		return this.name;
	}
	
	public String getColor(){
		return this.color;
	}
	
	public String getColorHexa(){
		return this.colorHexa;
	}
	
	public String getName(){
		return this.name;
	}
	
}

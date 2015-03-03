package lig.steamer.cwb.model;

import com.vividsolutions.jts.geom.Coordinate;

public enum CWBStudyArea {
	
	GRENOBLE("Grenoble", 
			new Coordinate(45.188986, 5.724622),
			new CWBBBox(45.154005, 5.678004, 45.214326, 5.753081)),
	
	LYON("Lyon", 
			new Coordinate(45.763524, 4.835893),
			new CWBBBox(45.707486, 4.771849, 45.808425, 4.898393));
	
	private String name = "";
	private CWBBBox bbox;
	private Coordinate coordinate;

	CWBStudyArea(String name, Coordinate coordinate, CWBBBox bbox) {
		this.name = name;
		this.coordinate = coordinate;
		this.bbox = bbox;
	}

	public String toString() {
		return name;
	}
	
	public CWBBBox getBBox(){
		return bbox;
	}
	
	public Coordinate getCoordinate(){
		return coordinate;
	}
}

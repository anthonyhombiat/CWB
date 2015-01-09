package lig.steamer.cwb.model;

import java.util.ArrayList;
import java.util.Collection;

import org.vaadin.addon.leaflet.shared.Point;


public abstract class CWBInstance {

	private Point point;
	private Collection<String> tags = new ArrayList<String>();
	private String label;
	
	public CWBInstance(Point point, String label, Collection<String> tags){
		this.point = point;
		this.label = label;
		this.tags = tags;
	}

	/**
	 * @return the point
	 */
	public Point getPoint() {
		return point;
	}

	/**
	 * @param point the point to set
	 */
	public void setPoint(Point point) {
		this.point = point;
	}

	/**
	 * @return the tags
	 */
	public Collection<String> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(Collection<String> tags) {
		this.tags = tags;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public String toString(){
		return this.getLabel();
	}
	
}

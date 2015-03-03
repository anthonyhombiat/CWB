package lig.steamer.cwb.model;

import java.util.ArrayList;
import java.util.Collection;

import org.vaadin.addon.leaflet.shared.Point;


public abstract class CWBInstance {

	private String id;
	private Point point;
	private Collection<String> tags = new ArrayList<String>();
	private String label;
	
	public CWBInstance(String id, Point point, String label, Collection<String> tags){
		setId(id);
		setPoint(point);
		setLabel(label);
		setTags(tags);
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

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
}

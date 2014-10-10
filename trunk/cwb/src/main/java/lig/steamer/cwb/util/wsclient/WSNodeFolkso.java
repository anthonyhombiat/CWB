package lig.steamer.cwb.util.wsclient;

import java.util.ArrayList;
import java.util.Collection;

public class WSNodeFolkso extends WSNode {

	private Collection<String> tags;
	
	public WSNodeFolkso(double lat, double lon, String name, Collection<String> tags){
		super(lat, lon, name);
		this.tags = tags;
	}
	
	public WSNodeFolkso(double lat, double lon, String name){
		this(lat, lon, name, new ArrayList<String>());
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

	public boolean addTag(String tag){
		if(!tags.contains(tag)){
			tags.add(tag);
			return true;
		}
		return false;
	}
	
	public boolean addTags(Collection<String> tags){
		boolean hasChanged = false;
		for(String tag : tags){
			if(addTag(tag)){
				hasChanged = true;
			}
		}
		return hasChanged;
	}
	
}

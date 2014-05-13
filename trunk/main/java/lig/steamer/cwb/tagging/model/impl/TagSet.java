package lig.steamer.cwb.tagging.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import lig.steamer.cwb.tagging.model.ILocalizedString;
import lig.steamer.cwb.tagging.model.ITag;
import lig.steamer.cwb.tagging.model.ITagSet;

public class TagSet implements ITagSet {
	
	private ILocalizedString description;
	private Collection<ITag> tags;
	private Date lastModificationDate;
	
	public TagSet(){
		tags = new ArrayList<ITag>();
	}

	public ILocalizedString getDescription() {
		return this.description;
	}
	
	public void setDescription(ILocalizedString description) {
		this.description = description;
	}

	public Collection<ITag> getTags() {
		return this.tags;
	}

	public int getTotalNumberOfTags() {
		return tags.size();
	}

	public Collection<ITag> getTagsByKey(String key) {
		Collection <ITag> matchingTags = new ArrayList<ITag>();
		for(ITag tag:this.tags){
			if(tag.getKey().equals(key)){
				matchingTags.add(tag);
			}
		}
		return matchingTags;
	}

	public Date getLastModificationDate() {
		return this.lastModificationDate;
	}

	public int getTagsTotalCooccurence() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getTagsTotalCooccurenceFrequency() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean addTag(ITag tag) {
		if(!this.tags.contains(tag)){
			this.tags.add(tag);
			return true;
		}
		return false;
	}

	public boolean removeTag(ITag tag) {
		if(this.tags.contains(tag)){
			this.tags.remove(tag);
			return true;
		}
		return false;
	}

	public boolean mergeTagSet(ITagSet tagSet) {
		boolean hasChanged = false;
		for(ITag tag: tagSet.getTags()){
			if(this.tags.add(tag)){
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	public boolean addTags(Collection<ITag> tags) {
		return this.tags.addAll(tags);
	}

}

package lig.steamer.cwb.model.tagging.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import lig.steamer.cwb.model.tagging.ILocalizedString;
import lig.steamer.cwb.model.tagging.ITag;
import lig.steamer.cwb.model.tagging.ITagSet;

/**
 * @author Anthony Hombiat
 *
 */
public class TagSet implements ITagSet {
	
	private ILocalizedString description;
	private Collection<ITag> tags;
	private Date lastModificationDate;
	
	public TagSet(){
		tags = new ArrayList<ITag>();
	}

	/**
	 * {@inheritDoc}
	 */
	public ILocalizedString getDescription() {
		return this.description;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setDescription(ILocalizedString description) {
		this.description = description;
	}

	public Collection<ITag> getTags() {
		return this.tags;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getTotalNumberOfTags() {
		return tags.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<ITag> getTagsByKey(String key) {
		Collection <ITag> matchingTags = new ArrayList<ITag>();
		for(ITag tag:this.tags){
			if(tag.getKey().equals(key)){
				matchingTags.add(tag);
			}
		}
		return matchingTags;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getLastModificationDate() {
		return this.lastModificationDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getTagsTotalCooccurence() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getTagsTotalCooccurenceFrequency() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean addTag(ITag tag) {
		if(!this.tags.contains(tag)){
			this.tags.add(tag);
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean removeTag(ITag tag) {
		if(this.tags.contains(tag)){
			this.tags.remove(tag);
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean mergeTagSet(ITagSet tagSet) {
		boolean hasChanged = false;
		for(ITag tag: tagSet.getTags()){
			if(this.tags.add(tag)){
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean addTags(Collection<ITag> tags) {
		return this.tags.addAll(tags);
	}

}

package lig.steamer.cwb.tagging.model;

import java.util.Collection;
import java.util.Date;

/**
 * @author Anthony Hombiat
 * A TagSet is a collection of Tags.
 */
public interface ITagSet {
	
	public ILocalizedString getDescription();
	
	public void setDescription(ILocalizedString description);
	
	public boolean addTag(ITag tag);
	
	public boolean addTags(Collection<ITag> tags);
	
	public boolean removeTag(ITag tag);

	public Collection<ITag> getTags();
	
	public int getTotalNumberOfTags();
	
	public Collection<ITag> getTagsByKey(String key);
	
	public Date getLastModificationDate();
	
	public int getTagsTotalCooccurence();
	
	public int getTagsTotalCooccurenceFrequency();

	public boolean mergeTagSet(ITagSet tagSet);
	
}

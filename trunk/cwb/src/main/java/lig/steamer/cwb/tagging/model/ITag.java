package lig.steamer.cwb.tagging.model;

import java.util.Date;

public interface ITag {

	public ILocalizedString getKey();
	
	public ILocalizedString getValue();
	
	public ILocalizedString getDescription();
	
	public void setDescription(ILocalizedString description);
	
	public Date getCreationDate();
	
	public Date getLastModificationDate();
	
	public Date getLastUsageDate();
	
	public int getNumberOfOccurences();
	
	public int getCooccurence();
	
	public double getCooccurenceFrequency();
	
}

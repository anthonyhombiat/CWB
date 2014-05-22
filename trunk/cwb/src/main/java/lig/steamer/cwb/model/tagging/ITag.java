package lig.steamer.cwb.model.tagging;

import java.util.Date;

/**
 * @author Anthony Hombiat
 * A Tag is key-value pair that describes a Resource.
 */
public interface ITag {

	/**
	 * Returns the key of the Tag.
	 * @return the key
	 */
	public ILocalizedString getKey();
	
	/**
	 * Returns the value of the Tag.
	 * @return the value
	 */
	public ILocalizedString getValue();
	
	/**
	 * Returns the description of the Tag.
	 * @return the description
	 */
	public ILocalizedString getDescription();
	
	/**
	 * Sets the description of the Tag.
	 * @param the description to set
	 */
	public void setDescription(ILocalizedString description);
	
	/**
	 * Returns the creation date of the Tag.
	 * @return the creation date
	 */
	public Date getCreationDate();
	
	/**
	 * Returns the last modification date of the Tag.
	 * @return the last modification date
	 */
	public Date getLastModificationDate();
	
	/**
	 * Returns the last usage date of the Tag.
	 * @return the last usage date
	 */
	public Date getLastUsageDate();
	
	/**
	 * Returns the number of occurences of the Tag.
	 * @return the number of occurences
	 */
	public int getNumberOfOccurences();
	
	/**
	 * Returns the number of cooccurences of the Tag in the TagSet in which it belongs.
	 * @return the last usage date
	 */
	public int getNumberOfCooccurences();
	
	/**
	 * Returns the cooccurence frequency of the Tag in the TagSet in which it belongs.
	 * @return the last usage date
	 */
	public double getCooccurenceFrequency();
	
}

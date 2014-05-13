package lig.steamer.cwb.tagging.model.impl;

import java.util.Date;

import lig.steamer.cwb.tagging.model.ILocalizedString;
import lig.steamer.cwb.tagging.model.ITag;

public class Tag implements ITag {

	private ILocalizedString key;
	private ILocalizedString value;
	private ILocalizedString description;
	private Date creationDate;
	private Date modificationDate;
	private Date lastUsageDate;

	public Tag() {

	}

	public Tag(ILocalizedString key, ILocalizedString value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * @return the key
	 */
	public ILocalizedString getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(ILocalizedString key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public ILocalizedString getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(ILocalizedString value) {
		this.value = value;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the modificationDate
	 */
	public Date getModificationDate() {
		return modificationDate;
	}

	/**
	 * @param modificationDate
	 *            the modificationDate to set
	 */
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	/**
	 * @return the lastUsageDate
	 */
	public Date getLastUsageDate() {
		return lastUsageDate;
	}

	/**
	 * @param lastUsageDate
	 *            the lastUsageDate to set
	 */
	public void setLastUsageDate(Date lastUsageDate) {
		this.lastUsageDate = lastUsageDate;
	}

	public Date getLastModificationDate() {
		return this.modificationDate;
	}

	public int getNumberOfOccurences() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCooccurence() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getCooccurenceFrequency() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @return the description
	 */
	public ILocalizedString getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(ILocalizedString description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "[" + this.getKey().getString() + "@"
				+ this.getKey().getLanguage() + ", "
				+ this.getValue().getString() + "@"
				+ this.getValue().getLanguage() + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ITag) {
			ITag tag = (ITag) o;
			if (tag.getKey() == this.getKey()
					&& tag.getValue() == this.getValue()) {
				return true;
			}
		}
		return false;
	}

}

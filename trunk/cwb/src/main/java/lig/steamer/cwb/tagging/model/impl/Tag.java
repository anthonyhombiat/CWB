package lig.steamer.cwb.tagging.model.impl;

import java.util.Date;

import lig.steamer.cwb.tagging.model.ILocalizedString;
import lig.steamer.cwb.tagging.model.ITag;

/**
 * @author Anthony Hombiat
 * 
 */
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
	 * {@inheritDoc}
	 */
	public ILocalizedString getKey() {
		return key;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setKey(ILocalizedString key) {
		this.key = key;
	}

	/**
	 * {@inheritDoc}
	 */
	public ILocalizedString getValue() {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setValue(ILocalizedString value) {
		this.value = value;
	}
	
	/**
	 * @return the description
	 */
	public ILocalizedString getDescription() {
		return description;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDescription(ILocalizedString description) {
		this.description = description;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getModificationDate() {
		return modificationDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getLastUsageDate() {
		return lastUsageDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLastUsageDate(Date lastUsageDate) {
		this.lastUsageDate = lastUsageDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date getLastModificationDate() {
		return this.modificationDate;
	}

	public int getNumberOfOccurences() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getNumberOfCooccurences() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getCooccurenceFrequency() {
		// TODO Auto-generated method stub
		return 0;
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

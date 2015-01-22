package lig.steamer.cwb.model;

import java.util.Collection;
import java.util.Date;

public abstract class CWBDataSet<T extends CWBInstance> {

	private String title;
	private String description;
	private String creator;
	private Date creationDate;
	private Date lastUpdate;
	private CWBDataProvider dataProvider;
	private CWBStudyArea studyArea;
	private CWBZoning zoning;
	
	public CWBDataSet(){
		
	}

	/**
	 * @return the dataProvider
	 */
	public CWBDataProvider getDataProvider() {
		return dataProvider;
	}

	/**
	 * @param dataProvider the dataProvider to set
	 */
	public void setDataProvider(CWBDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	/**
	 * @return the studyArea
	 */
	public CWBStudyArea getStudyArea() {
		return studyArea;
	}

	/**
	 * @param studyArea the studyArea to set
	 */
	public void setStudyArea(CWBStudyArea studyArea) {
		this.studyArea = studyArea;
	}

	/**
	 * @return the zoning
	 */
	public CWBZoning getZoning() {
		return zoning;
	}

	/**
	 * @param zoning the zoning to set
	 */
	public void setZoning(CWBZoning zoning) {
		this.zoning = zoning;
	}

	/**
	 * @return the instances
	 */
	public abstract Collection<T> getInstances();
	
	public abstract boolean addInstance(T instance);

	public abstract boolean removeInstance(T instance);
	
	public abstract boolean addInstances(Collection<T> instances);
	
	public abstract boolean removeInstances(Collection<T> instances);
	
	public abstract boolean removeAllInstances();
	
	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the lastUpdate
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
}

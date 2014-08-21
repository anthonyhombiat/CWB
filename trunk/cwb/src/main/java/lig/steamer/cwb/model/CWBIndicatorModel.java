package lig.steamer.cwb.model;

import java.io.Serializable;
import java.util.Date;

public class CWBIndicatorModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String title;
	private String description;
	private CWBDataModel dataModel;
	private String creator;
	private Date creationDate;
	private Date LastUpdate;

	public CWBIndicatorModel() {

	}

	public CWBIndicatorModel(String title) {
		this.title = title;
	}

	public CWBIndicatorMeasureSet calculate() {
		return null;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param name the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the dataModel
	 */
	public CWBDataModel getDataModel() {
		return dataModel;
	}

	/**
	 * @param dataModel the dataModel to set
	 */
	public void setDataModel(CWBDataModel dataModel) {
		this.dataModel = dataModel;
	}

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
		return LastUpdate;
	}

	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(Date lastUpdate) {
		LastUpdate = lastUpdate;
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

package lig.steamer.cwb.model;

import java.util.Date;

import lig.steamer.cwb.io.visitor.CWBVisitable;
import lig.steamer.cwb.io.visitor.CWBVisitor;

public class CWBIndicatorModel implements CWBVisitable {

	private String name;
	private CWBDataModel dataModel;
	private Date creationDate;
	private Date LastUpdate;

	public CWBIndicatorModel() {

	}

	public CWBIndicatorModel(String name) {
		this.name = name;
	}

	public CWBIndicatorMeasureSet calculate() {
		return null;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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

	@Override
	public void acceptCWBVisitor(CWBVisitor visitor) {
		visitor.visitIndicatorModel(this);
	}

}

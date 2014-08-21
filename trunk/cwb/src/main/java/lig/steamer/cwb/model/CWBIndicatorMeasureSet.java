package lig.steamer.cwb.model;

import java.util.Collection;
import java.util.Date;

public class CWBIndicatorMeasureSet {

	private String title;
	private String description;
	private CWBIndicatorModel indicatorModel;
	private CWBDataSet dataset;
	private String creator;
	private Date creationDate;
	private Collection<CWBIndicatorMeasure> measures;
	private CWBStudyArea studyArea;
	private CWBZoning zoning;
	
	public CWBIndicatorMeasureSet(){
		
	}

	/**
	 * @return the indicatorModel
	 */
	public CWBIndicatorModel getIndicatorModel() {
		return indicatorModel;
	}

	/**
	 * @param indicatorModel the indicatorModel to set
	 */
	public void setIndicatorModel(CWBIndicatorModel indicatorModel) {
		this.indicatorModel = indicatorModel;
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
	 * @return the measures
	 */
	public Collection<CWBIndicatorMeasure> getMeasures() {
		return measures;
	}

	/**
	 * @param measures the measures to set
	 */
	public void setMeasures(Collection<CWBIndicatorMeasure> measures) {
		this.measures = measures;
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
	 * @return the dataset
	 */
	public CWBDataSet getDataset() {
		return dataset;
	}

	/**
	 * @param dataset the dataset to set
	 */
	public void setDataset(CWBDataSet dataset) {
		this.dataset = dataset;
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
	
}

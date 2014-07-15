package lig.steamer.cwb.model;

import java.util.Collection;
import java.util.Date;

import lig.steamer.cwb.io.visitor.CWBVisitable;
import lig.steamer.cwb.io.visitor.CWBVisitor;

public class CWBIndicatorMeasureSet implements CWBVisitable {

	private CWBIndicatorModel indicatorModel;
	private Date creationDate;
	private Collection<CWBIndicatorMeasure> measures;
	
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

	@Override
	public void acceptCWBVisitor(CWBVisitor visitor) {
		visitor.visitIndicatorMeasureSet(this);
	}
	
}

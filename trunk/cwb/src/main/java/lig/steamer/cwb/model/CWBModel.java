package lig.steamer.cwb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import lig.steamer.cwb.io.visitor.CWBVisitable;
import lig.steamer.cwb.io.visitor.CWBVisitor;

public class CWBModel implements Serializable, CWBVisitable {

	private static final long serialVersionUID = 1L;

	private Collection<CWBDataModel> dataModels;
	private Collection<CWBDataSet> dataSets;
	private Collection<CWBIndicatorModel> indicatorModels;
	private Collection<CWBIndicatorMeasureSet> indicatorMeasureSets;

	public CWBModel() {
		dataModels = new ArrayList<CWBDataModel>();
		dataSets = new ArrayList<CWBDataSet>();
		indicatorModels = new ArrayList<CWBIndicatorModel>();
		indicatorMeasureSets = new ArrayList<CWBIndicatorMeasureSet>();
	}

	/**
	 * @return the dataModels
	 */
	public Collection<CWBDataModel> getDataModels() {
		return dataModels;
	}

	/**
	 * @param dataModels the dataModels to set
	 */
	public void setDataModels(Collection<CWBDataModel> dataModels) {
		this.dataModels = dataModels;
	}

	public boolean addDataModel(CWBDataModel dataModel) {
		if (!dataModels.contains(dataModel)) {
			dataModels.add(dataModel);
			return true;
		}
		return false;
	}

	public boolean removeDataModel(CWBDataModel dataModel) {
		if (dataModels.contains(dataModel)) {
			dataModels.remove(dataModel);
			return true;
		}
		return false;
	}

	public boolean addDataModels(Collection<CWBDataModel> dataModels) {
		boolean hasChanged = false;
		for (CWBDataModel dataModel : dataModels) {
			if (addDataModel(dataModel)) {
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	public boolean removeDataModels(Collection<CWBDataModel> dataModels) {
		boolean hasChanged = false;
		for (CWBDataModel dataModel : dataModels) {
			if (removeDataModel(dataModel)) {
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	/**
	 * @return the dataSets
	 */
	public Collection<CWBDataSet> getDataSets() {
		return dataSets;
	}

	/**
	 * @param dataSets the dataSets to set
	 */
	public void setDataSets(Collection<CWBDataSet> dataSets) {
		this.dataSets = dataSets;
	}

	public boolean addDataSet(CWBDataSet dataSet) {
		if (!dataSets.contains(dataSet)) {
			dataSets.add(dataSet);
			return true;
		}
		return false;
	}

	public boolean removeDataSet(CWBDataSet dataSet) {
		if (dataSets.contains(dataSet)) {
			dataSets.remove(dataSet);
			return true;
		}
		return false;
	}

	public boolean addDataSets(Collection<CWBDataSet> dataSets) {
		boolean hasChanged = false;
		for (CWBDataSet dataSet : dataSets) {
			if (addDataSet(dataSet)) {
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	public boolean removeDataSets(Collection<CWBDataSet> dataSets) {
		boolean hasChanged = false;
		for (CWBDataSet dataSet : dataSets) {
			if (removeDataSet(dataSet)) {
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	/**
	 * @return the indicatorModels
	 */
	public Collection<CWBIndicatorModel> getIndicatorModels() {
		return indicatorModels;
	}

	/**
	 * @param indicatorModels the indicatorModels to set
	 */
	public void setIndicatorModels(Collection<CWBIndicatorModel> indicatorModels) {
		this.indicatorModels = indicatorModels;
	}

	public boolean addIndicatorModel(CWBIndicatorModel indicatorModel) {
		if (!indicatorModels.contains(indicatorModel)) {
			indicatorModels.add(indicatorModel);
			return true;
		}
		return false;
	}

	public boolean removeIndicatorModel(CWBIndicatorModel indicatorModel) {
		if (indicatorModels.contains(indicatorModel)) {
			indicatorModels.remove(indicatorModel);
			return true;
		}
		return false;
	}

	public boolean addIndicatorModels(
			Collection<CWBIndicatorModel> indicatorModels) {
		boolean hasChanged = false;
		for (CWBIndicatorModel indicatorModel : indicatorModels) {
			if (addIndicatorModel(indicatorModel)) {
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	public boolean removeIndicatorModels(
			Collection<CWBIndicatorModel> indicatorModels) {
		boolean hasChanged = false;
		for (CWBIndicatorModel indicatorModel : indicatorModels) {
			if (removeIndicatorModel(indicatorModel)) {
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	/**
	 * @return the indicatorMeasureSets
	 */
	public Collection<CWBIndicatorMeasureSet> getIndicatorMeasureSets() {
		return indicatorMeasureSets;
	}

	/**
	 * @param indicatorMeasureSets the indicatorMeasureSets to set
	 */
	public void setIndicatorMeasureSets(
			Collection<CWBIndicatorMeasureSet> indicatorMeasureSets) {
		this.indicatorMeasureSets = indicatorMeasureSets;
	}

	public boolean addIndicatorMeasureSet(
			CWBIndicatorMeasureSet indicatorMeasureSet) {
		if (!indicatorMeasureSets.contains(indicatorMeasureSet)) {
			indicatorMeasureSets.add(indicatorMeasureSet);
			return true;
		}
		return false;
	}

	public boolean removeIndicatorMeasureSet(
			CWBIndicatorMeasureSet indicatorMeasureSet) {
		if (indicatorMeasureSets.contains(indicatorMeasureSet)) {
			indicatorMeasureSets.remove(indicatorMeasureSet);
			return true;
		}
		return false;
	}

	public boolean addIndicatorMeasureSets(
			Collection<CWBIndicatorMeasureSet> indicatorMeasureSets) {
		boolean hasChanged = false;
		for (CWBIndicatorMeasureSet indicatorMeasureSet : indicatorMeasureSets) {
			if (addIndicatorMeasureSet(indicatorMeasureSet)) {
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	public boolean removeIndicatorMeasureSets(
			Collection<CWBIndicatorMeasureSet> indicatorMeasureSets) {
		boolean hasChanged = false;
		for (CWBIndicatorMeasureSet indicatorMeasureSet : indicatorMeasureSets) {
			if (removeIndicatorMeasureSet(indicatorMeasureSet)) {
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	@Override
	public void acceptCWBVisitor(CWBVisitor visitor) {
		visitor.visitModel(this);
	}

	public boolean isEmpty() {
		return dataModels.isEmpty() && dataSets.isEmpty()
				&& indicatorModels.isEmpty() && indicatorMeasureSets.isEmpty();
	}

}

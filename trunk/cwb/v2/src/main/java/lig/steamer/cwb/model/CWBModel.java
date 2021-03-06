package lig.steamer.cwb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class CWBModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Collection<CWBDataModel> dataModels;
	private Collection<CWBDataSet> dataSets;
	private Collection<CWBIndicatorModel> indicatorModels;
	private Collection<CWBIndicatorMeasureSet> indicatorMeasureSets;

	private CWBDataModel sourceDataModel;
	private CWBDataModel targetDataModel;

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

	public boolean isEmpty() {
		return dataModels.isEmpty() && dataSets.isEmpty()
				&& indicatorModels.isEmpty() && indicatorMeasureSets.isEmpty();
	}

	public Collection<CWBDataModelFolkso> getDataModelsFolkso() {

		Collection<CWBDataModelFolkso> dataModelsFolkso = new ArrayList<CWBDataModelFolkso>();

		for (CWBDataModel dataModel : dataModels) {
			if (dataModel instanceof CWBDataModelFolkso) {
				dataModelsFolkso.add((CWBDataModelFolkso) dataModel);
			}
		}

		return dataModelsFolkso;
	}

	public Collection<CWBDataModelNomen> getDataModelsNomen() {

		Collection<CWBDataModelNomen> dataModelsNomen = new ArrayList<CWBDataModelNomen>();

		for (CWBDataModel dataModel : dataModels) {
			if (dataModel instanceof CWBDataModelNomen) {
				dataModelsNomen.add((CWBDataModelNomen) dataModel);
			}
		}

		return dataModelsNomen;
	}

	public Collection<CWBDataModelMatched> getDataModelsMatched() {

		Collection<CWBDataModelMatched> dataModelsMatched = new ArrayList<CWBDataModelMatched>();

		for (CWBDataModel dataModel : dataModels) {
			if (dataModel instanceof CWBDataModelMatched) {
				dataModelsMatched.add((CWBDataModelMatched) dataModel);
			}
		}

		return dataModelsMatched;
	}

	/**
	 * @return the sourceDataModel
	 */
	public CWBDataModel getSourceDataModel() {
		return sourceDataModel;
	}

	/**
	 * @param sourceDataModel the sourceDataModel to set
	 */
	public void setSourceDataModel(CWBDataModel sourceDataModel) {
		this.sourceDataModel = sourceDataModel;
	}

	/**
	 * @return the targetDataModel
	 */
	public CWBDataModel getTargetDataModel() {
		return targetDataModel;
	}

	/**
	 * @param targetDataModel the targetDataModel to set
	 */
	public void setTargetDataModel(CWBDataModel targetDataModel) {
		this.targetDataModel = targetDataModel;
	}

}

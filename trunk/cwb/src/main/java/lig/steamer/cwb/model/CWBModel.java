package lig.steamer.cwb.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Observable;

public class CWBModel extends Observable implements Serializable {

	private static final long serialVersionUID = 1L;

	private CWBDataModelNomen nomenclature;
	private CWBDataModelFolkso folksonomy;

	private CWBAlignment alignment;
	
	private CWBDataSetNomen datasetNomen;
	private CWBDataSetFolkso datasetFolkso;

	private CWBBBox bbox;

	private Boolean isReadyForMatching;

	public CWBModel() {
		datasetNomen = new CWBDataSetNomen();
		datasetFolkso = new CWBDataSetFolkso();
		isReadyForMatching = false;
	}

	/**
	 * @return the folksonomy
	 */
	public CWBDataModelFolkso getFolksonomy() {
		return folksonomy;
	}

	/**
	 * @param folksonomy the folksonomy to set
	 */
	public void setFolksonomy(CWBDataModelFolkso folksonomy) {
		this.folksonomy = folksonomy;
		setChanged();
		notifyObservers(folksonomy);
		if (this.folksonomy != null && this.nomenclature != null) {
			setReadyForMatching(true);
		}
	}

	/**
	 * @return the nomenclature
	 */
	public CWBDataModelNomen getNomenclature() {
		return nomenclature;
	}

	/**
	 * @param nomenclature the nomenclature to set
	 */
	public void setNomenclature(CWBDataModelNomen nomenclature) {
		this.nomenclature = nomenclature;
		setChanged();
		notifyObservers(nomenclature);
		if (this.folksonomy != null && this.nomenclature != null) {
			setReadyForMatching(true);
		}
	}

	public boolean isEmpty() {
		return folksonomy == null && nomenclature == null && alignment == null;
	}

	/**
	 * @return the instancesFolkso
	 */
	public Collection<CWBInstanceFolkso> getInstancesFolkso() {
		return datasetFolkso.getInstances();
	}

	/**
	 * @param instancesFolkso the instancesFolkso to set
	 */
	public boolean addInstancesFolkso(
			Collection<CWBInstanceFolkso> instancesFolkso) {
		boolean hasChanged = datasetFolkso.addInstances(instancesFolkso);
		setChanged();
		notifyObservers(datasetFolkso);
		return hasChanged;
	}

	public void removeAllInstancesFolkso() {
		datasetFolkso.removeAllInstances();
		setChanged();
		notifyObservers(datasetFolkso);
	}

	/**
	 * @return the instancesNomen
	 */
	public Collection<CWBInstanceNomen> getInstancesNomen() {
		return datasetNomen.getInstances();
	}

	/**
	 * @param instancesNomen the instancesNomen to set
	 */
	public boolean addInstancesNomen(Collection<CWBInstanceNomen> instancesNomen) {
		boolean hasChanged = datasetNomen.addInstances(instancesNomen);
		setChanged();
		notifyObservers(datasetNomen);
		return hasChanged;
	}

	public void removeAllInstancesNomen() {
		datasetNomen.removeAllInstances();
		setChanged();
		notifyObservers(datasetNomen);
	}

	/**
	 * @return the bbox
	 */
	public CWBBBox getBBox() {
		return bbox;
	}

	/**
	 * @param bbox the bbox to set
	 */
	public void setBBox(CWBBBox bbox) {
		this.bbox = bbox;
	}

	/**
	 * @return the isReadyForMatching
	 */
	public Boolean isReadyForMatching() {
		return isReadyForMatching;
	}

	/**
	 * @param isReadyForMatching the isReadyForMatching to set
	 */
	public void setReadyForMatching(Boolean isReadyForMatching) {
		setChanged();
		notifyObservers(isReadyForMatching);
		this.isReadyForMatching = isReadyForMatching;
	}

	/**
	 * @return the alignment
	 */
	public CWBAlignment getAlignment() {
		return alignment;
	}

	/**
	 * @param alignment the alignment to set
	 */
	public void setAlignment(CWBAlignment alignment) {
		this.alignment = alignment;
		setChanged();
		notifyObservers(alignment);
	}

	public Collection<CWBEquivalence> getSelectedEquivalences() {
		if (alignment == null) {
			return new HashSet<CWBEquivalence>();
		}
		return alignment.getSelectedEquivalences();
	}

	public boolean addEquivalences(Collection<CWBEquivalence> equivalences) {
		if (alignment != null) {
			boolean hasChanged = alignment.addEquivalences(equivalences);
			setChanged();
			notifyObservers(alignment.getEquivalences());
			return hasChanged;
		}
		return false;
	}

	public boolean removeEquivalences(Collection<CWBEquivalence> equivalences) {
		if (alignment != null) {
			boolean hasChanged = alignment.removeEquivalences(equivalences);
			setChanged();
			notifyObservers(alignment.getEquivalences());
			return hasChanged;
		}
		return false;
	}

	public boolean removeAllSelectedEquivalences() {
		if (alignment != null) {
			return alignment.removeAllSelectedEquivalences();
		}
		return false;
	}

	public boolean addSelectedEquivalence(CWBEquivalence equivalence) {
		if (alignment != null) {
			return alignment.selectEquivalence(equivalence);
		}
		return false;
	}

	public boolean removeSelectedEquivalence(CWBEquivalence equivalence) {
		if (alignment != null) {
			return alignment.unselectEquivalence(equivalence);
		}
		return false;
	}

}

package lig.steamer.cwb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Observable;

public class CWBModel extends Observable implements Serializable {

	private static final long serialVersionUID = 1L;

	private CWBDataModelFolkso folksonomy;
	private CWBDataModelNomen nomenclature;

	private CWBAlignment alignment;

	private Collection<CWBInstanceFolkso> instancesFolkso;
	private Collection<CWBInstanceNomen> instancesNomen;

	private CWBBBox bbox;

	private Boolean isReadyForMatching;

	public CWBModel() {
		instancesFolkso = new ArrayList<CWBInstanceFolkso>();
		instancesNomen = new ArrayList<CWBInstanceNomen>();
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
		return instancesFolkso;
	}

	private boolean addInstanceFolkso(CWBInstanceFolkso instanceFolkso) {
		System.out.println("adding folkso instance " + instanceFolkso.toString());
		if (!instancesFolkso.contains(instanceFolkso)) {
			instancesFolkso.add(instanceFolkso);
			return true;
		}
		return false;
	}

	/**
	 * @param instancesFolkso the instancesFolkso to set
	 */
	public boolean addInstancesFolkso(
			Collection<CWBInstanceFolkso> instancesFolkso) {
		boolean hasChanged = false;
		for (CWBInstanceFolkso instanceFolkso : instancesFolkso) {
			if (addInstanceFolkso(instanceFolkso)) {
				hasChanged = true;
			}
		}
		setChanged();
		notifyObservers(instancesFolkso);
		return hasChanged;
	}

	public void removeAllInstancesFolkso() {
		this.instancesFolkso = new ArrayList<CWBInstanceFolkso>();
		setChanged();
		notifyObservers(this.instancesFolkso);
	}

	/**
	 * @return the instancesNomen
	 */
	public Collection<CWBInstanceNomen> getInstancesNomen() {
		return instancesNomen;
	}

	private boolean addInstanceNomen(CWBInstanceNomen instanceNomen) {
		System.out.println("adding nomen instance " + instanceNomen.toString());
		if (!instancesNomen.contains(instanceNomen)) {
			instancesNomen.add(instanceNomen);
			return true;
		}
		return false;
	}

	/**
	 * @param instancesNomen the instancesNomen to set
	 */
	public boolean addInstancesNomen(Collection<CWBInstanceNomen> instancesNomen) {
		boolean hasChanged = false;
		for (CWBInstanceNomen instanceNomen : instancesNomen) {
			if (addInstanceNomen(instanceNomen)) {
				hasChanged = true;
			}
		}
		setChanged();
		notifyObservers(instancesNomen);
		return hasChanged;
	}

	public void removeAllInstancesNomen() {
		this.instancesNomen = new ArrayList<CWBInstanceNomen>();
		setChanged();
		notifyObservers(instancesNomen);
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

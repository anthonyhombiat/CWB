package lig.steamer.cwb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;

public class CWBModel extends Observable implements Serializable {

	private static final long serialVersionUID = 1L;

	private CWBDataModelFolkso folksonomy;
	private CWBDataModelNomen nomenclature;
	
	private Collection<CWBEquivalence> equivalences;
	private Collection<CWBEquivalence> selectedEquivalences;
	
	private Collection<CWBInstanceFolkso> instancesFolkso;
	private Collection<CWBInstanceNomen> instancesNomen;
	
	private CWBBBox bbox;

	private Boolean isReadyForMatching;
	
	public CWBModel() {
		equivalences = new ArrayList<CWBEquivalence>();
		selectedEquivalences = new ArrayList<CWBEquivalence>();
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
		if(this.folksonomy != null && this.nomenclature != null){
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
		if(this.folksonomy != null && this.nomenclature != null){
			setReadyForMatching(true);
		}
	}
	
	/**
	 * @return the equivalences
	 */
	public Collection<CWBEquivalence> getEquivalences() {
		return equivalences;
	}
	
	private boolean addEquivalence(CWBEquivalence equivalence) {
		if (!equivalences.contains(equivalence)) {
			equivalences.add(equivalence);
			return true;
		}
		return false;
	}

	private boolean removeEquivalence(CWBEquivalence equivalence) {
		if (equivalences.contains(equivalence)) {
			equivalences.remove(equivalence);
			return true;
		}
		return false;
	}

	public boolean addEquivalences(Collection<CWBEquivalence> equivalences) {
		boolean hasChanged = false;
		for (CWBEquivalence equivalence : equivalences) {
			if (addEquivalence(equivalence)) {
				hasChanged = true;
			}
		}
		setChanged();
		notifyObservers(equivalences);
		return hasChanged;
	}

	public boolean removeEquivalences(Collection<CWBEquivalence> equivalences) {
		boolean hasChanged = false;
		for (CWBEquivalence equivalence : equivalences) {
			if (removeEquivalence(equivalence)) {
				hasChanged = true;
			}
		}
		setChanged();
		notifyObservers(equivalences);
		return hasChanged;
	}
	
	/**
	 * @return the equivalences
	 */
	public Collection<CWBEquivalence> getSelectedEquivalences() {
		return selectedEquivalences;
	}
	
	public boolean addSelectedEquivalence(CWBEquivalence selectedEquivalence) {
		if (!selectedEquivalences.contains(selectedEquivalence)) {
			selectedEquivalences.add(selectedEquivalence);
			return true;
		}
		return false;
	}

	public boolean removeSelectedEquivalence(CWBEquivalence selectedEquivalence) {
		if (selectedEquivalences.contains(selectedEquivalence)) {
			selectedEquivalences.remove(selectedEquivalence);
			return true;
		}
		return false;
	}
	
	public void removeAllSelectedEquivalences(){
		this.selectedEquivalences = new ArrayList<CWBEquivalence>();
	}
	
	public boolean isEmpty(){
		return folksonomy != null || nomenclature != null;
	}

	/**
	 * @return the instancesFolkso
	 */
	public Collection<CWBInstanceFolkso> getInstancesFolkso() {
		return instancesFolkso;
	}
	
	private boolean addInstanceFolkso(CWBInstanceFolkso instanceFolkso) {
		if (!instancesFolkso.contains(instanceFolkso)) {
			instancesFolkso.add(instanceFolkso);
			return true;
		}
		return false;
	}

	/**
	 * @param instancesFolkso the instancesFolkso to set
	 */
	public boolean addInstancesFolkso(Collection<CWBInstanceFolkso> instancesFolkso) {
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
	
	public void removeAllInstancesFolkso(){
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
	
	public void removeAllInstancesNomen(){
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

}

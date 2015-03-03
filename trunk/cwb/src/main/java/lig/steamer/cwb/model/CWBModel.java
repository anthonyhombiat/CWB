package lig.steamer.cwb.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Observable;

import lig.steamer.cwb.util.wsclient.CWBDatasetFolksoProviderWSClient;
import lig.steamer.cwb.util.wsclient.CWBDatasetNomenProviderWSClient;
import lig.steamer.cwb.util.wsclient.bdtopo.BDTopoWSClient;
import lig.steamer.cwb.util.wsclient.overpass.OverpassWSClient;

public class CWBModel extends Observable implements Serializable {

	private static final long serialVersionUID = 1L;

	private CWBDataModelNomen nomenclature;
	private CWBDataModelFolkso folksonomy;

	private CWBAlignment alignment;

	private CWBDatasetNomenProviderWSClient datasetNomenProvider;
	private CWBDatasetFolksoProviderWSClient datasetFolksoProvider;

	private CWBDataSetNomen datasetNomen;
	private CWBDataSetFolkso datasetFolkso;

	private CWBStudyArea studyArea;

	private CWBBuffer buffer;

	private Boolean isReadyForMatching;

	public CWBModel() {
		init();
	}

	public void init() {
		setFolksonomy(new CWBDataModelFolkso(null));
		setNomenclature(new CWBDataModelNomen(null));
		setAlignment(new CWBAlignment(null, null));
		setDatasetNomen(new CWBDataSetNomen());
		setDatasetFolkso(new CWBDataSetFolkso());
		setReadyForMatching(false);
		setStudyArea(CWBStudyArea.GRENOBLE);
		setDatasetNomenProvider(new BDTopoWSClient());
		setDatasetFolksoProvider(new OverpassWSClient());
		setBuffer(new CWBBuffer());
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
		setReadyForMatching(!folksonomy.getConcepts().isEmpty()
				&& !nomenclature.getConcepts().isEmpty());
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
		setReadyForMatching(!folksonomy.getConcepts().isEmpty()
				&& !nomenclature.getConcepts().isEmpty());
	}

	public boolean isEmpty() {
		return folksonomy.getConcepts().isEmpty()
				&& nomenclature.getConcepts().isEmpty()
				&& alignment.getEquivalences().isEmpty();
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
		setChanged();
		notifyObservers(studyArea);
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
		this.isReadyForMatching = isReadyForMatching;
		setChanged();
		notifyObservers(isReadyForMatching);
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

	/**
	 * @return the datasetNomenProvider
	 */
	public CWBDatasetNomenProviderWSClient getDatasetNomenProvider() {
		return datasetNomenProvider;
	}

	/**
	 * @param datasetNomenProvider the CWBDatasetNomenProvider to set
	 */
	public void setDatasetNomenProvider(
			CWBDatasetNomenProviderWSClient datasetNomenProvider) {
		this.datasetNomenProvider = datasetNomenProvider;
	}

	/**
	 * @return the datasetFolksoProvider
	 */
	public CWBDatasetFolksoProviderWSClient getDatasetFolksoProvider() {
		return datasetFolksoProvider;
	}

	/**
	 * @param datasetFolksoProvider the CWBDatasetFolksoProvider to set
	 */
	public void setDatasetFolksoProvider(
			CWBDatasetFolksoProviderWSClient datasetFolksoProvider) {
		this.datasetFolksoProvider = datasetFolksoProvider;
	}

	/**
	 * @return the buffer
	 */
	public CWBBuffer getBuffer() {
		return buffer;
	}

	/**
	 * @param buffer the buffer to set
	 */
	public void setBuffer(CWBBuffer buffer) {
		this.buffer = buffer;
		setChanged();
		notifyObservers(buffer);
	}

	/**
	 * @return the datasetNomen
	 */
	public CWBDataSetNomen getDatasetNomen() {
		return datasetNomen;
	}

	/**
	 * @param datasetNomen the datasetNomen to set
	 */
	public void setDatasetNomen(CWBDataSetNomen datasetNomen) {
		this.datasetNomen = datasetNomen;
	}

	/**
	 * @return the datasetFolkso
	 */
	public CWBDataSetFolkso getDatasetFolkso() {
		return datasetFolkso;
	}

	/**
	 * @param datasetFolkso the datasetFolkso to set
	 */
	public void setDatasetFolkso(CWBDataSetFolkso datasetFolkso) {
		this.datasetFolkso = datasetFolkso;
	}

}

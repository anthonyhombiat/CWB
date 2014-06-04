package lig.steamer.cwb.model;

import java.util.Collection;

import org.semanticweb.owlapi.model.IRI;

public class CWBMatchedDataModel extends CWBDataModel {

	private static final long serialVersionUID = 1L;
	
	private Collection<CWBDataModel> sourceDataModels;
	
	public CWBMatchedDataModel(IRI namespace, Collection<CWBDataModel> sourceDataModels) {
		super(namespace);
		this.sourceDataModels =sourceDataModels;
	}

	/**
	 * @return the sourceDataModels
	 */
	public Collection<CWBDataModel> getSourceDataModels() {
		return sourceDataModels;
	}

}

package lig.steamer.cwb.model;

import lig.steamer.cwb.io.write.CWBDataModelVisitor;

import org.semanticweb.owlapi.model.IRI;

public class CWBDataModelNomen extends CWBDataModel {

	private static final long serialVersionUID = 1L;

	public CWBDataModelNomen(IRI namespace) {
		super(namespace);
	}

	@Override
	public void acceptCWBDataModelVisitor(CWBDataModelVisitor visitor) {
		visitor.visitNomenclature(this);
	}

}

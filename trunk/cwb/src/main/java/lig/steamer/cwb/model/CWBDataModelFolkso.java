package lig.steamer.cwb.model;

import lig.steamer.cwb.io.write.CWBDataModelVisitor;

import org.semanticweb.owlapi.model.IRI;

public class CWBDataModelFolkso extends CWBDataModel {

	private static final long serialVersionUID = 1L;

	public CWBDataModelFolkso(IRI namespace) {
		super(namespace);
	}

	@Override
	public void acceptCWBDataModelVisitor(CWBDataModelVisitor visitor) {
		visitor.visitFolksonomy(this);
	}

}

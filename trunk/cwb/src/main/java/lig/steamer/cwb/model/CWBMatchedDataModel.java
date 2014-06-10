package lig.steamer.cwb.model;

import org.semanticweb.owlapi.model.IRI;

public class CWBMatchedDataModel extends CWBDataModel {

	private static final long serialVersionUID = 1L;

	private IRI source1;
	private IRI source2;

	public CWBMatchedDataModel(IRI namespace, IRI source1, IRI source2) {
		super(namespace);
		this.source1 = source1;
		this.source2 = source2;
	}

	/**
	 * @return the source1
	 */
	public IRI getSource1() {
		return source1;
	}

	/**
	 * @return the source2
	 */
	public IRI getSource2() {
		return source2;
	}

}

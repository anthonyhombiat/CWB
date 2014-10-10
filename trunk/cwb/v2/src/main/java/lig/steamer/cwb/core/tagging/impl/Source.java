package lig.steamer.cwb.core.tagging.impl;

import lig.steamer.cwb.core.tagging.ISource;

import org.semanticweb.owlapi.model.IRI;

public class Source implements ISource {

	private IRI iri;

	public Source(IRI iri) {
		this.iri = iri;
	}

	@Override
	public IRI getIRI() {
		return iri;
	}

}

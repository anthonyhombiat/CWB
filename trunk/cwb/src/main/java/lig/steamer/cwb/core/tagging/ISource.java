package lig.steamer.cwb.core.tagging;

import org.semanticweb.owlapi.model.IRI;

/**
 * @author Anthony Hombiat
 * A Source is were a Resource described by a Tag can be found.
 */
public interface ISource {

	/**
	 * Returns the IRI of the Source.
	 * @return iri
	 */
	public IRI getIRI();

}

package lig.steamer.cwb.util.matching;

import java.util.Collection;

import lig.steamer.cwb.model.CWBEquivalence;

/**
 * 
 * @author Anthony Hombiat
 * 
 */
public interface CWBOntologyMatcher {


	/**
	 * Returns the collection of equivalences between the source and the target ontologies
	 * @param sourceURI, the source ontology URI
	 * @param targetURI, the target ontology URI
	 * @return the collection of equivalences between the source and the target ontologies
	 */
	public Collection<CWBEquivalence> getEquivalences(String sourceURI, String targetURI);
	
}

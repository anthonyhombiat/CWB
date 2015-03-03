package lig.steamer.cwb.util.wsclient;

import java.util.Collection;

import lig.steamer.cwb.model.CWBBBox;
import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBInstanceNomen;
import lig.steamer.cwb.util.wsclient.exception.WSDatasetNomenClientException;

public interface CWBDatasetNomenProviderWSClient {

	/**
	 * Returns the nomenclature instances corresponding to the given concept
	 * for the given locale, for the given frequency of occurrence threshold, in
	 * the given bounding box and in the given output format.
	 * @param concept, the CWB concept
	 * @param bbox, the bounding box
	 * @param threshold, the threshold
	 * @param outputFormat, the output format
	 * @return the instances
	 * @throws WSDatasetNomenClientException
	 */
	public Collection<CWBInstanceNomen> getNomenInstances(CWBConcept concept,
			CWBBBox bbox, double threshold, String outputFormat)
			throws WSDatasetNomenClientException;

	/**
	 * Returns the nomenclature instances corresponding to the given concept
	 * for the given bbox.
	 * @param concept, the CWB concept
	 * @param bbox
	 * @return the instances
	 * @throws WSDatasetNomenClientException
	 */
	public Collection<CWBInstanceNomen> getNomenInstances(CWBConcept concept,
			CWBBBox bbox) throws WSDatasetNomenClientException;

	/**
	 * Returns the nomenclature instances corresponding to the given concept
	 * for the given locale.
	 * @param concept, the CWB concept
	 * @param bbox
	 * @param threshold
	 * @return the instances
	 * @throws WSDatasetNomenClientException
	 */
	public Collection<CWBInstanceNomen> getNomenInstances(CWBConcept concept,
			CWBBBox bbox, double threshold)
			throws WSDatasetNomenClientException;

}

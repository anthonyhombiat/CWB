package lig.steamer.cwb.util.wsclient;

import java.util.Collection;

import lig.steamer.cwb.model.CWBBBox;
import lig.steamer.cwb.model.CWBInstanceNomen;
import lig.steamer.cwb.util.wsclient.exception.WSClientException;

public interface InstancesNomenProviderWSClient {

	/**
	 * Returns the nomenclature instances corresponding to the given concept
	 * for the given locale, for the given frequency of occurrence threshold, in
	 * the given bounding box and in the given output format.
	 * @param value, the value
	 * @param bbox, the bounding box
	 * @param threshold, the threshold
	 * @param outputFormat, the output format
	 * @return the instances
	 * @throws WSClientException
	 */
	public Collection<CWBInstanceNomen> getNomenInstances(String value,
			CWBBBox bbox, double threshold, String outputFormat)
			throws WSClientException;

	/**
	 * Returns the nomenclature instances corresponding to the given concept.
	 * @param value
	 * @return the instances
	 * @throws WSClientException
	 */
	public Collection<CWBInstanceNomen> getNomenInstances(String value)
			throws WSClientException;

	/**
	 * Returns the nomenclature instances corresponding to the given concept
	 * for the given bbox.
	 * @param value
	 * @param bbox
	 * @return the instances
	 * @throws WSClientException
	 */
	public Collection<CWBInstanceNomen> getNomenInstances(String value,
			CWBBBox bbox) throws WSClientException;

	/**
	 * Returns the nomenclature instances corresponding to the given concept
	 * for the given locale.
	 * @param value
	 * @param bbox
	 * @param threshold
	 * @return the instances
	 * @throws WSClientException
	 */
	public Collection<CWBInstanceNomen> getNomenInstances(String value,
			CWBBBox bbox, double threshold)
			throws WSClientException;

}

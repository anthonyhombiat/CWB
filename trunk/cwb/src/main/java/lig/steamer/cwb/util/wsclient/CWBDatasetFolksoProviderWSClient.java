package lig.steamer.cwb.util.wsclient;

import java.util.Collection;
import java.util.Locale;

import lig.steamer.cwb.model.CWBBBox;
import lig.steamer.cwb.model.CWBConcept;
import lig.steamer.cwb.model.CWBInstanceFolkso;
import lig.steamer.cwb.util.wsclient.exception.WSDatasetFolksoClientException;

public interface CWBDatasetFolksoProviderWSClient {

	/**
	 * Returns the folksonomy instances corresponding to the given concept
	 * for the given locale, for the given frequency of occurrence threshold, in
	 * the given bounding box and in the given output format.
	 * @param concept, the CWB concept
	 * @param bbox, the bounding box
	 * @param locale, the locale
	 * @param threshold, the threshold
	 * @param outputFormat, the output format
	 * @return the instances
	 * @throws WSDatasetFolksoClientException
	 */
	public Collection<CWBInstanceFolkso> getFolksoInstances(CWBConcept concept,
			CWBBBox bbox, Locale locale, double threshold, String outputFormat)
			throws WSDatasetFolksoClientException;

	/**
	 * Returns the folksonomy instances corresponding to the given concept
	 * for the given bbox.
	 * @param concept, the CWB concept
	 * @param bbox
	 * @return the instances
	 * @throws WSDatasetFolksoClientException
	 */
	public Collection<CWBInstanceFolkso> getFolksoInstances(CWBConcept concept,
			CWBBBox bbox) throws WSDatasetFolksoClientException;

	/**
	 * Returns the folksonomy instances corresponding to the given concept
	 * for the given bbox and locale.
	 * @param concept, the CWB concept
	 * @param bbox
	 * @param locale
	 * @return the instances
	 * @throws WSDatasetFolksoClientException
	 */
	public Collection<CWBInstanceFolkso> getFolksoInstances(CWBConcept concept,
			CWBBBox bbox, Locale locale) throws WSDatasetFolksoClientException;

	/**
	 * Returns the folksonomy instances corresponding to the given concept
	 * for the given locale.
	 * @param concept, the CWB concept
	 * @param bbox
	 * @param locale
	 * @param threshold
	 * @return the instances
	 * @throws WSDatasetFolksoClientException
	 */
	public Collection<CWBInstanceFolkso> getFolksoInstances(CWBConcept concept,
			CWBBBox bbox, Locale locale, double threshold)
			throws WSDatasetFolksoClientException;

}

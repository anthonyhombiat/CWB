package lig.steamer.cwb.util.wsclient;

import lig.steamer.cwb.model.CWBDataModelNomen;
import lig.steamer.cwb.util.wsclient.exception.WSClientException;

public interface DataModelNomenProviderWSClient {

	public CWBDataModelNomen getDataModelNomen() throws WSClientException;
	
}

package lig.steamer.cwb.util.wsclient;

import lig.steamer.cwb.model.CWBDataModelFolkso;
import lig.steamer.cwb.util.wsclient.exception.WSClientException;

public interface DataModelFolksoProviderWSClient {

	public CWBDataModelFolkso getDataModelFolkso() throws WSClientException;
	
}

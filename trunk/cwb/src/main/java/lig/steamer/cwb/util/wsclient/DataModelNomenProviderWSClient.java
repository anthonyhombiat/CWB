package lig.steamer.cwb.util.wsclient;

import lig.steamer.cwb.model.CWBDataModelNomen;
import lig.steamer.cwb.util.wsclient.bdtopo.exception.BDTopoDataModelWSClientException;

public interface DataModelNomenProviderWSClient {

	public CWBDataModelNomen getDataModelNomen() throws BDTopoDataModelWSClientException;
	
}

package lig.steamer.cwb.util.wsclient;

import lig.steamer.cwb.core.tagging.IFolksonomy;
import lig.steamer.cwb.util.wsclient.exception.FolksoProviderWSClientException;

public interface FolksoProviderWSClient {

	public IFolksonomy getTags() throws FolksoProviderWSClientException;
	
}

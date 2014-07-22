package lig.steamer.cwb.util.wsclient;

import lig.steamer.cwb.core.tagging.IFolksonomy;
import lig.steamer.cwb.util.wsclient.exception.FolksonomyWSClientException;

public interface FolksonomyWSClient {

	public IFolksonomy getFolksonomy() throws FolksonomyWSClientException;
	
}

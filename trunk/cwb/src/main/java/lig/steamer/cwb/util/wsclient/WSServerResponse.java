package lig.steamer.cwb.util.wsclient;

import java.util.Collection;

public interface WSServerResponse<T extends WSNode> {

	static final String NAME_UNDEFINED = "<i>undefined name</i>";
	
	public Collection<T> getNodes();
	
}

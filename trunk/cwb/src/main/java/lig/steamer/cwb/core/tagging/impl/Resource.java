package lig.steamer.cwb.core.tagging.impl;

import java.net.URI;

import lig.steamer.cwb.core.tagging.IResource;
import lig.steamer.cwb.core.tagging.ISource;

public class Resource implements IResource {

	private ISource source;
	private URI uri;

	public Resource(URI uri){
		this.uri = uri;
	}
	
	@Override
	public ISource getSource() {
		return source;
	}

	@Override
	public URI getURI() {
		return uri;
	}

}

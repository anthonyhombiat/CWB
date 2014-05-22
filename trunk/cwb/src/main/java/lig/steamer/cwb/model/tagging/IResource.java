package lig.steamer.cwb.model.tagging;

import java.net.URI;

/**
 * @author Anthony Hombiat
 * A Resource is described by a Tag.
 */
public interface IResource {

	public ISource getSource();
	
	public URI getURI();
	
}

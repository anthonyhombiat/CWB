package lig.steamer.cwb.core.tagging.impl;

import lig.steamer.cwb.core.tagging.IResource;
import lig.steamer.cwb.core.tagging.ITag;
import lig.steamer.cwb.core.tagging.ITagging;
import lig.steamer.cwb.core.tagging.IUser;

public class Tagging implements ITagging {

	private IResource resource;
	private ITag tag;
	private IUser user;
	
	public Tagging(IResource resource, ITag tag, IUser user){
		this.resource = resource;
		this.tag = tag;
		this.user = user;
	}
	
	@Override
	public IResource getResource() {
		return resource;
	}

	@Override
	public ITag getTag() {
		return tag;
	}

	@Override
	public IUser getIUser() {
		return user;
	}

}

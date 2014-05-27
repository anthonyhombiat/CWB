package lig.steamer.cwb.core.tagging.impl;

import lig.steamer.cwb.core.tagging.IFolksonomy;
import lig.steamer.cwb.core.tagging.ISource;
import lig.steamer.cwb.core.tagging.ITagSet;
import lig.steamer.cwb.core.tagging.ITagging;
import lig.steamer.cwb.core.tagging.IUserGroup;

public class Folksonomy implements IFolksonomy {

	private ITagSet tagset;
	private IUserGroup userGroup;
	private ISource source;
	private ITagging tagging;
	
	public Folksonomy(ITagSet tagset, IUserGroup userGroup, ISource source, ITagging tagging){
		this.tagset = tagset;
		this.userGroup = userGroup;
		this.source = source;
		this.tagging = tagging;
	}
	
	@Override
	public ITagSet getTagSet() {
		return tagset;
	}

	@Override
	public IUserGroup getUserGroup() {
		return userGroup;
	}

	@Override
	public ISource getSource() {
		return source;
	}

	@Override
	public ITagging getTagging() {
		return tagging;
	}
	
}

package lig.steamer.cwb.core.tagging.impl;

import lig.steamer.cwb.core.tagging.ISource;

public class Source implements ISource {

	private String name;

	public Source(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

}

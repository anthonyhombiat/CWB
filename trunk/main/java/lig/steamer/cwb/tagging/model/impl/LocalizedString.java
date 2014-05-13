package lig.steamer.cwb.tagging.model.impl;

import lig.steamer.cwb.tagging.model.ILocalizedString;


public class LocalizedString implements ILocalizedString {

	private String language;
	private String string;

	/**
	 * @param language
	 * @param string
	 */
	public LocalizedString(String string, String language) {
		this.string = string;
		this.language = language;
	}
	
	public String getLanguage() {
		return language;
	}

	public String getString() {
		return string;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (! (obj instanceof ILocalizedString))
			return false;
		ILocalizedString ls = (ILocalizedString) obj;
		return language.equals(ls.getLanguage()) && string.equals(ls.getString());
	}

	@Override
	public int hashCode() {
		return language.hashCode() + string.hashCode();
	}

}

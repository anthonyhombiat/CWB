package lig.steamer.cwb.core.tagging.impl;

import lig.steamer.cwb.core.tagging.ILocalizedString;


/**
 * @author Anthony Hombiat
 * @see lig.steamer.cwb.core.tagging.ILocalizedString
 */
public class LocalizedString implements ILocalizedString {

	private String language;
	private String string;

	/**
	 * {@inheritDoc}
	 */
	public LocalizedString(String string, String language) {
		this.string = string;
		this.language = language;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * {@inheritDoc}
	 */
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
	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return language.hashCode() + string.hashCode();
	}

}

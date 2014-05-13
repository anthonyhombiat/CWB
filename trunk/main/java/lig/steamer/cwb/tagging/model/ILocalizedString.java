package lig.steamer.cwb.tagging.model;

/**
 * A LocalizedString is a String associated with a locale (language). 
 */
public interface ILocalizedString {

	/**
	 * returns the language of this String
	 * @return the language
	 */
	public String getLanguage();
	
	/**
	 * returns the String
	 * @return the String
	 */
	public String getString();
}
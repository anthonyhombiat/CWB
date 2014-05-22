package lig.steamer.cwb.model.tagging;

/**
 * @author Anthony Hombiat
 * A LocalizedString is a String associated with a locale (language). 
 */
public interface ILocalizedString {

	/**
	 * Returns the language of the String of the LocalizedString.
	 * @return the language
	 */
	public String getLanguage();
	
	/**
	 * Returns the String of the LocalizedString.
	 * @return the String
	 */
	public String getString();
}
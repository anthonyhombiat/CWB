package lig.steamer.cwb.model.tagging;

/**
 * @author Anthony Hombiat
 * A User (or a Tagger) is a person that is involved in the process of Tagging 
 * by describing a Resource by associating Tags to it.
 */
public interface IUser {

	/**
	 * Returns the User name.
	 * @return the User name
	 */
	public String getUsername();
	
}

package lig.steamer.cwb.model.tagging;

/**
 * @author Anthony Hombiat
 * A Tagging is, according to Hak Lae Kim's model, the process of tagging 
 * involving a user (the "tagger"), a Resource (the tagged object) and a Tag. 
 */
public interface ITagging {
	
	/**
	 * Returns the Resource involved in the Tagging process.
	 * @return the Resource
	 */
	public IResource getResource();
	
	/**
	 * Returns the Tag involved in the Tagging process.
	 * @return the Tag
	 */
	public ITag getTag();
	
	/**
	 * Returns the User involved in the Tagging process.
	 * @return the User
	 */
	public IUser getIUser();
	
}

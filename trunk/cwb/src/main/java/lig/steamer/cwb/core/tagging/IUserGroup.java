package lig.steamer.cwb.core.tagging;

import java.util.Collection;

public interface IUserGroup {

	public String getName();
	
	public Collection<IUser> getUsers();
	
}

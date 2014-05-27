package lig.steamer.cwb.core.tagging.impl;

import java.util.Collection;

import lig.steamer.cwb.core.tagging.IUser;
import lig.steamer.cwb.core.tagging.IUserGroup;

public class UserGroup implements IUserGroup {

	private String name;
	private Collection<IUser> users;
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Collection<IUser> getUsers() {
		return users;
	}
	
	public boolean addUser(IUser user){
		if(!users.contains(user)){
			users.add(user);
			return true;
		}
		return false;
	}
	
	public boolean removeUser(IUser user){
		if(users.contains(user)){
			users.remove(user);
			return true;
		}
		return false;
	}

}

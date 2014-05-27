package lig.steamer.cwb.core.tagging.impl;

import lig.steamer.cwb.core.tagging.IUser;

public class User implements IUser {

	private String username;
	
	public User(String username){
		this.username = username;
	}
	
	@Override
	public String getUsername() {
		return username;
	}

}

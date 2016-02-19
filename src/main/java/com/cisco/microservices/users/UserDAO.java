package com.cisco.microservices.users;

import java.util.List;

public interface UserDAO {
	public long addUser(User p);

	public void updateUser(User p);

	public List<User> listUser();
	
	public Long countUser();
	
	public List<User> getUserByUserName(String userName);

	public void removeUser(int id);

}


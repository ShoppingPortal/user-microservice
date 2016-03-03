package com.cisco.microservices.users;

import java.util.List;

public interface UserDAO {
	public long addUser(User p);

	public boolean updateUser(User p);

	public List<User> listUser(String userType);
	
	public Long countUser();
	
	public Object getUserByUserName(String userName);

	public boolean removeUser(long id);

}


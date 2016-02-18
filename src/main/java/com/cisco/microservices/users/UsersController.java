package com.cisco.microservices.users;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UsersController {

	protected Logger logger = Logger.getLogger(UsersController.class.getName());

	@Autowired
	protected UserDAO userDao;

	@Autowired
	public UsersController(UserDAO userDao) {
		logger.info("UserRepository says system has " + userDao.countUser() + " users");
	}

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	private @ResponseBody User addUser() {
		User user = new User();
		user.setUserName("Sandip007");
		user.setUserType("Admin");
		userDao.addUser(user);
		logger.info("User created in DB!!");
		return user;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	private @ResponseBody List<User> list() {
		List<User> userList = userDao.listUser();
		return userList;
	}

}

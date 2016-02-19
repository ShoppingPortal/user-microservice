package com.cisco.microservices.users;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * RESTful Client controller, fetches Product info from the microservice via
 * {@link userDao}.
 * 
 * @author Sandip Bastapure
 */
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

	/**
	 * This method is used to add User with .
	 * 
	 * @param args
	 * 		Product : userName, userType 
	 */
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	private Response addUser(@RequestBody User user) {
		String userName = user.getUserName();
		List<User> userList = new ArrayList<User>();
		Response res = new Response();
		logger.info("User's addUser() invoked for :" + userName);

		if (validateUserName(userName)) {
			res.setStatus("400");
			res.setDescription("User already exist!");
			UserError err = new UserError();
			err.setMsg("User already exist");
			err.setParam("Username");
			err.setValue(userName);
			res.setError(err);
			System.out.println("User already exist");
		} else {

			try {
				User u = new User();
				u.setUserName(userName);
				u.setUserType(user.getUserType());
				userDao.addUser(u);
				logger.info("User added!");
				userList.add(u);

				res.setStatus("200");
				res.setDescription("User added successfully!");
				res.setData(userList);
			} catch (ConstraintViolationException e) {
				res.setStatus("400");
				res.setDescription("Server error!");
				UserError err = new UserError();
				err.setMsg("Something went wrong");
				err.setParam("Username");
				err.setValue(userName);
				res.setError(err);
			}
		}
		return res;
	}

	/**
	 * This method is used to get list of all the Users .
	 * 
	 * @param args
	 * 		NA
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	private Response list() {
		Response res = new Response();
		List<User> userList = userDao.listUser();
		if (userList.size() <= 0) {
			res.setStatus("200");
			res.setDescription("OOPs! it seems there are no users added yet!");
		} else {
			res.setStatus("200");
			res.setDescription("This is the user's list");
		}
		res.setData(userList);
		return res;
	}

	/**
	 * Validates user exits in the system or not .
	 * 
	 * @param args
	 * 		String username
	 */
	private boolean validateUserName(String userName) {
		List<User> userData = null;
		try {
			System.out.println(userName);
			userData = userDao.getUserByUserName(userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (userData.size() <= 0) {
			return false;
		} else {
			return true;
		}
	}

}

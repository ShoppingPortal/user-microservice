package com.cisco.microservices.users;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	 *            Product : userName, userType
	 */
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	private Response addUser(@RequestBody User user) {
		String userName = user.getUserName();
		List<User> userList = new ArrayList<User>();
		Response res = new Response();
		logger.info("User's addUser() invoked for :" + userName);
		if (userName == "") {
			res.setStatus("400");
			res.setDescription("Invalid Username");
			UserError err = new UserError();
			err.setMsg("Username can not be blank");
			err.setParam("Username");
			err.setValue(userName);
			res.setError(err);
			logger.info("Username can not be blank");
		} else if (validateUserName(userName)) {
			res.setStatus("400");
			res.setDescription("User already exist");
			UserError err = new UserError();
			err.setMsg("User already exist");
			err.setParam("Username");
			err.setValue(userName);
			res.setError(err);
			System.out.println("User already exist");
			logger.info("User already exist");
		} else {

			try {
				User u = new User();
				u.setUserName(userName);
				u.setUserType(user.getUserType());
				userDao.addUser(u);
				logger.info("User added");
				userList.add(u);

				res.setStatus("200");
				res.setDescription("User added successfully");
				res.setData(userList);
				logger.info("User added successfully");
			} catch (Exception e) {
				res.setStatus("500");
				res.setDescription("Internal Server error");
				UserError err = new UserError();
				err.setMsg("Something went wrong");
				err.setParam("Username");
				err.setValue(userName);
				res.setError(err);
				logger.info("Something went wrong");
			}
		}
		return res;
	}

	/**
	 * This method is used to get list of all the Users .
	 * 
	 * @param args
	 *            NA
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	private Response list(@RequestParam(value="type", required=false) String userType) {
		logger.info("get list of all Users");
		Response res = new Response();

		if (StringUtils.isEmpty(userType)) {
			  userType = null;
		}
		List<User> userList = userDao.listUser(userType);
		if (userList.size() <= 0) {
			logger.info("there are no users added yet");
			res.setStatus("404");
			res.setDescription("No users found");
		} else {
			res.setStatus("200");
			res.setDescription("This is the user's list");
		}
		res.setData(userList);
		return res;
	}

	@RequestMapping(value = "/delete/{username}", method = RequestMethod.DELETE)
	public Response delete(@PathVariable("username") String username) {
		Response res = null;
		try {
			res = new Response();
			User user =  (User) userDao.getUserByUserName(username);
			if(user==null){
				res.setStatus("404");
				res.setDescription("Record Not Found");
				return res;
			}
			boolean wasOk = userDao.removeUser(user.getId()); 
			if (wasOk) {
				res.setStatus("200");
				res.setDescription("User deleted");
			}else{
				res.setStatus("500");
				res.setDescription("Internal server error");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info(e.getMessage());
			res.setStatus("500");
			res.setDescription("Internal server error");
		}
	    
	    return res;
	}
	
	/**
	 * Validates user exits in the system or not .
	 * 
	 * @param args
	 *            String username
	 */
	private boolean validateUserName(String userName) {
		Object userData = null;
		try {
			System.out.println(userName);
			userData = userDao.getUserByUserName(userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (userData == null) {
			return false;
		} else {
			return true;
		}
	}

}

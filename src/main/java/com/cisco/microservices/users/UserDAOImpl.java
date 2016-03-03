package com.cisco.microservices.users;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Here you can perform CRUD operations on User entity.
 * {@link UserDAO}.
 * 
 * @author Sandip Bastapure
 */
@Repository
@Transactional
@EnableTransactionManagement
public class UserDAOImpl implements UserDAO {

	private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public long addUser(User p) {
		// Add system current date as User Created Date
		long id = 0;
		try {
			Session session = this.sessionFactory.getCurrentSession();
			id = (Long) session.save(p);
			System.out.println("id : "+ id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("User saved successfully, User Details=" + p);
		return id;
	}

	@Override
	public boolean updateUser(User p) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(p);
		logger.info("User updated successfully, User Details=" + p);
		return true;
	}

	@Override
	@Transactional
	public List<User> listUser(String userType) {
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM User U";
		if(userType != null) {
			hql += " where U.userType='" + userType + "'";
		}
		List<User> UserList = session.createQuery(hql).list();
		for (User p : UserList) {
			logger.info("User List::" + p);
		}
		return UserList;
	}
	@Override
	@Transactional
	public Long countUser() {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT COUNT(*) FROM User");
		Long count = (Long)query.uniqueResult();
		return count;
	}
	
	@Override
	@Transactional
	public Object getUserByUserName(String userName) {
		Query q = null;
		try {
			Session session = this.sessionFactory.getCurrentSession();
			String hql = "FROM User WHERE user_name = :userName";

			if (userName != null) {
				q = session.createQuery(hql).setString("userName", userName);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		
		return q.list().get(0);
	}

	@Override
	public boolean removeUser(long id) {
		Session session = this.sessionFactory.getCurrentSession();
		User p = (User) session.load(User.class, id);
		if (null != p) {
			session.delete(p);
		}
		logger.info("User deleted successfully, User details=" + p);
		return true;
	}
	
	@Override
	public User getUserById(long id) {
		// TODO Auto-generated method stub
		Session session = null;
		User user = null;
		try {
			session = this.sessionFactory.getCurrentSession();
			user = (User) session.get(User.class, id);			
		} catch (HibernateException e) {
			// TODO: handle exception
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		logger.info("User Details=" + user);
		return user;
	}

}

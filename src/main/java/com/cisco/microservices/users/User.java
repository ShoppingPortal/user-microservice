package com.cisco.microservices.users;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Persistent user entity with JPA markup. Users are stored in an MySQL relational
 * database.
 * 
 * @author Sandip Bastapure
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Long nextId = 0L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "user_name", unique = true, nullable=false)
	private String userName;

	@Column(name = "user_type")
	private String userType;

	private static Long getNextId() {
		synchronized (nextId) {
			return nextId++;
		}
	}

	protected User() {

	}

	public User(String number, String userName) {
		id = getNextId();
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public long getId() {
		return id;
	}

	protected void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return userName + " " + userType;
	}
}

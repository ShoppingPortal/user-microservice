package com.cisco.microservices.users;

import java.util.List;

public class Response {

	private String status;

	private String description;

	private List<User> usersList;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<User> getProductList() {
		return usersList;
	}

	public void setProductList(List<User> usersList) {
		this.usersList = usersList;
	}

}

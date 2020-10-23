package com.developerblog.app.ui.model.request;

public class UserLoginRequestModel {
	
	private String email;
	private String password;
	
/*	public UserLoginRequestModel(String email) {
		this.email = email;
	}*/
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}	
}

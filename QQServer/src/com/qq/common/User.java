package com.qq.common;
//common包存放服务器可客户端都有的类
import java.io.Serializable;
//User类为用户信息   
public class User implements Serializable {

	private String userId;
	private String password;
	private static final long serialVersionUID=1L;
	
	public User(String userId, String password) {
		super();
		this.userId = userId;
		this.password = password;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}

package com.qq.common;
//common����ŷ������ɿͻ��˶��е���
import java.io.Serializable;
//User��Ϊ�û���Ϣ   
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

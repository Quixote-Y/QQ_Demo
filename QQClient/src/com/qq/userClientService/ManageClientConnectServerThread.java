package com.qq.userClientService;

import java.util.HashMap;

//管理所有的socket线程
public class ManageClientConnectServerThread {

	//这里属性为static，因为这个类可能随时会被调用
	public static HashMap<String,ClientConnectServerThread> hm=new HashMap<>();
	
	//将线程加入到hashmap中
	public static void addClientConnectServerThread(String userId,ClientConnectServerThread clientConnectServerThread) {
		hm.put(userId, clientConnectServerThread);
	}
	
	//根据用户名返回ClientConnectServerThread 线程
	public static ClientConnectServerThread getClientConnectServerThread(String userId) {
		return hm.get(userId);
	}
	//从线程池中移除某个线程
	public static void removeClientConnectServerThread(String userId) {
		hm.remove(userId);
	}
	
}

package com.qq.server;

import java.util.HashMap;

//������������кͿͻ��˽�����ͨѶ�߳�
public class ManageServerConnectClient {


	public static HashMap<String,ServerConnectClientThread> hm=new HashMap<>();
	
	public static void addServerConnectClientThread(String userId,ServerConnectClientThread serverConnectClientThread) {
		hm.put(userId, serverConnectClientThread);
	}
	
	public static ServerConnectClientThread getServerConnectClientThread(String userId) {
		return hm.get(userId);
	}
	
	public static void removeServerConnectClientThread(String userId) {
		hm.remove(userId);
	}
	public static boolean exitsServerConnectClientThread(String userId) {
		if(hm.get(userId)==null) {
			return false;
		}
		return true;
	}
}

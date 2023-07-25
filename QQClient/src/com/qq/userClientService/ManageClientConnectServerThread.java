package com.qq.userClientService;

import java.util.HashMap;

//�������е�socket�߳�
public class ManageClientConnectServerThread {

	//��������Ϊstatic����Ϊ����������ʱ�ᱻ����
	public static HashMap<String,ClientConnectServerThread> hm=new HashMap<>();
	
	//���̼߳��뵽hashmap��
	public static void addClientConnectServerThread(String userId,ClientConnectServerThread clientConnectServerThread) {
		hm.put(userId, clientConnectServerThread);
	}
	
	//�����û�������ClientConnectServerThread �߳�
	public static ClientConnectServerThread getClientConnectServerThread(String userId) {
		return hm.get(userId);
	}
	//���̳߳����Ƴ�ĳ���߳�
	public static void removeClientConnectServerThread(String userId) {
		hm.remove(userId);
	}
	
}

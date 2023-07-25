package com.qq.server;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.qq.common.Message;
import com.qq.common.User;

public class MessageLoad {
	//��Ų������û�����Ϣ
	//��Ӧ��ʱ���ݿ������
	//��Ϊ����ͬһ�������û������յ����message����������map
//	private static  ConcurrentHashMap<String, Message> hm=new ConcurrentHashMap<>();
	
	private static Vector<Message> vc=new Vector<>();
	
	public static void addMessage(Message message) {
		vc.add(message);
	}
	public static Vector<Message> getMessage(String userId) {
		Vector<Message> tmp=new Vector<Message>();
		for(int i=0;i<vc.size();i++) {
			if(vc.get(i).getGetter().equals(userId)) {
				tmp.add(vc.get(i));
			}
		}
		return tmp;
	}
	public static boolean exitsMessage(String userId) {
		if(getMessage(userId).isEmpty()) {
			return false;	
		}
		return true;
	}
	public static void removeMessage(String userId) {
		Vector<Message> tmp=getMessage(userId);
		vc.removeAll(tmp);
	}

}

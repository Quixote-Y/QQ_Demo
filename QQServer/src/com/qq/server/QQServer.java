package com.qq.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.qq.common.Message;
import com.qq.common.MessageType;
import com.qq.common.User;

//����������9999�˿ڣ��ȴ��ͻ������ӣ�������ͨѶ
public class QQServer {

	private ServerSocket serverSocket = null;
	//��ʹ��һ��HahsMap����������û���֮���滻Ϊsql���ݿ�
	//���߳�ʹ��ConcurrentHashMap���̰߳�ȫ��hashMap�̲߳���ȫ
	private static  ConcurrentHashMap<String, User> hm=new ConcurrentHashMap<>();
	//��̬����� ���û��б�
	static {
		hm.put("100", new User("100","123456"));
		hm.put("200", new User("200","123456"));
		hm.put("300", new User("300","123456"));
		hm.put("400", new User("400","123456"));
		hm.put("�����", new User("�����","123456"));
		hm.put("��ɮ", new User("��ɮ","123456"));
	}
	
	public static ConcurrentHashMap<String, User> getHm() {
		return hm;
	}

	//�ṩһ�������������û��Ƿ�Ϸ�
	public boolean checkUser(User user) {
		String userId=user.getUserId();
		if(hm.get(userId)==null) {
			return false;
		}//hashMapget��������null�����ֵ������
		String passwd=user.getPassword();
		if(!passwd.equals(hm.get(userId).getPassword())) {
			return false;
		}
		return true;
	}

	public QQServer() {

		try {
			System.out.println("�������˼���9999�˿ڵȴ��ͻ�������");
			serverSocket = new ServerSocket(9999);
			//����������Ⱥ��
			new ServerSendMessage().start(); 
			//��ʼ����,һֱ����
			while(true) {
				Socket socket=serverSocket.accept();//����ֱ���пͻ�������
				//��ȡ�����user����
				ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
				User user=(User)ois.readObject();
				Message message=new Message();
				
				
				//������user������бȶԣ����ؽ��
				//�������100��123456
				if(checkUser(user)) {
					//�û��Ϸ�
					//��һ����½�ɹ�
					message.setMessageType(MessageType.MESSAGE_LOGIN_SUCCEED);
					oos.writeObject(message);
					//������ά��һ���Ϳͻ������ӵ��߳�
					ServerConnectClientThread serverConnectClientThread=new ServerConnectClientThread(user.getUserId(), socket);
					//�߳�����
					serverConnectClientThread.start();
					//�����߳� -��ManageServerConnectClient ��
					ManageServerConnectClient.addServerConnectClientThread(user.getUserId(), serverConnectClientThread);
					System.out.println("�������Ϳͻ���"+user.getUserId()+"���ӳɹ�����������......");
					//����Ƿ���������Ϣ��
					if(MessageLoad.exitsMessage(user.getUserId())==true) {
						//����������Ϣ
						Vector<Message> message1=MessageLoad.getMessage(user.getUserId());
						//��������Ϣ����ȥ
						for(int i=0;i<message1.size();i++) {
							//�����������⣬��һ�ε�ʱ��ֱ��ʹ���������oos��ÿ���������������Եģ�������˵����һ�����룬����������
							ObjectOutputStream oos2=new ObjectOutputStream(socket.getOutputStream());
							oos2.writeObject(message1.get(i));
						}
					}
				}else {
					//�û����Ϸ�����һ��ʧ��
					System.out.println("�������Ϳͻ���"+user.getUserId()+"����ʧ��");
					
					message.setMessageType(MessageType.MESSAGE_LOGIN_FAIL);
					oos.writeObject(message);
				}
				
			}
	
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			//������������˳�while��˵�������������������˳�������
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}

		}
	}

	
	
	
}

package com.qq.userClientService;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.qq.common.Message;
import com.qq.common.MessageType;
import com.qq.common.User;
import com.qq.utils.StreamUtils;
import com.qq.utils.Utility;

//����ͷ�����������--�����̹߳���socket����ʵ��һ̨����ͬʱ��������ͻ��ˣ��˶��Ƿ�user������ȷ
//����û���½��֤
public class UserClientService {

	//����ط����ܻ�ʹ�õ��û�
	private User user=null;
	Socket socket=null;
	//����USerId��pwdȥ������������û��Ƿ�Ϸ�
	public boolean checkUser(String userId,String pwd) {
		boolean res=false;
		//����User����
		user=new User(userId,pwd);
		
		try {
			//�ͷ�������������
			socket=new Socket(InetAddress.getByName("127.0.0.1"),9999);
			//��ȡSocket��Object������
			ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
			//��Uer���󴫵���������
			oos.writeObject(user);
			
			//�������˸����յ���user���󣬷���һ��Message��������������ǵ�¼״̬��
			//�ͻ��˽�������ͨ������������������
			ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
			try {
				//��ȡ��message,Ҫ�Ƿ�����û����Ϣ�ͻ��������
				Message message=(Message)ois.readObject();
				
				//������Ϣֵ�жϵ�¼״̬
				if(message.getMessageType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {
					//������Ϣֵ��1����½�ɹ�
					//����һ���ͷ���������ͨѶ���߳�-������һ��ClientConnectServerThread ��
					ClientConnectServerThread clientConnectServerThread=new ClientConnectServerThread(socket);
					//��������
					clientConnectServerThread.start();
					//Ҫʵ�ֶ��û���¼���Ǿ���Ҫһ�����ۺϹ������н������߳�
					//����һ��ManageClientConnectServerThread �����ۺϹ������е��߳�
					//��clientConnectServerThread�̼߳ӵ�ManageClientConnectServerThread��hashMap��
					ManageClientConnectServerThread.addClientConnectServerThread(userId, clientConnectServerThread);
					
					res=true;//��ʾ�������ӳɹ�
					
		
				}else {
					//��½ʧ��
					//�Ͽ�socket
					socket.close();
				}
				
				
			} catch (ClassNotFoundException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return res;
	}
	
	//����������������û��б�
	public void onlineFriendList() {
		//����һ��Message������Ϊ	String MESSAGE_GET_ONLINE_FRIEND ="4";//Ҫ�󷵻������û��б�
		Message message=new Message();
		message.setMessageType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
		message.setSender(user.getUserId());
		//���͸�������
		try {
			//��ȡ�̵߳���socket����������ͨ���Ķ��������
			ObjectOutputStream oos=new ObjectOutputStream(
					ManageClientConnectServerThread.getClientConnectServerThread(
							user.getUserId()).getSocket().getOutputStream());
//			ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
			//����Ϊʲô����socket����Ϊ�����Գ�Ա��Ȼ������������һ����ֱ�ӵ��ã�
			// ���ȣ��������̳߳ص����ã�ΪʲôҪ��������̳߳أ������Ϊ��ʵ�ֶ��û�ͬʱ��½������ʵÿ���û��ڵ�½ʱ��������
			//       ���г����Ǿ���һ���µĽ����ˣ���Դ������ͻ���̳߳ص����������ڲ��˳������˳����е��û���������ڴ�
			//       ��½һ���µ��û����о�Ҳ����ʵ����������ļ����䲢�н��У�һ���û�һ���߳̽������죬һ���߳̽����ļ����䣬
			//        �̳߳ش洢��ͬ���߳�
			
			//����
			oos.writeObject(message);
			
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
	}
	
	//������������˳���Ϣ
	public void clientExit() {
		//message
		Message message=new Message();
		message.setSender(user.getUserId());
		message.setMessageType(MessageType.MESSAGE_CLIENT_EXIT);
		//���͸�������
		
		try {
			ObjectOutputStream oos=new ObjectOutputStream(
					ManageClientConnectServerThread.getClientConnectServerThread(
							user.getUserId()).getSocket().getOutputStream());
			//fasong
			oos.writeObject(message);
			System.out.println(user.getUserId()+"�����˳���Ϣ��������");
			
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
				
	}
	public void chat() {
		System.out.println("������˽�Ķ�����ǳƣ�");
		String getter=Utility.readString(50);
		System.out.println("����˵��");
		String content=Utility.readString(50);
		Message message=new Message();
		message.setContent(content);
		message.setGetter(getter);
		message.setSender(user.getUserId());
		message.setMessageType(MessageType.MESSAGE_COMM_MES);
		LocalDateTime ldt=LocalDateTime.now();//
		DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formatDate=dtf.format(ldt);
		message.setSendTime(formatDate);
		//
		try {
			ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(message);
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
	}
	//�����������û�����Ϣ
	public void groupChat() {
		System.out.println("�Դ��˵��");
		String content=Utility.readString(50);
		Message message=new Message();
		message.setContent(content);
		message.setGetter("all");
		message.setSender(user.getUserId());
		message.setMessageType(MessageType.MESSAGE_COMM_MES);
		LocalDateTime ldt=LocalDateTime.now();//
		DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formatDate=dtf.format(ldt);
		message.setSendTime(formatDate);
		
		//
		try {
			ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(message);
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	
	//�����ļ�
	public void sendFile() {
		System.out.println("��ѡ��Ҫ�����ļ��Ķ���");
		String getter=Utility.readString(50);
		System.out.println("������Ҫ�����ļ��ĵ�ַ��");
		String path=Utility.readString(50);
		//��ȡ�ļ�
		try {
			FileInputStream fi=new FileInputStream(path);
			//byte[] file=StreamUtils.streamToByteArray(bi);
			byte[] file2=StreamUtils.streamToByteArray(fi);
			//��װ��message����
			Message message=new Message();
			message.setFileByte(file2);
			message.setGetter(getter);
			message.setSender(user.getUserId());
			message.setMessageType(MessageType.MESSAGE_SEND_FILE);
			LocalDateTime ldt=LocalDateTime.now();//
			DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String formatDate=dtf.format(ldt);
			message.setSendTime(formatDate);
			//����message����
			
			ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(message);
			fi.close();
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
}

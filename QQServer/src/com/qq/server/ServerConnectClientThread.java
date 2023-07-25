package com.qq.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.qq.common.Message;
import com.qq.common.MessageType;

//�������Ϳͻ������ӵ��߳�
public class ServerConnectClientThread extends Thread{

	private String userId="";
	private Socket socket=null;
	
	public ServerConnectClientThread(String userId, Socket socket) {
		super();
		this.userId = userId;
		this.socket = socket;
	}
	public String getUserId() {
		return userId;
	}
	public Socket getSocket() {
		return socket;
	}
	@Override
	public void run() {//�������Ӻ��߳�һֱ���У������Ƿ�ͻ�������Ϣ���͹���

		while(true) {
			try {
				ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
				//��ͻ��˵������߳�һ�����յ���Ϣ�ͼ���ִ�У�û�յ�����������
				Message message=(Message)ois.readObject();
				//������յ�����Ϣ
				
				
				//������յ�����Ϣ�� String MESSAGE_GET_ONLINE_FRIEND ="4";//Ҫ�󷵻������û��б�
				if(message.getMessageType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
					
					System.out.println(message.getSender()+"����õ������û��б�");
					//��ȡ�����û��б�
					StringBuffer userList=new StringBuffer();
					//ͨ���̳߳�
					//һ������HashMap������
					Set<String> key=ManageServerConnectClient.hm.keySet();
					for(String t: key) {
						userList.append(t);
						userList.append(" ");
					}//��������ȫ�ӵ�һ���ַ�������
					
					//����Ϣ��װ��Message������
					Message friendList =new Message();
					friendList.setContent(userList.substring(0));
					friendList.setMessageType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
					
					//��message�����ͻ���
					ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(friendList);
					
					//���յ��ͻ����˳���Ϣ
				}else if(message.getMessageType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
					
					System.out.println("�û�"+message.getSender()+"�˳�");
					
					//���̳߳���ȥ������߳�
					ManageServerConnectClient.removeServerConnectClientThread(message.getSender());
					//�ر����socket
					socket.close();
					//�˳�ѭ��
					break;
					//˽����Ϣ��ת���ļ�---7/11����ʵ�ָ������û����ļ�����Ϣ
				}else if(message.getMessageType().equals(MessageType.MESSAGE_COMM_MES)||message.getMessageType().equals(MessageType.MESSAGE_SEND_FILE)) {
					//˽��-��-���ļ�
					if (!message.getGetter().equals("all")) {
						//����û������ߣ������ȵ÷�����ȷ��ȷʵ������û�
						//����hshMap
						String getter = message.getGetter();
						if(QQServer.getHm().get(getter)==null) {
							//����������û�
							String sender=message.getSender();
							Socket socketSender = ManageServerConnectClient.getServerConnectClientThread(sender)
									.getSocket();
							//�ظ�����ʧ�ܣ��û�������
							ObjectOutputStream oos = new ObjectOutputStream(socketSender.getOutputStream());
							Message message2=new Message();
							message2.setMessageType(MessageType.MESSAGE_SEND_FAIL);
							oos.writeObject(message2);
						}else {
							//�û�����
							//�ж��û��������
							if(ManageServerConnectClient.exitsServerConnectClientThread(getter)) {
								//����
								//�ҵ������߶�Ӧ��socket
								Socket socketGetter = ManageServerConnectClient.getServerConnectClientThread(getter)
										.getSocket();
								//��Ϣת��
								ObjectOutputStream oos = new ObjectOutputStream(socketGetter.getOutputStream());
								oos.writeObject(message);
							}else {
								//������
								//��ʱ���ǽ�message�浽һ��Collection���������һ���µ��̣߳��ල���ߵ��û�����Ŀ���û�����ʱ������
								MessageLoad.addMessage(message);
							}
							
	
						}			
						
						//Ⱥ��
					}else {
						/*����
						 * //��EntrySetȡk-v
		                     Set entrySet=hashMap.entrySet();
		                  //(1)��ǿfor
		                     System.out.println("\n=====EntrySet��ǿfor=======");
		                     for(Object t:entrySet) {
			                    //ȡ����������HashMap$Node,Nodeʵ����Map.ENtry������ת�ͣ��������ķ���
			                     Map.Entry entry=(Map.Entry)t;//����ת��
			                     System.out.println(entry.getKey()+"-"+entry.getValue());  }
						 */
						//ֱ��ȡvalue
						Collection<ServerConnectClientThread> ser=ManageServerConnectClient.hm.values();
						for(ServerConnectClientThread t:ser) {
							if(t.getUserId().equals(message.getSender()))continue;
							ObjectOutputStream oos =new ObjectOutputStream(t.getSocket().getOutputStream());
							oos.writeObject(message);
							
						}
					}
					
				}
				
				
				
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			
		}
	}
	
	
	
}

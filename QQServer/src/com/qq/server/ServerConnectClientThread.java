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

//服务器和客户端连接的线程
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
	public void run() {//建立连接后线程一直运行，监听是否客户端有消息发送过来

		while(true) {
			try {
				ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
				//像客户端的连接线程一样，收到消息就继续执行，没收到就阻塞在这
				Message message=(Message)ois.readObject();
				//处理接收到的消息
				
				
				//如果接收到的消息是 String MESSAGE_GET_ONLINE_FRIEND ="4";//要求返回在线用户列表
				if(message.getMessageType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
					
					System.out.println(message.getSender()+"请求得到在线用户列表");
					//获取在线用户列表
					StringBuffer userList=new StringBuffer();
					//通过线程池
					//一个遍历HashMap的问题
					Set<String> key=ManageServerConnectClient.hm.keySet();
					for(String t: key) {
						userList.append(t);
						userList.append(" ");
					}//这样就完全加到一个字符串里了
					
					//把信息封装到Message对象里
					Message friendList =new Message();
					friendList.setContent(userList.substring(0));
					friendList.setMessageType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
					
					//把message发给客户端
					ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(friendList);
					
					//接收到客户端退出消息
				}else if(message.getMessageType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
					
					System.out.println("用户"+message.getSender()+"退出");
					
					//从线程池里去除这个线程
					ManageServerConnectClient.removeServerConnectClientThread(message.getSender());
					//关闭这个socket
					socket.close();
					//退出循环
					break;
					//私聊消息或转发文件---7/11考虑实现给离线用户发文件和消息
				}else if(message.getMessageType().equals(MessageType.MESSAGE_COMM_MES)||message.getMessageType().equals(MessageType.MESSAGE_SEND_FILE)) {
					//私聊-和-发文件
					if (!message.getGetter().equals("all")) {
						//如果用户不在线，但首先得服务器确认确实有这个用户
						//遍历hshMap
						String getter = message.getGetter();
						if(QQServer.getHm().get(getter)==null) {
							//不存在这个用户
							String sender=message.getSender();
							Socket socketSender = ManageServerConnectClient.getServerConnectClientThread(sender)
									.getSocket();
							//回复发送失败，用户不存在
							ObjectOutputStream oos = new ObjectOutputStream(socketSender.getOutputStream());
							Message message2=new Message();
							message2.setMessageType(MessageType.MESSAGE_SEND_FAIL);
							oos.writeObject(message2);
						}else {
							//用户存在
							//判断用户在线与否
							if(ManageServerConnectClient.exitsServerConnectClientThread(getter)) {
								//在线
								//找到接收者对应的socket
								Socket socketGetter = ManageServerConnectClient.getServerConnectClientThread(getter)
										.getSocket();
								//消息转发
								ObjectOutputStream oos = new ObjectOutputStream(socketGetter.getOutputStream());
								oos.writeObject(message);
							}else {
								//不在线
								//暂时考虑将message存到一个Collection集合里，创立一个新的线程，监督上线的用户·，目标用户上线时给他发
								MessageLoad.addMessage(message);
							}
							
	
						}			
						
						//群发
					}else {
						/*遍历
						 * //用EntrySet取k-v
		                     Set entrySet=hashMap.entrySet();
		                  //(1)增强for
		                     System.out.println("\n=====EntrySet增强for=======");
		                     for(Object t:entrySet) {
			                    //取到的是整个HashMap$Node,Node实现了Map.ENtry，可以转型，利用他的方法
			                     Map.Entry entry=(Map.Entry)t;//向上转型
			                     System.out.println(entry.getKey()+"-"+entry.getValue());  }
						 */
						//直接取value
						Collection<ServerConnectClientThread> ser=ManageServerConnectClient.hm.values();
						for(ServerConnectClientThread t:ser) {
							if(t.getUserId().equals(message.getSender()))continue;
							ObjectOutputStream oos =new ObjectOutputStream(t.getSocket().getOutputStream());
							oos.writeObject(message);
							
						}
					}
					
				}
				
				
				
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
		}
	}
	
	
	
}

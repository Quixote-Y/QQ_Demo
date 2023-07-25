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

//服务器监听9999端口，等待客户端连接，并保持通讯
public class QQServer {

	private ServerSocket serverSocket = null;
	//先使用一个HahsMap来存放已有用户，之后替换为sql数据库
	//多线程使用ConcurrentHashMap，线程安全，hashMap线程不安全
	private static  ConcurrentHashMap<String, User> hm=new ConcurrentHashMap<>();
	//静态代码块 ，用户列表
	static {
		hm.put("100", new User("100","123456"));
		hm.put("200", new User("200","123456"));
		hm.put("300", new User("300","123456"));
		hm.put("400", new User("400","123456"));
		hm.put("孙悟空", new User("孙悟空","123456"));
		hm.put("唐僧", new User("唐僧","123456"));
	}
	
	public static ConcurrentHashMap<String, User> getHm() {
		return hm;
	}

	//提供一个方法，检验用户是否合法
	public boolean checkUser(User user) {
		String userId=user.getUserId();
		if(hm.get(userId)==null) {
			return false;
		}//hashMapget方法返回null，则键值不存在
		String passwd=user.getPassword();
		if(!passwd.equals(hm.get(userId).getPassword())) {
			return false;
		}
		return true;
	}

	public QQServer() {

		try {
			System.out.println("服务器端监听9999端口等待客户端连接");
			serverSocket = new ServerSocket(9999);
			//开启服务器群发
			new ServerSendMessage().start(); 
			//开始监听,一直监听
			while(true) {
				Socket socket=serverSocket.accept();//监听直到有客户端连接
				//获取里面的user对象
				ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
				User user=(User)ois.readObject();
				Message message=new Message();
				
				
				//和现有user对象进行比对，返回结果
				//这里假设100，123456
				if(checkUser(user)) {
					//用户合法
					//回一个登陆成功
					message.setMessageType(MessageType.MESSAGE_LOGIN_SUCCEED);
					oos.writeObject(message);
					//建立和维护一个和客户端连接的线程
					ServerConnectClientThread serverConnectClientThread=new ServerConnectClientThread(user.getUserId(), socket);
					//线程启动
					serverConnectClientThread.start();
					//管理线程 -》ManageServerConnectClient 类
					ManageServerConnectClient.addServerConnectClientThread(user.getUserId(), serverConnectClientThread);
					System.out.println("服务器和客户端"+user.getUserId()+"连接成功，传输数据......");
					//检查是否有滞留消息，
					if(MessageLoad.exitsMessage(user.getUserId())==true) {
						//存在滞留消息
						Vector<Message> message1=MessageLoad.getMessage(user.getUserId());
						//吧滞留消息发过去
						for(int i=0;i<message1.size();i++) {
							//这里会出现问题，第一次的时候直接使用了上面的oos，每个输入输出都是相对的，并不是说可以一个输入，多个输出来读
							ObjectOutputStream oos2=new ObjectOutputStream(socket.getOutputStream());
							oos2.writeObject(message1.get(i));
						}
					}
				}else {
					//用户不合法，回一个失败
					System.out.println("服务器和客户端"+user.getUserId()+"连接失败");
					
					message.setMessageType(MessageType.MESSAGE_LOGIN_FAIL);
					oos.writeObject(message);
				}
				
			}
	
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			//如果服务器端退出while，说明服务器结束监听，退出程序了
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}

		}
	}

	
	
	
}

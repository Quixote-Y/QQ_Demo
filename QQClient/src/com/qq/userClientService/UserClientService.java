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

//负责和服务器端连接--》多线程管理socket，以实现一台主机同时启动多个客户端，核对是否user对象正确
//完成用户登陆验证
public class UserClientService {

	//多个地方可能回使用到用户
	private User user=null;
	Socket socket=null;
	//根据USerId和pwd去服务器检验该用户是否合法
	public boolean checkUser(String userId,String pwd) {
		boolean res=false;
		//创建User对象
		user=new User(userId,pwd);
		
		try {
			//和服务器建立连接
			socket=new Socket(InetAddress.getByName("127.0.0.1"),9999);
			//获取Socket的Object对象流
			ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
			//将Uer对象传到服务器端
			oos.writeObject(user);
			
			//服务器端根据收到的user对象，返回一个Message对象，里面的数据是登录状态，
			//客户端接收数据通道服务器发来的数据
			ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
			try {
				//读取到message,要是服务器没回消息就会在这堵塞
				Message message=(Message)ois.readObject();
				
				//根据消息值判断登录状态
				if(message.getMessageType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {
					//返回消息值是1，登陆成功
					//创建一个和服务器保持通讯的线程-》创建一个ClientConnectServerThread 类
					ClientConnectServerThread clientConnectServerThread=new ClientConnectServerThread(socket);
					//启动监听
					clientConnectServerThread.start();
					//要实现多用户登录，那就需要一个类综合管理所有建立的线程
					//创建一个ManageClientConnectServerThread 类来综合管理所有的线程
					//将clientConnectServerThread线程加到ManageClientConnectServerThread的hashMap里
					ManageClientConnectServerThread.addClientConnectServerThread(userId, clientConnectServerThread);
					
					res=true;//表示建立连接成功
					
		
				}else {
					//登陆失败
					//断开socket
					socket.close();
				}
				
				
			} catch (ClassNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return res;
	}
	
	//向服务器请求在线用户列表
	public void onlineFriendList() {
		//发送一个Message，类型为	String MESSAGE_GET_ONLINE_FRIEND ="4";//要求返回在线用户列表
		Message message=new Message();
		message.setMessageType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
		message.setSender(user.getUserId());
		//发送给服务器
		try {
			//获取线程的与socket相连的数据通道的对线输出流
			ObjectOutputStream oos=new ObjectOutputStream(
					ManageClientConnectServerThread.getClientConnectServerThread(
							user.getUserId()).getSocket().getOutputStream());
//			ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
			//关于为什么不把socket设置为类属性成员，然后这里向上面一样的直接调用：
			// 首先：得理清线程池的作用：为什么要创建这个线程池，起初是为了实现多用户同时登陆，但其实每个用户在登陆时都会重新
			//       运行程序，那就是一个新的进程了，资源互不冲突，线程池的作用让你在不退出程序不退出现有的用户的情况下在次
			//       登陆一个新的用户，感觉也可以实现在聊天和文件传输并行进行，一个用户一个线程进行聊天，一个线程进行文件传输，
			//        线程池存储不同的线程
			
			//发送
			oos.writeObject(message);
			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	
	//向服务器发送退出消息
	public void clientExit() {
		//message
		Message message=new Message();
		message.setSender(user.getUserId());
		message.setMessageType(MessageType.MESSAGE_CLIENT_EXIT);
		//发送给服务器
		
		try {
			ObjectOutputStream oos=new ObjectOutputStream(
					ManageClientConnectServerThread.getClientConnectServerThread(
							user.getUserId()).getSocket().getOutputStream());
			//fasong
			oos.writeObject(message);
			System.out.println(user.getUserId()+"发送退出消息给服务器");
			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
				
	}
	public void chat() {
		System.out.println("请输入私聊对象的昵称：");
		String getter=Utility.readString(50);
		System.out.println("对他说：");
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
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	//给所有在线用户发消息
	public void groupChat() {
		System.out.println("对大家说：");
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
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	//发送文件
	public void sendFile() {
		System.out.println("请选择要传输文件的对象：");
		String getter=Utility.readString(50);
		System.out.println("请输入要传输文件的地址：");
		String path=Utility.readString(50);
		//获取文件
		try {
			FileInputStream fi=new FileInputStream(path);
			//byte[] file=StreamUtils.streamToByteArray(bi);
			byte[] file2=StreamUtils.streamToByteArray(fi);
			//包装到message对象
			Message message=new Message();
			message.setFileByte(file2);
			message.setGetter(getter);
			message.setSender(user.getUserId());
			message.setMessageType(MessageType.MESSAGE_SEND_FILE);
			LocalDateTime ldt=LocalDateTime.now();//
			DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String formatDate=dtf.format(ldt);
			message.setSendTime(formatDate);
			//发送message对象
			
			ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(message);
			fi.close();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}

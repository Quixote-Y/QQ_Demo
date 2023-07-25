package com.qq.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Scanner;

import com.qq.common.Message;
import com.qq.common.MessageType;

public class ServerSendMessage extends Thread {
	//生成一个线程，负责检测服务器是否输入关键字 通知 消息
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Scanner scanner=new Scanner(System.in);
			while(true) {
				String input=scanner.nextLine();
				if(input.equals("notice")) {
					System.out.println("请输入通知消息：");
					String content=scanner.nextLine();
					Message message=new Message();
					message.setContent(content);
					message.setSender("服务器");
					message.setMessageType(MessageType.MESSAGE_COMM_MES);
					LocalDateTime ldt=LocalDateTime.now();//
					DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					String formatDate=dtf.format(ldt);
					message.setSendTime(formatDate);
					
					ObjectOutputStream oos=null;
					//遍历hashmap
					Collection<ServerConnectClientThread> serverConnectClientThreads=ManageServerConnectClient.hm.values();
					for(ServerConnectClientThread t:serverConnectClientThreads) {
						try {
							oos=new ObjectOutputStream(t.getSocket().getOutputStream());
							oos.writeObject(message);
						} catch (IOException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}


					}
					System.out.println("发送成功");
				}
			}
		}
}

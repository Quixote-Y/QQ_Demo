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
	//����һ���̣߳�������������Ƿ�����ؼ��� ֪ͨ ��Ϣ
		@Override
		public void run() {
			// TODO �Զ����ɵķ������
			Scanner scanner=new Scanner(System.in);
			while(true) {
				String input=scanner.nextLine();
				if(input.equals("notice")) {
					System.out.println("������֪ͨ��Ϣ��");
					String content=scanner.nextLine();
					Message message=new Message();
					message.setContent(content);
					message.setSender("������");
					message.setMessageType(MessageType.MESSAGE_COMM_MES);
					LocalDateTime ldt=LocalDateTime.now();//
					DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					String formatDate=dtf.format(ldt);
					message.setSendTime(formatDate);
					
					ObjectOutputStream oos=null;
					//����hashmap
					Collection<ServerConnectClientThread> serverConnectClientThreads=ManageServerConnectClient.hm.values();
					for(ServerConnectClientThread t:serverConnectClientThreads) {
						try {
							oos=new ObjectOutputStream(t.getSocket().getOutputStream());
							oos.writeObject(message);
						} catch (IOException e) {
							// TODO �Զ����ɵ� catch ��
							e.printStackTrace();
						}


					}
					System.out.println("���ͳɹ�");
				}
			}
		}
}

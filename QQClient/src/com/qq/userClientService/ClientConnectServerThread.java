package com.qq.userClientService;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

import com.qq.common.Message;
import com.qq.common.MessageType;
import com.qq.utils.Utility;
import com.qq.view.View;

//维护客户端和服务器的连接
public class ClientConnectServerThread extends Thread {

	// 线程持有Socket
	private Socket socket = null;
	

	// 关于怎样得到socket，构造器就可以提供
	public ClientConnectServerThread(Socket socket) {
		super();
		this.socket = socket;
	}
	// get获取这个线程的socket

	public Socket getSocket() {
		return socket;
	}
	

	@Override
	public void run() {
		// TODO 自动生成的方法存根
		super.run();
		// 线程开启就一直和服务器连接，直到程序结束
		while (true) {
			// 在这个线程里客户端一直读取数据通道，如果有服务器发来的数据就接受，如果没有就阻塞在这等着
			try {
				// System.out.println("客户端等待服务器发来数据");
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				// 读取
				Message message = (Message) ois.readObject();
				// 后期根据message进行相应操作

				// 如果读取到的是 服务器返回的在线用户列表
				if (message.getMessageType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {

					// 规定服务器返回用户在线列表时，每个用户名添加一个空格隔开
					String[] userList = message.getContent().split(" ");// 将里面的内容按空格分隔开
					// s输出显示
					System.out.println("\n=================在线用户列表==========");
					for (int i = 0; i < userList.length; i++) {
						System.out.println("用户名：" + userList[i]);
					}
					// 收到转发的聊天
				} else if (message.getMessageType().equals(MessageType.MESSAGE_COMM_MES)) {
					System.out.println("\n时间" + message.getSendTime());
					System.out.println(message.getSender() + ":" + message.getContent());
					// 收到转发文件
				} else if (message.getMessageType().equals(MessageType.MESSAGE_SEND_FILE)) {
					System.out.println(
							"\n" + message.getSender() + " 给你发来了一个文件，请输入文件存放地址：");
					System.out.print("请先输入1");
					//输入的时候和主线程的输入冲突了
					Scanner scanner=new Scanner(System.in);
					String path =scanner.next();
					FileOutputStream fos = new FileOutputStream(path);
					fos.write(message.getFileByte());
					System.out.println("文件成功存储到地址" + path);

					
					fos.close();
				}else if(message.getMessageType().equals(MessageType.MESSAGE_SEND_FAIL)) {
					System.out.println("该用户不存在或已经注销");
				}

			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}

		}
	}

}
/*
 * 对象流不同于普通的字节流，当对象流中没有数据时，程序却尝试读取数据，会报EOFException；而字节流就不会出现这种情况，字节流会返回-1

ObjectInputStream写入的数据，在ObjectOutputStream上读取时，应该按照相同的数据类型依次读取，否则数据类型不等会抛出EOFException

最好在实际使用的过程中，我们先实例化ObjectOutputStream，再实例化 ObjectInputStream，这是由这两个类的设计思想所决定的。如此能保证在同一资源的对象流ObjectInputStream能够及时读取到序列化头而不至于阻塞或者引发EOF异常(阻塞对应于Socket IO，EOF异常对应于文件IO)
*/

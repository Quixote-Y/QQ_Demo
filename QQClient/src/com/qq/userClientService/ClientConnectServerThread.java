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

//ά���ͻ��˺ͷ�����������
public class ClientConnectServerThread extends Thread {

	// �̳߳���Socket
	private Socket socket = null;
	

	// ���������õ�socket���������Ϳ����ṩ
	public ClientConnectServerThread(Socket socket) {
		super();
		this.socket = socket;
	}
	// get��ȡ����̵߳�socket

	public Socket getSocket() {
		return socket;
	}
	

	@Override
	public void run() {
		// TODO �Զ����ɵķ������
		super.run();
		// �߳̿�����һֱ�ͷ��������ӣ�ֱ���������
		while (true) {
			// ������߳���ͻ���һֱ��ȡ����ͨ��������з��������������ݾͽ��ܣ����û�о������������
			try {
				// System.out.println("�ͻ��˵ȴ���������������");
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				// ��ȡ
				Message message = (Message) ois.readObject();
				// ���ڸ���message������Ӧ����

				// �����ȡ������ ���������ص������û��б�
				if (message.getMessageType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {

					// �涨�����������û������б�ʱ��ÿ���û������һ���ո����
					String[] userList = message.getContent().split(" ");// ����������ݰ��ո�ָ���
					// s�����ʾ
					System.out.println("\n=================�����û��б�==========");
					for (int i = 0; i < userList.length; i++) {
						System.out.println("�û�����" + userList[i]);
					}
					// �յ�ת��������
				} else if (message.getMessageType().equals(MessageType.MESSAGE_COMM_MES)) {
					System.out.println("\nʱ��" + message.getSendTime());
					System.out.println(message.getSender() + ":" + message.getContent());
					// �յ�ת���ļ�
				} else if (message.getMessageType().equals(MessageType.MESSAGE_SEND_FILE)) {
					System.out.println(
							"\n" + message.getSender() + " ���㷢����һ���ļ����������ļ���ŵ�ַ��");
					System.out.print("��������1");
					//�����ʱ������̵߳������ͻ��
					Scanner scanner=new Scanner(System.in);
					String path =scanner.next();
					FileOutputStream fos = new FileOutputStream(path);
					fos.write(message.getFileByte());
					System.out.println("�ļ��ɹ��洢����ַ" + path);

					
					fos.close();
				}else if(message.getMessageType().equals(MessageType.MESSAGE_SEND_FAIL)) {
					System.out.println("���û������ڻ��Ѿ�ע��");
				}

			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}

		}
	}

}
/*
 * ��������ͬ����ͨ���ֽ���������������û������ʱ������ȴ���Զ�ȡ���ݣ��ᱨEOFException�����ֽ����Ͳ����������������ֽ����᷵��-1

ObjectInputStreamд������ݣ���ObjectOutputStream�϶�ȡʱ��Ӧ�ð�����ͬ�������������ζ�ȡ�������������Ͳ��Ȼ��׳�EOFException

�����ʵ��ʹ�õĹ����У�������ʵ����ObjectOutputStream����ʵ���� ObjectInputStream��������������������˼���������ġ�����ܱ�֤��ͬһ��Դ�Ķ�����ObjectInputStream�ܹ���ʱ��ȡ�����л�ͷ��������������������EOF�쳣(������Ӧ��Socket IO��EOF�쳣��Ӧ���ļ�IO)
*/

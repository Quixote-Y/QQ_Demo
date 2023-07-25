package com.qq.view;

import com.qq.userClientService.ClientConnectServerThread;
import com.qq.userClientService.UserClientService;
import com.qq.utils.*;

public class View {

	private boolean loop = true;//�����Ƿ�����˵�
	

	private UserClientService userClientService=new UserClientService();//�����û���¼/ע�����

	public static void main(String[] args) {
		new View().mainMenu();
	}

	// ��ʾ���˵�
	public void mainMenu() {
		// ��loopΪ��һֱѭ��
		while (loop) {
			print1();

		}
	}

	// ��ӡһ���˵�
	public void print1() {
		System.out.println("===================��¼QQ=================");
		System.out.println("\t\t" + "1 ��¼ϵͳ");
		System.out.println("\t\t" + "9 �˳���¼");
		// �Ӽ��̶�ȡһ������
		System.out.print("���������ѡ��");
		int sysIn = Utility.readInt();
		switch (sysIn) {
		case 1:
			System.out.print("�������û����� ");
			// ���û���
			String userId = Utility.readString(50);
			System.out.print("���������룺");
			// du����
			String password = Utility.readString(50);
			// ȥ�������֤�û��Ƿ�Ϸ�������һ��ֵ
			/* */
			//�����û�������ȥ����˼���Ƿ�Ϸ�
			boolean res =userClientService.checkUser(userId, password);
			if (res) {
				// ��ӡ2���û��˵�
				while (loop) {
					print2(userId);
				}
			} else {
				// ����˺��������ѭ���ٴ�����
				System.out.println("�˺Ż������������������");
			}
			break;
		case 9:
			loop = false;
			break;
		}
	}

	public void print2(String userId) {
		// ��ӡ2���û��˵�
		System.out.println("\n\n==================�ǳƣ�" + userId + "==============");
		System.out.println("\t\t 1 ��ʾ�����û��б�");
		System.out.println("\t\t 2 Ⱥ����Ϣ");
		System.out.println("\t\t 3 ˽����Ϣ");
		System.out.println("\t\t 4 �����ļ�");
		System.out.println("\t\t 9 �˳�");
		System.out.print("���������ѡ��");
		int key =Utility.readInt();
		switch (key) {
		case 1:
			//System.out.println("\t\t 1 ��ʾ�����û��б�");
			//дһ������ʵ�ֻ�ȡ�û��б�
			//�û��б����ڷ������˵ĵ�¼�û�
			userClientService.onlineFriendList();
			break;
		case 2:
			//Ⱥ��
			//System.out.println("\t\t 2 Ⱥ����Ϣ");
			userClientService.groupChat();
			break;
		case 3:
			//һ�����������ܼ�����������ݣ��������շ�����װ��message�������������������ת�������շ�
			//System.out.println("\t\t 3 ˽����Ϣ");
			userClientService.chat();
			break;
		case 4:
			//System.out.println("\t\t 4 �����ļ�");
			userClientService.sendFile();
			break;
		case 9:
			System.out.println("\t\t 9 �˳�");
			//����дһ�����������͸�message����������˵������ͻ������ߣ����ѷ������˹ر������·��socket
			userClientService.clientExit();
			System.exit(0);
			loop = false;
			break;
		}
	}
}

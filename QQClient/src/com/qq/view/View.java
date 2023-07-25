package com.qq.view;

import com.qq.userClientService.ClientConnectServerThread;
import com.qq.userClientService.UserClientService;
import com.qq.utils.*;

public class View {

	private boolean loop = true;//控制是否输入菜单
	

	private UserClientService userClientService=new UserClientService();//用于用户登录/注册服务

	public static void main(String[] args) {
		new View().mainMenu();
	}

	// 显示主菜单
	public void mainMenu() {
		// 当loop为真一直循环
		while (loop) {
			print1();

		}
	}

	// 打印一级菜单
	public void print1() {
		System.out.println("===================登录QQ=================");
		System.out.println("\t\t" + "1 登录系统");
		System.out.println("\t\t" + "9 退出登录");
		// 从键盘读取一个整数
		System.out.print("请输入你的选择：");
		int sysIn = Utility.readInt();
		switch (sysIn) {
		case 1:
			System.out.print("请输入用户名： ");
			// 读用户名
			String userId = Utility.readString(50);
			System.out.print("请输入密码：");
			// du密码
			String password = Utility.readString(50);
			// 去服务端验证用户是否合法，返回一个值
			/* */
			//根据用户名密码去服务端检查是否合法
			boolean res =userClientService.checkUser(userId, password);
			if (res) {
				// 打印2级用户菜单
				while (loop) {
					print2(userId);
				}
			} else {
				// 输出账号密码错误，循环再次输入
				System.out.println("账号或密码错误，请重新输入");
			}
			break;
		case 9:
			loop = false;
			break;
		}
	}

	public void print2(String userId) {
		// 打印2级用户菜单
		System.out.println("\n\n==================昵称：" + userId + "==============");
		System.out.println("\t\t 1 显示在线用户列表");
		System.out.println("\t\t 2 群发消息");
		System.out.println("\t\t 3 私聊消息");
		System.out.println("\t\t 4 发送文件");
		System.out.println("\t\t 9 退出");
		System.out.print("请输入你的选择：");
		int key =Utility.readInt();
		switch (key) {
		case 1:
			//System.out.println("\t\t 1 显示在线用户列表");
			//写一个方法实现获取用户列表
			//用户列表是在服务器端的登录用户
			userClientService.onlineFriendList();
			break;
		case 2:
			//群发
			//System.out.println("\t\t 2 群发消息");
			userClientService.groupChat();
			break;
		case 3:
			//一个方法，接受键盘输入的数据，包含接收方，包装到message里，发给服务器，服务器转发给接收方
			//System.out.println("\t\t 3 私聊消息");
			userClientService.chat();
			break;
		case 4:
			//System.out.println("\t\t 4 发送文件");
			userClientService.sendFile();
			break;
		case 9:
			System.out.println("\t\t 9 退出");
			//这里写一个方法，发送个message给服务器，说明这个客户端下线，提醒服务器端关闭这个链路的socket
			userClientService.clientExit();
			System.exit(0);
			loop = false;
			break;
		}
	}
}

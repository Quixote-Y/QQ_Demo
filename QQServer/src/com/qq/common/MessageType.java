package com.qq.common;

//用于表示你接受到的消息的类型
public interface MessageType {

	//在接口中定义一些常量，不同的常量值表示不同的消息消息
	//接口中的常量默认为  public static 类型
	//关于为什么这样的形式不采用枚举类型，我在网上查到的资料是
	
	//:接口的属性和枚举类的对象是一样的效果,简单的数据枚举用接口更方便
	
	String MESSAGE_LOGIN_SUCCEED ="1";//登录成功
	String MESSAGE_LOGIN_FAIL ="2";//登陆失败
	String MESSAGE_COMM_MES ="3";//普通消息包
	String MESSAGE_GET_ONLINE_FRIEND ="4";//要求返回在线用户列表
	String MESSAGE_RET_ONLINE_FRIEND ="5";//返回在线用户列表
	String MESSAGE_CLIENT_EXIT ="6";//客户端请求退出
	String MESSAGE_SEND_FILE="7";//发送文件
	String MESSAGE_SEND_FAIL="8";//发送失败
}

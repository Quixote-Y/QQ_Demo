package com.qq.common;

//���ڱ�ʾ����ܵ�����Ϣ������
public interface MessageType {

	//�ڽӿ��ж���һЩ��������ͬ�ĳ���ֵ��ʾ��ͬ����Ϣ��Ϣ
	//�ӿ��еĳ���Ĭ��Ϊ  public static ����
	//����Ϊʲô��������ʽ������ö�����ͣ��������ϲ鵽��������
	
	//:�ӿڵ����Ժ�ö����Ķ�����һ����Ч��,�򵥵�����ö���ýӿڸ�����
	
	String MESSAGE_LOGIN_SUCCEED ="1";//��¼�ɹ�
	String MESSAGE_LOGIN_FAIL ="2";//��½ʧ��
	String MESSAGE_COMM_MES ="3";//��ͨ��Ϣ��
	String MESSAGE_GET_ONLINE_FRIEND ="4";//Ҫ�󷵻������û��б�
	String MESSAGE_RET_ONLINE_FRIEND ="5";//���������û��б�
	String MESSAGE_CLIENT_EXIT ="6";//�ͻ��������˳�
	String MESSAGE_SEND_FILE="7";//�����ļ�
	String MESSAGE_SEND_FAIL="8";//����ʧ��
}

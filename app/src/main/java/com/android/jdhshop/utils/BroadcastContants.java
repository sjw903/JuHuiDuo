package com.android.jdhshop.utils;

/**
 * 广播配置类
 * 
 * @author 陈飞
 *
 */
public class BroadcastContants {

	public static String getPack() {
		return "com.android.jdhshop";
	}
	//传任何消息类
	public static String sendAllObjectMessage=getPack()+"SENDALLOBJECTMESSAGE";

	public static String sendServiceMessage=getPack()+"SEND_SERVICE_MESSAGE";
	//获取用户信息
	public static String sendUserMessage=getPack()+"SENDUSERMESSAGE";
	public static String sendLoginMessage=getPack()+"SEND_SERVICE_MESSAGE";
	//发送我的用户请求
	public static String sendUserInfoMessage=getPack()+"SENDUSERINFOMESSAGE";
	//发送地址刷新
	public static String sendAddressMessage=getPack()+"SENDADDRESSMESSAGE";
	//发送搜索内容
	public static String sendSearchMessage=getPack()+"SENDSEARCHMESSAGE";
	//发送关闭搜索内容
	public static String sendCloseSearchMessage=getPack()+"SENDCLOSESEARCHMESSAGE";
	//发送刷新收藏的消息
	public static String sendRefreshCollectionMessage=getPack()+"SENDREFRESHCOLLECTIONMESSAGE";
	//刷新历史记录列表
	public static String sendRefreshHistoryList=getPack()+"SENDREFRESHHISTORYLIST";

	//发送支付成功的消息
	public static String sendPaymentSuccessMessage = getPack() + "SENDPAYMENTSUCCESSMESSAGE";
}

package com.cklogic.model;

import java.io.UnsupportedEncodingException;

import com.cklogic.tool.GetHttp;
import com.google.gson.Gson;

public class TulinMsg {
	public String key="8e382693edb9f93ec18789bdfb34d830";
	public String info;
	class Msg{
		public int code;
		public String text;
	}
	public static String getMsg(String msg){
		Gson gson = new Gson();
		Msg msginfo=gson.fromJson(msg, Msg.class);
		return msginfo.text;
	}
	public String sendMsg(String msgText) throws UnsupportedEncodingException{
		msgText=java.net.URLEncoder.encode(msgText,"utf-8"); 
		String rStr=GetHttp.postText("http://www.tuling123.com/openapi/api","key=8e382693edb9f93ec18789bdfb34d830&info="+msgText);
		return getMsg(rStr);
	}
}

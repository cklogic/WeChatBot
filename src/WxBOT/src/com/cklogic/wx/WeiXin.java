package com.cklogic.wx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cklogic.model.MemberContact;
import com.cklogic.model.MsgList;
import com.cklogic.model.SyncKey;
import com.cklogic.tool.GetHttp;
import com.google.gson.Gson;

public class WeiXin {
	public String getUUID() throws Exception{
		Date date = new Date();
		String text=GetHttp.getText("http://login.wx.qq.com/jslogin?appid=wx782c26e4c19acffb&redirect_uri=https%3A%2F%2Fwx.qq.com%2Fcgi-bin%2Fmmwebwx-bin%2Fwebwxnewloginpage&fun=new&lang=zh_CN&_="+date.getTime());
		String uuid=find(text,"= \"","\";");
		return uuid;
	}
	public String getQR(String uuid) throws Exception{
		InputStream is = GetHttp.getIOStream("http://login.weixin.qq.com/qrcode/"+uuid); 
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		OutputStream os = new FileOutputStream(new File("D:/temp/"+uuid+".png"));
		while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		is.close();
		return "D:/temp/"+uuid+".png";
	}
	public String getTicket(String uuid) throws Exception{
		Date date = new Date();
		String text=GetHttp.getText("http://login.weixin.qq.com/cgi-bin/mmwebwx-bin/login?tip=1&uuid="+uuid+"&_="+date.getTime());
		String ticket=find(text,"ticket=","&uuid");
		return ticket;
	}
	public String[] get_skey_passticket_wxsid_wxuin_cookieStr(String ticket,String uuid) throws Exception{
		Date date = new Date();
		Map<String, List<String>>[] maps=GetHttp.getTextAndCookies("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage?ticket="+ticket+"&uuid="+uuid+"&lang=zh_CN&scan="+date.getTime()+"&fun=new&version=v2");
		
		
		String text=maps[1].get("TEXT").get(0);
		Map<String, List<String>> map = maps[0];
		List<String> cookies=map.get("Set-Cookie");
		String webwx_data_tickets=cookies.get(2);
		String webwx_data_ticket=find(webwx_data_tickets,"webwx_data_ticket=","; ");
		String cookieStr="";
		for (int i = 0; i < cookies.size(); i++) {
			String cookie=cookies.get(i);
			int num=cookie.indexOf(';');
			cookie=cookie.substring(0, num+1);
			cookieStr+=cookie+" ";
		}
		
		String skey=find(text,"<skey>","</skey>");
		String pass_ticket = find(text,"<pass_ticket>","</pass_ticket>");
		String wxsid = find(text,"<wxsid>","</wxsid>");
		String wxuin = find(text,"<wxuin>","</wxuin>");
		String tempStr[]=new String [5];
		tempStr[0]=skey;
		tempStr[1]=pass_ticket;
		tempStr[2]=wxsid;
		tempStr[3]=wxuin;
		tempStr[4]=cookieStr;
		return tempStr;
	}
	public static String find(String s,String s1,String s2){
		String regex = "(?<="+s1+").*?(?="+s2+")";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(s);
		String group="";
		if (matcher.find()) {
			group = matcher.group();
		}
		return group;
	}
	public String getUserList(String pass_ticket,String skey) throws Exception{
		Date date = new Date();
		String text=GetHttp.getText("http://wx.qq.com/cgi-bin/mmwebwx-bin/webwxgetcontact?pass_ticket="+pass_ticket+"&r="+date.getTime()+"&seq=0&skey="+skey);
		String ticket=find(text,"ticket=","&uuid");
		System.out.println(text);
		return ticket;
	}
	public MsgList getMsg(String pass_ticket,String skey,String wxuin,String wxsid,String deviceID,String synckey,String cookieStr) throws Exception{
		Date date = new Date();
		
		String parm="{\"BaseRequest\":{\"Uin\":"+wxuin+",\"Sid\":\""+wxsid+"\",\"Skey\":\""+skey+"\",\"DeviceID\":\""+deviceID+"\"},\"SyncKey\":"+synckey+",\"rr\":"+date.getTime()+"}";
		String text=GetHttp.postText("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxsync?sid="+wxsid+"&skey="+skey+"&pass_ticket="+pass_ticket,parm,cookieStr);
		/*String syncKey=find(text,"\"SyncCheckKey\": ","}]}}");
		syncKey+="}]}";
		String msg=find(text,"\"Content\": ","\",");*/
		Gson gson = new Gson();
		MsgList msgs = gson.fromJson(text, MsgList.class);
		return msgs;
	}
	public String[] get_uname_synckey(String wxuin,String wxsid,String skey,String pass_ticket,String deviceID){
		Date date = new Date();
		String parm="{\"BaseRequest\":{\"Uin\":\""+wxuin+"\",\"Sid\":\""+wxsid+"\",\"Skey\":\""+skey+"\",\"DeviceID\":\""+deviceID+"\"}}";
		String url="https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxinit?r="+date.getTime()+"&lang=zh_CN&pass_ticket="+pass_ticket+"";
		String text=GetHttp.postText(url, parm);
		//System.out.println(text);
		String synckeyStr=find(text,"SyncKey\": ",",\"User");
		String uuserName=find(text,""+wxuin+",\"UserName\": \"","\",\"");
		String uname_synckey[]=new String[2];
		uname_synckey[0]=uuserName;
		uname_synckey[1]=synckeyStr;
		return uname_synckey;
	}
	public MemberContact getMemberList(String wxuin,String wxsid,String skey,String pass_ticket,String deviceID,String uusername) throws Exception{
		/*String parm="{\"BaseRequest\":{\"Uin\":"+wxuin+",\"Sid\":\""+wxsid+"\",\"Skey\":\""+skey+"\",\"DeviceID\":\""+deviceID+"\"},\"Code\":3,\"FromUserName\":\""+uusername+"\",\"ToUserName\":\""+uusername+"\",\"ClientMsgId\":1492883873525}";
		String url="https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxstatusnotify?lang=zh_CN&pass_ticket="+pass_ticket;
		String text=GetHttp.postText(url, parm);*/
		Date date = new Date();
		String text=GetHttp.postText("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxgetcontact?seq=0&pass_ticket="+pass_ticket+"&skey="+skey+"&r="+date.getTime()+"","","wxuin="+wxuin+";skey="+skey+";wxsid="+wxsid+";last_wxuin="+wxuin);
		//System.out.println(text);
		Gson gson = new Gson();
		MemberContact members = gson.fromJson(text, MemberContact.class);
		return members;
	}
	public String sendMsg(String wxuin,String wxsid,String skey,String pass_ticket,String deviceID,String uusername,String cookies,String content,String fromUserName,String toUserName){
		String ranStr="134";
		for (int i = 0; i < 17; i++) {
			int trandom=(int)(Math.random()*10);
			ranStr+=trandom;
		}
		String url="https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxsendmsg?pass_ticket="+pass_ticket;
		String parm="{\"BaseRequest\":{\"Uin\":"+wxuin+",\"Sid\":\""+wxsid+"\",\"Skey\":\""+skey+"\",\"DeviceID\":\""+deviceID+"\"},\"Msg\":{\"Type\":1,\"Content\":\""+content+"\",\"FromUserName\":\""+fromUserName+"\",\"ToUserName\":\""+toUserName+"\",\"LocalID\":\""+ranStr+"\",\"ClientMsgId\":\""+ranStr+"\"},\"Scene\":0}";
		String text=GetHttp.postText(url, parm);
		return text;
	}
	public String synckeyStatus(String wxuin,String wxsid,String skey,String pass_ticket,String deviceID,String uusername,String synckey,String cookieStr) throws Exception{
		Date date = new Date();
		String text = GetHttp.postText("https://webpush.wx.qq.com/cgi-bin/mmwebwx-bin/synccheck?r="+date.getTime()+"&skey="+skey+"&sid="+wxsid+"&uin="+wxuin+"&deviceid="+deviceID+"&synckey="+synckey+"&_="+date.getTime(),"",cookieStr);
		//System.out.println("https://webpush.wx.qq.com/cgi-bin/mmwebwx-bin/synccheck?synckey="+synckey+"&skey="+skey+"&uin="+wxuin+"&r=1492892744&deviceid="+deviceID+"&sid="+wxsid+"&_=1492892744");
		//window.synccheck={retcode:"0",selector:"0"}
		//window.synccheck={retcode:"0",selector:"2"}
		
		String selector=find(text,"selector:\"","\"}");
		//System.out.println(selector);
		return selector;
	}
	public String getSynckeyStatusSync(String wxuin,String wxsid,String skey,String pass_ticket,String deviceID,String uusername,String synckey,String cookieStr) throws Exception{
		Date date = new Date();
		String text = GetHttp.postText("https://webpush.wx.qq.com/cgi-bin/mmwebwx-bin/synccheck?r="+date.getTime()+"&skey="+skey+"&sid="+wxsid+"&uin="+wxuin+"&deviceid="+deviceID+"&synckey="+synckey+"&_="+date.getTime(),"",cookieStr);
		
		String tsynckey=find(text,"SyncKey\": ","\",");
		//System.out.println(selector);
		return tsynckey;
	}
	public void getCookies() throws Exception{
		Map<String, List<String>> map=GetHttp.getCookies("");
	}
}

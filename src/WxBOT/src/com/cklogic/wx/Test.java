package com.cklogic.wx;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.cklogic.model.SyncKey;
import com.cklogic.model.TulinMsg;
import com.cklogic.tool.GetHttp;
import com.google.gson.Gson;

public class Test {
	public static String getText(String ip,String cookie) throws Exception {
		//System.setProperty("jsse.enableSNIExtension", "false");
		try {
			URL url = new URL(ip);
			BufferedReader breader = null;
			InputStream is = null;
			StringBuffer resultBuffer = new StringBuffer();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Cookie", cookie);  
			 Map<String, List<String>> map=conn.getHeaderFields();
			is = conn.getInputStream();
			System.out.println(conn.getHeaderFields());
			breader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line = "";
			while ((line = breader.readLine()) != null) {
				resultBuffer.append(line);
			}
			return resultBuffer.toString();
		}catch (Exception e) {
			return "";
		}
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
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//GetHttp.postText();
		/*while(true){
			String s = GetHttp.postText("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxsync?sid=e55OxYe2G22LXmmd&skey=@crypt_691b7df1_6396540903da9a9bb4f6abccd44da803&pass_ticket=ITTsBwksfjfqDgHwo%252BPHQzv9LqGSZuovh%252BUlUg1yJ7hmY2y1JOcfOdu8NE6Z06Ui", "{\"BaseRequest\":{\"Uin\":1712485661,\"Sid\":\"e55OxYe2G22LXmmd\",\"Skey\":\"@crypt_691b7df1_6396540903da9a9bb4f6abccd44da803\",\"DeviceID\":\"e715203211620369\"},\"SyncKey\":{\"Count\":9,\"List\":[{\"Key\":1,\"Val\":686894254},{\"Key\":2,\"Val\":686894422},{\"Key\":3,\"Val\":686894311},{\"Key\":11,\"Val\":686894207},{\"Key\":13,\"Val\":686850319},{\"Key\":201,\"Val\":1492893732},{\"Key\":1000,\"Val\":1492878892},{\"Key\":1001,\"Val\":1492855772},{\"Key\":1004,\"Val\":1492607930}]},\"rr\":1754175146}");
			String ss=find(s,"Content\": \"","\",");
			if(ss.length()>0){
				System.out.println(ss);
			}
			
		}*/
		
		Gson gson = new Gson();
		TulinMsg tl=new TulinMsg();
		String tlMsg=tl.sendMsg("今天北京气温");
		/*tl.info="hello";
		String json = gson.toJson(tl);
		System.out.println(json);
		String rStr=GetHttp.postText("http://www.tuling123.com/openapi/api","key=8e382693edb9f93ec18789bdfb34d830&info=%e4%bb%8a%e5%a4%a9%e5%8c%97%e4%ba%ac%e6%b0%94%e6%b8%a9");
		System.out.println(rStr);*/
	}

}

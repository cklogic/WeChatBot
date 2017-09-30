package com.cklogic.wx;


import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;



import com.cklogic.model.*;
import com.cklogic.model.NewsFlashes.QuickNew;
import com.cklogic.tool.GetHttp;
import com.google.gson.Gson;

public class Test {
	public static String testGroupUserName="";
	public static int newsId;
	public static List<String> groupList;
	public static void main(String[] args) throws Exception {
		System.setProperty("jsse.enableSNIExtension", "false");
		WeiXin wx = new WeiXin();
		String uuid=wx.getUUID();
		String qr=wx.getQR(uuid);
		
		//Frame frame= ShowImg.showImg(qr);
		String ticket="";
		while(true){
			ticket=wx.getTicket(uuid);
			if(ticket.length()>0){
				//frame.dispose();
				break;
			}
		}
		String skey_passticket_wxsid_wxuin[]=wx.get_skey_passticket_wxsid_wxuin_cookieStr(ticket, uuid);
		String skey=skey_passticket_wxsid_wxuin[0];
		String pass_ticket=skey_passticket_wxsid_wxuin[1];
		String wxsid=skey_passticket_wxsid_wxuin[2];
		String wxuin=skey_passticket_wxsid_wxuin[3];
		String cookieStr=skey_passticket_wxsid_wxuin[4];
		String rStr="";
		for (int i = 0; i < 17; i++) {
			int trandom=(int)(Math.random()*10);
			rStr+=trandom;
		}
		String deviceID="e"+rStr;
		String uname_synckey[]=wx.get_uname_synckey(wxuin, wxsid, skey, pass_ticket, deviceID);
		String uusername=uname_synckey[0];
		String synckeyStr=uname_synckey[1];
		Gson gson=new Gson();
		SyncKey _synckey = gson.fromJson(synckeyStr, SyncKey.class);
		String synckey="";
		for (int i = 0; i < _synckey.List.length; i++) {
			synckey+=_synckey.List[i].Key+"_"+_synckey.List[i].Val+"|";
		}
		synckey=synckey.substring(0, synckey.length()-1);
		MemberContact memberContact=wx.getMemberList(wxuin, wxsid, skey, pass_ticket, deviceID, uusername);
		Member[] members=memberContact.MemberList;
		Map<String,String> memberMap = new HashMap<String,String>();
		groupList = new ArrayList<String>();
		for (int i = 0; i < members.length; i++) {

			if("测试群".equals(members[i].NickName)||"这周末休养生息".equals(members[i].NickName)||"2017BHU未来科技领袖峰会".equals(members[i].NickName)){
				groupList.add(members[i].UserName);
			}
			if("测试群".equals(members[i].NickName)){
				testGroupUserName=members[i].UserName;
			}
		}
		System.out.println(testGroupUserName);
		

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				wx.sendMsg(wxuin, wxsid, skey, pass_ticket, deviceID, uusername, cookieStr, "hi"+Math.random(), uusername, testGroupUserName);
		        String text="";
				try {
					text = GetHttp.getText("http://36kr.com/api/info-flow/newsflash_columns/newsflashes");
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Gson gson = new Gson();
			        NewsFlashes newsFlashes =gson.fromJson(text,NewsFlashes.class);
			        QuickNew[] quickNews=newsFlashes.getData().getItems();
			        Arrays.sort(quickNews,Comparator.comparing(QuickNew::getId));
			        QuickNew quickNew = quickNews[quickNews.length-1];
			        if(quickNew!=null) {
			        	if(quickNew.getId()!=newsId) {
			        		String html="*******科技快讯********\r";
			        		html+=""+quickNew.getTitle()+"\r";
			        		html+="=====================\r";
				        	html+=""+quickNew.getDescription()+"\r";
				        	html+=""+quickNew.getNews_url()+"\r";
				        	System.out.println(html);
				        	String a = new String(html.getBytes("UTF-8"), "GBK"); 
				        	
				        	newsId=quickNew.getId();
				        	for(String groupId:groupList) {
				        		wx.sendMsg(wxuin, wxsid, skey, pass_ticket, deviceID, uusername, cookieStr, a, uusername, groupId);
				        	}
			        	}
			        }
			        
				} catch (Exception e) {
					// TODO: handle exception
				}
		       
			}
		}, 1000, 30000);
	}

}

package com.cklogic.wx;


import java.awt.Frame;
import java.util.HashMap;
import java.util.Map;

import com.cklogic.model.Member;
import com.cklogic.model.MemberContact;
import com.cklogic.model.Msg;
import com.cklogic.model.MsgList;
import com.cklogic.model.SyncKey;
import com.cklogic.model.TulinMsg;
import com.cklogic.tool.ShowImg;
import com.google.gson.Gson;

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
			System.setProperty("jsse.enableSNIExtension", "false");
			WeiXin wx = new WeiXin();
			String uuid=wx.getUUID();
			String qr=wx.getQR(uuid);
			Frame frame=ShowImg.showImg(qr);
			String ticket="";
			while(true){
				ticket=wx.getTicket(uuid);
				if(ticket.length()>0){
					frame.dispose(); 
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
			for (int i = 0; i < members.length; i++) {
				
				memberMap.put(members[i].UserName, members[i].NickName);
			}
			wx.synckeyStatus(wxuin, wxsid, skey, pass_ticket, deviceID, uusername,synckey,cookieStr);
			String msgId="";
			while(true){
				String selector=wx.synckeyStatus(wxuin, wxsid, skey, pass_ticket, deviceID, uusername,synckey,cookieStr);
				if(!selector.equals("0")){
					MsgList msgs=wx.getMsg(pass_ticket, skey, wxuin, wxsid, deviceID, synckeyStr,cookieStr);
					MsgList msgs2=wx.getMsg(pass_ticket, skey, wxuin, wxsid, deviceID, synckeyStr,cookieStr);
					SyncKey _synckey_ = msgs2.SyncKey;
					synckey="";
					for (int k = 0; k < _synckey_.List.length; k++) {
						synckey+=_synckey_.List[k].Key+"_"+_synckey_.List[k].Val+"|";
					}
					synckey=synckey.substring(0, synckey.length()-1);
					for(int i= 0 ;i<msgs.AddMsgList.length;i++){
						Msg msg = msgs.AddMsgList[i];
						if(msg.ToUserName.equals(uusername)&&msg.MsgType==1&&(!msg.MsgId.equals(msgId))){
							String nickName = memberMap.get(msg.FromUserName);
							System.out.println(nickName+": "+msg.Content);
							TulinMsg tl=new TulinMsg();
							String tlMsg=tl.sendMsg(msg.Content);
							//tlMsg=java.net.URLEncoder.encode(tlMsg,"utf-8"); 
							System.out.println(tlMsg);
							wx.sendMsg(wxuin, wxsid, skey, pass_ticket, deviceID, uusername, cookieStr, tlMsg, uusername, msg.FromUserName);
							msgs=wx.getMsg(pass_ticket, skey, wxuin, wxsid, deviceID, synckeyStr,cookieStr);
							_synckey_ = msgs.SyncKey;
							synckey="";
							for (int k = 0; k < _synckey_.List.length; k++) {
								synckey+=_synckey_.List[k].Key+"_"+_synckey_.List[k].Val+"|";
							}
							synckey=synckey.substring(0, synckey.length()-1);
							msgId=msg.MsgId;
							break;
						}
					}
					
					
				}
				
			}
			//wx.getUserList(pass_ticket, skey);
			
			
	}

}

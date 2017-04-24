package com.cklogic.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetHttp {
	public static String getText(String ip) throws Exception {
		try {
			URL url = new URL(ip);
			BufferedReader breader = null;
			InputStream is = null;
			StringBuffer resultBuffer = new StringBuffer();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			is = conn.getInputStream();
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
	public static Map<String, List<String>>[] getTextAndCookies(String ip) throws Exception {
		try {
			URL url = new URL(ip);
			BufferedReader breader = null;
			InputStream is = null;
			StringBuffer resultBuffer = new StringBuffer();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			Map<String, List<String>> map=conn.getHeaderFields();
			is = conn.getInputStream();
			breader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line = "";
			while ((line = breader.readLine()) != null) {
				resultBuffer.append(line);
			}
			Map<String, List<String>> mapString = new HashMap<String, List<String>>();
			List<String> list = new ArrayList<String>();
			list.add(resultBuffer.toString());
			mapString.put("TEXT", list);
			@SuppressWarnings("unchecked")
			Map<String, List<String>>[] maps=new Map[2];
			maps[0]=map;
			maps[1]=mapString;
			return maps;
		}catch (Exception e) {
			return null;
		}
	}
	public static Map<String, List<String>> getCookies(String ip) throws Exception {
		try {
			URL url = new URL(ip);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			Map<String, List<String>> map=conn.getHeaderFields();
			return map;
		}catch (Exception e) {
			return null;
		}
	}
	public static String getText(String ip,String cookie) throws Exception {
		try {
			URL url = new URL(ip);
			BufferedReader breader = null;
			InputStream is = null;
			StringBuffer resultBuffer = new StringBuffer();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Cookie", cookie);  
			is = conn.getInputStream();
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
	public static InputStream getIOStream(String ip) throws Exception {
		try {
			URL url = new URL(ip);
			InputStream is = null;
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(1000);
			is = conn.getInputStream();
			return is;
		}catch (Exception e) {
			return null;
		}
	}
	public static String postText(String url,String parm){
		PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(parm);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
	}
	public static String postText(String url,String parm,String cookies){
		PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
            conn.setRequestProperty("Cookie", cookies);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(parm);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
	}
}

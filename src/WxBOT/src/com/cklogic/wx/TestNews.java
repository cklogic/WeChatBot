package com.cklogic.wx;

import java.util.Arrays;
import java.util.Comparator;

import com.cklogic.model.NewsFlashes;
import com.cklogic.model.NewsFlashes.QuickNew;
import com.cklogic.tool.GetHttp;
import com.google.gson.Gson;

public class TestNews {


    public static void main(String[] args) throws Exception {
    	System.out.println("HELLO");
        String text=GetHttp.getText("http://36kr.com/api/info-flow/newsflash_columns/newsflashes");
        System.out.println(text);
        Gson gson = new Gson();
        NewsFlashes newsFlashes =gson.fromJson(text,NewsFlashes.class);
        QuickNew[] quickNews=newsFlashes.getData().getItems();
        Arrays.sort(quickNews,Comparator.comparing(QuickNew::getId));
        QuickNew quickNew = quickNews[quickNews.length-1];
    	System.out.println(quickNew.getId());
    	System.out.println(quickNew.getDescription());
    	System.out.println(quickNew.getTitle());
    	System.out.println(quickNew.getNews_url());
        
        
	}
}

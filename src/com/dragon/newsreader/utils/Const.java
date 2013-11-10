package com.dragon.newsreader.utils;

import android.content.Context;

public class Const {
	public static class PE {
		public static final int NEWS_BASKETEBALL = 0;
		public static final int NEWS_NBA = 1;
		public static final int NEWS_CBA = 2;
		public static final int NEWS_FOOTBALL = 3;
		public static final int NEWS_SPANISH_LEAGUE = 4;
		public static final int NEWS_CHINESE_LEAGUE = 5;
		public static final int NEWS_PREMIER_LEAGUE = 6; 
		
		public static final String [] NEWS_XML_LINK = {
			"http://sports.qq.com/basket/rss_basket.xml",
			"http://sports.qq.com/basket/nba/nbarep/rss_nbarep.xml",
			"http://sports.qq.com/basket/bskb/cba/rss_cba.xml",
			"http://sports.qq.com/isocce/yijia/rss_sereasa.xml",
			"http://sports.qq.com/isocce/xijia/rss_laliga.xml",
			"http://sports.qq.com/isocce/yingc/rss_pl.xml",
			"http://sports.qq.com/csocce/rss_csocce.xml",
		};
		
		public static String getNewsLink(Context context,int index){
			String result = null;
			return result;
		}
		
		public static String getNewsName(Context context,int index){
			String result = null;
			return result;
		}
		
		/*
		public static final String NEWS_XML_BASKETBALL = "http://sports.qq.com/basket/rss_basket.xml";
		public static final String NEWS_XML_NBA = "http://sports.qq.com/basket/nba/nbarep/rss_nbarep.xml";
		public static final String NEWS_XML_CBA = "http://sports.qq.com/basket/bskb/cba/rss_cba.xml";
		public static final String NEWS_XML_FOOTBALL = "http://sports.qq.com/isocce/rss_isocce.xml";
		public static final String NEWS_XML_SPANISH_LEAGUE = "http://sports.qq.com/isocce/xijia/rss_laliga.xml";
		public static final String NEWS_XML_CHINESE_LEAGUE = "http://sports.qq.com/csocce/rss_csocce.xml";
		public static final String NEWS_XML_PREMIER_LEAGUE = "http://sports.qq.com/isocce/yingc/rss_pl.xml";
		*/
		
		public static final String NEWS_TAG_ITEM = "item";
		public static final String NEWS_TAG_TITLE = "title";
		public static final String NEWS_TAG_DATE = "pubDate";
		public static final String NEWS_TAG_DESC = "description";
		public static final String NEWS_TAG_LINK = "link";
	}
	
	
}

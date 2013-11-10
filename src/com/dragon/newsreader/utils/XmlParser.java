package com.dragon.newsreader.utils;

import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Xml;

import com.dragon.newsreader.Data.News;

public class XmlParser {
	public static ArrayList<News> parserXml(Context context,InputStream is) {
		ArrayList<News> arrayList = null;
		News news = null;
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, "UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (parser.getName().equals(Const.PE.NEWS_TAG_ITEM)) {
						news = new News(context);
					} else if (parser.getName().equals(Const.PE.NEWS_TAG_DATE)
							&& news != null) {
						parser.next();
						news.setDate(parser.getText());
					}else if(parser.getName().equals(Const.PE.NEWS_TAG_DESC)
							&& news != null){
						parser.next();
						news.setDesc(parser.getText());
					}else if(parser.getName().equals(Const.PE.NEWS_TAG_TITLE)
							&& news != null){
						parser.next();
						news.setTitle(parser.getText());
					}else if(parser.getName().equals(Const.PE.NEWS_TAG_LINK)
							&& news != null){
						parser.next();
						news.setLinck(parser.getText());
					}
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals(Const.PE.NEWS_TAG_ITEM)) {
						if(arrayList == null)
							arrayList = new ArrayList<News>();
						arrayList.add(news);
					}
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrayList;
	}
}

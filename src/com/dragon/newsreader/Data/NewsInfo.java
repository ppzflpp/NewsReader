package com.dragon.newsreader.Data;

import java.io.Serializable;

public class NewsInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String title;
	private String desc;
	private String date;
	private String link;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
}

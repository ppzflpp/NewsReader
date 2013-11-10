package com.dragon.newsreader.Data;

import java.io.Serializable;
import java.lang.ref.SoftReference;

import com.dragon.newsreader.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class News {


	private static final String TAG = "News";
	private static final boolean DEBUG = true;
	
	private String title;
	private String desc;
	private String date;
	private String link;
	private SoftReference<Bitmap> image;
	
	private Context mContext;
	public News(Context context){
		mContext = context;
	}
	
	public String getLink(){
		return link;
	}
	
	public void setLinck(String link){
		this.link = link;
	}	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
		if(DEBUG)
			Log.d(TAG,"title = " + title);
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
	public Bitmap getImage() {
		Bitmap b = null;
		if(image != null)
			b = image.get();
		if(b == null){
			//we need reload bitmap
			b = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.image_thumb);
		}
		return b;
	}
	public void setImage(Bitmap bitmap) {
		this.image = new SoftReference<Bitmap>(bitmap);;
	}
	
	public NewsInfo getNewsInfo(){
		NewsInfo info = new NewsInfo();
		info.setDate(this.getDate());
		info.setDesc(this.getDesc());
		info.setLink(this.getLink());
		info.setTitle(this.getTitle());
		return info;
	}
	
}

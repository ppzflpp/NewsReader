package com.dragon.newsreader.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class NewsPageAdapter extends PagerAdapter{
	
	private static final String TAG = "NewsPageAdapter";
	private static final boolean DEBUG = true;
	
	private Context mContext ;
	private List<View> mViews;
	
	public NewsPageAdapter(Context context,List<View> views){
		mContext = context;
		mViews = views;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		if(DEBUG)
			Log.d(TAG,"NewsPageAdapter...instantiateItem,position =" + position);
		View view = mViews.get(position);
		container.addView(view);
		return view;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int count = mViews != null ? mViews.size() : 0;
		if(DEBUG)
			Log.d(TAG,"getCount = " + count);
		return count;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		View view = mViews.get(position);
		container.removeView(view);
	}

}

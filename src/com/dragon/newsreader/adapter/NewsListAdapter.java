package com.dragon.newsreader.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import org.apache.http.HttpConnection;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dragon.newsreader.MainActivity;
import com.dragon.newsreader.R;
import com.dragon.newsreader.Data.News;
import com.dragon.newsreader.net.NetHelper;
import com.dragon.newsreader.utils.Const;
import com.dragon.newsreader.utils.DownLoader;
import com.dragon.newsreader.utils.XmlParser;

public class NewsListAdapter extends BaseAdapter {
	
	private final boolean DEBUG = true;
	private final boolean USE_LOCAL_XML= false;
	private final String TAG = "NewsListAdapter";

	private Context mContext;
	private int mId;
	private List<News> mNewsList;
	private boolean mLoading;
	private ViewHolder mViewHolder;
	private Handler mHandler;

	private class ViewHolder {
		ImageView itemImage;
		TextView itemTitle;
		TextView itemDesc;
		TextView itemDate;
	}

	public NewsListAdapter(Context context, int newsId) {
		mContext = context;
		mId = newsId;
	}

	public int getNewsId() {
		return mId;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int count = mNewsList != null ? mNewsList.size() : 0;
		if(DEBUG)
			Log.d(TAG,"getCount = " + count);
		return count;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.news_list_item, null);
			mViewHolder = new ViewHolder();
			mViewHolder.itemImage = (ImageView) convertView
					.findViewById(R.id.item_image);
			mViewHolder.itemDate = (TextView) convertView
					.findViewById(R.id.item_date);
			mViewHolder.itemTitle = (TextView) convertView
					.findViewById(R.id.item_title);
			mViewHolder.itemDesc = (TextView) convertView
					.findViewById(R.id.item_desc);
			convertView.setTag(mViewHolder);
		}

		mViewHolder = (ViewHolder) convertView.getTag();

		mViewHolder.itemDate.setText(mNewsList.get(pos).getDate());
		mViewHolder.itemDesc.setText(mNewsList.get(pos).getDesc());
		mViewHolder.itemImage.setImageBitmap(mNewsList.get(pos).getImage());
		mViewHolder.itemTitle.setText(mNewsList.get(pos).getTitle());

		return convertView;
	}

	public void setHandler(Handler handler) {
		mHandler = handler;
	}

	public void refreshData(boolean force) {
		if ((mNewsList == null && !mLoading)
				|| force) {
			mLoading = true;
			DownLoader loader = DownLoader.getInstance();
			loader.execute(new Runnable() {
				public void run() {
					HttpURLConnection conn = null;
					InputStream is = null;
					if(!USE_LOCAL_XML){
					is = NetHelper.getInputStream(mContext,
							Const.PE.NEWS_XML_LINK[mId], conn);
					}else{
						try {
							is = mContext.getAssets().open("rss_basket.xml");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (is != null) {
						mNewsList = XmlParser.parserXml(mContext,is);
						if (conn != null)
							conn.disconnect();
					}
					if(DEBUG){
						if(mNewsList != null)
							Log.d(TAG,"refreshData...news num  = " + mNewsList.size());
						else
							Log.d(TAG,"refreshData...not fetch data");
					}
					mLoading = false;
					if (mHandler != null) {
						Message msg = Message.obtain();
						msg.arg1 = mId;
						if (mNewsList != null && mNewsList.size() != 0) {
							msg.what = MainActivity.MSG_LOAD_NEWS_DATA_SUCCESS;
						} else {
							msg.what = MainActivity.MSG_LOAD_NEWS_DATA_FAIL;
						}
						mHandler.sendMessage(msg);
					}
				}

			});
		}
	}

	public News getNews(int index){
		if(mNewsList != null
				&& index >= 0
				&& index < mNewsList.size())
			return mNewsList.get(index);
		else 
			return null;
	}
	
}

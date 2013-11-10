package com.dragon.newsreader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsChangeNotify;
import net.youmi.android.offers.PointsManager;
import net.youmi.android.spot.SpotManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dragon.newsreader.adapter.NewsListAdapter;
import com.dragon.newsreader.adapter.NewsPageAdapter;
import com.dragon.newsreader.view.DropDownListView;
import com.dragon.newsreader.view.DropDownListView.OnDropDownListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.jmp.sfc.uti.JuMiManager;

public class MainActivity extends SlidingFragmentActivity implements
		PointsChangeNotify {

	private static final String TAG = "MainActivity";
	private static final boolean DEBUG = true;

	// this value maybe changed
	private static int NEWS_ITEM = 8;

	private ViewPager mNewsPager;
	private TextView mNewItemAdd;
	private DropDownListView mNewsList[];
	private NewsListAdapter mNewsListAdapter[];
	private ViewHolder mHeaderViewHolder;
	private TextView mNewsItem[];
	private ViewGroup mLoadingView[];

	public static final int MSG_LOAD_NEWS_DATA_SUCCESS = 0;
	public static final int MSG_LOAD_NEWS_DATA_FAIL = 1;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			final int id = msg.arg1;
			switch (msg.what) {
			case MSG_LOAD_NEWS_DATA_SUCCESS:
				mNewsListAdapter[id].notifyDataSetInvalidated();
				mLoadingView[id].setVisibility(View.GONE);
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"MM-dd HH:mm:ss");
				mNewsList[id].onDropDownComplete(getString(R.string.update)
						+ dateFormat.format(new Date()));
				break;
			case MSG_LOAD_NEWS_DATA_FAIL:
				if (mLoadingView[id] instanceof LinearLayout
						&& mLoadingView[id].getChildCount() == 2) {
					ProgressBar pb = (ProgressBar) mLoadingView[id]
							.getChildAt(0);
					pb.setVisibility(View.GONE);
					TextView tv = (TextView) mLoadingView[id].getChildAt(1);
					tv.setText(R.string.load_error);
					tv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							ProgressBar pb = (ProgressBar) mLoadingView[id]
									.getChildAt(0);
							pb.setVisibility(View.VISIBLE);
							mLoadingView[id].setVisibility(View.VISIBLE);
							TextView tv = (TextView) mLoadingView[id]
									.getChildAt(1);
							tv.setText(R.string.loading);
							mNewsListAdapter[id].refreshData(true);
						}

					});
					mNewsList[id].onDropDownComplete();
				}
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {  
			// youmi
			AdManager.getInstance(this).init("XXXXX",
					"XXXXX", false); 
			// SpotManager.getInstance(this).loadSpotAds();
			// SpotManager.getInstance(this).showSpotAds(this);
			OffersManager.getInstance(this).onAppLaunch();
			PointsManager.getInstance(this).registerNotify(this);
			// jumi
			JuMiManager manager = new JuMiManager();
			//manager.startService(this);
		} catch (Exception e) {
			//Log.d(TAG, "init jumi error...");
			e.printStackTrace();
		}

		setContentView(R.layout.activity_main);

		setTitle(R.string.app_name);
		// set the content view
		setBehindContentView(R.layout.menu_frame);
		setContentView(R.layout.activity_main);
		// configure the SlidingMenu
		SlidingMenu menu = this.getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT_RIGHT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.left_fragment_shadow);
		menu.setSecondaryShadowDrawable(R.drawable.right_fragment_shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.setMenu(R.layout.left_menu);
		menu.setSecondaryMenu(R.layout.right_menu);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(
				this.getResources().getDrawable(R.drawable.actionbar_bg));

		setupViews();
	}

	private void setupViews() {

		setupNewsItem();

		mNewItemAdd = (TextView) this.findViewById(R.id.item_add);
		mNewItemAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.d(TAG, "click add button");
			}

		});
		;

		mNewsPager = (ViewPager) this.findViewById(R.id.news_main_list);
		List<View> views = new ArrayList<View>();
		mLoadingView = new ViewGroup[NEWS_ITEM];
		mNewsList = new DropDownListView[NEWS_ITEM];
		mNewsListAdapter = new NewsListAdapter[NEWS_ITEM];
		for (int i = 0; i < NEWS_ITEM; i++) {
			View view = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.news_page_item, null);
			mNewsList[i] = (DropDownListView) view
					.findViewById(R.id.news_page_list);
			mLoadingView[i] = (ViewGroup) view.findViewById(R.id.loading);
			mNewsListAdapter[i] = new NewsListAdapter(getApplicationContext(),
					i);
			final int index = i;
			mNewsList[i].setOnDropDownListener(new OnDropDownListener() {

				@Override
				public void onDropDown() {
					// TODO Auto-generated method stub
					mNewsListAdapter[index].refreshData(true);
				}

			});
			mNewsList[i].setAdapter(mNewsListAdapter[i]);
			mNewsList[i].setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long id) {
					// TODO Auto-generated method stub
					if(pos == 0)
						return;
					pos = pos -1;
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							DetailActivity.class);
					intent.putExtra("news", mNewsListAdapter[index]
							.getNews(pos).getNewsInfo());
					startActivity(intent);
				}

			});
			mNewsListAdapter[i].setHandler(mHandler);
			views.add(view);
		}
		mNewsPager.setAdapter(new NewsPageAdapter(getApplicationContext(),
				views));
		mNewsPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageSelected(int position) {
				refreshNewItemTextColor(position);
				mNewsListAdapter[position].refreshData(false);
				switch (position) {
				case 0:
					getSlidingMenu().setTouchModeAbove(
							SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
				default:
					getSlidingMenu().setTouchModeAbove(
							SlidingMenu.TOUCHMODE_MARGIN);
					break;
				}
			}

		});
		mNewsPager.setCurrentItem(0);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		mNewsListAdapter[0].refreshData(false);
		refreshNewItemTextColor(0);
	}

	private void setupNewsItem() {
		ViewGroup viewGroup = (ViewGroup) findViewById(R.id.item_selector_linearlayout);
		NEWS_ITEM = viewGroup.getChildCount();
		mNewsItem = new TextView[NEWS_ITEM];
		for (int i = 0; i < NEWS_ITEM; i++) {
			mNewsItem[i] = (TextView) viewGroup.getChildAt(i);
			mNewsItem[i].setTag(i);
			mNewsItem[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					int n = ((Integer) view.getTag()).intValue();
					mNewsPager.setCurrentItem(n);
					mNewsListAdapter[n].refreshData(true);
					refreshNewItemTextColor(n);
				}

			});
		}
	}

	private void refreshNewItemTextColor(int id) {
		for (int i = 0; i < NEWS_ITEM; i++) {
			if (i == id)
				mNewsItem[i].setTextColor(Color.RED);
			else
				mNewsItem[i].setTextColor(Color.BLACK);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		case R.id.login:
			this.getSlidingMenu().showSecondaryMenu(true);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onPointBalanceChange(int pointsBalance) {
		// TODO Auto-generated method stub
		// 当该方法被调用时，表示积分账户余额已经变动了，这里的pointsBalance是积分的当前余额数
		Log.d("test", "积分账户余额已变动，当前余额为:" + pointsBalance);
		// 注:您可以在这里进行更新界面显示等操作，
	}

	private class ViewHolder {
		ImageView arrowView;
		TextView updateTextView;
		TextView dateTextView;
	}

	public void onStart() {
		super.onStart();
	}

	public void onDestroy() {
		super.onDestroy();
		PointsManager.getInstance(this).unRegisterNotify(this);
		OffersManager.getInstance(this).onAppExit();
	}

	public void onStop() {
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		// this.moveTaskToBack(true);
	}

}

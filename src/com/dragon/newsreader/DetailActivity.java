package com.dragon.newsreader;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.dragon.newsreader.Data.NewsInfo;

public class DetailActivity extends SherlockFragmentActivity {

	private final static String TAG = "DetailActivity";
	private final static boolean DEBUG = true;

	private NewsInfo mNews;
	private TextView mTitleView;
	private TextView mDateView;
	private TextView mAuthorView;
	private TextView mContentView;
	private ImageView mImgView;
	private List<String> mImageLink;
	private Bitmap mImgBitmap;
	private Bitmap mScaleBitmap;
	private Context mContext;
	private LinearLayout mProgressParent;
	private ProgressBar mProgressBar;
	private TextView mProgressTextView;
	private LinearLayout mSpreadParent;

	private final static String HTML_ENCODING = "UTF-8";

	private String mResultData = "";
	private boolean mLoadDataSuccess = false;// only used for news content

	private final static int MSG_LOAD_PAGE_FINISH = 0;
	private final static int MSG_LOAD_PAGE_IMG_FINISH = 1;
	private final static int MSG_LOAD_PAGE_BITMAP_FINISH = 2;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LOAD_PAGE_FINISH:
				if (mLoadDataSuccess) {
					mContentView.setText(mResultData);
					mSpreadParent.setVisibility(View.VISIBLE);
					mProgressParent.setVisibility(View.GONE);
				} else {
					mContentView.setText("");
					mProgressTextView.setText(R.string.load_error);
					mProgressBar.setVisibility(View.GONE);
					mProgressParent.setVisibility(View.VISIBLE);
				}
				break;
			case MSG_LOAD_PAGE_IMG_FINISH:
				if (DEBUG)
					printLink();
				new Thread() {
					public void run() {
						super.run();
						HttpURLConnection conn = null;
						InputStream is = null;
						if (mImageLink != null && mImageLink.size() > 0) {
							try {
								URL url = new URL(mImageLink.get(0));
								conn = (HttpURLConnection) url.openConnection();
								conn.setDoInput(true);
								conn.connect();

								is = conn.getInputStream();
								mImgBitmap = BitmapFactory.decodeStream(is);
								mHandler.sendEmptyMessage(MSG_LOAD_PAGE_BITMAP_FINISH);
							} catch (Exception e) {
								try {
									if (is != null)
										is.close();
									if (conn != null)
										conn.disconnect();
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								e.printStackTrace();
							}
						}
					}
				}.start();

				break;
			case MSG_LOAD_PAGE_BITMAP_FINISH:
				if (mImgBitmap != null) {
					int rawWidth = mImgBitmap.getWidth();
					int rawHeight = mImgBitmap.getHeight();
					int newWidth = mContext.getResources().getDisplayMetrics().widthPixels;
					// 计算缩放因子
					float widthScale = ((float) newWidth) / rawWidth;
					// 新建立矩阵
					Matrix matrix = new Matrix();
					matrix.postScale(widthScale, widthScale);
					mScaleBitmap = Bitmap.createBitmap(mImgBitmap, 0, 0,
							rawWidth, rawHeight, matrix, true);
					if (mScaleBitmap != null) {
						mImgView.setImageBitmap(mScaleBitmap);
					}
				}
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		mContext = this.getApplicationContext();

		// youmi
		// AdManager.getInstance(this).init("8a1831b5bbd658b6","c568726734660a37",
		// true);
		// SpotManager.getInstance(this).loadSpotAds();
		// SpotManager.getInstance(this).showSpotAds(this);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(
				this.getResources().getDrawable(R.drawable.actionbar_bg));

		mNews = (NewsInfo) getIntent().getSerializableExtra("news");

		mTitleView = (TextView) findViewById(R.id.detail_title);
		mDateView = (TextView) findViewById(R.id.detail_date);
		mAuthorView = (TextView) findViewById(R.id.detail_author);
		mContentView = (TextView) findViewById(R.id.detail_content);
		mImgView = (ImageView) findViewById(R.id.detail_img);
		mSpreadParent = (LinearLayout) findViewById(R.id.spread_parent);
		mSpreadParent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),
						getString(R.string.no_ads), Toast.LENGTH_LONG).show();
			}

		});
		mSpreadParent.setVisibility(View.GONE);

		mProgressParent = (LinearLayout) findViewById(R.id.detail_loading_parent);
		mProgressParent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mProgressTextView.setText(R.string.loading);
				mProgressBar.setVisibility(View.VISIBLE);
				loadNews();
			}

		});
		mProgressTextView = (TextView) findViewById(R.id.detail_loading);
		mProgressTextView.setText(R.string.loading);
		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

		// mContentView.getSettings().setUseWideViewPort(true);
		// mContentView.getSettings().setLoadWithOverviewMode(true);

		loadNews();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mScaleBitmap != null && !mScaleBitmap.isRecycled()) {
			mScaleBitmap.recycle();
		}
		if (mImgBitmap != null && !mImgBitmap.isRecycled()) {
			mImgBitmap.recycle();
		}
	}

	private void printLink() {
		if (mImageLink != null) {
			for (int i = 0; i < mImageLink.size(); i++) {
				Log.d(TAG, "image link is " + mImageLink.get(i));
			}
		}
	}

	private void loadNews() {
		if (mNews != null) {
			if (DEBUG)
				Log.d(TAG, "link = " + mNews.getLink());
			mTitleView.setText(mNews.getTitle());
			mDateView.setText(mNews.getDate());
			// mAuthorView.setText(mItemNew.getAuthor());
			new Thread() {
				public void run() {
					super.run();
					// Parser parser = Parser.c reateParser(mNews.getLink(),
					// "utf-8");
					Parser parser = null;
					try {
						URL url = new URL(mNews.getLink());
						HttpURLConnection connection = (HttpURLConnection) url
								.openConnection();
						parser = new Parser(connection);
						parser.setEncoding(HTML_ENCODING);

						NodeFilter filter = new AndFilter(new TagNameFilter(
								"div"), new HasAttributeFilter("id",
								"Cnt-Main-Article-QQ"));
						NodeList nodes = null;

						if (DEBUG)
							Log.d(TAG, "load contents...");

						NodeFilter pf = new AndFilter(new NodeClassFilter(
								ParagraphTag.class),
								new HasParentFilter(filter));

						nodes = parser.parse(pf);

						if (nodes.size() > 0) {
							// 解析新闻内容
							mResultData = parserData(nodes);
							if (DEBUG)
								Log.d(TAG, "data = " + mResultData);
							/*
							if (mResultData.trim().equals("")) {
								NodeList nodes0 = nodes.elementAt(0)
										.getChildren();
								if (nodes0.size() > 0) {
									nodes0.remove(0);
									if (DEBUG)
										Log.d(TAG, "html =  " + nodes0.toHtml());
									mResultData = parserData(nodes0);
									if (DEBUG)
										Log.d(TAG, "data = " + mResultData);
								}else{
									if(DEBUG)
										Log.d(TAG,"children = 0");
								}
							}
							*/
							if (mResultData.trim().equals("")) {
								mLoadDataSuccess = false;
							} else {
								mLoadDataSuccess = true;
								if (mResultData.contains("\n")) {
									mResultData = mResultData.replace("\n",
											"\n\n          ");
									mResultData = "          " + mResultData;
								}
							}

							if (DEBUG) {
								if (mLoadDataSuccess) {
									Log.d(TAG, "load data success");
								} else {
									Log.d(TAG, "load data fail");
								}
							}

							mHandler.sendEmptyMessage(MSG_LOAD_PAGE_FINISH);

							// 解析图片
							Node node = nodes.elementAt(0);
							NodeList imgNodes = node != null ? node
									.getChildren() : null;
							for (int i = 0; imgNodes != null
									&& i < imgNodes.size(); i++) {
								if (imgNodes.elementAt(i) instanceof ImageTag) {
									ImageTag it = (ImageTag) imgNodes
											.elementAt(i);
									if (mImageLink == null)
										mImageLink = new ArrayList<String>();
									mImageLink.add(it.getAttribute("src"));
								}
							}
							mHandler.sendEmptyMessage(MSG_LOAD_PAGE_IMG_FINISH);

						} else {
							mLoadDataSuccess = false;
							if (DEBUG)
								Log.d(TAG, "not fatch contents...");
						}

					} catch (Exception e) {
						if (DEBUG)
							Log.d(TAG, "exception happens");
						e.printStackTrace();
					}
				}
			}.start();

			// mContentView.loadUrl(mNews.getLink());
		}
	}

	private String parserData(NodeList nodes) {
		String result = "";
		StringBean sb = new StringBean();
		sb.setReplaceNonBreakingSpaces(true);
		sb.setCollapse(true);
		try {
			nodes.visitAllNodesWith(sb);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (sb.getStrings() != null) {
			result = sb.getStrings();

		}
		return result;
	}

}

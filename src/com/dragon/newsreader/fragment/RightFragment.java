package com.dragon.newsreader.fragment;

import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dragon.newsreader.AboutActivity;
import com.dragon.newsreader.LoginActivity;
import com.dragon.newsreader.MailSendActivity;
import com.dragon.newsreader.R;
import com.google.zxing.client.android.CaptureActivity;

public class RightFragment extends Fragment {

	private static final String TAG = "RightFragment";
	private TextView mCurrentScoreView;

	private final static int MSG_GET_SCORE = 0;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_GET_SCORE:
				if (mCurrentScoreView != null) {
					mCurrentScoreView.setText(RightFragment.this.getActivity()
							.getResources().getString(R.string.current_score)
							+ msg.arg1);
					if(msg.arg1 > 50){
						mCurrentScoreView.setTextColor(Color.GREEN);
					}else{
						mCurrentScoreView.setTextColor(Color.RED);
					}
				}
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.right_fragment_layout, container,
				false);
		//login
		View clickView = view.findViewById(R.id.login1);
		clickView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), LoginActivity.class);
				RightFragment.this.getActivity().startActivity(intent);
			}

		});
		//contact us
		View contactView = view.findViewById(R.id.contact_us);
		contactView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), MailSendActivity.class);
				RightFragment.this.getActivity().startActivity(intent);
			}

		});
		//getScore
		View getScoreView = view.findViewById(R.id.get_score);
		getScoreView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				OffersManager.getInstance(RightFragment.this.getActivity())
						.showOffersWall();
			}

		});

		refreshScore();

		mCurrentScoreView = (TextView) getScoreView
				.findViewById(R.id.current_score);
		
		//about
		View aboutView = view.findViewById(R.id.about);
		aboutView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), AboutActivity.class);
				RightFragment.this.getActivity().startActivity(intent);
			}

		});
		//scan
		View scanView = view.findViewById(R.id.scan);
		scanView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), CaptureActivity.class);
				RightFragment.this.getActivity().startActivity(intent);
			}

		});

		return view;
	}

	private void refreshScore() {
		new Thread() {
			public void run() {
				super.run();
				
				//PointsManager.getInstance(getActivity()).awardPoints(102);
				int myPointBalance = PointsManager.getInstance(
						RightFragment.this.getActivity()).queryPoints();
				
				Message msg = Message.obtain();
				msg.what = MSG_GET_SCORE;
				msg.arg1 = myPointBalance;
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshScore();
	}

}

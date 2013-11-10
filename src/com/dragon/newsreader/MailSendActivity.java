package com.dragon.newsreader;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.dragon.newsreader.mail.SimpleMainSender;

public class MailSendActivity extends SherlockFragmentActivity {
	
	private static final String HEAD_NAME = "AAAAA";
	
	private Button mSubmit;
	private EditText mContent;
	private EditText mContactWay;
	
	private Context mContext;
	
	private final static int MSG_SEND_MAIL_SUCCESS = 0;
	private final static int MSG_SEND_MAIL_FAIL = 1;
	private Handler mHanlder = new Handler(){
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			switch(msg.what){
			case MSG_SEND_MAIL_SUCCESS:
				Toast.makeText(mContext, mContext.getString(R.string.send_mail_success),Toast.LENGTH_SHORT).show();
				break;
			case MSG_SEND_MAIL_FAIL:
				Toast.makeText(mContext, mContext.getString(R.string.send_mail_fail),Toast.LENGTH_SHORT).show();
				break;
			}
			enableViews(true);
			finish();
		}
	};
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentView(R.layout.send_mail);
		this.setTitle(R.string.contact_us);
		mContext = this.getApplicationContext();
		
		mContent = (EditText)findViewById(R.id.mail_content);
		mContactWay = (EditText)findViewById(R.id.mail_contact_way);
		mSubmit = (Button)findViewById(R.id.submit);
		mSubmit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				enableViews(false);
				String content = mContent.getText().toString();
				String contactWay = mContactWay.getText().toString();
				final String result = content + "|" + contactWay;
				new Thread() {
					public void run() {
						super.run();
						boolean res = SimpleMainSender.sendMail(HEAD_NAME, result);
						Message msg = Message.obtain();
						if(res){
							msg.what = MSG_SEND_MAIL_SUCCESS;
						}else
							msg.what = MSG_SEND_MAIL_FAIL;
						mHanlder.sendMessage(msg);
					}
				}.start();
				
			}
			
		});

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(
				this.getResources().getDrawable(R.drawable.actionbar_bg));

	}
	
	private void enableViews(boolean enable){
		mContent.setEnabled(enable);
		mContactWay.setEnabled(enable);
		mSubmit.setEnabled(enable);
	}
}

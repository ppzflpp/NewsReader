package com.dragon.newsreader;

import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.dragon.newsreader.utils.ConstantS;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.util.AccessTokenKeeper;

public class LoginActivity extends SherlockFragmentActivity {

	private TextView mSinaLogin;
	private View mSinaLoginParent;
	private Weibo mWeibo;
	private Oauth2AccessToken mAccessToken;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentView(R.layout.user_login);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(
				this.getResources().getDrawable(R.drawable.actionbar_bg));

		setupViews();
		mWeibo = Weibo.getInstance(ConstantS.APP_KEY, ConstantS.REDIRECT_URL,
				ConstantS.SCOPE);

	}

	private void setupViews() {
		mSinaLogin = (TextView) findViewById(R.id.sina_login);
		mSinaLoginParent = findViewById(R.id.sina_login_parent);
		mSinaLoginParent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mWeibo.anthorize(LoginActivity.this, new AuthDialogListener());
			}

		});
	}

	class AuthDialogListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {

			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			mAccessToken = new Oauth2AccessToken(token, expires_in);
			if (true/*mAccessToken.isSessionValid()*/) {
				String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
						.format(new java.util.Date(mAccessToken
								.getExpiresTime()));
				Log.d("TAG", "认证成功: \r\n access_token: " + token + "\r\n"
						+ "expires_in: " + expires_in + "\r\n有效期：" + date);

				AccessTokenKeeper.keepAccessToken(getApplicationContext(),
						mAccessToken);
				Toast.makeText(LoginActivity.this, "认证成功", Toast.LENGTH_SHORT)
						.show();
			}
		}

		@Override
		public void onError(WeiboDialogError e) {
			Toast.makeText(getApplicationContext(),
					"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(),
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
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

}

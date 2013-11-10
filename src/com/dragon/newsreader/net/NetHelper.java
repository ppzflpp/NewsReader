package com.dragon.newsreader.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetHelper {

	private final static String TAG = "NetHelper";
	private final static boolean DEBUG = false;

	private final static ConnectivityManager getManager(Context context) {
		if (context != null)
			return (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		else
			return null;
	}

	public static enum NetType {
		TYPE_NONE, TYPE_WIFI, TYPE_MOBILE
	};

	public final static NetType getNetWorkType(Context context) {
		NetType type = NetType.TYPE_NONE;
		ConnectivityManager manager = getManager(context);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null) {
			if (info.getType() == ConnectivityManager.TYPE_WIFI)
				type = NetType.TYPE_WIFI;
			else if (info.getType() == ConnectivityManager.TYPE_MOBILE)
				type = NetType.TYPE_MOBILE;
		}
		return type;
	}

	public final static InputStream getInputStream(Context context,
			String urlString, HttpURLConnection copyFrom) {
		InputStream inputStream = null;
		if (urlString == null || urlString.equals(""))
			return inputStream;
		HttpURLConnection connection = null;
		java.net.Proxy p = null;
		switch (getNetWorkType(context)) {
		case TYPE_MOBILE:
			String proxyHost = android.net.Proxy.getDefaultHost();
			if (proxyHost != null) {
				p = new java.net.Proxy(java.net.Proxy.Type.HTTP,
						new InetSocketAddress(
								android.net.Proxy.getDefaultHost(),
								android.net.Proxy.getDefaultPort()));
			}
		case TYPE_WIFI:
			try {
				if (DEBUG)
					Log.d(TAG, "proxyHost = " + p);
				if (DEBUG)
					Log.d(TAG, "urlString = " + urlString);
				if (p != null)
					connection = (HttpURLConnection) new URL(urlString)
							.openConnection(p);
				else
					connection = (HttpURLConnection) new URL(urlString)
							.openConnection();
				copyFrom = connection;
				connection.setConnectTimeout(20 * 1000);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case TYPE_NONE:
			inputStream = null;
		}

		try {
			if (connection != null) {
				connection.connect();
				inputStream = connection.getInputStream();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inputStream;
	}
}

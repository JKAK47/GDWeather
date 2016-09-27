package com.example.gdweather.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gdweather.application.MyApplication;

public class HttpUtil {

	public static final int VolleyLib = 1;
	public static final int Default = 0;
	private static final String TAG = "HttpUtil";

	// volley网络请求库队列
	static RequestQueue mQueue = Volley.newRequestQueue(MyApplication
			.getContext());

	public static void sendHttpRequest(String urlPath,
			HttpCallbackListener listener, int method) {

		if (method == HttpUtil.VolleyLib) {
			LogUtil.i(TAG, "volleyLib");
			sendHttpRequestFromVolley(urlPath, listener);
		} else if (method == HttpUtil.Default) {
			sendHttpRequestFromHttpUrlConnec(urlPath, listener);
		} else {
			LogUtil.i(TAG, "没有这种网络请求库");
		}

	}

	private static void sendHttpRequestFromVolley(String urlPath,
			final HttpCallbackListener listener) {
		// TODO Auto-generated method stub
		if (listener == null) {
			throw new IllegalArgumentException("HttpCallbackListener 必须提供");

		}
		if (mQueue != null) {

			JsonObjectRequest request = new JsonObjectRequest(urlPath, null,
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							// TODO Auto-generated method stub
							try {
								URLDecoder.decode(response.toString(), "utf-8");
								LogUtil.d(TAG, response.toString());
								listener.onFinish(response.toString());
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							// listener.onFinish(response);
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							// TODO Auto-generated method stub
							LogUtil.e(TAG, "下载失败");
						}
					});
			mQueue.add(request);
		}
	}

	// 默认网络请求库
	private static void sendHttpRequestFromHttpUrlConnec(final String urlPath,
			final HttpCallbackListener listener) {
		// TODO Auto-generated method stub

		new Thread(new Runnable() {
			@Override
			public void run() {
				LogUtil.i(TAG, "开始从服务器下载" + " path " + urlPath);
				HttpURLConnection connection = null;
				try {
					connection = (HttpURLConnection) new URL(urlPath)
							.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(5000);
					connection.setReadTimeout(8000);
					BufferedReader bReader = new BufferedReader(
							new InputStreamReader(connection.getInputStream()));

					if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
						StringBuilder response = new StringBuilder();
						String line;
						while ((line = bReader.readLine()) != null) {
							response.append(line);
						}
						if (listener != null) {
							LogUtil.d(TAG, "下载完成了");
							LogUtil.d(TAG, response.toString());
							listener.onFinish(response.toString());
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					if (listener != null) {
						listener.onError(e);

					}
				} finally {
					if (connection != null) {
						connection.disconnect();

					}
				}
			}
		}).start();
	}
}

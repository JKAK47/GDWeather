package com.example.gdweather.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

	public static final int Volley = 1;
	public static final int Default = 0;
	private static final String TAG = "HttpUtil";

	public static void sendHttpRequest(String urlPath,
			HttpCallbackListener listener, int method) {

		if (method == HttpUtil.Volley) {
			LogUtil.i(TAG, "还没实现这个网络请求");
		} else if (method == HttpUtil.Default) {
			sendHttpRequest(urlPath, listener);
		} else {
			LogUtil.i(TAG, "没有这种网络请求库");
		}

	}

	// 默认网络请求库
	private static void sendHttpRequest(final String urlPath,
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

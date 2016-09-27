package com.example.gdweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gdweather.R;
import com.example.gdweather.util.HttpCallbackListener;
import com.example.gdweather.util.HttpUtil;
import com.example.gdweather.util.LogUtil;
import com.example.gdweather.util.ParseUtility;

public class WeatherActivity extends Activity implements OnClickListener {

	private static final String TAG = "WeatherActivity";
	private LinearLayout weatherInfoLayout;
	private TextView cityNameText;
	private TextView publishText;
	private TextView weatherDescText;
	private TextView temp1;
	private TextView temp2;
	private TextView currentDateText;

	// 城市切换按钮
	private ImageButton switchCity;
	private ImageButton refreshWeather;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weathor_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_time_text);
		weatherDescText = (TextView) findViewById(R.id.weather_desc);
		temp1 = (TextView) findViewById(R.id.temp1);
		temp2 = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		String countyCode = getIntent().getStringExtra("county_code");// 从另外一个页面获取到城市的code
		LogUtil.d(TAG, "countyCode: " + countyCode);
		if (!TextUtils.isEmpty(countyCode)) {
			publishText.setText("同步中");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);// 根据这个地面code获取到对应的天气code
		} else {
			showWeather();// 没有传递地方code进来，显示默认
		}

		switchCity = (ImageButton) findViewById(R.id.switch_city);
		switchCity.setOnClickListener(this);
		refreshWeather = (ImageButton) findViewById(R.id.reflesh_weather);
		refreshWeather.setOnClickListener(this);
	}

	private void showWeather() {
		// TODO Auto-generated method stub
		SharedPreferences sharedPreferences = getSharedPreferences("weather",
				MODE_PRIVATE);
		cityNameText.setText(sharedPreferences.getString("city_name", ""));
		temp1.setText(sharedPreferences.getString("temp1", ""));
		temp2.setText(sharedPreferences.getString("temp2", ""));
		weatherDescText
				.setText(sharedPreferences.getString("weather_desc", ""));
		publishText.setText("今天 " + sharedPreferences.getString("ptime", "")
				+ "发布");
		currentDateText
				.setText(sharedPreferences.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}

	/**
	 * 查询一个地方对应的天气代码
	 * 
	 * @param countyCode
	 */
	private void queryWeatherCode(String countyCode) {
		// TODO Auto-generated method stub

		String path = "http://www.weather.com.cn/data/list3/city" + countyCode
				+ ".xml";
		LogUtil.d(TAG, "Path: " + path);
		queryFromServer(path, "countyCode");
	}

	private void queryWeatherInfo(String weatherCode) {
		String path = "http://www.weather.com.cn/data/cityinfo/" + weatherCode
				+ ".html";
		LogUtil.d(TAG, "Weather path: " + path);
		queryFromServer(path, "weatherCode");
		/**
		 * 通过volley网络请求库请求天气详情json数据
		 */
		// HttpUtil.sendHttpRequest(path, new HttpCallbackListener() {
		//
		// @Override
		// public void onFinish(String result) {
		// // TODO Auto-generated method stub
		// boolean response = false;
		// LogUtil.d(TAG, result);
		// response = ParseUtility.handleWeatherResponseFromJson(result);//
		// 将json结果解析并存放到sharedference
		// if (response) {
		// WeatherActivity.this.runOnUiThread(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// showWeather();// 读取sharedferences文件并显示到ui
		// }
		// });
		//
		// }
		// }
		//
		// @Override
		// public void onError(Exception e) {
		// // TODO Auto-generated method stub
		//
		// }
		// }, HttpUtil.VolleyLib);

	}

	private void queryFromServer(final String path, final String type) {
		// TODO Auto-generated method stub

		HttpUtil.sendHttpRequest(path, new HttpCallbackListener() {

			@Override
			public void onFinish(String result) {
				// TODO Auto-generated method stub

				if ("countyCode".equals(type)) {

					if (!TextUtils.isEmpty(result)) {

						String[] arrays = result.split("\\|");
						if (arrays != null && arrays.length == 2) {

							String weatherCode = arrays[1];
							queryWeatherInfo(weatherCode);// 根据天气code获取天气json数据
						}
					}
				} else if ("weatherCode".equals(type)) {
					boolean response = false;
					LogUtil.d(TAG, result);
					response = ParseUtility
							.handleWeatherResponseFromJson(result);// 将json结果解析并存放到sharedference
					if (response) {
						WeatherActivity.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								showWeather();// 读取sharedferences文件并显示到ui
							}
						});

					}
				}
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				WeatherActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						publishText.setText("同步失败，显示历史天气");
						Toast.makeText(WeatherActivity.this, "下载天气信息失败，显示历史信息",
								Toast.LENGTH_LONG).show();
						showWeather();
					}
				});
			}
		}, HttpUtil.Default);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		LogUtil.d(TAG, "onBackPressed on WeatherActivity");
		startActivity(new Intent(this, ChooseAreaActivity.class));
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		// 切换城市时候我需要去选择页面
		case R.id.switch_city:
			Intent nIntent = new Intent(this, ChooseAreaActivity.class);
			nIntent.putExtra("from_weather_activity", true);// 标记是来自于weatheractivity
			startActivity(nIntent);
			finish();// 关闭这个页面
			break;
		case R.id.reflesh_weather:
			publishText.setText("同步中...稍等片刻");
			SharedPreferences sp = getSharedPreferences("weather", MODE_PRIVATE);
			String weatherCode = sp.getString("weather_code", "");
			if (!TextUtils.isEmpty(weatherCode)) {
				queryWeatherInfo(weatherCode);
			}
			break;
		default:
			break;
		}
	}
}

package com.example.gdweather.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gdweather.R;
import com.example.gdweather.db.WeatherDbOperation;
import com.example.gdweather.model.City;
import com.example.gdweather.model.County;
import com.example.gdweather.model.Province;
import com.example.gdweather.util.HttpCallbackListener;
import com.example.gdweather.util.HttpUtil;
import com.example.gdweather.util.LogUtil;
import com.example.gdweather.util.ParseUtility;

public class ChooseAreaActivity extends Activity {

	// 当前选中的级别
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	protected static final String TAG = "ChooseAreaActivity";

	private ProgressDialog progressDialog;
	private TextView textView;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private WeatherDbOperation dbOperation;
	private final List<String> dataList = new ArrayList<String>();

	private List<Province> provinces;// 省列表
	private List<City> cities;// 市列表
	private List<County> counties;// 县列表
	private Province selectedProvince;// 选中的省份
	private City selectedCity;// 选中的市
	private County selectedCounty;

	private int currentLevel = LEVEL_PROVINCE;// 当前处于那个级别

	private boolean isFromWeatherActivity;// 标记是否从weatheractivity页面返回来的

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		isFromWeatherActivity = getIntent().getBooleanExtra(
				"from_weather_activity", false);// 获取到这个字段值看是不是从那边过来的
		// 直接调转到天气详情页
		SharedPreferences sp = this.getSharedPreferences("weather",
				MODE_PRIVATE);
		if (sp.getBoolean("city_selected", false) && !isFromWeatherActivity) {
			// sp文件有城市信息并不是从weatheractivity页面跳过来的
			// 比如应用刚启动
			Intent intent = new Intent(this, WeatherActivity.class);
			// intent.putExtra("county_code", sp.getString("", defValue))
			startActivity(intent);
			finish();
			return;
		}
		listView = (ListView) findViewById(R.id.list_view);
		textView = (TextView) findViewById(R.id.title_tv);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		dbOperation = WeatherDbOperation.getInstance(this);

		// listView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// LogUtil.i(TAG, "listview 被单击");
		// }
		// });listview不能设置onclick监听器，不然抛异常
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinces.get(position);
					LogUtil.d(TAG, selectedProvince.getProvinceName() + "/"
							+ selectedProvince.getProvinceCode());
					queryCities();
				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cities.get(position);
					LogUtil.d(TAG, selectedCity.getCityName() + "/"
							+ selectedCity.getCityCode());
					queryCounties();

				} else if (currentLevel == LEVEL_COUNTY) {
					selectedCounty = counties.get(position);
					LogUtil.i(TAG,
							"最后一个层级,单击的是：" + selectedCounty.getCountyName()
									+ "/" + selectedCounty.getCountyCode());
					Intent intent = new Intent(ChooseAreaActivity.this,
							WeatherActivity.class);
					intent.putExtra("county_code",
							selectedCounty.getCountyCode());
					startActivity(intent);
					finish();
				}
			}
		});
		queryProvinces();// 加载省级数据
	}

	/**
	 * 查询省份名字列表
	 */
	private void queryProvinces() {
		// TODO Auto-generated method stub
		provinces = dbOperation.readProvincesFromDB();
		if (provinces.size() > 0) {
			dataList.clear();
			for (Province province : provinces) {
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();// 通知adaper数据已经改变
			listView.setSelection(0);// 设定光标设定在哪里
			textView.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		} else {// 如果数据库哪里没有，去网上下载
			queryFromServer(null, "province");
		}
	}

	/**
	 * 从网上获取省份表，省份下的城市表，一个城市下的县级城市表
	 * 
	 * @param code
	 * @param string
	 */
	private void queryFromServer(final String code, final String type) {
		String path;
		LogUtil.i(TAG, "queryFromServer");
		if (!TextUtils.isEmpty(code)) {
			path = "http://www.weather.com.cn/data/list3/city" + code + ".xml";

		} else {
			path = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();// 打开对话框
		// 开始下载
		HttpUtil.sendHttpRequest(path, new HttpCallbackListener() {

			@Override
			public void onFinish(String result) {
				// TODO Auto-generated method stub
				// 从网络上下载完后回调这个方法
				boolean resPons = false;
				LogUtil.d(TAG, "result: " + result);
				if ("province".equals(type)) {// 将网络上下载的所有省份保存到数据库
					resPons = ParseUtility.handleProvinceResponse(dbOperation,
							result);
					LogUtil.d(TAG, "province "
							+ (resPons == true ? "true" : "false"));
				} else if ("city".equals(type)) {// 将一个省份的所有城市存入数据库
					resPons = ParseUtility.handleCityResponse(dbOperation,
							result, selectedProvince.getProvinceId());

					LogUtil.d(TAG, "city "
							+ (resPons == true ? "true" : "false"));
				} else if ("county".equals(type)) {
					resPons = ParseUtility.handleCountyResponse(dbOperation,
							result, selectedCity.getCityId());// 将结果写入到数据库
					LogUtil.d(TAG, "county "
							+ (resPons == true ? "true" : "false"));
				}

				if (resPons) {
					// 通过runOnUiThread方法更新UI
					ChooseAreaActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							LogUtil.d(TAG, "更新到UI");
							if ("province".equals(type)) {
								queryProvinces();// 跟新省份

							} else if ("city".equals(type)) {
								queryCities();// 更新城市

							} else if ("county".equals(type)) {
								queryCounties();
							}
						}
					});
				}

			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub

				ChooseAreaActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeProgressDialog();
						LogUtil.e(TAG, "加载失败");
					}
				});
			}
		}, HttpUtil.Default);
	}

	private void showProgressDialog() {
		// TODO Auto-generated method stub
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("download...");
			// progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();

		}

	}

	/**
	 * 根据选中的市，获取该市所有的县级城市名列表
	 */
	private void queryCounties() {
		// TODO Auto-generated method stub
		textView.setText(selectedCity.getCityName());
		counties = dbOperation.readCountyFromDB(selectedCity.getCityId());
		if (counties.size() > 0) {
			dataList.clear();
			for (County county : counties) {
				dataList.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			currentLevel = LEVEL_COUNTY;
		} else {
			queryFromServer(selectedCity.getCityCode(), "county");
		}
	}

	/**
	 * 根据选中的省份获取到该省份的所有的城市名列表
	 */
	private void queryCities() {
		// TODO Auto-generated method stub
		cities = dbOperation.readCitysFromDB(selectedProvince.getProvinceId());
		textView.setText(selectedProvince.getProvinceName());
		if (cities.size() > 0) {
			dataList.clear();
			for (City city : cities)
				dataList.add(city.getCityName());
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			LogUtil.d(TAG, "back key pressed");

		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 改写back键的后退逻辑
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();不掉用就不会闪退
		LogUtil.d(TAG, "onBackPressed");
		if (this.currentLevel == LEVEL_COUNTY) {
			queryCities();// 如果现在在县一级返回上一级的城市列表

		} else if (this.currentLevel == LEVEL_CITY) {
			queryProvinces();

		} else {
			if (isFromWeatherActivity) {
				// 如果是从weatheractivity跳过来的
				startActivity(new Intent(this, WeatherActivity.class));
			}
			finish();
		}
	}
}

package com.example.gdweather.util;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.gdweather.application.MyApplication;
import com.example.gdweather.db.WeatherDbOperation;
import com.example.gdweather.model.City;
import com.example.gdweather.model.County;
import com.example.gdweather.model.Province;

public class ParseUtility {

	private static final String TAG = "ParseUtility";

	/*
	 * 解析从网络接口返回的一个天气数据的JSON对象
	 */
	/**
	 * 
	 * @param response
	 *            ,某一个城市的天气数据响应
	 */
	public static synchronized boolean handleWeatherResponseFromJson(
			String response) {

		JSONObject sJsonObject;
		try {
			sJsonObject = new JSONObject(response);
			JSONObject weatherInfo = sJsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String cityWeatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesc = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			// 将这个数据存放到preferences出
			saveWeatherInfo(cityName, cityWeatherCode, temp1, temp2,
					weatherDesc, publishTime);
			return true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 将一个解析后的数据存放到SharedPreferences中
	 * 
	 * @param cityName
	 * @param cityWeatherCode
	 * @param temp1
	 * @param temp2
	 * @param weatherDesc
	 * @param publishTime
	 */
	private static void saveWeatherInfo(String cityName,
			String cityWeatherCode, String temp1, String temp2,
			String weatherDesc, String publishTime) {
		// TODO Auto-generated method stub

		SharedPreferences.Editor editor = MyApplication
				.getContext()
				.getSharedPreferences("weather",
						MyApplication.getContext().MODE_PRIVATE).edit();
		editor.putString("city_name", cityName);
		editor.putString("weather_code", cityWeatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desc", weatherDesc);
		editor.putString("ptime", publishTime);
		editor.putBoolean("city_selected", true);
		editor.putString("current_date", DateSyncUtil.formatDate(new Date()));
		editor.commit();
		// Thread.currentThread().getStackTrace();

	}

	/**
	 * 解析返回的省级名字数据
	 * 
	 */

	public static synchronized boolean handleProvinceResponse(
			WeatherDbOperation dbOperation, String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");

			if (allProvinces != null && allProvinces.length > 0) {
				for (String string : allProvinces) {
					String[] province = string.split("\\|");
					Province p = new Province();
					p.setProvinceCode(province[0]);
					p.setProvinceName(province[1]);
					LogUtil.d(TAG, p.toString());
					dbOperation.savaProvince(p);

				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 插入一组某一个省的所有城市的城市名和code到city表
	 * 
	 * @param dbOperation
	 * @param response
	 * @param provinceId
	 * @return
	 */
	public static synchronized boolean handleCityResponse(
			WeatherDbOperation dbOperation, String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCitys = response.split(",");

			if (allCitys != null && allCitys.length > 0) {
				for (String string : allCitys) {
					String[] city = string.split("\\|");
					City c = new City();
					c.setCityCode(city[0]);
					c.setCityName(city[1]);
					c.setProvinceId(provinceId);
					LogUtil.d(TAG, c.toString());
					dbOperation.saveCity(c);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 往数据库插入一组县城的城市和code到County表
	 * 
	 * @param dbOperation
	 * @param response
	 * @param cityId
	 * @return
	 */
	public static synchronized boolean handleCountyResponse(
			WeatherDbOperation dbOperation, String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCounties = response.split(",");

			if (allCounties != null && allCounties.length > 0) {
				for (String string : allCounties) {
					String[] county = string.split("\\|");
					County c = new County();
					c.setCityId(cityId);
					c.setCountyCode(county[0]);
					c.setCountyName(county[1]);
					LogUtil.d(TAG, c.toString());
					dbOperation.saveCounty(c);
				}
				return true;
			}
		}
		return false;
	}

}

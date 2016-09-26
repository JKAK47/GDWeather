package com.example.gdweather.util;

import android.text.TextUtils;

import com.example.gdweather.db.WeatherDbOperation;
import com.example.gdweather.model.City;
import com.example.gdweather.model.County;
import com.example.gdweather.model.Province;

public class ParseUtility {

	private static final String TAG = "ParseUtility";

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

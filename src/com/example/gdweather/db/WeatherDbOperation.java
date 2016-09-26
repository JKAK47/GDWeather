package com.example.gdweather.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gdweather.model.City;
import com.example.gdweather.model.County;
import com.example.gdweather.model.Province;

/**
 * 
 * @author cindy
 * 
 */
public class WeatherDbOperation {

	/*
	 * 数据库名字
	 */
	public static final String DB_NAME = "GD_Weather";
	/*
	 * 数据库版本
	 */
	public static final int VERSION = 1;
	private static WeatherDbOperation dbOperation = null;
	private final SQLiteDatabase db;
	private static Object lock = new Object();

	private WeatherDbOperation(Context context) {
		WeatherOpenHelper openHelper = new WeatherOpenHelper(context, DB_NAME,
				null, VERSION);
		db = openHelper.getWritableDatabase();
	}

	/**
	 * 获取到一个操作类实例
	 * 
	 * @param context
	 * @return
	 */
	public static WeatherDbOperation getInstance(Context context) {
		if (dbOperation == null) {
			synchronized (lock) {
				dbOperation = new WeatherDbOperation(context);
			}
		}
		return dbOperation;
	}

	public void savaProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}

	}

	public List<Province> readProvincesFromDB() {
		List<Province> list = new ArrayList<Province>();

		Cursor cursor = db
				.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setProvinceCode(cursor.getString(cursor
						.getColumnIndex("province_code")));
				province.setProvinceId(cursor.getInt(cursor
						.getColumnIndex("province_id")));
				province.setProvinceName(cursor.getString(cursor
						.getColumnIndex("province_name")));
				list.add(province);
			} while (cursor.moveToNext());

		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}

	/**
	 * 将city数据存放到数据库
	 */

	public void saveCity(City city) {

		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);

		}
	}

	/**
	 * 获取某个省份下面的所有城市
	 * 
	 * @return
	 */
	public List<City> readCitysFromDB(int provinceId) {

		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id= ?",
				new String[] { String.valueOf(provinceId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {

				City city = new City();
				city.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
				city.setProvinceId(cursor.getInt(cursor
						.getColumnIndex("province_id")));
				city.setCityName(cursor.getString(cursor
						.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor
						.getColumnIndex("city_code")));
				list.add(city);

			} while (cursor.moveToNext());

		}

		if (cursor != null) {
			cursor.close();

		}
		return list;
	}

	public void saveCounty(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			db.insert("County", null, values);
		}
	}

	public List<County> readCountyFromDB(int cityId) {

		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?",
				new String[] { String.valueOf(cityId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {

				County county = new County();
				county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
				county.setCountyId(cursor.getInt(cursor
						.getColumnIndex("county_id")));
				county.setCountyCode(cursor.getString(cursor
						.getColumnIndex("county_code")));
				county.setCountyName(cursor.getString(cursor
						.getColumnIndex("county_name")));
				list.add(county);
			} while (cursor.moveToNext());

		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}
}

package com.example.gdweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gdweather.util.LogUtil;

/**
 * //weather的天气数据库操作类，主要逻辑是表创建，数据库创建
 * 
 * @author cindy
 * 
 */
public class WeatherOpenHelper extends SQLiteOpenHelper {

	private static final String CREATE_PROVINCE = "create table Province("
			+ "province_id integer primary key autoincrement NOT NULL, "
			+ "province_name text NOT NULL, " + "province_code text NOT NULL)";

	private static final String CREATE_CITY = "create table City("
			+ "city_id integer primary key autoincrement NOT NULL, "
			+ "city_name text NOT NULL, "
			+ "city_code text NOT NULL, province_id integer NOT NULL)";

	private static final String CREATE_COUNTY = "create table County("
			+ "county_id integer primary key autoincrement NOT NULL, "
			+ "county_name text NOT NULL, "
			+ "county_code text NOT NULL, city_id integer NOT NULL)";

	private static final String TAG = "WeatherOpenHelper";

	public WeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		LogUtil.i(TAG, "database create successfully");
		db.execSQL(CREATE_PROVINCE);// 创建省表
		db.execSQL(CREATE_CITY);// 创建城市表
		db.execSQL(CREATE_COUNTY);// 创建县表
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}

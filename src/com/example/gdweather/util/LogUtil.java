package com.example.gdweather.util;

import android.util.Log;

/**
 * log可以分等级打印调试信息
 * 
 * @author cindy
 * 
 */
public class LogUtil {
	// 定义六种调式等级
	public static final int VERBOSE = 0;
	public static final int DEBUG = 1;
	public static final int INFO = 2;
	public static final int WARN = 3;
	public static final int ERROR = 4;
	public static final int NOTHING = 5;

	// 标识当前调试的等级
	public static int LEVEL = VERBOSE;

	public static int getLEVEL() {
		return LEVEL;
	}

	public static void setLEVEL(int lEVEL) {
		LEVEL = lEVEL;
	}

	public static void v(String tag, String msg) {
		if (getLEVEL() <= VERBOSE)
			Log.v(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (getLEVEL() <= DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (getLEVEL() <= INFO) {
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (getLEVEL() <= WARN) {
			Log.w(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (getLEVEL() <= ERROR) {
			Log.e(tag, msg);
		}
	}
}

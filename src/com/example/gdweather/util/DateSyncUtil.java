package com.example.gdweather.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期格式化工具
 * 
 * @author cindy
 * 
 */
public class DateSyncUtil {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy年M月d日", Locale.CHINA);

	public static String formatDate(Date date) {
		synchronized (dateFormat) {
			return dateFormat.format(date);
		}

	}

	public static Date parseString(String stringDate) {
		synchronized (dateFormat) {
			try {
				return dateFormat.parse(stringDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}
	}
}

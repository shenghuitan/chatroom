package com.romtn.netty.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * TimeUtil
 * 线程安全的时间类工具
 * 
 * @author RomanTan
 * @since 2015-08-31
 *
 */
public final class TimeUtil {

	private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private static final String MIN_DATE_PATTERN = "yyyyMMddHHmmss";
	
	/**
	 * 1秒的毫秒数
	 */
	public static final long SECOND_MILLIS = 1000L;
	
	/**
	 * 1分钟的毫秒数
	 */
	public static final long MINUTE_MILLIS = 60L * SECOND_MILLIS;
	
	/**
	 * 1小时的毫秒数
	 */
	public static final long HOUR_MILLIS = 60L * MINUTE_MILLIS;
	
	/**
	 * 1天的毫秒数
	 */
	public static final long DAY_MILLIS = 24L * HOUR_MILLIS;
	
	/**
	 * 1年的毫秒数
	 */
	public static final long YEAR_MILLIS = 365L * DAY_MILLIS;
	
	private static final DateFormat getDateFormat(){
		return new SimpleDateFormat(DATE_PATTERN);
	}
	
	/**
	 * 线程安全，获取日期Format
	 */
	private static final DateFormat getDateFormat(String pattern){
		return new SimpleDateFormat(pattern);
	}
	
	/**
	 * 当前时间的字符串表示
	 */
	public static final String now(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
	}
	
	/**
	 * Date取字符串：格式：yyyy-MM-dd HH:mm:ss
	 */
	public static final String toString(Date date){
		if (date == null){
			return null;
		}
		return getDateFormat().format(date);
	}
	
	/**
	 * Date日期字符串转换成日期格式
	 * @param dateString 格式：yyyy-MM-dd HH:mm:ss
	 * @return	{@link Date}
	 * @throws ParseException 
	 */
	public static final Date toDate(String dateString) throws ParseException{
		Long timeMillis = toTimeMillis(dateString);
		if (timeMillis != null){
			return new Date(timeMillis);
		}
		return null;
	}
	
	/**
	 * Date日期字符串转换成时间戳
	 * @param dateString 格式：yyyy-MM-dd HH:mm:ss
	 * @return	{@link System#currentTimeMillis()}
	 * @throws ParseException 
	 */
	public static final Long toTimeMillis(String dateString) throws ParseException{
		String value = dateString.replaceAll("\\D*", "");
		int len = value.length() > MIN_DATE_PATTERN.length() ? MIN_DATE_PATTERN.length() : value.length();
		value = value.substring(0, len);
		Date date = getDateFormat(MIN_DATE_PATTERN.substring(0, len)).parse(value);
		return date.getTime();
	}
	
}

package cn.i4.report.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理的工具类
 * 
 * @author Vernon.Chen
 * 
 */
public class DateUtils {
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	public static final String FORMAT_DATE_NUM = "yyyyMMdd";
	public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_DATEHOUR = "yyyy-MM-dd HH";
	public static final String FORMAT_MONTH = "yyyy-MM";

	/**
	 * 当前日期
	 * 
	 * @return Date
	 */
	public static Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}

	/**
	 * 设置时间
	 * 
	 * @param date
	 *            Date
	 * @param field
	 *            时间段
	 * @param amount
	 *            数量
	 * @return Date
	 */
	public static Date set(Date date, int field, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(field, amount);
		return calendar.getTime();
	}

	/**
	 * 时间增加
	 * 
	 * @param date
	 *            Date
	 * @param field
	 *            时间段
	 * @param amount
	 *            数量
	 * @return Date
	 */
	public static Date add(Date date, int field, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * 时间增加
	 * 
	 * @param field
	 *            时间段
	 * @param amount
	 *            数量
	 * @return Date
	 */
	public static Date add(int field, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * 按照format指定的格式,格式化字符串为时间类型,如果格式化失败返回null
	 * 
	 * @param str
	 *            时间字符串
	 * @param format
	 *            格式
	 * @return Date
	 */
	public static Date string2Date(String str, String format) {
		try {
			if (str != null) {
				return new SimpleDateFormat(format).parse(str);
			}
		} catch (ParseException ignored) {
		}
		return null;
	}

	/**
	 * 按照format指定的格式,格式化字符串为时间类型,如果格式化失败返回null
	 * 
	 * @param str
	 *            时间字符串
	 * @param format
	 *            格式
	 * @param defaultValue
	 *            默认时间
	 * @return Date
	 */
	public static Date string2Date(String str, String format, Date defaultValue) {
		try {
			if (str != null) {
				return new SimpleDateFormat(format).parse(str);
			}
		} catch (ParseException ignored) {
		}
		return defaultValue;
	}

	/**
	 * 日期串格式化
	 * 
	 * @param str
	 * @param format
	 * @return
	 */
	public static String formatDateString(String str, String format) {
		return date2String(string2Date(str, format), format);
	}

	/**
	 * 日期格式化
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date formatDate(Date date, String format) {
		return string2Date(date2String(date, format), format);
	}

	/**
	 * 按照format指定的格式,格式化字符串为时间类型,如果格式化失败返回null
	 * 
	 * @param date
	 *            时间
	 * @param format
	 *            格式
	 * @return String
	 */
	public static String date2String(Date date, String format) {
		if (date != null) {
			return new SimpleDateFormat(format).format(date);
		}
		return null;
	}

	/**
	 * 默认日期格式
	 * 
	 * @param date
	 * @return
	 */
	public static String date2StringDefault(Date date) {
		return date2String(date, FORMAT_DATE);
	}

	/**
	 * 标准输出日期格式化,当日的不显示日期,本年的不显示年份
	 * 
	 * @param date
	 *            时间
	 * @return String
	 */
	public static String date2String(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		Calendar dateCalendar = Calendar.getInstance();
		dateCalendar.setTime(date);
		StringBuffer format = new StringBuffer();
		if (dateCalendar.get(Calendar.YEAR) != calendar.get(Calendar.YEAR)) {
			format.append("yy-MM-dd ");
		} else if (dateCalendar.get(Calendar.DAY_OF_YEAR) != calendar
				.get(Calendar.DAY_OF_YEAR)) {
			format.append("MM-dd ");
		}
		format.append("HH:mm:ss");
		return date2String(date, format.toString());
	}

	/**
	 * 获取时间差
	 * 
	 * @param date
	 *            时间
	 * @param postfix
	 *            修饰词(e.g:xxx之前)
	 * @return String
	 */
	public static String dateDiff2String(Date date, String postfix) {
		long longTime = date.getTime();
		long aa = System.currentTimeMillis() - longTime;
		if (postfix == null) {
			postfix = "";
		}
		if (aa <= 1000) {
			return "1秒" + postfix;
		}
		long bb = aa / 1000;// 秒
		if (bb < 60) {
			return bb + "秒" + postfix;
		}
		long cc = bb / 60;// minitue
		if (cc < 60) {
			return cc + "分钟" + postfix;
		}
		long dd = cc / 60;
		if (dd < 24) {
			return dd + "小时" + postfix;
		}
		long ee = dd / 24;
		if (ee < 7) {
			return ee + "天" + postfix;
		}
		long ff = ee / 7;
		if (ff <= 4) {
			return ff + "周" + postfix;
		}
		return date2String(date);
	}

	/**
	 * 获取时间差
	 * 
	 * @param date
	 *            时间
	 * @param postfix
	 *            修饰
	 * @param format
	 *            格式
	 * @return String
	 */
	public static String dateDiff2String(Date date, String postfix,
			String format) {
		long longTime = date.getTime();
		long aa = System.currentTimeMillis() - longTime;
		if (postfix == null) {
			postfix = "";
		}
		if (aa <= 1000) {
			return "1秒" + postfix;
		}
		long bb = aa / 1000;// 秒
		if (bb < 60) {
			return bb + "秒" + postfix;
		}
		long cc = bb / 60;// minitue
		if (cc < 60) {
			return cc + "分钟" + postfix;
		}
		long dd = cc / 60;
		if (dd < 24) {
			return dd + "小时" + postfix;
		}
		long ee = dd / 24;
		if (ee < 7) {
			return ee + "天" + postfix;
		}
		long ff = ee / 7;
		if (ff <= 4) {
			return ff + "周" + postfix;
		}
		return date2String(date, format);
	}

	public static String cutHost(Date date, int value) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, value);
		return date2String(cal.getTime(), FORMAT_DATEHOUR);
	}

	public static String cutDay(Date date, int value) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, value);
		return date2StringDefault(cal.getTime());
	}
	
	public static String operMonth(int month){
		Date date = new Date();//当前日期
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//格式化对象
		Calendar calendar = Calendar.getInstance();//日历对象
		calendar.setTime(date);//设置当前日期
		calendar.add(Calendar.MONTH, month);//月份加、减
		return format.format(calendar.getTime());
	}

	public static String operMonth(String str, int month){
		Date date = string2Date(str, FORMAT_MONTH);//当前日期
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");//格式化对象
		Calendar calendar = Calendar.getInstance();//日历对象
		calendar.setTime(date);//设置当前日期
		calendar.add(Calendar.MONTH, month);//月份加、减
		return format.format(calendar.getTime());
	}
	
	public static String operDay(int day){
		Date date = new Date();//当前日期
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//格式化对象
		Calendar calendar = Calendar.getInstance();//日历对象
		calendar.setTime(date);//设置当前日期
		calendar.add(Calendar.DAY_OF_MONTH, day);//月份加、减
		return format.format(calendar.getTime());
	}

	public static String operDayNum(int day){
		Date date = new Date();//当前日期
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");//格式化对象
		Calendar calendar = Calendar.getInstance();//日历对象
		calendar.setTime(date);//设置当前日期
		calendar.add(Calendar.DAY_OF_MONTH, day);//月份加、减
		return format.format(calendar.getTime());
	}
	
	public static String operDay(String str, int day){
		Date date = string2Date(str, FORMAT_DATE_NUM);//当前日期
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//格式化对象
		Calendar calendar = Calendar.getInstance();//日历对象
		calendar.setTime(date);//设置当前日期
		calendar.add(Calendar.DAY_OF_MONTH, day);//月份加、减
		return format.format(calendar.getTime());
	}

	public static String operDayNum(String str, int day){
		Date date = string2Date(str, FORMAT_DATE_NUM);//当前日期
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");//格式化对象
		Calendar calendar = Calendar.getInstance();//日历对象
		calendar.setTime(date);//设置当前日期
		calendar.add(Calendar.DAY_OF_MONTH, day);//月份加、减
		return format.format(calendar.getTime());
	}
	
	public static String curMonth(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//格式化对象
		Calendar c = Calendar.getInstance();   
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        return format.format(c.getTime());
	}
	
	public static int daysBetween(String smdate,String bdate) throws ParseException{  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(sdf.parse(smdate));    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(sdf.parse(bdate));    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));     
    }
}

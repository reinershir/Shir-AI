package io.github.reinershir.boot.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	/**
	 * 
	* @Title:       nowDate 
	* @Version:     v1.0
	* @Author:      ReinerShir
	* @CreateDate:  2016年9月10日 下午4:27:08
	* @Description: 获取当前时间    比SimpleDateFormat 块
	* @return    
	* String    
	* @throws
	 */
	public static String getNowDate(){
		StringBuilder sBuilder = new StringBuilder();
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH)+1;
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int day = calendar.get(Calendar.DATE);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		sBuilder.append(calendar.get(Calendar.YEAR)).append("-");
		sBuilder.append(month<10?"0"+month:month).append("-");
		sBuilder.append(day<10?"0"+day:day).append(" ");
		sBuilder.append(hour<10?"0"+hour:hour).append(":");
		sBuilder.append(minute<10?"0"+minute:minute).append(":");;
		sBuilder.append(second<10?"0"+second:second);
		return sBuilder.toString();
	}
	
	
	public static String getNowDay(){
		StringBuilder sBuilder = new StringBuilder();
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DATE);
		sBuilder.append(calendar.get(Calendar.YEAR));
		sBuilder.append(month<10?"0"+month:month);
		sBuilder.append(day<10?"0"+day:day);
		return sBuilder.toString();
	}
	
	public static Date getStartNowDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
		// return new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
	}

	public static Date getEndNowDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, -1);
		return c.getTime();
		// return new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
	}

	public static String getNowDate(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}

	/**
	 * 将短时间格式时间转换为字符串 MM-dd
	 *
	 * @param date
	 * @return
	 */
	public static String dateToStr(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}

	/**
	 * 校验日期格式 HH:mm(时分格式)
	 * @param dateStr
	 * @return
	 */
	public static boolean isValidDate(String dateStr) {
		if (dateStr.matches("(0\\d{1}|1\\d{1}|2[0-3]):([0-5]\\d{1})")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 分钟转换天时分钟时间格式(前端展示用)
	 * @param minute
	 * @return
	 */
	public static String turnDayHourMinuteString(int minute) {
		//如果传入的分钟是0，默认0天
		if (0 == minute) {
			return 0 + "天";
		}
		//如果分钟小于60，默认返回分钟
		if (0 < minute && minute < 60) {
			return minute + "分钟";
		}
		//如果分钟小于24小时（1440分钟），返回小时和分钟
		if (60 <= minute && minute < 1440) {

			if (minute % 60 == 0) {
				int h = minute / 60;
				return h + "小时";
			} else {
				int h = minute / 60;
				int m = minute % 60;
				return h + "小时" + m + "分钟";
			}

		}
		//如果分钟大于1天
		if (minute >= 1440) {

			int d = minute / 60 / 24;
			int h = minute / 60 % 24;
			int m = minute % 60;
			String s1 = null;
			if (d > 0) {
				s1 = d + "天";
			}
			//h如果计算大于等于1再展示，否则只展示天和分钟
			if (h >= 1) {
				s1 += h + "小时";
			}
			if (m > 0) {
				s1 += m + "分钟";
			}

			return s1;
		}
		return null;
	}
}

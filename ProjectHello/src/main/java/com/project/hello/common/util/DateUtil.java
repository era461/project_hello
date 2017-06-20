package com.project.hello.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class DateUtil {
	private final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private final static int MILLIS_PER_HOUR = 3600000; // 60*60*1000

	// //range
	// //solar 1881.01.30 -- 2044.01.29
	// //lunar 1881.01.01 -- 2043.12.30
	// public final static int MIN_SOLAR_DATE = 18810130;
	// public final static int MAX_SOLAR_DATE = 20440129;
	//
	// public final static int MIN_LUNAR_DATE = 18810101;
	// public final static int MAX_LUNAR_DATE = 20431230;
	//
	// private final static int BASE_SOLAR_YEAR = Integer.parseInt(
	// (MIN_SOLAR_DATE+"").substring(0,4) );
	// private final static int BASE_LUNAR_YEAR = Integer.parseInt(
	// (MIN_LUNAR_DATE+"").substring(0,4) );
	//
	private final static int[] SOLAR_MONTH_ARRAY = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	private final static String[] TEN_KR_ARRAY = { "갑", "을", "병", "정", "무", "기", "경", "신", "임", "계" };

	private final static String[] TEN_CH_ARRAY = { "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸" };

	private final static String[] ZODIAC_KR_ARRAY = { "자", "축", "인", "묘", "진", "사", "오", "미", "신", "유", "술", "해" };

	private final static String[] ZODIAC_CH_ARRAY = { "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥" };

	private final static String[] ZODIAC_ARRAY = { "쥐", "소", "호랑이", "토끼", "용", "뱀", "말", "양", "원숭이", "닭", "개", "돼지" };

	// /**
	// * 음력 월일을 해당연도(음력) 양력 년월일로 변환
	// * @param yyyy yyyy
	// * @param lunar_md MMdd
	// * @param leap_month true : 윤달, false : 평달
	// * @return yyyyMMdd
	// */
	// public static String convertLunar(String yyyy, String lunar_md, boolean
	// leap_month)
	// {
	// int year = Integer.parseInt(yyyy);
	//
	// int month = Integer.parseInt( lunar_md.substring(0, 2) );
	// int day = Integer.parseInt( lunar_md.substring(2, 4) );
	//
	// lunar_md = year + lunar_md;
	// leap_month = (leap_month) ? isLeapMonth(year, month, false) : false;
	// int last_day = getLunarMonthDay(year, month, leap_month);
	// day = (day > last_day) ? last_day : day;
	//
	// return lunar2solar(lunar_md, leap_month);
	// }

	// public static void main(String[] args)
	// {
	// if (args.length < 1)
	// {
	// System.out.println("Usage : Lunar yyyyMMdd");
	// System.exit(-1);
	// }
	//
	// String str = solar2lunar(args[0]);
	// //System.out.println(args[0] + " ---> " + lunar2solar(args[0], false));
	// System.out.println( "Solar to Lunar : " + args[0] + " ---> " +
	// (str.charAt(0) == '0' ? "평달 " : "윤달 ") + str.substring(1) );
	// System.out.println( "Lunar to Solar : " + (str.charAt(0) == '0' ? "평달 " :
	// "윤달 ") + str.substring(1) + " ---> " + lunar2solar(str.substring(1),
	// str.charAt(0) == '1') );
	// System.out.println( convertLunar("2001", str.substring(5), str.charAt(0)
	// == '1') );
	// }
	/**
	 * 날짜 덧셈 (String + long) @ param String (yyyyMMdd or yyyyMMddHHmmss) @ param
	 * int @ return java.Util.Date
	 */
	public static java.util.Date addDate(String dt, long day) {
		if (dt == null) {
			throw new IllegalArgumentException("dt can not be null");
		}

		int len = dt.length();
		if (!(len == 8 || len == 14)) {
			throw new IllegalArgumentException("dt length must be 8 or 14 (yyyyMMdd or yyyyMMddHHmmss)");
		}

		if (len == 8) {
			dt += "090000";
		}

		return addDate(getDate(dt), day);
	}

	public static String convertQuicsFormat(String dt) {
		if (dt == null)
			return "";
		try {
			return convertFormat(dt, "yyyy/MM/dd HH:mm:ss");
		} catch (Exception e) {
			return dt;
		}
	}

	public static String convertQuicsTimeFormat(String dt) {
		if (dt == null)
			return "";
		try {
			return convertFormat("20000101" + dt, "HH:mm:ss");
		} catch (Exception e) {
			return dt;
		}
	}

	public static String convertShortQuicsFormat(String dt) {
		if (dt == null)
			return "";
		try {
			return convertFormat(dt, "yyyy/MM/dd");
		} catch (Exception e) {
			return dt;
		}
	}

	/**
	 * 날짜 뎃셈 (Date + long) @ param java.Util.Date @ param long @ return
	 * java.Util.Date
	 */
	public static java.util.Date addDate(java.util.Date dt, long day) {
		return new java.util.Date(dt.getTime() + (MILLIS_PER_HOUR * 24L * day));
	}

	/**
	 * add date @ param String (yyyyMMdd or yyyyMMddHHmmss) @ param int @ return
	 * String (yyyyMMddHHmmss)
	 */
	public static String addDay(String dt, long day) {
		if (dt == null) {
			throw new IllegalArgumentException("dt can not be null");
		}

		int len = dt.length();
		if (!(len == 8 || len == 14)) {
			throw new IllegalArgumentException("dt length must be 8 or 14 (yyyyMMdd or yyyyMMddHHmmss)");
		}

		if (len == 8) {
			dt += "090000";
		}

		return addDay(getDate(dt), day);
	}

	/**
	 * add date @ param java.Util.Date @ param int @ return String
	 * (yyyyMMddHHmmss)
	 */
	public static String addDay(java.util.Date dt, long day) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf.setTimeZone(getTimeZone());

		return sdf.format(addDate(dt, day));
	}

	/**
	 * between date
	 * 
	 * @param String
	 *            from (yyyyMMdd or yyyyMMddHHmmss)
	 * @param String
	 *            to (yyyyMMdd or yyyyMMddHHmmss)
	 * @return int
	 */
	public static long betweenDate(String from, String to) {
		if (from == null) {
			throw new IllegalArgumentException("from can not be null");
		}
		if (to == null) {
			throw new IllegalArgumentException("to can not be null");
		}

		int len = from.length();
		if (!(len == 8 || len == 14)) {
			throw new IllegalArgumentException("from length must be 8 or 14 (yyyyMMdd or yyyyMMddHHmmss)");
		}

		if (len == 8) {
			from += "090000";
		}

		len = to.length();
		if (!(len == 8 || len == 14)) {
			throw new IllegalArgumentException("to length must be 8 or 14 (yyyyMMdd or yyyyMMddHHmmss)");
		}

		if (len == 8) {
			to += "090000";
		}

		return betweenDate(getDate(from), getDate(to));
	}

	/**
	 * between date
	 * 
	 * @param java.util.Date
	 *            from
	 * @param java.util.Date
	 *            to
	 * @return int
	 */
	public static long betweenDate(java.util.Date from, java.util.Date to) {
		return new Double(Math.ceil((to.getTime() - from.getTime()) / (MILLIS_PER_HOUR * 24))).longValue();
	}
	
	/**
	 * between Hour
	 * 
	 * @param String
	 *            from (yyyyMMdd or yyyyMMddHHmmss)
	 * @param String
	 *            to (yyyyMMdd or yyyyMMddHHmmss)
	 * @return int
	 */
	public static long betweenHour(String from, String to) {
		if (from == null) {
			throw new IllegalArgumentException("from can not be null");
		}
		if (to == null) {
			throw new IllegalArgumentException("to can not be null");
		}

		int len = from.length();
		if (!(len == 8 || len == 14)) {
			throw new IllegalArgumentException("from length must be 8 or 14 (yyyyMMdd or yyyyMMddHHmmss)");
		}

		if (len == 8) {
			from += "090000";
		}

		len = to.length();
		if (!(len == 8 || len == 14)) {
			throw new IllegalArgumentException("to length must be 8 or 14 (yyyyMMdd or yyyyMMddHHmmss)");
		}

		if (len == 8) {
			to += "090000";
		}

		return betweenHour(getDate(from), getDate(to));
	}
	
	/**
	 * between Hour
	 * 
	 * @param java.util.Date
	 *            from
	 * @param java.util.Date
	 *            to
	 * @return int
	 */
	public static long betweenHour(java.util.Date from, java.util.Date to) {
		return new Double(Math.ceil((to.getTime() - from.getTime()) / MILLIS_PER_HOUR)).longValue();
	}

	/**
	 * conver date format
	 * 
	 * @param java.sql.Timestamp
	 *            dt
	 * @param String
	 *            formatter (yyyyMMdd.....)
	 * @return String
	 */
	public static String converFormat(java.sql.Timestamp dt, String formatter) {
		return convertFormat((java.util.Date) dt, formatter);
	}

	public static String convertFormat(long dt, String formatter) {
		return convertFormat(toUtilDate(dt), formatter);
	}

	public static String convertFormat(String dt) {
		if (dt == null)
			return "";//throw new IllegalArgumentException("dt can not be null");

		String formatter = "";
		int len = dt.length();
		if (!(len == 8 || len == 14))
			return "";//throw new IllegalArgumentException("dt length must be 8 or 14 (yyyyMMdd or yyyyMMddHHmmss)");

		if (dt.length() == 8) {
			dt += "090000";
			formatter = "MM-dd-yyyy";
		} else
			formatter = "MM-dd-yyyy HH:mm:ss";

		SimpleDateFormat sdf = new SimpleDateFormat(formatter);
		return sdf.format(getDate(dt));
	}

	/**
	 * conver date format
	 * 
	 * @param String
	 *            dt (yyyyMMdd or yyyyMMddHHmmss)
	 * @param String
	 *            formatter (yyyyMMdd.....)
	 * @return String
	 */
	public static String convertFormat(String dt, String formatter) {
		if (dt == null)
			return "";//throw new IllegalArgumentException("dt can not be null");

		int len = dt.length();
		if (!(len == 8 || len == 14))
			return "";//throw new IllegalArgumentException("dt length must be 8 or 14 (yyyyMMdd or yyyyMMddHHmmss)");

		if (dt.length() == 8)
			dt += "090000";

		SimpleDateFormat sdf = new SimpleDateFormat(formatter);
		return sdf.format(getDate(dt));
	}

	/**
	 * conver date format
	 * 
	 * @param java.sql.Date
	 *            dt
	 * @param String
	 *            formatter (yyyyMMdd.....)
	 * @return String
	 */
	public static String convertFormat(java.sql.Date dt, String formatter) {
		return convertFormat((java.util.Date) dt, formatter);
	}

	/**
	 * conver date format
	 * 
	 * @param java.util.Date
	 *            dt
	 * @param String
	 *            formatter (yyyyMMdd.....)
	 * @return String
	 */
	public static String convertFormat(java.util.Date dt, String formatter) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatter);
		return sdf.format(dt);
	}

	// /**
	// * get system date (yyyyMMdd)
	// * @return String
	// */
	// public static String getCurrentDay()
	// {
	// return getCurrentTime("yyyyMMdd");
	// }

	/**
	 * get system date
	 * 
	 * @return java.util.Date
	 */
	public static java.util.Date getCurrentDate() {
		return new java.util.Date(System.currentTimeMillis());
	}

	/**
	 * get system time with a default date format
	 * 
	 * @return String
	 */
	public static String getCurrentTime() {
		return getCurrentTime(getDefaultFormat());
	}

	/**
	 * get system time with a formatter
	 * 
	 * @param String
	 *            formatter : yyyyMMdd....
	 * @return String
	 */
	public static String getCurrentTime(String formatter) {
		SimpleDateFormat fmt = new SimpleDateFormat(formatter);
		fmt.setTimeZone(getTimeZone());

		return fmt.format(new java.util.Date(System.currentTimeMillis()));
	}

	// /**
	// * 주어진 시간스트링에 대한 java.util.Date객체를 얻는다.
	// * @param dt HH:mm:ss ex) 15:00:00
	// * @return java.util.Date
	// */
	// public static java.util.Date getCurrentDate(String dt)
	// {
	// if( dt.length() != 8 ) return null;
	//
	// Calendar cal = Calendar.getInstance();
	// cal.set(
	// cal.get(cal.YEAR),
	// cal.get(cal.MONTH),
	// cal.get(cal.DATE),
	// Integer.valueOf( dt.substring(0,2) ).intValue(),
	// Integer.valueOf( dt.substring(3,5) ).intValue(),
	// Integer.valueOf( dt.substring(6,8) ).intValue()
	// );
	//
	// return cal.getTime();
	// }

	/**
	 * yyyyMMddHHmmss --> java.util.Date
	 * 
	 * @param String
	 *            dt
	 * @return java.util.Date
	 */
	private static java.util.Date getDate(String dt) {
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.valueOf(dt.substring(0, 4)).intValue(), Integer.valueOf(dt.substring(4, 6)).intValue() - 1,
				Integer.valueOf(dt.substring(6, 8)).intValue(), Integer.valueOf(dt.substring(8, 10)).intValue(),
				Integer.valueOf(dt.substring(10, 12)).intValue(), Integer.valueOf(dt.substring(12, 14)).intValue());

		return cal.getTime();
	}

	public static String getDayList() {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < 31; i++) {
			sb.append(StringUtil.lpad(i + 1, 2, '0'));
			sb.append(",");
		}

		return sb.toString();
	}

	/**
	 * 해당연도의 띠를 얻는다.
	 * 
	 * @param year
	 * @return 띠
	 */
	public static String getDDI(int year) {
		return ZODIAC_ARRAY[(year - 4) % 12];
	}

	//
	// //단기는 year + 2333
	// //달력 데이타 1881년에서 2043년까지
	// //0 X
	// //1 평달 29
	// //2 평달 30
	// //3 윤달 29
	// //4 윤달 30
	// private final static int[][] LUNAR_ARRAY = {
	// //1881
	// {1, 2, 1, 2, 1, 2, 2, 3, 2, 2, 1, 2, 1, 384},
	// {1, 2, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 0, 355},
	// {1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 1, 2, 0, 354},
	// {2, 1, 1, 2, 1, 3, 2, 1, 2, 2, 1, 2, 2, 384},
	// {2, 1, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 0, 354},
	// {2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 0, 354},
	// {2, 2, 1, 2, 3, 2, 1, 1, 2, 1, 2, 1, 2, 384},
	// {2, 1, 2, 2, 1, 2, 1, 1, 2, 1, 2, 1, 0, 354},
	// {2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 0, 355},
	// {1, 2, 3, 2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 384},
	// //1891
	// {1, 2, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 0, 355},
	// {1, 1, 2, 1, 1, 2, 3, 2, 2, 1, 2, 2, 2, 384},
	// {1, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 0, 354},
	// {1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 0, 354},
	// {2, 1, 2, 1, 2, 3, 1, 2, 1, 2, 1, 2, 1, 383},
	// {2, 2, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 0, 355},
	// {1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 0, 354},
	// {2, 1, 2, 3, 2, 2, 1, 2, 1, 2, 1, 2, 1, 384},
	// {2, 1, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 0, 355},
	// {1, 2, 1, 1, 2, 1, 2, 2, 3, 2, 2, 1, 2, 384},
	// //1901
	// {1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1, 0, 354},
	// {2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 0, 355},
	// {1, 2, 1, 2, 1, 3, 2, 1, 1, 2, 2, 1, 2, 383},
	// {2, 2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 0, 354},
	// {2, 2, 1, 2, 2, 1, 1, 2, 1, 2, 1, 2, 0, 355},
	// {1, 2, 2, 1, 4, 1, 2, 1, 2, 1, 2, 1, 2, 384},
	// {1, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 0, 354},
	// {2, 1, 1, 2, 2, 1, 2, 1, 2, 2, 1, 2, 0, 355},
	// {1, 2, 3, 1, 2, 1, 2, 1, 2, 2, 2, 1, 2, 384},
	// {1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1, 0, 354},
	// //1911
	// {2, 1, 2, 1, 1, 2, 3, 1, 2, 2, 1, 2, 2, 384},
	// {2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 2, 0, 354},
	// {2, 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 0, 354},
	// {2, 2, 1, 2, 2, 3, 1, 2, 1, 2, 1, 1, 2, 384},
	// {2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 0, 355},
	// {1, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 0, 354},
	// {2, 1, 3, 2, 1, 2, 2, 1, 2, 2, 1, 2, 1, 384},
	// {2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1, 2, 0, 355},
	// {1, 2, 1, 1, 2, 1, 2, 3, 2, 2, 1, 2, 2, 384},
	// {1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 2, 2, 0, 354},
	// //1921
	// {2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 0, 354},
	// {2, 1, 2, 2, 1, 3, 2, 1, 1, 2, 1, 2, 2, 384},
	// {1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 1, 2, 0, 354},
	// {2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 1, 0, 354},
	// {2, 1, 2, 2, 3, 2, 1, 2, 2, 1, 2, 1, 2, 385},
	// {1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 0, 354},
	// {2, 1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 0, 355},
	// {1, 2, 3, 1, 2, 1, 1, 2, 2, 1, 2, 2, 2, 384},
	// {1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 0, 354},
	// {1, 2, 2, 1, 1, 2, 3, 1, 2, 1, 2, 2, 1, 383},
	// //1931
	// {2, 2, 2, 1, 1, 2, 1, 1, 2, 1, 2, 1, 0, 354},
	// {2, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 0, 355},
	// {1, 2, 2, 1, 2, 4, 1, 2, 1, 2, 1, 1, 2, 384},
	// {1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 1, 2, 0, 355},
	// {1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 0, 354},
	// {2, 1, 1, 4, 1, 2, 1, 2, 1, 2, 2, 2, 1, 384},
	// {2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 1, 0, 354},
	// {2, 2, 1, 1, 2, 1, 1, 4, 1, 2, 2, 1, 2, 384},
	// {2, 2, 1, 1, 2, 1, 1, 2, 1, 2, 1, 2, 0, 354},
	// {2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 0, 354},
	// //1941
	// {2, 2, 1, 2, 2, 1, 4, 1, 1, 2, 1, 2, 1, 384},
	// {2, 1, 2, 2, 1, 2, 2, 1, 2, 1, 1, 2, 0, 355},
	// {1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 0, 355},
	// {1, 1, 2, 1, 4, 1, 2, 1, 2, 2, 1, 2, 2, 384},
	// {1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 1, 2, 0, 354},
	// {2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 0, 354},
	// {2, 2, 3, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 384},
	// {2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 0, 354},
	// {2, 2, 1, 2, 1, 2, 1, 3, 2, 1, 2, 1, 2, 384},
	// {2, 1, 2, 2, 1, 2, 1, 1, 2, 1, 2, 1, 0, 354},
	// //1951
	// {2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 0, 355},
	// {1, 2, 1, 2, 1, 4, 2, 1, 2, 1, 2, 1, 2, 384},
	// {1, 2, 1, 1, 2, 2, 1, 2, 2, 1, 2, 2, 0, 355},
	// {1, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 0, 354},
	// {2, 1, 1, 4, 1, 1, 2, 1, 2, 1, 2, 2, 2, 384},
	// {1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 0, 354},
	// {2, 1, 2, 1, 2, 1, 1, 2, 3, 2, 1, 2, 2, 384},
	// {1, 2, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 0, 354},
	// {1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 0, 354},
	// {2, 1, 2, 1, 2, 2, 3, 2, 1, 2, 1, 2, 1, 384},
	// //1961
	// {2, 1, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 0, 355},
	// {1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1, 0, 354},
	// {2, 1, 2, 1, 3, 2, 1, 2, 1, 2, 2, 2, 1, 384},
	// {2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 0, 355},
	// {1, 2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 0, 353},
	// {2, 2, 2, 3, 2, 1, 1, 2, 1, 1, 2, 2, 1, 384},
	// {2, 2, 1, 2, 2, 1, 1, 2, 1, 2, 1, 2, 0, 355},
	// {1, 2, 2, 1, 2, 1, 2, 3, 2, 1, 2, 1, 2, 384},
	// {1, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 0, 354},
	// {2, 1, 1, 2, 2, 1, 2, 1, 2, 2, 1, 2, 0, 355},
	// //1971
	// {1, 2, 1, 1, 2, 3, 2, 1, 2, 2, 2, 1, 2, 384},
	// {1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1, 0, 354},
	// {2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 2, 1, 0, 354},
	// {2, 2, 1, 2, 3, 1, 2, 1, 1, 2, 2, 1, 2, 384},
	// {2, 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 0, 354},
	// {2, 2, 1, 2, 1, 2, 1, 2, 3, 2, 1, 1, 2, 384},
	// {2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 1, 0, 354},
	// {2, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 0, 355},
	// {2, 1, 1, 2, 1, 2, 4, 1, 2, 2, 1, 2, 1, 384},
	// {2, 1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 0, 355},
	// //1981
	// {1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 2, 2, 0, 354},
	// {2, 1, 2, 1, 3, 2, 1, 1, 2, 2, 1, 2, 2, 384},
	// {2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 0, 354},
	// {2, 1, 2, 2, 1, 1, 2, 1, 1, 2, 3, 2, 2, 384},
	// {1, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 0, 354},
	// {1, 2, 2, 1, 2, 2, 1, 2, 1, 2, 1, 1, 0, 354},
	// {2, 1, 2, 2, 1, 2, 3, 2, 2, 1, 2, 1, 2, 385},
	// {1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 0, 354},
	// {2, 1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 0, 355},
	// {1, 2, 1, 1, 2, 3, 1, 2, 1, 2, 2, 2, 2, 384},
	// //1991
	// {1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 0, 354},
	// {1, 2, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 0, 354},
	// {1, 2, 2, 3, 2, 1, 2, 1, 1, 2, 1, 2, 1, 383},
	// {2, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 0, 355},
	// {1, 2, 2, 1, 2, 2, 1, 2, 3, 2, 1, 1, 2, 384},
	// {1, 2, 1, 2, 2, 1, 2, 1, 2, 2, 1, 2, 0, 355},
	// {1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 0, 354},
	// {2, 1, 1, 2, 1, 3, 2, 2, 1, 2, 2, 2, 1, 384},
	// {2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 1, 0, 354},
	// {2, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 1, 0, 354},
	// //2001
	// {2, 2, 2, 1, 3, 2, 1, 1, 2, 1, 2, 1, 2, 384},
	// {2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 0, 354},
	// {2, 2, 1, 2, 2, 1, 2, 1, 1, 2, 1, 2, 0, 355},
	// {1, 2, 3, 2, 2, 1, 2, 1, 2, 2, 1, 1, 2, 384},
	// {1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 0, 355},
	// {1, 1, 2, 1, 2, 1, 2, 3, 2, 2, 1, 2, 2, 384},
	// {1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 1, 2, 0, 354},
	// {2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 0, 354},
	// {2, 2, 1, 1, 2, 3, 1, 2, 1, 2, 1, 2, 2, 384},
	// {2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 0, 354},
	// //2011
	// {2, 1, 2, 2, 1, 2, 1, 1, 2, 1, 2, 1, 0, 354},
	// {2, 1, 2, 4, 2, 1, 2, 1, 1, 2, 1, 2, 1, 384},
	// {2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 0, 355},
	// {1, 2, 1, 2, 1, 2, 1, 2, 2, 3, 2, 1, 2, 384},
	// {1, 2, 1, 1, 2, 1, 2, 2, 2, 1, 2, 2, 0, 355},
	// {1, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 0, 354},
	// {2, 1, 1, 2, 1, 3, 2, 1, 2, 1, 2, 2, 2, 384},
	// {1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 0, 354},
	// {2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 0, 354},
	// {2, 1, 2, 2, 3, 2, 1, 1, 2, 1, 2, 1, 2, 384},
	// //2021
	// {1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 0, 354},
	// {2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 0, 355},
	// {1, 2, 3, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 384},
	// {1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1, 0, 354},
	// {2, 1, 2, 1, 1, 2, 3, 2, 1, 2, 2, 2, 1, 384},
	// {2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 0, 355},
	// {1, 2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 2, 0, 354},
	// {1, 2, 2, 1, 2, 3, 1, 2, 1, 1, 2, 2, 1, 383},
	// {2, 2, 1, 2, 2, 1, 1, 2, 1, 1, 2, 2, 0, 355},
	// {1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 0, 354},
	// //2031
	// {2, 1, 2, 3, 2, 1, 2, 2, 1, 2, 1, 2, 1, 384},
	// {2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 0, 355},
	// {1, 2, 1, 1, 2, 1, 2, 3, 2, 2, 2, 1, 2, 384},
	// {1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1, 0, 354},
	// {2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 2, 0, 354},
	// {2, 2, 1, 2, 1, 1, 4, 1, 1, 2, 1, 2, 2, 384},
	// {2, 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 0, 354},
	// {2, 2, 1, 2, 1, 2, 1, 2, 1, 1, 2, 1, 0, 354},
	// {2, 2, 1, 2, 2, 3, 2, 1, 2, 1, 2, 1, 1, 384},
	// {2, 1, 2, 2, 1, 2, 2, 1, 2, 1, 2, 1, 0, 355},
	// //2041
	// {2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 0, 355},
	// {1, 2, 3, 1, 2, 1, 2, 1, 2, 2, 2, 1, 2, 384},
	// {1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 2, 2, 0, 354}
	// };

	/**
	 * get default date format : yyyyMMdd, yyyy-MM-dd HH:mm:ss
	 * 
	 * @return String
	 */
	public static String getDefaultFormat() {
		return DEFAULT_DATE_FORMAT;
		// return
		// BasePropManager.getBaseProperties("Environment").getString("date.formatter",
		// DEFAULT_DATE_FORMAT);
	}

	/**
	 * 해당 일자 월의 마지말 일자를 얻는다.
	 * 
	 * @param String
	 *            dt yyyyMM, yyyyMMdd, yyyyMMddHHmmss
	 * @return int 28, 29, 30, 31
	 */
	public static int getLastDay(String dt) {
		if (dt.length() < 6)
			throw new IllegalArgumentException("date type -- yyyyMM, yyyyMMdd, yyyyMMddHHmmss");
		int year = Integer.parseInt(dt.substring(0, 4));
		int month = Integer.parseInt(dt.substring(4, 6));
		if (isLeapYear(year))
			return (month == 2) ? 29 : SOLAR_MONTH_ARRAY[month - 1];
		else
			return SOLAR_MONTH_ARRAY[month - 1];
	}

	/**
	 * 해당 일자 월의 마지말 일자를 얻는다.
	 * 
	 * @param java.sql.Date
	 *            dt
	 * @return int 28, 29, 30, 31
	 */
	public static int getLastDay(java.sql.Date dt) {
		return getLastDay(convertFormat(dt, "yyyyMM"));
	}

	/**
	 * 해당 일자 월의 마지말 일자를 얻는다.
	 * 
	 * @param java.sql.Timestamp
	 *            dt
	 * @return int 28, 29, 30, 31
	 */
	public static int getLastDay(java.sql.Timestamp dt) {
		return getLastDay(convertFormat(dt, "yyyyMM"));
	}

	/**
	 * 해당 일자 월의 마지말 일자를 얻는다.
	 * 
	 * @param java.util.Date
	 *            dt
	 * @return int 28, 29, 30, 31
	 */
	public static int getLastDay(java.util.Date dt) {
		return getLastDay(convertFormat(dt, "yyyyMM"));
	}

	public static String getMonthList() {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < 12; i++) {
			sb.append(StringUtil.lpad(i + 1, 2, '0'));
			sb.append(",");
		}

		return sb.toString();
	}

	/**
	 * get default raw off set (System Properties)
	 * 
	 * @return String
	 */
	public static int getRawOffSet() {
		return (TimeZone.getDefault()).getRawOffset();
	}

	//
	// /**
	// * 해당 년도 월의 윤달여부
	// * @param year yyyy
	// * @param month
	// * @param solar_flag true 양력, false 음력
	// * @return true 윤달 false 평달
	// * @exception IllegalArgumentException
	// * 음력의 경우 연도범위가 MIN_LUNAR_DATE ~ MAX_LUNAR_DATE 를 벗어난 경우
	// */
	// public static boolean isLeapMonth(int year, int month, boolean
	// solar_flag)
	// {
	// if ( !solar_flag && (year < BASE_LUNAR_YEAR || year > Integer.parseInt(
	// (MAX_LUNAR_DATE+"").substring(0,4)) ) )
	// throw new IllegalArgumentException("range : " + MIN_LUNAR_DATE + " ~ " +
	// MAX_LUNAR_DATE);
	//
	// return (solar_flag)
	// ? (month == 2 && isLeapYear(year, true))
	// : (LUNAR_ARRAY[year-BASE_LUNAR_YEAR][month] == 3 ||
	// LUNAR_ARRAY[year-BASE_LUNAR_YEAR][month] == 4);
	// }
	//
	// /**
	// * 해당 년월의 음력 일수를 얻는다.
	// * @param year yyyy
	// * @param month
	// * @param leap_flag true 윤달, false 평달
	// * @return 월의 일수
	// * @exception IllegalArgumentException
	// * 연도범위가 MIN_LUNAR_DATE ~ MAX_LUNAR_DATE 를 벗어난 경우
	// */
	// public static int getLunarMonthDay(int year, int month, boolean
	// leap_flag)
	// {
	// if ( year < BASE_LUNAR_YEAR || year > Integer.parseInt(
	// (MAX_LUNAR_DATE+"").substring(0,4)) )
	// throw new IllegalArgumentException("range : " + MIN_LUNAR_DATE + " ~ " +
	// MAX_LUNAR_DATE);
	//
	// if (leap_flag && (LUNAR_ARRAY[year-BASE_LUNAR_YEAR][month] == 3 ||
	// LUNAR_ARRAY[year-BASE_LUNAR_YEAR][month] == 4))
	// month = month+1;
	//
	// switch (LUNAR_ARRAY[year-BASE_LUNAR_YEAR][month-1])
	// {
	// case 1:
	// case 3:
	// return 29;
	//
	// case 2:
	// case 4:
	// return 30;
	//
	// default:
	// return 0;
	// }
	// }
	//
	// /**
	// * 해당 년월의 양력 일수를 얻는다.
	// * @param year yyyy
	// * @param month
	// * @return 월의 일수
	// */
	// public static int getSolarMonthDay(int year, int month)
	// {
	// if (month == 2)
	// return (isLeapYear(year, true)) ? 29 : 28;
	// else
	// return SOLAR_MONTH_ARRAY[month-1];
	// }
	//
	// /**
	// * 해당 년도의 일수를 얻는다.
	// * @param year yyyy
	// * @param solar_flag true 양력, false 음력
	// * @return 년도 일수
	// * @exception IllegalArgumentException
	// * 음력의 경우 연도범위가 MIN_LUNAR_DATE ~ MAX_LUNAR_DATE 를 벗어난 경우
	// */
	// public static int getYearDay(int year, boolean solar_flag)
	// {
	// if (solar_flag)
	// return (isLeapYear(year, true)) ? 366 : 365;
	// else
	// {
	// if ( year < BASE_LUNAR_YEAR || year > Integer.parseInt(
	// (MAX_LUNAR_DATE+"").substring(0,4)) )
	// throw new IllegalArgumentException("range : " + MIN_LUNAR_DATE + " ~ " +
	// MAX_LUNAR_DATE);
	// return LUNAR_ARRAY[year - BASE_LUNAR_YEAR][13];
	// }
	// }
	//
	// /**
	// * 해당년도의 육갑을 얻는다.
	// * @param year 연도
	// * @param flag true 한글육갑, false 한문육갑
	// */
	// public static String getSexagenaryCycle(String year, boolean flag)
	// {
	// return getSexagenaryCycle(Integer.parseInt(year), flag);
	// }
	//
	/**
	 * 해당년도의 육갑을 얻는다.
	 * 
	 * @param year
	 *            연도
	 * @param flag
	 *            true 한글육갑, false 한문육갑
	 */
	public static String getSexagenaryCycle(int year, boolean flag) {
		return (flag ? TEN_KR_ARRAY[(year + 6) % 10] : TEN_CH_ARRAY[(year + 6) % 10])
				+ (flag ? ZODIAC_KR_ARRAY[(year - 4) % 12] : ZODIAC_CH_ARRAY[(year - 4) % 12]);
	}

	/**
	 * get default SimpleTimeZone (System Properties)
	 * 
	 * @return SimpleTimeZone
	 */
	public static TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}

	/**
	 * get default time zone ID (System Properties)
	 * 
	 * @return String
	 */
	public static String getTimeZoneID() {
		return (TimeZone.getDefault()).getID();
	}

	public static String getYearList(int max) {
		StringBuffer sb = new StringBuffer();

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);

		for (int i = 0; i < max; i++) {
			sb.append(String.valueOf(year));
			sb.append(",");
			year--;
		}

		return sb.toString();
	}

	// /**
	// * 음력일자를 양력일자로 변환
	// * @param lunar_date yyyyMMdd type
	// * @param leap_month true : 윤달, false : 평달
	// * @return yyyyMMdd
	// * @exception IllegalArgumentException 음력일자 범위가 MIN_LUNAR_DATE ~
	// MAX_LUNAR_DATE 를 벗어나는 경우
	// */
	// public static String lunar2solar(String lunar_date, boolean leap_month)
	// {
	// int year = Integer.parseInt(lunar_date);
	// if (year < MIN_LUNAR_DATE || year > MAX_LUNAR_DATE)
	// throw new IllegalArgumentException("range : " + MIN_LUNAR_DATE + " ~ " +
	// MAX_LUNAR_DATE);
	//
	// year = Integer.parseInt(lunar_date.substring(0, 4));
	// int month = Integer.parseInt(lunar_date.substring(4, 6));
	// int day = Integer.parseInt(lunar_date.substring(6, 8));
	//
	// long lunar_delta = 0;
	//
	// int y = 0;
	// int m = 0;
	//
	// while (y < LUNAR_ARRAY.length && y<year-BASE_LUNAR_YEAR)
	// {
	// lunar_delta = lunar_delta + LUNAR_ARRAY[y][13];
	// y++;
	// }
	//
	// int intFlag = -1;
	// while (m<month)
	// {
	// //System.out.print(m + "-" + LUNAR_ARRAY[y][m]+", ");
	// switch (LUNAR_ARRAY[y][m])
	// {
	// case 1:
	// lunar_delta = lunar_delta + 29;
	// break;
	//
	// case 2:
	// lunar_delta = lunar_delta + 30;
	// break;
	//
	// case 3:
	// lunar_delta = lunar_delta + 29;
	// intFlag = m;
	// break;
	//
	// case 4:
	// lunar_delta = lunar_delta + 30;
	// intFlag = m;
	// break;
	// }
	// m++;
	// }
	//
	// if ( intFlag > -1 || ( intFlag == -1 && leap_month && (LUNAR_ARRAY[y][m]
	// == 3 || LUNAR_ARRAY[y][m] == 4) ) )
	// {
	// lunar_delta = lunar_delta + getMonthDay(y, m);
	// m++;
	// }
	//
	// lunar_delta = lunar_delta - (getMonthDay(y, m-1) - day) - 1;
	// return addDay(MIN_SOLAR_DATE+"090000", lunar_delta).substring(0, 8);
	// }
	//
	// /**
	// * 양력일자를 음력일자로 변환
	// * @param solar_date yyyyMMdd type
	// * @return XyyyyMMdd X : 0 평달, 1 윤달
	// * @exception IllegalArgumentException 양력일자 범위가 MIN_SOLAR_DATE ~
	// MAX_SOLAR_DATE 를 벗어나는 경우
	// */
	// public static String solar2lunar(String solar_date)
	// {
	// int y = Integer.parseInt(solar_date);
	// if (y < MIN_SOLAR_DATE || y > MAX_SOLAR_DATE)
	// throw new IllegalArgumentException("range : " + MIN_SOLAR_DATE + " ~ " +
	// MAX_SOLAR_DATE);
	//
	// long solar_delta = betweenDate(MIN_SOLAR_DATE+"090000",
	// solar_date+"090000");
	// long lunar_delta = 0L;
	//
	// y = 0;
	// int m = 0;
	// int d = 0;
	//
	// while (y<LUNAR_ARRAY.length && LUNAR_ARRAY[y][13]+lunar_delta <
	// solar_delta)
	// {
	// lunar_delta = lunar_delta + LUNAR_ARRAY[y][13];
	// y++;
	// }
	//
	// int intFlag = -1;
	// while (m<LUNAR_ARRAY[y].length-1)
	// {
	// switch (LUNAR_ARRAY[y][m])
	// {
	// case 1:
	// lunar_delta = lunar_delta + 29;
	// break;
	//
	// case 2:
	// lunar_delta = lunar_delta + 30;
	// break;
	//
	// case 3:
	// lunar_delta = lunar_delta + 29;
	// intFlag = m;
	// break;
	//
	// case 4:
	// lunar_delta = lunar_delta + 30;
	// intFlag = m;
	// break;
	// }
	// if (lunar_delta < solar_delta)
	// m++;
	// else
	// break;
	// }
	//
	// d = getMonthDay(y, m) - (new Long(lunar_delta-solar_delta).intValue()) +
	// 1;
	// m = (intFlag == -1) ? m+1 : m;
	// y = y + BASE_SOLAR_YEAR;
	//
	// StringBuffer sb = new StringBuffer();
	// sb.append(intFlag == m ? 1 : 0);
	// sb.append(y);
	// if (m<10)
	// sb.append(0).append(m);
	// else
	// sb.append(m);
	// if (d<10)
	// sb.append(0).append(d);
	// else
	// sb.append(d);
	//
	// return sb.toString();
	// }
	//
	// private static int getMonthDay(int year, int month)
	// {
	// switch (LUNAR_ARRAY[year][month])
	// {
	// case 1:
	// case 3:
	// return 29;
	//
	// case 2:
	// case 4:
	// return 30;
	//
	// default:
	// return 0;
	// }
	// }
	//
	// /**
	// * 해당 년도의 윤년여부
	// * @param year yyyy
	// * @param solar_flag true 양력, false 음력
	// * @return true 윤년, false 평년
	// * @exception IllegalArgumentException
	// * 음력의 경우 연도범위가 MIN_LUNAR_DATE ~ MAX_LUNAR_DATE 를 벗어난 경우
	// */
	// public static boolean isLeapYear(int year, boolean solar_flag)
	// {
	// if ( !solar_flag && (year < BASE_LUNAR_YEAR || year > Integer.parseInt(
	// (MAX_LUNAR_DATE+"").substring(0,4)) ) )
	// throw new IllegalArgumentException("range : " + MIN_LUNAR_DATE + " ~ " +
	// MAX_LUNAR_DATE);
	//
	// return (solar_flag)
	// ? (year%400==0 || year%100!=0 && year%4==0)
	// : !(LUNAR_ARRAY[year - BASE_LUNAR_YEAR][12] == 0);
	// }

	/**
	 * 해당 년도의 윤년여부
	 * 
	 * @param year
	 *            yyyy
	 * @return true 윤년, false 평년
	 */
	public static boolean isLeapYear(int year) {
		return (year % 400 == 0 || year % 100 != 0 && year % 4 == 0);
	}

	/**
	 * convert String date (yyyyMMdd or yyyyMMddHHmmss) to java.sql.Date
	 * 
	 * @param String
	 *            dt (yyyyMMdd or yyyyMMddHHmmss)
	 * @return java.sql.Date
	 */
	public static java.sql.Date toSQLDate(String dt) {
		return new java.sql.Date(toUtilDate(dt).getTime());
	}

	/**
	 * convert String date (yyyyMMdd or yyyyMMddHHmmss) to java.sql.Timestamp
	 * 
	 * @param String
	 *            dt (yyyyMMdd or yyyyMMddHHmmss)
	 * @return java.sql.Timestamp
	 */
	public static java.sql.Timestamp toTimestamp(String dt) {
		if (dt == null) {
			throw new IllegalArgumentException("dt can not be null");
		}

		int len = dt.length();
		if (!(len == 8 || len == 14)) {
			throw new IllegalArgumentException("dt length must be 8 or 14 (yyyyMMdd or yyyyMMddHHmmss)");
		}

		if (dt.length() == 8) {
			dt += "090000";
		}

		return new java.sql.Timestamp(toUtilDate(dt).getTime());
	}

	public static java.util.Date toUtilDate(long dt) {
		return new java.util.Date(dt);
	}

	/**
	 * convert String date (yyyyMMdd or yyyyMMddHHmmss) to java.util.Date
	 * 
	 * @param String
	 *            dt (yyyyMMdd or yyyyMMddHHmmss)
	 * @return java.util.Date
	 */
	public static java.util.Date toUtilDate(String dt) {
		if (dt == null) {
			throw new IllegalArgumentException("dt can not be null");
		}

		int len = dt.length();
		if (!(len == 8 || len == 14)) {
			throw new IllegalArgumentException("dt length must be 8 or 14 (yyyyMMdd or yyyyMMddHHmmss)");
		}

		if (dt.length() == 8) {
			dt += "090000";
		}

		return getDate(dt);
	}

	/** 해당월의 첫 월요일의 day를 return * */
	public static int getFirstMonday(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, 1);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek < 2) {
			cal.add(Calendar.DATE, 2 - dayOfWeek);
		} else if (dayOfWeek > 2) {
			cal.add(Calendar.DATE, 9 - dayOfWeek);
		}
		return cal.get(Calendar.DAY_OF_MONTH);
	}
}
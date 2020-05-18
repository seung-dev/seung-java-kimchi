package seung.java.kimchi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateUtils;

import seung.java.kimchi.util.STimeUnit;

/**
 * <pre>
 * Date 관련 함수 모음
 * </pre>
 * @author seung
 * @since 2020.05.11
 */
public class SDateU {

	/**
	 * @param from
	 * @param to
	 * @param sTimeUnit {@link seung.java.kimchi.util.STimeUnit}
	 * @return
	 */
	public static String dateDiff(Date from, Date to, STimeUnit sTimeUnit) {
		
		long diff = 0l;
		
		switch(sTimeUnit) {
			case MILLISECOND:
				diff = to.getTime() - from.getTime();
				break;
			case SECOND:
				diff = TimeUnit.SECONDS.convert(to.getTime() - from.getTime(), TimeUnit.MILLISECONDS);
				break;
			case MINUTE:
				diff = TimeUnit.MINUTES.convert(to.getTime() - from.getTime(), TimeUnit.MILLISECONDS);
				break;
			case HOUR:
				diff = TimeUnit.HOURS.convert(to.getTime() - from.getTime(), TimeUnit.MILLISECONDS);
				break;
			case DAY:
				diff = TimeUnit.DAYS.convert(to.getTime() - from.getTime(), TimeUnit.MILLISECONDS);
				break;
			default:
				break;
		}
		
		return Long.toString(diff);
	}
	
	/**
	 * @param date
	 * @param sTimeUnit {@link seung.java.kimchi.util.STimeUnit}
	 * @param amount
	 * @return
	 * @throws ParseException
	 */
	public static Date addDate(Date date, STimeUnit sTimeUnit, int amount) throws ParseException {
		
		Date addedDate = null;
		
		switch (sTimeUnit) {
		case MILLISECOND:
			addedDate = DateUtils.addMilliseconds(date, amount);
			break;
		case SECOND:
			addedDate = DateUtils.addSeconds(date, amount);
			break;
		case MINUTE:
			addedDate = DateUtils.addMinutes(date, amount);
			break;
		case HOUR:
			addedDate = DateUtils.addHours(date, amount);
			break;
		case WEEK:
			addedDate = DateUtils.addWeeks(date, amount);
			break;
		case DAY:
			addedDate = DateUtils.addDays(date, amount);
			break;
		case MONTH:
			addedDate = DateUtils.addMonths(date, amount);
			break;
		case YEAR:
			addedDate = DateUtils.addYears(date, amount);
			break;
		default:
			break;
		}
		
		return addedDate;
	}
	
	/**
	 * @return
	 */
	public static int getDateInteger() {
		return Integer.parseInt(getDateString("yyyyMMdd", new Date()).replaceAll("[^0-9]", ""));
	}
	/**
	 * @param pattern
	 * @return
	 */
	public static int getDateInteger(String pattern) {
		return Integer.parseInt(getDateString(pattern, new Date()).replaceAll("[^0-9]", ""));
	}
	/**
	 * @param pattern
	 * @param yyyyMMdd
	 * @return
	 * @throws ParseException
	 */
	public static int getDateInteger(String pattern, String yyyyMMdd) throws ParseException {
		return getDateInteger(pattern, yyyyMMdd, TimeZone.getDefault());
	}
	/**
	 * @param pattern
	 * @param date
	 * @return
	 */
	public static int getDateInteger(String pattern, Date date) {
		return Integer.parseInt(getDateString(pattern, date, TimeZone.getDefault()).replaceAll("[^0-9]", ""));
	}
	/**
	 * @param pattern
	 * @param yyyyMMdd
	 * @param timeZone
	 * @return
	 * @throws ParseException
	 */
	public static int getDateInteger(String pattern, String yyyyMMdd, TimeZone timeZone) throws ParseException {
		return getDateInteger(pattern, stringToDate(yyyyMMdd), timeZone);
	}
	/**
	 * @param pattern
	 * @param date
	 * @param timeZone
	 * @return
	 */
	public static int getDateInteger(String pattern, Date date, TimeZone timeZone) {
		return Integer.parseInt(getDateString(pattern, date, timeZone).replaceAll("[^0-9]", ""));
	}
	
	/**
	 * <pre>
	 * default pattern is "yyyy-MM-dd HH:mm:ss.SSS".
	 * default date is today.
	 * default timeZone is "TimeZone.getDefault()".
	 * </pre>
	 * @return {@link #getDateString(String, Date, TimeZone)}
	 */
	public static String getDateString() {
		return getDateString("yyyy-MM-dd HH:mm:ss.SSS", new Date(), TimeZone.getDefault());
	}
	/**
	 * <pre>
	 * default date is today.
	 * default timeZone is "TimeZone.getDefault()".
	 * </pre>
	 * @param pattern
	 * @return
	 */
	public static String getDateString(String pattern) {
		return getDateString(pattern, new Date());
	}
	/**
	 * <pre>
	 * default timeZone is "TimeZone.getDefault()".
	 * </pre>
	 * @param pattern
	 * @param yyyyMMdd
	 * @return
	 * @throws ParseException
	 */
	public static String getDateString(String pattern, String yyyyMMdd) throws ParseException {
		return getDateString(pattern, yyyyMMdd, TimeZone.getDefault());
	}
	/**
	 * <pre>
	 * default timeZone is "TimeZone.getDefault()".
	 * </pre>
	 * @param pattern
	 * @param date
	 * @return
	 */
	public static String getDateString(String pattern, Date date) {
		return getDateString(pattern, date, TimeZone.getDefault());
	}
	/**
	 * @param pattern
	 * @param date
	 * @param timeZone
	 * @return
	 */
	public static String getDateString(String pattern, Date date, String timeZone) {
		return getDateString(pattern, date, TimeZone.getTimeZone(timeZone));
	}
	/**
	 * @param pattern
	 * @param yyyyMMdd
	 * @param timeZone
	 * @return
	 * @throws ParseException
	 */
	public static String getDateString(String pattern, String yyyyMMdd, TimeZone timeZone) throws ParseException {
		return getDateString(pattern, stringToDate(yyyyMMdd, "yyyyMMdd", timeZone), timeZone);
	}
	/**
	 * @see {@link java.text.SimpleDateFormat}
	 * @see {@link #getAvailableTimeZoneIDs()}
	 * @param pattern year=yyyy, month=MM, day=dd, hour=HH, minute=mm, second=ss, millisecond=SSS
	 * @param date
	 * @param timeZone {@link java.util.TimeZone}
	 * @return
	 */
	public static String getDateString(String pattern, Date date, TimeZone timeZone) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		simpleDateFormat.setTimeZone(timeZone);
		return simpleDateFormat.format(date);
	}
	
	/**
	 * <pre>
	 * default pattern is "yyyyMMdd".
	 * default timeZone is "TimeZone.getDefault()".
	 * </pre>
	 * @param yyyyMMdd
	 * @return {@link #stringToDate(String, String)}
	 * @throws ParseException
	 */
	public static Date stringToDate(String yyyyMMdd) throws ParseException {
		return stringToDate(yyyyMMdd, "yyyyMMdd", TimeZone.getDefault());
	}
	/**
	 * @see {@link java.text.SimpleDateFormat}
	 * @see {@link #getAvailableTimeZoneIDs()}
	 * @param source
	 * @param pattern year=yyyy, month=MM, day=dd, hour=HH, minute=mm, second=ss, millisecond=SSS
	 * @param timeZone {@link java.util.TimeZone}
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String source, String pattern, TimeZone timeZone) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		simpleDateFormat.setTimeZone(timeZone);
		return simpleDateFormat.parse(source);
	}
	
	/**
	 * @see {@link java.util.TimeZone#getAvailableIDs()}
	 * @return
	 */
	public static String[] getAvailableTimeZoneIDs() {
		return TimeZone.getAvailableIDs();
	}
	
	/**
	 * @see {@link java.util.TimeZone#getDefault()}
	 * @see {@link java.util.TimeZone#getID()}
	 * @return
	 */
	public static String getCurrentTimeZone() {
		return TimeZone.getDefault().getID();
	}
	
}

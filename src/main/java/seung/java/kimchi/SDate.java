package seung.java.kimchi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateUtils;

import seung.java.kimchi.util.SKimchiException;
import seung.java.kimchi.util.STimeUnit;

/**
 * <pre>
 * Date 관련 함수 모음
 * </pre>
 * 
 * @author seung
 */
public class SDate {

	private SDate() {}
	
	/**
	 * @param from
	 * @param to
	 * @param sTimeUnit {@link seung.java.kimchi.util.STimeUnit}
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
	 */
	public static Date addDate(Date date, STimeUnit sTimeUnit, int amount) {
		
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
	 */
	public static int getDateInteger() {
		return Integer.parseInt(getDateString("yyyyMMdd", new Date()).replaceAll("[^0-9]", ""));
	}
	/**
	 * @param pattern
	 */
	public static int getDateInteger(String pattern) {
		return Integer.parseInt(getDateString(pattern, new Date()).replaceAll("[^0-9]", ""));
	}
	/**
	 * @param pattern
	 * @param yyyyMMdd
	 * @throws SKimchiException 
	 */
	public static int getDateInteger(String pattern, String yyyyMMdd) throws SKimchiException {
		return getDateInteger(pattern, yyyyMMdd, TimeZone.getDefault());
	}
	/**
	 * @param pattern
	 * @param date
	 */
	public static int getDateInteger(String pattern, Date date) {
		return Integer.parseInt(getDateString(pattern, date, TimeZone.getDefault()).replaceAll("[^0-9]", ""));
	}
	/**
	 * @param pattern
	 * @param yyyyMMdd
	 * @param timeZone
	 * @throws SKimchiException 
	 */
	public static int getDateInteger(String pattern, String yyyyMMdd, TimeZone timeZone) throws SKimchiException {
		return getDateInteger(pattern, toDate(yyyyMMdd), timeZone);
	}
	/**
	 * @param pattern
	 * @param date
	 * @param timeZone
	 */
	public static int getDateInteger(String pattern, Date date, TimeZone timeZone) {
		return Integer.parseInt(getDateString(pattern, date, timeZone).replaceAll("[^0-9]", ""));
	}
	
	/**
	 * <pre>
	 * default pattern is "yyyy-MM-dd'T'HH:mm:ss.SSSXXX".
	 * default date is today.
	 * default timeZone is "TimeZone.getDefault()".
	 * </pre>
	 * 
	 * @return {@link #getDateString(String, Date, TimeZone)}
	 */
	public static String getDateString() {
		return getDateString("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", new Date(), TimeZone.getDefault());
	}
	/**
	 * <pre>
	 * default date is today.
	 * default timeZone is "TimeZone.getDefault()".
	 * </pre>
	 * 
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
	 * 
	 * @param pattern
	 * @param yyyyMMdd
	 * @throws SKimchiException 
	 */
	public static String getDateString(String pattern, String yyyyMMdd) throws SKimchiException {
		return getDateString(pattern, yyyyMMdd, TimeZone.getDefault());
	}
	/**
	 * <pre>
	 * default timeZone is "TimeZone.getDefault()".
	 * </pre>
	 * 
	 * @param pattern
	 * @param date
	 */
	public static String getDateString(String pattern, Date date) {
		return getDateString(pattern, date, TimeZone.getDefault());
	}
	/**
	 * @param pattern
	 * @param date
	 * @param timeZone
	 */
	public static String getDateString(String pattern, Date date, String timeZone) {
		return getDateString(pattern, date, TimeZone.getTimeZone(timeZone));
	}
	/**
	 * @param pattern
	 * @param yyyyMMdd
	 * @param timeZone
	 * @throws SKimchiException 
	 */
	public static String getDateString(String pattern, String yyyyMMdd, TimeZone timeZone) throws SKimchiException {
		return getDateString(pattern, toDate(yyyyMMdd, "yyyyMMdd", timeZone), timeZone);
	}
	/**
	 * @see {@link java.text.SimpleDateFormat}
	 * @see {@link #getAvailableTimeZoneIDs()}
	 * @param pattern year=yyyy, month=MM, day=dd, hour=HH, minute=mm, second=ss, millisecond=SSS
	 * @param date
	 * @param timeZone {@link java.util.TimeZone}
	 */
	public static String getDateString(String pattern, Date date, TimeZone timeZone) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		simpleDateFormat.setTimeZone(timeZone);
		return simpleDateFormat.format(date);
	}
	
	public static String getDateString(String pattern, Locale locale) {
		return getDateString(pattern, new Date(), locale);
	}
	public static String getDateString(String pattern, Date date, Locale locale) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
		return simpleDateFormat.format(date);
	}
	
	/**
	 * <pre>
	 * default pattern is "yyyyMMdd".
	 * default timeZone is "TimeZone.getDefault()".
	 * </pre>
	 * 
	 * @param yyyyMMdd
	 * @return {@link #stringToDate(String, String)}
	 * @throws SKimchiException 
	 */
	public static Date toDate(String yyyyMMdd) throws SKimchiException {
		return toDate(yyyyMMdd, "yyyyMMdd");
	}
	/**
	 * <pre>
	 * default timeZone is "TimeZone.getDefault()".
	 * </pre>
	 * 
	 * @param dateString
	 * @param pattern year=yyyy, month=MM, day=dd, hour=HH, minute=mm, second=ss, millisecond=SSS
	 * @return {@link #stringToDate(String, String)}
	 * @throws SKimchiException 
	 */
	public static Date toDate(String dateString, String pattern) throws SKimchiException {
		return toDate(dateString, pattern, TimeZone.getDefault());
	}
	/**
	 * @see {@link java.text.SimpleDateFormat}
	 * @see {@link #getAvailableTimeZoneIDs()}
	 * @param source
	 * @param pattern year=yyyy, month=MM, day=dd, hour=HH, minute=mm, second=ss, millisecond=SSS
	 * @param timeZone {@link java.util.TimeZone}
	 * @throws SKimchiException 
	 */
	public static Date toDate(String source, String pattern, TimeZone timeZone) throws SKimchiException {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			simpleDateFormat.setTimeZone(timeZone);
			return simpleDateFormat.parse(source);
		} catch (ParseException e) {
			throw new SKimchiException(e);
		}
	}
	
	/**
	 * @see {@link java.util.TimeZone#getAvailableIDs()}
	 */
	public static String[] getAvailableTimeZoneIDs() {
		return TimeZone.getAvailableIDs();
	}
	
	/**
	 * @see {@link java.util.TimeZone#getDefault()}
	 * @see {@link java.util.TimeZone#getID()}
	 */
	public static String getCurrentTimeZone() {
		return TimeZone.getDefault().getID();
	}
	
}

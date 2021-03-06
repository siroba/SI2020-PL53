package PL53.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;

/**
 * @author Marcos
 *
 * Wrapper class for the {@link java.util.Date} and
 * {@link java.sql.Timestamp} classes.<br/>
 * The {@link java.util.Date} has most of its functionality deprecated
 * (that's why I don't use the
 * {@link java.util.Date#Date(int, int, int, int, int, int)} in this
 * class' constructors), but they would be useful for us. <br/>
 * Also, the {@link java.sql.Timestamp} class is mostly obsolete. Its
 * only good use is to read/write from/to SQL databases. That's why the
 * functions {@link #toTimestamp()} and {@link #parseString(String)}
 * (Timestamps are stored as longs in the database) exist.
 */
public class DateTime extends Date {
	// Auto-generated serial ID
	private static final long serialVersionUID = 2169788639882609776L;

	/**
	 * {@link DateFormat} variable to format the dates
	 */
	public static final DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private int hour, minute;

	/**
	 * Default constructor
	 *
	 * @param minute
	 * @param hour
	 * @param day
	 * @param month
	 * @param year
	 */
	public DateTime(int minute, int hour, int day, int month, int year) {
		super(day, month, year);

		this.hour = hour;
		this.minute = minute;
	}

	/**
	 * Copies the date from the given object
	 *
	 * @param minute
	 * @param hour
	 * @param date
	 */
	public DateTime(int minute, int hour, Date d) {
		super(d);

		this.hour = hour;
		this.minute = minute;
	}

	/**
	 * Copy constructor
	 *
	 * @param other
	 */
	public DateTime(DateTime other) {
		super(other.getDay(), other.getMonth(), other.getYear());

		this.hour = other.getHour();
		this.minute = other.getMinute();
	}

	/**
	 * This constructor takes a Date and assumes the time to be 00:00
	 *
	 * @param other
	 */
	public DateTime(Date other) {
		super(other.getDay(), other.getMonth(), other.getYear());

		this.hour = 0;
		this.minute = 0;
	}

	/**
	 * Returns a {@link java.sql.Timestamp} object. The {@link Date#toSQL()} returns
	 * a {@link java.sql.Date} object, which ignores the time
	 *
	 * @return Timestamp
	 */
	public java.sql.Timestamp toTimestamp() {
		return new java.sql.Timestamp(this.toMillis());
	}

	/**
	 * Parses a String containing the date and time to an object The input date must
	 * be in format "yyyy-MM-dd HH:mm"
	 *
	 * @param datetime
	 * @return
	 * @throws ParseException
	 */
	public static DateTime parseString(String datetime) throws ParseException {
		java.util.Date d = dateformat.parse(datetime);

		return DateTime.fromMillis(d.getTime());
	}

	/**
	 * Parses a {@link java.sql.Timestamp} to a {@link DateTime} object by using
	 * {@link java.sql.Timestamp#getTime()}.
	 *
	 * @param datetime
	 * @return DateTime
	 */
	public static DateTime parse(java.sql.Timestamp datetime) {
		return fromMillis(datetime.getTime());
	}

	/**
	 * Uses the {@link #toLocalDateTime()} function combined with the
	 * {@link LocalDateTime#toEpochSecond(ZoneOffset)} (assumes UTC+1) * 1000L
	 */
	@Override
	public long toMillis() {
		return this.toLocalDateTime().toEpochSecond(ZoneOffset.ofHours(2)) * 1000l;
	}

	/**
	 * Parses milliseconds to a {@link DateTime} object. <br/>
	 * Uses the {@link Calendar#setTimeInMillis(long)} to then parse it with the
	 * {@link DateTime#DateTime(int, int, int, int, int)} constructor.
	 *
	 * @param millis
	 * @return
	 */
	public static DateTime fromMillis(long millis) {
		LocalDateTime ldt = LocalDateTime.ofEpochSecond(millis / 1000L, 0, ZoneOffset.ofHours(2));

		return new DateTime(ldt.getMinute(), ldt.getHour(), ldt.getDayOfMonth(), ldt.getMonthValue(), ldt.getYear());
	}

	/**
	 * Parses this object to an LocalDateTime object
	 *
	 * @return LocalDate
	 */
	public LocalDateTime toLocalDateTime() {
		try {
			return LocalDateTime.of(year, month, day, hour, minute);
		}catch(DateTimeException e) {
			if(month == 2) {
				return LocalDateTime.of(year, month, 28, hour, minute);
			}else {
				return LocalDateTime.of(year, month, 30, hour, minute);
			}
		}
	}

	/**
	 * Uses {@link LocalDateTime#now()} to generate the values
	 *
	 * @return
	 */
	public static DateTime now() {
		LocalDateTime d = LocalDateTime.now();

		return new DateTime(d.getMinute(), d.getHour(), d.getDayOfMonth(), d.getMonthValue(), d.getYear());
	}

	/**
	 * Gives the days passed between the two dates Uses the function
	 * {@link #daysSince(Date, Date)} and assumes the other date to be today (uses
	 * the function {@link #now()})
	 *
	 * @param date
	 * @return days passed
	 */
	public static int daysSince(DateTime d) {
		return daysSince(d, DateTime.now());
	}

	/**
	 * Gives the days passed between the two given dates
	 *
	 * @param date1
	 * @param date2
	 * @return days passed
	 */
	public static int daysSince(DateTime date1, DateTime date2) {
		long difference = date1.toMillis() - date2.toMillis();
		int daysBetween = Math.round(difference / (1000.0f * 60.0f * 60.0f * 24.0f));

		return daysBetween;
	}
	
	/**
	 * Same as {@link Date#daysSince(Date)}, but with minutes
	 *
	 * @param d
	 * @return
	 */
	public static int minutesSince(DateTime d) {
		return minutesSince(d, DateTime.now());
	}

	/**
	 * Same as {@link Date#daysSince(Date, Date)}, but with minutes
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int minutesSince(DateTime date1, DateTime date2) {
		long difference = date1.toMillis() - date2.toMillis();
		int hBetween = Math.round(difference / (1000.0f * 60.0f));

		return hBetween;
	}


	/**
	 * The returned string is formatted according to Spanish standards (dd/MM/yy
	 * HH:MM)
	 */
	@Override
	public String toString() {
		String h = (this.hour < 10 ? "0" : "") + this.hour;
		String m = (this.minute < 10 ? "0" : "") + this.minute;
		String d = (this.day < 10 ? "0" : "") + this.day;
		String mm = (this.month < 10 ? "0" : "") + this.month;
 		return d + "/" +mm + "/" + this.year + " " + h + ":" + m;
	}
	
	/**
	 * The returned string is formatted according to sqlite date return value (yyyy-MM-dd hh:mm:00.0)
	 */
	public String toSQLiteString() {
        String h = (this.hour < 10 ? "0" : "") + this.hour;
        String m = (this.minute < 10 ? "0" : "") + this.minute;
        String d = (this.day < 10 ? "0" : "") + this.day;
        String M = (this.month < 10 ? "0" : "") + this.month;
        return this.year + "-" + M + "-" + d + " " + h + ":" + m + ":00.0";
    }

	public void setTime(int minute, int hour) {
		this.minute = minute;
		this.hour = hour;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

}
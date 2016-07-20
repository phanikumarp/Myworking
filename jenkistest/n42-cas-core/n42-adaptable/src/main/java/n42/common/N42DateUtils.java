package n42.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public final class N42DateUtils {

	private static final Date EARLIEST_VIABLE_DATE = new GregorianCalendar(1900, 0, 01).getTime();
	private static final Date LATEST_VIABLE_DATE = new GregorianCalendar(2100, 0, 01).getTime();

	/**
	 * ISO 8601 format that includes date, time with milliseconds and a timezone offset.
	 * 
	 * Note: the name of the constant is intentionally made short and generic as an indication
	 * that this is the only pattern that should be used across the system (excluding classes
	 * that parse date from text like Rules and PipelineProcessors).
	 * 
	 * Example: 2001-07-04T12:08:56.235-0700
	 */
	public static final String ISO_DATE = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	private N42DateUtils() {
		//hidden constructor
	}

	public static String toString(Date date) {
		return new SimpleDateFormat(ISO_DATE).format(date);
	}

	/**
	 * Null-safe before() check. A <code>null</code> value is considered to
	 * occur after a non-<code>null</code> value.
	 * 
	 * @return Returns <code>true</code> when <code>a</code> is strictly before <code>b</code>; i.e. when the inputs are equal, this method
	 *         returns <code>false</code>.
	 */
	public static boolean before(Date a, Date b) {
		if (a == b) {
			return false;
		} else if (a == null) {
			return false;
		} else if (b == null) {
			return true;
		} else {
			return a.before(b);
		}
	}

	/**
	 * Wrapper for <code>copyOf(date, null)</code>.
	 */
	public static Date copyOf(Date date) {
		return copyOf(date, null);
	}

	/**
	 * @return a new {@link Date} instance that represents the same time
	 *         as the input. If input is null, return `<code>nullDefault</code> '.
	 */
	public static Date copyOf(Date date, Date nullDefault) {
		if (date == null) {
			return nullDefault;
		}
		return new Date(date.getTime());
	}

	/**
	 * Tell if a date is viable. For our purposes, we throw away any dates before 1900 and after 2100.
	 * */
	public static boolean isDateViable(Date date) {
		return date != null && date.after(EARLIEST_VIABLE_DATE) && date.before(LATEST_VIABLE_DATE);
	}
}

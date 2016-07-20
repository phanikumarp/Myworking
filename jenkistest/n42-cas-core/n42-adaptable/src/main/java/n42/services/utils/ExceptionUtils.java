package n42.services.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import n42.exceptions.N42RuntimeException;

public final class ExceptionUtils { 

	public static String errorLogEntry(final Throwable base) {
		return errorLogEntry(base, null);
	}

	public static String errorLogEntry(final Throwable base, final String info) {
		final StringBuilder str = new StringBuilder();
		for (Throwable cause = base; cause != null; cause = cause.getCause()) {
			if (!cause.equals(base)) {
				str.append(String.format("%n\tCaused By: "));
			}
			str.append(cause.getClass().getSimpleName());
			if (cause.getMessage() != null) {
				str.append(String.format(": %s", cause.getMessage()));
			}
			if (cause.equals(base) && info != null) {
				str.append(" - ").append(info);
			}
		}

		return str.toString();
	}
	
	public static String fullTraceback(final Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	/**
	 * The return value allows the code to call this method by throwing it.
	 * This allows logic blocks to compile as expected when throwing an
	 * an exception via this method (even though nothing is actually returned).
	 * 
	 */
	public static RuntimeException propagate(final Throwable throwable) {
		if (throwable == null) {
			throw new NullPointerException();
		} else if (Error.class.isInstance(throwable)) {
			throw Error.class.cast(throwable);
		} else if (RuntimeException.class.isInstance(throwable)) {
			throw RuntimeException.class.cast(throwable);
		} else {
			throw new N42RuntimeException(throwable);
		}
	}

	private ExceptionUtils() {
	}
}

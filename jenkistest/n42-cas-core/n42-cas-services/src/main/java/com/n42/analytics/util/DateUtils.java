package com.n42.analytics.util;

import java.util.Date;

public class DateUtils {
	
	public static long getTimeDifferenceInMillis(Date d1, Date d2) {
		long createdTime = d1.getTime();
		long updatedTime = d2.getTime();
		
		long timeDiff = updatedTime - createdTime;
		
		return timeDiff;
	}

}

package com.n42.analytics.kairosclient.exception;

@SuppressWarnings("serial")
public class AnalyticsException extends Exception {

	public AnalyticsException() {
	}
	
	public AnalyticsException(String message) {
		System.out.println("ERROR getting Data from Kairos");
	}

}

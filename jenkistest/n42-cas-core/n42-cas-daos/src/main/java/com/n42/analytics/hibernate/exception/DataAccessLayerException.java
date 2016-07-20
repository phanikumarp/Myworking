package com.n42.analytics.hibernate.exception;

/**
 * Represents Exceptions thrown by the Data Access Layer.
 */
public class DataAccessLayerException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DataAccessLayerException() {
    }

    public DataAccessLayerException(String message) {
        super(message);
    }

    public DataAccessLayerException(Throwable cause) {
        super(cause);
    }

    public DataAccessLayerException(String message, Throwable cause) {
        super(message, cause);
    }
}

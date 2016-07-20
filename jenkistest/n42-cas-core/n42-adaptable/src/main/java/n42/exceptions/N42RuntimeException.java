package n42.exceptions;

/**
 * An exception to use when the system is mis-configured or broken in some
 * unrecoverable way.
 * 
 */
public class N42RuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public N42RuntimeException() {
		super();
	}

	public N42RuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public N42RuntimeException(String message) {
		super(message);
	}

	public N42RuntimeException(Throwable cause) {
		super(cause);
	}

}

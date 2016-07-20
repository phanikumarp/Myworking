package n42.api;

public class PropertyAccessException extends RuntimeException {

	private static final long serialVersionUID = 2490511905202045424L;

	public PropertyAccessException(String message) {
		super(message);
	}

	public PropertyAccessException(String message, Throwable cause) {
		super(message, cause);
	}

}

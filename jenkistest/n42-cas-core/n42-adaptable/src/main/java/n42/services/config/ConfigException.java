package n42.services.config;

public class ConfigException extends Exception {

	private static final long serialVersionUID = -8050966097758297836L;

	public ConfigException(String message) {
		super(message);
	}

	public ConfigException(String message, Exception rootCause) {
		super(message, rootCause);
	}
}

package n42.config.test;

public class SubConfigElementImplementation extends ConfigElementImplementation {

	private String pattern;
	private String replacement;
	private boolean allowFallback = true;

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getReplacement() {
		return replacement;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

	public boolean isAllowFallback() {
		return allowFallback;
	}

	public void setAllowFallback(boolean allowFallback) {
		this.allowFallback = allowFallback;
	}
}

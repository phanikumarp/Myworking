package n42.config.test;

import java.util.List;

/**
 * Used in ConfigManager implementation testing
 */
public class ConfigElementImplementation implements ConfigElement {
	private static final long serialVersionUID = 1778673317685593740L;

	private String path;
	private String extractionExpression;

	private List<ConfigElement> postProcessors;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getExtractionExpression() {
		return extractionExpression;
	}

	public void setExtractionExpression(String extractionExpression) {
		this.extractionExpression = extractionExpression;
	}

	public List<ConfigElement> getPostProcessors() {
		return postProcessors;
	}

	public void setPostProcessors(List<ConfigElement> postProcessors) {
		this.postProcessors = postProcessors;
	}
}

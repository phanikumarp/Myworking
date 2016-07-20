package n42.config.test;

import java.util.List;

import n42.config.ConfigBase;

/**
 * Used in ConfigManager implementation testing
 */
public class RootContainer extends ConfigBase {
	private static final long serialVersionUID = 4296608187326549802L;

	private List<ConfigElement> rules;

	public List<ConfigElement> getRules() {
		return rules;
	}

	public void setRules(List<ConfigElement> rules) {
		this.rules = rules;
	}
}

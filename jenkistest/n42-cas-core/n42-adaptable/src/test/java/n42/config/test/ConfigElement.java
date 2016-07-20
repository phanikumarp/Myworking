package n42.config.test;

import java.io.Serializable;
import java.util.List;

/**
 * Used in ConfigManager implementation testing
 */
public interface ConfigElement extends Serializable {

	List<ConfigElement> getPostProcessors();
}

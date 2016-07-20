package n42.services.config;

import java.io.InputStream;

import n42.config.ConfigBase;

public interface ConfigManager {

	ConfigBase loadConfig(InputStream stream) throws ConfigException;

	ConfigBase loadConfig(String resourcePath) throws ConfigException;

	void saveConfig(String resourcePath, ConfigBase config) throws ConfigException;

}

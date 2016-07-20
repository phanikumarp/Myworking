package n42.services.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import n42.config.ConfigBase;
import n42.services.utils.ExceptionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

public class XStreamConfigManager implements ConfigManager {
	private static final Logger LOG = LoggerFactory.getLogger(XStreamConfigManager.class);

	private static final String ROOT_PACKAGE = "n42.config.";
	private static final String DOT_CLASS = ".class";

	private XStream xstream;

	public XStreamConfigManager() {
		xstream = new XStream();
		xstream.setClassLoader(Thread.currentThread().getContextClassLoader());
	}

	/**
	 * The initialization will find all available classes in classpath, filter
	 * out those that are not in {@code ROOT_PACKAGE} and register them with
	 * XStream.
	 */
	public void init() {
		List<String> classes = loadClassesFromClassLoader(xstream.getClassLoader());
		for (String className: classes) {
			try {
				Class<?> clazz = Class.forName(className);
				xstream.alias(clazz.getSimpleName(), clazz);
			} catch (ClassNotFoundException e) {
				LOG.warn("Class not found: " + className, e);
			}
		}
	}

	@Override
	public ConfigBase loadConfig(InputStream stream) throws ConfigException {
		ConfigBase config;

		try {
			config = (ConfigBase) xstream.fromXML(stream);
		} catch (RuntimeException re) {
			throw new ConfigException("Error loading stream.", re);
		}

		return config;
	}

	@Override
	public ConfigBase loadConfig(String resourcePath) throws ConfigException {
		InputStream xml = null;
		ConfigBase config;

		try {
			try {
				xml = createInputStream(resourcePath);
			} catch (FileNotFoundException e) {
				xml = getClass().getClassLoader().getResourceAsStream(resourcePath);
				if (xml == null) {
					throw new ConfigException("File not found: " + resourcePath, e);
				}
			} catch (RuntimeException e) {
				throw new ConfigException("Error loading file: " + resourcePath, e);
			}

			try {
				config = (ConfigBase) xstream.fromXML(xml);
			} catch (RuntimeException e) {
				throw new ConfigException("Error loading file: " + resourcePath, e);
			}
		} finally {
			try {
				if (xml != null) {
					xml.close();
				}
			} catch (IOException e) {
				LOG.warn("Failed to close resource: " + resourcePath);
			}
		}

		return config;
	}

	protected InputStream createInputStream(String resourcePath) throws FileNotFoundException {
		return new FileInputStream(resourcePath);
	}

	@Override
	public void saveConfig(String resourcePath, ConfigBase config) throws ConfigException {

		OutputStream writer = null;
		try {
			writer = createOutputStream(resourcePath);
			xstream.toXML(config, writer);
		} catch (IOException e) {
			throw new ConfigException("IO exception writing to: " + resourcePath, e);
		} catch (RuntimeException re) {
			throw new ConfigException("Error writing to file: " + resourcePath, re);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				LOG.warn("Failed to close resource: " + resourcePath);
			}
		}
	}

	protected OutputStream createOutputStream(String resourcePath) throws IOException {
		return new FileOutputStream(resourcePath);
	}

	private List<String> loadClassesFromClassLoader(ClassLoader classLoader) {
		List<String> classesFound = new ArrayList<String>();
		Object[] classPaths;
		try {
			// get a list of all classpaths
			classPaths = ((java.net.URLClassLoader) classLoader).getURLs();
		} catch (ClassCastException cce) {
			// or cast failed; tokenize the system classpath
			LOG.warn("Failed to load classloader", cce);
			classPaths = System.getProperty("java.class.path", "").split(File.pathSeparator);
		}

		//This block is to workaround the issue with Maven Surefire plugin. More details here: http://docs.codehaus.org/display/MAVENUSER/Classloading+and+Forking+under+Maven+Surefire
		if (classPaths != null && classPaths.length == 1 && classPaths[0] != null
				&& classPaths[0].toString().matches(".*/surefirebooter.*\\.jar$")) {
			try {
				JarInputStream module = new JarInputStream(((URL) classPaths[0]).openStream());
				try {
					classPaths = module.getManifest().getMainAttributes().get(Attributes.Name.CLASS_PATH).toString()
							.replaceAll("file:", "").split("\\s+");
				} finally {
					module.close();
				}
			} catch (IOException e) {
				throw ExceptionUtils.propagate(e);
			}
		}

		if (classPaths != null) {
			for (int h = 0; h < classPaths.length; h++) {

				// for each classpath ...
				File classPath = new File((URL.class).isInstance(classPaths[h]) ? ((URL) classPaths[h]).getFile()
						.replaceAll("%20", " ") : classPaths[h].toString().replaceAll("%20", " "));

				if (classPath.isDirectory()) {
					LOG.debug("Checking: " + classPath);
					List<String> dirListing = new ArrayList<String>();
					listAllContentsRecursively(dirListing, classPath, new StringBuffer());

					for (String listing: dirListing) {
						if (listing.startsWith(ROOT_PACKAGE) && listing.endsWith(DOT_CLASS)) {
							LOG.debug("Naming shortcut created for: " + listing);
							classesFound.add(listing.substring(0, listing.length() - DOT_CLASS.length()));
						}
					}

				} else if (classPath.getName().endsWith(".jar")) {
					LOG.debug("Loading: " + classPath);
					String rootJar = ROOT_PACKAGE.replace('.', '/');
					try {
						// if our resource is a jar, instantiate a jarfile using the
						// full path to resource
						JarFile module = new JarFile(classPath);
						try {
							Enumeration<JarEntry> e = module.entries();
							for (; e.hasMoreElements();) {
								String ne = e.nextElement().toString();
								if (ne.contains(rootJar) && ne.contains(DOT_CLASS)) {
									String path = ne.substring(0, ne.length() - DOT_CLASS.length());
									String packagePath = path.replace('/', '.');
									LOG.debug("Naming shortcut created for: " + packagePath);
									classesFound.add(packagePath);
								}
							}
						} finally {
							module.close();
						}
					} catch (IOException io) {
						LOG.error("Jar file '" + classPath.getName() + "' could not be instantiate from file path.", io);
					}
				}
			}
		}

		return classesFound;
	}

	private void listAllContentsRecursively(List<String> dirListing, File dir, StringBuffer packageBase) {
		if (dir.isDirectory()) {
			if (packageBase.length() > 0) {
				packageBase.append('.');
			}

			int packageLength = packageBase.length();

			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {

				listAllContentsRecursively(dirListing, files[i], packageBase.append(files[i].getName()));
				// delete for next path
				packageBase.delete(packageLength, packageBase.length());
			}
		} else {
			// this is a file; append it to the relative to path and add it
			// to the directory listing

			dirListing.add(packageBase.toString());
		}
	}

	public void registerClasses(Class<?>... classes) {
		for (Class<?> clazz: classes) {
			xstream.alias(clazz.getSimpleName(), clazz);
		}
	}
}

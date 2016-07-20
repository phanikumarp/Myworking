package n42.services.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import n42.config.test.ConfigElement;
import n42.config.test.ConfigElementImplementation;
import n42.config.test.RootContainer;
import n42.config.test.SubConfigElementImplementation;
import n42.config.ConfigBase;

import org.junit.Assert;
import org.junit.Test;

public class XStreamConfigManagerTest {

	private XStreamConfigManager manager;
	private final String resourcePath = "my resource path";

	@Test
	public void testConfigSave() throws ConfigException {
		RootContainer map = new RootContainer();
		String xpath = "//*[@id='talk_content_6705125']";

		ConfigElementImplementation rule = new ConfigElementImplementation();
		rule.setExtractionExpression(xpath);
		rule.setPath("message");

		List<ConfigElement> postProcessors = new ArrayList<>();
		rule.setPostProcessors(postProcessors);

		SubConfigElementImplementation transform = new SubConfigElementImplementation();
		transform.setPattern(".*");
		transform.setAllowFallback(false);
		postProcessors.add(transform);

		List<ConfigElement> rules = new ArrayList<>();
		rules.add(rule);
		map.setRules(rules);

		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		manager = new XStreamConfigManager() {
			@Override
			protected OutputStream createOutputStream(String path) throws IOException {
				assertEquals(resourcePath, path);
				return out;
			}
		};

		manager.init();
		manager.saveConfig(resourcePath, map);

		assertEquals(
				"<RootContainer>\n" +
				"  <rules>\n" +
				"    <ConfigElementImplementation>\n" +
				"      <path>message</path>\n" +
				"      <extractionExpression>//*[@id=&apos;talk_content_6705125&apos;]</extractionExpression>\n" +
				"      <postProcessors>\n" +
				"        <SubConfigElementImplementation>\n" +
				"          <pattern>.*</pattern>\n" +
				"          <allowFallback>false</allowFallback>\n" +
				"        </SubConfigElementImplementation>\n" +
				"      </postProcessors>\n" +
				"    </ConfigElementImplementation>\n" +
				"  </rules>\n" +
				"</RootContainer>", new String(out.toByteArray()));
	}

	@Test
	public void testConfigLoad() throws ConfigException {
		final String fileContents =
				"<RootContainer>\n" +
				"  <rules>\n" +
				"    <ConfigElementImplementation>\n" +
				"      <path>message</path>\n" +
				"      <extractionExpression>//*[@id=&apos;talk_content_6705125&apos;]</extractionExpression>\n" +
				"      <postProcessors>\n" +
				"        <SubConfigElementImplementation>\n" +
				"          <pattern>foo</pattern>\n" +
				"          <replacement>bar</replacement>\n" +
				"        </SubConfigElementImplementation>\n" +
				"      </postProcessors>\n" +
				"    </ConfigElementImplementation>\n" +
				"  </rules>\n" +
				"</RootContainer>";

		final ByteArrayInputStream in = new ByteArrayInputStream(fileContents.getBytes());
		manager = new XStreamConfigManager() {
			@Override
			protected InputStream createInputStream(String path) throws FileNotFoundException {
				assertEquals(resourcePath, path);
				return in;
			}
		};

		manager.init();
		ConfigBase result = manager.loadConfig(resourcePath);
		assertTrue(result instanceof RootContainer);
		RootContainer map = (RootContainer) result;

		ConfigElement rule = map.getRules().get(0);
		assertTrue(rule instanceof ConfigElementImplementation) ;

		ConfigElementImplementation simpleRule = (ConfigElementImplementation) rule;
		Assert.assertEquals("//*[@id='talk_content_6705125']", simpleRule.getExtractionExpression());
		Assert.assertEquals("message", simpleRule.getPath());
		Assert.assertEquals(1, simpleRule.getPostProcessors().size());

		ConfigElement transform = simpleRule.getPostProcessors().get(0);
		assertTrue(transform instanceof SubConfigElementImplementation);
		Assert.assertEquals("foo", ((SubConfigElementImplementation) transform).getPattern());
	}
}

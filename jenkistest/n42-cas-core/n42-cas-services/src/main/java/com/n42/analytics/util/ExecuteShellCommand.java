package com.n42.analytics.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecuteShellCommand {

	private static final Logger LOG = LoggerFactory.getLogger(ExecuteShellCommand.class);
	public static void main(String[] args) {
		
		File file = new File("D:/Emberjs/N42-trunk-branch/trunk/n42/n42-core/n42-services/src/main/resources/scripts/drools_restart.bat");
		String command = file.getAbsolutePath();
		LOG.debug("Script file path : "+command);

		String output = executeCommand(command);

		LOG.debug("Output for the drools script restart : "+output);

	}

	public static String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = 
					new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.warn("Could not restart Drools : "+e.getMessage());
		}

		return output.toString();

	}

}

package com.rockit.common.blackboxtester.suite.configuration;

import static com.rockit.common.blackboxtester.suite.configuration.ConfigurationHolder.configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.configuration.ConfigurationMap;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.log4j.Logger;

public class PayloadReplacer {

	public static final Logger LOGGER = Logger.getLogger(PayloadReplacer.class.getName());
	
	public static final String TEMP = System.getProperty("java.io.tmpdir") ;
	
	public static File interpolate(File file)  {
		File tmpFile = null;
		try {
			String content = new String(Files.readAllBytes(file.toPath()));
			String replace = StrSubstitutor.replace(content, new ConfigurationMap( configuration()));
			tmpFile = new File(TEMP + File.separator + System.nanoTime() + File.separator + file.getName());
			tmpFile.getParentFile().mkdirs();
			LOGGER.debug("interpolate " + file.toPath() + " to temp " + tmpFile.toPath());
			Files.write(tmpFile.toPath(), replace.getBytes());
			

		} catch (IOException e) {
			LOGGER.error( file  +  " interpolation error ", e);
			
		}
		return tmpFile;
		
	}

	
	

	
}

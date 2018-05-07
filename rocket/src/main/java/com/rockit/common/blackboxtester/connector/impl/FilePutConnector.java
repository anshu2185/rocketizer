package com.rockit.common.blackboxtester.connector.impl;

import static com.rockit.common.blackboxtester.suite.configuration.ConfigurationHolder.configuration;

import java.io.File;

import org.apache.log4j.Logger;

import com.rockit.common.blackboxtester.connector.WriteConnector;
import com.rockit.common.blackboxtester.exceptions.ConnectorException;
import com.rockit.common.blackboxtester.suite.configuration.Constants;
import com.rockit.common.blackboxtester.suite.configuration.Constants.Connectors;
import com.rockit.common.blackboxtester.suite.configuration.PayloadReplacer;
import com.rockit.common.blackboxtester.util.FileUtils;

/**
 * @author rockit2lp
 *
 */
public class FilePutConnector implements WriteConnector {
	public static final Logger LOGGER = Logger.getLogger(FilePutConnector.class.getName());

	private String name;


	private String destPath;

	private File contentFile;




	/**
	 * @param name  - FILEPut and key (connectorFolder)
	 */
	public FilePutConnector(String name){
		this.name = name;
		this.destPath = configuration().getString(this.name + Constants.FILE_PATH_KEY);
	}
	

	

	/**
	 * @param destPath - junit enabled
	 */
	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}




	@Override
	public void proceed() {
		try {

			FileUtils.copy( contentFile, new File(destPath) );
		} catch (Exception e) {
			LOGGER.error("[Connector:"+getName()+"] \t Connector error: " + getType(), e);
		} finally {
		}

	}

	public String getType() {
		return  Connectors.FILEPUT.toString();
	}

	@Override
	public String getId() {
		return getName();
	}

	public String getName() {
		return name;
	}

	@Override
	public void setRequest(File requestFile)  {
		contentFile = PayloadReplacer.interpolate(requestFile);
	}

	@Override
	public void setRequest(String request)  {
		LOGGER.error("set method is not allowed");
		throw new ConnectorException(new RuntimeException("[Connector:"+getName()+"] \t Set method is not allowed"));

	}

}

package com.rockit.common.blackboxtester.connector.impl;

import static com.rockit.common.blackboxtester.suite.configuration.ConfigurationHolder.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;

import com.rockit.common.blackboxtester.connector.ReadConnector;
import com.rockit.common.blackboxtester.exceptions.ConnectorException;
import com.rockit.common.blackboxtester.suite.configuration.Constants;

public class HTTPGetConnector implements ReadConnector {
	public static final Logger LOGGER = Logger.getLogger(HTTPGetConnector.class.getName());

	private StringBuilder resultBuilder;
	private String name, urlStr;
	private URL url;

	public HTTPGetConnector(String name) {
		this.name = name;
		this.urlStr =  configuration().getString(this.name);
	}


	
	@Override
	public void proceed() {


		try {

			
			URL url = new URL(urlStr);
			URLConnection urlConnection = url.openConnection();
			
			if(  url.getUserInfo() != null && url.getUserInfo().split(":").length==2 ) {
				LOGGER.debug("Basic authentication usr/pwd >   " + url.getUserInfo());
				String name = url.getUserInfo().split(":")[0];
				String password = url.getUserInfo().split(":")[1];
		        byte[] authEncBytes = Base64.getEncoder().encode( (name + ":" + password).getBytes() );
		        urlConnection.setRequestProperty("Authorization", "Basic " + new String(authEncBytes));
			} 
	        
	        InputStream is = urlConnection.getInputStream();
			
			String xmlResp = XML.toString(new JSONObject(IOUtils.toString(is)));
	        
			StringBuilder response = new StringBuilder().append("<root>").append(xmlResp).append("</root>");

			setReponse(response.toString());

		} catch (IOException e) {

			LOGGER.error("can not open connection: " + url, e);
			throw new ConnectorException(e);

		}
	}

	public String getName() {
		return name;
	}

	@Override
	public String getId() {
		return getType() + Constants.FLOW_NAME_SEPARATOR + getName();
	}

	@Override
	public String getType() {
		return Constants.Connectors.HTTPGET.toString();
	}

	@Override
	public String getResponse() {
		return resultBuilder.toString();
	}

	@Override
	public void setReponse(String response) {
		this.resultBuilder = new StringBuilder(response);
	}

}

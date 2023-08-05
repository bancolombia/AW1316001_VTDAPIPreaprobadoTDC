/**
 * 
 */
package com.bancolombia.vtd.api.tarjetas.parameter.dto;

import java.util.Map;

public class PropertiesFile {
	
	private String filePath;
	private Map<String, String> properties;

	public PropertiesFile(String filePath, Map<String, String> properties) {
		super();
		this.filePath = filePath;
		this.properties = properties;
	}

	public String getFilePath() {
		return filePath;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}

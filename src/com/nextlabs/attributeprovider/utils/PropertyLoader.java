package com.nextlabs.attributeprovider.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class PropertyLoader {
	private static final Log LOG = LogFactory.getLog(PropertyLoader.class);

	public static Properties loadProperties(String propertiesPath) {
		// Get Home Path
		String dpcInstallHome = getDpcInstallHomeDirectory();
		
		// Check Path Validity
		if(!isPathValid(propertiesPath)){
			return new Properties();
		}
		
		// Get Full Path
		String fullPropertiesPath = getPropertiesPath(dpcInstallHome, propertiesPath);
		
		// Read Properties From File
		Properties properties = readPropertiesFromPath(fullPropertiesPath);
		
		return properties;
	}
	
	private static String getDpcInstallHomeDirectory(){
		String dpcInstallHome = System.getProperty("dpc.install.home");
		if (dpcInstallHome == null || dpcInstallHome.trim().length() < 1) {
			dpcInstallHome = ".";
		}
		LOG.debug("DPC Install Home: " + dpcInstallHome);
		return dpcInstallHome;
	}
	
	private static boolean isPathValid(String filePath){
		if (filePath == null){
			LOG.warn("Invalid file path: Null File Path");
			return false;
		}
		
		if(filePath.length() == 0){
			LOG.warn("Invalid file path: Empty File Path");
			return false;
		}
		
		return true;
	}
	
	private static String getPropertiesPath(String dpcInstallHome, String filePath){
		String fullPath = dpcInstallHome + filePath;
		return fullPath;
	}
	
	private static Properties readPropertiesFromPath(String filePath){
		try {
			File propertiesFile = new File(filePath);
			LOG.warn("Properties File Path: " + propertiesFile.getAbsolutePath());
			
			if(!propertiesFile.exists()){
				LOG.warn("Invalid file: Not exist");
				return new Properties();
			}
			
			if(!propertiesFile.isFile()){
				LOG.warn("Invalid file: Not file");
				return new Properties();
			}
			
			// Load
			FileInputStream inputStream = new FileInputStream(propertiesFile);
			Properties properties = new Properties();
			properties.load(inputStream);
			
			return properties;
		} catch (Exception e) {
			LOG.error("Error parsing properties file ", e);
			return new Properties();
		}
	}
}
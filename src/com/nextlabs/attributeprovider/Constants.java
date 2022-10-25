package com.nextlabs.attributeprovider;

public class Constants {
	// Properties File Path
	public static final String PROPERTIES_FILE_PATH = "/jservice/config/CacheSAPUserAttributeProvider.properties";

	// Plugin Properties
	public static final String PROPERTIES_SERVER_PREFIX = "sap_server_prefix";
	public static final String PROPERTIES_HANDLER = "sap_handler";
	public static final String PROPERTIES_AUTH_VALUE = "sap_auth_value";
	public static final String PROPERTIES_USE_CACHE = "use_cache";
	public static final String PROPERTIES_TIME_TO_LIVE_CACHE_DURATION = "time_to_live_cache_duration";
	public static final String PROPERTIES_TIME_TO_IDLE_CACHE_DURATION = "time_to_idle_cache_duration";
	
	// Default Properties
	public static final String DEFAULT_SERVER_PREFIX = "SERV4_";
	public static final String DEFAULT_HANDLER = "ZNXL_GET_USER_BERID";
	public static final String DEFAULT_AUTH_VALUE = "DF_BERID";
	public static final String DEFAULT_USE_CACHE= "NO";
	public static final String DEFAULT_TIME_TO_LIVE_CACHE_DURATION = "3600";
	public static final String DEFAULT_TIME_TO_IDLE_CACHE_DURATION = "300";
	public static final int DEFAULT_INTEGER_TIME_TO_LIVE_CACHE_DURATION = 3600;
	public static final int DEFAULT_INTEGER_TIME_TO_IDLE_CACHE_DURATION = 300;

	// SAP RFC Imports
	public static final String IMPORT_SWAF_USERNAME = "USERNAME";
	public static final String IMPORT_SWAF_AUTH_OBJECT = "AUTH_OBJECT";

	// SAP RFC Export
	public static final String EXPORT_SWAF_DATA = "MDLG_OUT";

	// General
	public static final String YES = "YES";
	public static final String NO = "NO";

}

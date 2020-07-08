package pageUtilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class _propMgr {
	private static _propMgr instance;
	private static final Object lock = new Object();
	private static String propertyFilePath = "config.properties";
	private static String site;
	private static String newUser;
	private static String marketType;
	
	public static String baseUrl;
	private static String username;
	private static String password;

	private static String stgUsUser;
	private static String stgUkUser;
	private static String stgAuUser;
	private static String stgNzUser;
	private static String stgPwd;
	
	private static String usUser; 
	private static String usPwd; 
	
	private static String ukUser;
	private static String ukPwd;  
	
	private static String auUser;
	private static String auPwd;  
	
	private static String nzUser;
	private static String nzPwd;  

	// Create a Singleton instance. We need only one instance of Property Manager.
	public static _propMgr getInstance() {
		if (instance == null) {
			synchronized (lock) {
				instance = new _propMgr();
				instance.loadData();
			}
		}
		return instance;
	}

	// Get all configuration data and assign to related fields.
	private void loadData() {
		Properties prop = new Properties();

		try {
			prop.load(new FileInputStream(propertyFilePath));
		} catch (IOException e) {
			System.out.println("Configuration properties file cannot be found");
		}

		site = prop.getProperty("environment");
		newUser = prop.getProperty("newAccount");
		marketType = prop.getProperty("marketType");

		stgUsUser = prop.getProperty("stgUsUser");
		stgUkUser = prop.getProperty("stgUkUser");
		stgAuUser = prop.getProperty("stgAuUser");
		stgNzUser = prop.getProperty("stgNzUser");
		stgPwd = prop.getProperty("stgPwd");
		
		usUser = prop.getProperty("usUser");
		usPwd = prop.getProperty("usPwd");
		
		ukUser = prop.getProperty("ukUser");
		ukPwd = prop.getProperty("ukPwd");
		
		auUser = prop.getProperty("auUser");
		auPwd = prop.getProperty("auPwd");
		
		nzUser = prop.getProperty("nzUser");
		nzPwd = prop.getProperty("nzPwd");
	}
	
	public static String getMarketType() {
		return marketType; 
	}
	
	public static String getNewUser() {
		return newUser; 
	}

	public static String getUrl() {
			if(site.contains("staging")) { 
				baseUrl = "https://staging.stortrack.com";
			} else {
				baseUrl = "https://www.stortrack.com";
			}
			return baseUrl;
	}
	
	
	public static String getSignupUrl() {
		String url = "";
		if(site.contains("staging")) {
			switch(_testData.regId) {
			case 1:
				url = "http://stgmvp.stortrack.com/";
				break;
			case 2:
				url = "";
				break;
			case 3:
				url = "";
				break;
			case 4:
				url = "";
				break;
			}
		} else {
			switch(_testData.regId) {
			case 1:
				url = "http://mvp.stortrack.com/";
				break;
			case 2:
				url = "";
				break;
			case 3:
				url = "";
				break;
			case 4:
				url = "";
				break;
			}
		}
		return url;
	}

	
	public static String getUsername() {
			if(site.contains("staging")) {
				switch (_testData.regId) {
				case 2:
					username = stgAuUser;
					break;
				case 3:
					username = stgUkUser;
					break;
				case 4:
					username = stgNzUser;
					break;
				case 1:
				default:
					username = stgUsUser;
					break;
			}
			} else {
				switch (_testData.regId) {
				case 2:
					username = auUser;
					break;
				case 3:
					username = ukUser;
					break;
				case 4:
					username = nzUser;
					break;
				case 1:
				default:
					username = usUser;
					break;
			}
				}
			return username;
		}
	
	public static void setUsername(String uname) {
		username = uname;
	}
	
	public static String getPassword() {
			if(site.contains("staging")) {
				password = stgPwd;
			} else {
				switch (_testData.regId) {
				case 2:
					password = auPwd;
					break;
				case 3:
					password = ukPwd;
					break;
				case 4:
					password = nzPwd;
					break;
				default:
					password = usPwd;
					break;
			}
				}
			return password;
	}
	
}

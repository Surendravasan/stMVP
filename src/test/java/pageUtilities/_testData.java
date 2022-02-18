package pageUtilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class _testData {
	
	public static int regId = 3;
	public static int countryId = 8;
	public static int storeId;
	public static String storeName;
	public static String address;
	public static String city;
	public static String state;
	public static String zipcode;
	public static int userStoreId = 14498;
	public static String baseUrl;
	public static int radius;
	public static int marketTypeId = 3;
	public static Queries queryIns; 
	
	static HashMap<String, String> map;
	
	public static void setRegion(String region) {
		
		if(region.equalsIgnoreCase("US"))
			regId = 1; 
		else if (region.equalsIgnoreCase("AU"))
			regId = 2;
		else if (region.equalsIgnoreCase("UK"))
			regId = 3;
		else if (region.equalsIgnoreCase("NZ"))
			regId = 4;
		else 
			regId = 1;
	}
	
	public static void setStoreId(int sId) {
		storeId = sId;
	}
	
	public static void setStoreName(String sName) {
		storeName = sName;
	}
	
	public static void setUserStoreId(int usId) {
		userStoreId = usId;
	}
	
	public static void setRadius(int rad) {
		radius = rad;
	}
	
	
	public static void setMarketAddress(HashMap<String, String> hm) {
		HashMap<String, String> testStore = hm;
		storeId = Integer.valueOf(testStore.get("storeid"));
		storeName = testStore.get("storename");
		address = testStore.get("address");
		city = testStore.get("city");
		state = testStore.get("state");
		zipcode = testStore.get("zipcode");
		radius = Integer.valueOf(testStore.get("radius"));
		regId = Integer.valueOf(testStore.get("regionid"));
		countryId = Integer.valueOf(testStore.get("countryid"));
	}
	
	public static void setMarketCityState(HashMap<String, String> hm) {
		HashMap<String, String> testStore = hm;
		city = testStore.get("city");
		state = testStore.get("state");
		regId = Integer.valueOf(testStore.get("regionid"));
		countryId = Integer.valueOf(testStore.get("countryid"));
	}
	
	public static void setMarketType() {
		List<String> givenList = Arrays.asList("geo", "list");
		String marketType = _propMgr.getMarketType();
		if(marketType.isEmpty()==true ) {
			marketType = givenList.get(_utils.getRandNumber(givenList.size())-1);
		} else if(givenList.contains(marketType.toLowerCase())==false) {
			marketType = givenList.get(_utils.getRandNumber(givenList.size())-1);
		}
		if(marketType.equalsIgnoreCase("geo")) {
			marketTypeId = 1;
		} else if(marketType.equalsIgnoreCase("list")) {
			marketTypeId = 3;
		}
	}
	
	public static void setQueryInstance() {
		if(regId==1) {
			queryIns = new _usQueries();
		} else if(regId==3) {
			queryIns = new _ukQueries();
		}
	}
}

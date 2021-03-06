package pageMethods;  

import java.util.List;
import org.openqa.selenium.WebElement;
import objRepository._marketDetailsPage;
import pageUtilities._databaseUtils;
import pageUtilities._testData;
import pageUtilities._utils;

public class _marketDetails extends _marketDetailsPage {
	
	public static String storeName;
	private String state;
	
	public _marketDetails() {
		super();
	}
	
//	HashMap<String, String> map = null;
	
	public void fillAddress(String source) {
		
		/*	Step 1: Add Address  */
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.waitForElementClickable($marketName);
		
			storeName = _testData.storeName+" ("+_utils.getDateTime()+")";
			_utils.fillData($marketName, storeName);
			_utils.fillData($address, _testData.address);
			_utils.fillData($city, _testData.city);
			
			if((_testData.regId)!=3) {
				state = _testData.state;
				List<WebElement> stateList = $state;

				for (WebElement stateLists : stateList) {
					String listValue = stateLists.getAttribute("value");
				    if (listValue.toLowerCase().startsWith(state.toLowerCase())) {
				    	stateLists.click();
				        break;
				    }
				}
			}
		
			_utils.fillData($zipCode, _testData.zipcode);
			
		
			_utils.waitForElementInVisibleByLocator(loader);
			
//		_testData.setStoreName(storeName);
	}
	
	public void saveAddress() {
		_utils.waitClick($saveAddress);
		_utils.waitForElementInVisibleByLocator(loader);
	}
	
	
	public void selectState() {

		List<WebElement> stateList = $stateList;
		for (WebElement stateLists : stateList) {
			String listValue = stateLists.getAttribute("value");
			if(_testData.regId==1) {
				if (listValue.toLowerCase().startsWith(_testData.state.toLowerCase())) {
			    	_utils.click(stateLists);
			        break;
			    }
			} else if(_testData.regId==3) {
				String countryName = _databaseUtils.getStringValue(_testData.queryIns.getCountryFullName()).toLowerCase();
				if (listValue.toLowerCase().startsWith(countryName)) {
			    	_utils.click(stateLists);
			        break;
			    }
			}
		}
	}
		
	public void selectCity() {
		System.out.println(_testData.city);
		List<WebElement> cityList = $cityList;
		for (WebElement cityLists : cityList) {
			String listValue = cityLists.getAttribute("value");
		    if (listValue.toLowerCase().startsWith(_testData.city.toLowerCase())) {
		    	_utils.click(cityLists);
		        break;
		    }
		}
	}

	
}

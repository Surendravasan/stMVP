package pageMethods	;  

import objRepository._confirmMarketPage;
import pageUtilities._testData;
import pageUtilities._utils;

public class _confirmMarket extends _confirmMarketPage {
	
	
	
	public _confirmMarket() {
		super();
	}
	
	public void confirmMarket() {
		
		/*	Step 4: Confirm Market  */  
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.waitClick($goToMyMarkets);
		
		if(_testData.marketTypeId==1) {
			_testData.setStoreName(_marketDetails.storeName);
		} else if(_testData.marketTypeId==3) {
			_testData.setStoreName(_testData.city+", "+_testData.state);
		}
	}
	
}

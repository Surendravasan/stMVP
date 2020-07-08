package pageMethods;  

import objRepository._defineMarketPage;
import pageUtilities._base;
import pageUtilities._testData;
import pageUtilities._utils;

public class _defineMarket extends _defineMarketPage {
	
	public _defineMarket() {
		super();
	}
	
	public void radius(int radius) {
		_utils.waitForElementInVisibleByLocator(loader);
		
		
	}
	
	public void radius() {
		_utils.waitForElementInVisibleByLocator(loader);
//		int i = _utils.getRandNumber($sliderPoints.size());
//		if (i!=10) {
//			_utils.submit($selectMiles(_base.driver, i));
//			_utils.waitForElementInVisibleByLocator(loader);
//		}
//		_testData.setRadius(i);
	}
	
	public void saveMarket() {
		_utils.waitClick($saveNxtBtn);
	}
	
}

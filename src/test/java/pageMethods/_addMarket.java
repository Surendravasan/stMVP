package pageMethods;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import pageUtilities._base;
import pageUtilities._excelUtils;
import pageUtilities._propMgr;
import pageUtilities._testData;

public class _addMarket {
	ExtentTest test = _base.test;
		
	public void addNewMarket() {
			_myMarket mm = new _myMarket();
			mm.addMarket();
			
			if(_testData.marketTypeId==1) {
				geoLocation();
			} else if(_testData.marketTypeId==3) {
				cityList();
			}
			logReport();
			_excelUtils.setStoreProcessed();
		}
	
	protected void logReport() {
		test.log(Status.INFO, _propMgr.getUsername());
		test.log(Status.INFO, _testData.storeName);
		if(_testData.marketTypeId==1) {
			test.log(Status.INFO, _testData.address+", "+_testData.city+", "+_testData.state+" "+_testData.zipcode);
			test.log(Status.INFO, "Coverage: "+_testData.radius+" mile radius");
		} else if(_testData.marketTypeId==3) {
			test.log(Status.INFO, _testData.city+", "+_testData.state);
		}
	}
		
		
		
	public void geoLocation() {
			
			_marketType ms = new _marketType();
			ms.selectGeoLocation();
			ms.saveMarketType();
			
			_marketDetails md = new _marketDetails();
			md.fillAddress("Excel");
			md.saveAddress();
			
			_defineMarket ct = new _defineMarket();
			ct.radius();
			ct.saveMarket();
			
			_confirmMarket cm = new _confirmMarket();
			cm.confirmMarket();
		}
		
		public void cityList() {
			
			_marketType ms = new _marketType();
			ms.selectCityList();
			ms.saveMarketType();
			
			_marketDetails md = new _marketDetails();
			md.selectState();
			md.saveAddress();
			md.selectCity();
			md.saveAddress();
			
			_defineMarket ct = new _defineMarket();
			ct.saveMarket();
			
			_confirmMarket cm = new _confirmMarket();
			cm.confirmMarket();
			
		}
	
	

}

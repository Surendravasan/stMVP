package pageMethods;

import org.openqa.selenium.By;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import pageUtilities._base;
import pageUtilities._excelUtils;
import pageUtilities._propMgr;
import pageUtilities._testData;
import pageUtilities._utils;

public class _addMarket {
	ExtentTest test = _base.test;
		
	public void addNewMarket() {
		
		_testData.setMarketType();
		
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
		int addNotFound;
		_marketType ms = new _marketType();
		ms.selectGeoLocation();
		ms.saveMarketType();
			
		_marketDetails md = new _marketDetails();
		
		do {
//			_excelUtils.getMarket();
			md.fillAddress("Excel");
			md.saveAddress();
			
			addNotFound = _base.driver.findElements(By.xpath("//div[@id='form-dialog-title']//b[@class='h-pop-head'][contains(text(),'Address Not Found')]")).size(); 
			if(addNotFound==1) {
				_utils.click(_base.driver.findElement(By.xpath("//div[@id='form-dialog-title']//b[@class='h-pop-head'][contains(text(),'Address Not Found')]/../button")));
			}
		} while(addNotFound==1);
			
//			md.fillAddress("Excel");
//			md.saveAddress();
			
			_defineMarket ct = new _defineMarket();
			ct.radius(_testData.radius);
			ct.saveMarket();
			
			_confirmMarket cm = new _confirmMarket();
			cm.confirmMarket();
		}
		
		public void cityList() {
			
			_marketType ms = new _marketType();
			
//			_excelUtils.getMarket();
			ms.selectCityList();
			ms.saveMarketType();
			
			_marketDetails md = new _marketDetails();
			System.out.println();
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

package pageMethods;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import objRepository._allStoresPage;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._queries;
import pageUtilities._utils;

public class _allStores extends _allStoresPage {
	
	ExtentTest test = _base.test;
	ExtentTest node;
	HashMap<String, String> gridHeaderValue;
	
	public _allStores() {
		super();
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.closeOpenWidgets();
		_utils.waitClick($allStoresLink);
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.waitClick($allStoresShowWidgetBtn);
		_utils.waitForElementInVisibleByLocator(loader);
	}
	
	
	
	
	public void overviewHeader() {
		
		node = test.createNode("Overview Header");
		
		for(int i=1; i<=$overHeaderList.size(); i++) {
			String uiLabel = "";
			String uiValue = "";
			String dbValue = "";
			switch(i) {
			case 1:
				uiLabel = $overHeaderLabel(_base.driver, i).getText();
				uiValue = $overHeaderValue(_base.driver, i).getText();
				dbValue = _databaseUtils.getStringValue(_queries.overNoOfStores());
				compareUiDb(uiLabel, uiValue, dbValue, node);
				break;
			case 2:
				uiLabel = $overHeaderLabel(_base.driver, i).getText();
				uiValue = $overHeaderValue(_base.driver, i).getText();
				dbValue = _databaseUtils.getStringValue(_queries.overNoOfReits());
				compareUiDb(uiLabel, uiValue, dbValue, node);
				break;
			case 3:
				uiLabel = $overHeaderLabel(_base.driver, i).getText();
				uiValue = $overHeaderValue(_base.driver, i).getText();
				dbValue = _databaseUtils.getStringValue(_queries.overNoOfMOps());
				compareUiDb(uiLabel, uiValue, dbValue, node);
				break;
			case 4:
				uiLabel = $overHeaderLabel(_base.driver, i).getText();
				uiValue = $overHeaderValue(_base.driver, i).getText();
				dbValue = _databaseUtils.getStringValue(_queries.overNoOfSOps());
				compareUiDb(uiLabel, uiValue, dbValue, node);
				break;
			case 5:
				uiLabel = $overHeaderLabel(_base.driver, i).getText();
				uiValue = $overHeaderValue(_base.driver, i).getText();
				dbValue = _databaseUtils.getStringValue(_queries.overTotalRentSqFo());
				compareUiDb(uiLabel, uiValue, dbValue, node);
				break;
			}
		}
		
	}
	
	/* Verify All Stores grid values */
	
	public void gridValues() {
		
		node = test.createNode("All Stores Grid Values");
		
		long duration;
		long diffInDays;
 		for(int i=2; i<=$storeList.size()+1; i++) {
			String address = $address(_base.driver, i).getText();
			gridHeaderValue = _databaseUtils.getColumnValues(_queries.gridHeaderValue(address));
			
			node.log(Status.INFO, MarkupHelper.createLabel(gridHeaderValue.get("StoreName"), ExtentColor.BLUE));
			String uiStorename = $storeName(_base.driver, i).getText();
			String dbStoreName = gridHeaderValue.get("StoreName");
			compareUiDb("StoreName", uiStorename, dbStoreName, node);
			
			String uiTotalSqFt = $totalSqFtValue(_base.driver, i).getText();
			String dbTotalSqFt = gridHeaderValue.get("squarefootage");
			compareUiDb("TotalSqFt", uiTotalSqFt, dbTotalSqFt, node);
			
			String uiRentSqFt = $rentSqFtValue(_base.driver, i).getText();
			String dbRentSqFt = gridHeaderValue.get("RentableSqFt");
			compareUiDb("RentableSqFt", uiRentSqFt, dbRentSqFt, node);
			
			String uiOwnedBy = $ownedByValue(_base.driver, i).getText();
			String dbOwnedBy = gridHeaderValue.get("ownedby");
			compareUiDb("OwnedBy", uiOwnedBy, dbOwnedBy, node);
			
			String uiOperatedBy = $operatedByValue(_base.driver, i).getText();
			String dbOperatedBy = gridHeaderValue.get("operatedby");
			compareUiDb("OperatedBy", uiOperatedBy, dbOperatedBy, node);
			
			String uiOpenedBy = $openedValue(_base.driver, i).getText();
			String dbOpenedBy = gridHeaderValue.get("yearbuild");
			compareUiDb("OpenedBy", uiOpenedBy, dbOpenedBy, node);
			
//			String uiRateVol = $rateVolValue(_base.driver, i).getAttribute("href");
//			String dbRateVol = gridHeaderValue.get("StoreName");
//			compareUiDb("RateVolatility", uiRateVol, dbRateVol, node);
			
			
			for(int j=1; j<=$unitList.size(); j++) {
				String unitName = $unitName(_base.driver, i, j).getText();
				String unitValue = $unitValue(_base.driver, i, j).getAttribute("data-icon");
				String dbRawValue = "times";
				Date dbValue = _databaseUtils.getDate(_queries.gridUnitValue(unitName, Integer.valueOf(gridHeaderValue.get("StoreID"))));
				Date maxDatePrice = _queries.maxDatePrice;
				if(dbValue != null) {
					duration = maxDatePrice.getTime() - dbValue.getTime();
					diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
					dbRawValue = (diffInDays==0) ? "check" : (diffInDays<=365) ? "eye-slash" : "times"; 
				}
				compareUiDb(unitName, unitValue, dbRawValue, node);
			}
		}
	}
	
	
	
	public void compareUiDb(String header, String rawUi, String rawDb, ExtentTest node) {
		String uiValue = rawUi;
		if(!header.toLowerCase().contains("storename")) {
			uiValue = rawUi.replace(",", "").replace("$", "").replace("%", "").replace(" Sq.ft", "").replace(" sqft", "");
		}
		String dbValue = (rawDb==null || rawDb==String.valueOf(0)) ? "N/A" : rawDb;
		
		if(uiValue.trim().equals(dbValue.trim())) {
			System.out.println("PASS---"+header+" - Site: "+uiValue+"  Db: "+dbValue);
			node.log(Status.PASS, header+" - Site: "+uiValue+"  Db: "+dbValue);
		} else {
			System.out.println("FAIL---"+header+" - Site: "+uiValue+"  Db: "+dbValue);
			node.log(Status.FAIL, header+" - Site: "+uiValue+"  Db: "+dbValue);
		}
		
	}
	

}

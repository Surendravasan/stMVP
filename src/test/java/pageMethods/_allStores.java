package pageMethods;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import objRepository._allStoresPage;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._queries;
import pageUtilities._testData;
import pageUtilities._utils;

public class _allStores extends _allStoresPage {
	
	ExtentTest test = _base.test;
	ExtentTest node;
	HashMap<String, String> gridHeaderValue;
	
	public _allStores() {
		super();
		_utils.waitForElementInVisibleByLocator(loader);
		_helperClass.closeOpenWidgets();
		_utils.clickJs($allStoresLink);
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.clickJs($allStoresShowWidgetBtn);
		_utils.waitForElementInVisibleByLocator(loader);
	}
	
	
	
	
	public void overviewHeader() {
		
		node = test.createNode("Overview Header");

		try {
			System.out.println();
			for(int i=1; i<=$overHeaderList.size(); i++) {
				String uiLabel = $overHeaderLabel(_base.driver, i).getText();
				String uiValue = $overHeaderValue(_base.driver, i).getText();
				String dbValue = "";
				
				switch(uiLabel) {
				case "No. of Stores":
					dbValue = _databaseUtils.getStringValue(_testData.queryIns.overNoOfStores());
					_helperClass.compareUiDb(uiLabel, uiValue, dbValue, node);
					break;
				case "No. of REIT's":
					uiLabel = $overHeaderLabel(_base.driver, i).getText();
					uiValue = $overHeaderValue(_base.driver, i).getText();
					dbValue = _databaseUtils.getStringValue(_testData.queryIns.overNoOfReits());
					_helperClass.compareUiDb(uiLabel, uiValue, dbValue, node);
					break;
				case "No. of Multi-Ops":
					dbValue = _databaseUtils.getStringValue(_testData.queryIns.overNoOfMOps());
					_helperClass.compareUiDb(uiLabel, uiValue, dbValue, node);
					break;
				case "No. of Single-Ops":
					dbValue = _databaseUtils.getStringValue(_testData.queryIns.overNoOfSOps());
					_helperClass.compareUiDb(uiLabel, uiValue, dbValue, node);
					break;
				case "Total Rentable Square Footage":
					uiLabel = $overHeaderLabel(_base.driver, i).getText();
					uiValue = $overHeaderValue(_base.driver, i).getText();
					dbValue = _databaseUtils.getStringValue(_testData.queryIns.overTotalRentSqFo());
					_helperClass.compareUiDb(uiLabel, uiValue, dbValue, node);
					break;
				}
			}
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
	}
	
	/* Verify All Stores grid values */
	
	public void allStoresGrid(int noOfStores) {

		node = test.createNode("All Stores Grid Values");
		String uiStoresCount = String.valueOf($storesCount.size());
		String dbStoresCount = _databaseUtils.getStringValue(_testData.queryIns.storesCount());
		_helperClass.compareUiDb("Stores Count (UI vs DB)", uiStoresCount, dbStoresCount, node);
		_helperClass.compareUiDb("Stores Count (UI vs OverviewHeader)", uiStoresCount, $overHeaderValue(_base.driver, 1).getText(), node);
		
		
		if($noCompetitorText.size()!=1) {
			int noOfStoresToCheck = (noOfStores==0) ? $storeList.size() : noOfStores; 
			
	 		for(int i=2; i<=noOfStoresToCheck+1; i++) {
	 			try {
	 				String address = $address(_base.driver, i).getText();
					gridHeaderValue = _databaseUtils.getColumnValues(_testData.queryIns.gridHeaderValue(address));
					
					node.log(Status.INFO, MarkupHelper.createLabel(gridHeaderValue.get("StoreName"), ExtentColor.BLUE));
					String uiStorename = $storeName(_base.driver, i).getText();
					String dbStoreName = gridHeaderValue.get("StoreName");
					_helperClass.compareUiDb("StoreName"+"@skip@", uiStorename, dbStoreName, node);
					
					String uiTotalSqFt = $totalSqFtValue(_base.driver, i).getText();
					String dbTotalSqFt = gridHeaderValue.get("squarefootage");
					_helperClass.compareUiDb("TotalSqFt", uiTotalSqFt, dbTotalSqFt, node);
					
					String uiRentSqFt = $rentSqFtValue(_base.driver, i).getText();
					String dbRentSqFt = gridHeaderValue.get("RentableSqFt");
					_helperClass.compareUiDb("RentableSqFt", uiRentSqFt, dbRentSqFt, node);
					
					String uiOwnedBy = $ownedByValue(_base.driver, i).getText();
					String dbOwnedBy = gridHeaderValue.get("ownedby");
					_helperClass.compareUiDb("OwnedBy"+"@skip@", uiOwnedBy, dbOwnedBy, node);
					
					String uiOperatedBy = $operatedByValue(_base.driver, i).getText();
					String dbOperatedBy = gridHeaderValue.get("operatedby");
					_helperClass.compareUiDb("OperatedBy"+"@skip@", uiOperatedBy, dbOperatedBy, node);
					
					String uiOpenedBy = $openedValue(_base.driver, i).getText();
					String dbOpenedBy = gridHeaderValue.get("yearbuild");
					_helperClass.compareUiDb("OpenedBy", uiOpenedBy, dbOpenedBy, node);
					
					/* Rate Volatility no query to match value
					String uiRateVol = $rateVolValue(_base.driver, i).getAttribute("href");
					String dbRateVol = gridHeaderValue.get("StoreName");
					compareUiDb("RateVolatility", uiRateVol, dbRateVol, node);
					*/
					
	 			} catch(Exception e) {
					node.log(Status.ERROR, "Exception: "+e);
				}
	 			
	 			for(int j=1; j<=$unitList.size(); j++) {
					try {
						String unitName = $unitName(_base.driver, i, j).getText();
						String unitValue = $unitValue(_base.driver, i, j).getAttribute("data-icon");
												
						HashMap<String, String> map;
						map = _databaseUtils.getColumnValues(_testData.queryIns.gridUnitValue(unitName, Integer.valueOf(gridHeaderValue.get("StoreID"))));
						
						String dbRawValue = "times";
						if(!map.isEmpty()) {
							SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
							String dateBeforeString = map.get("dateprice");
							String dateAfterString = String.valueOf(_testData.queryIns.maxDatePrice);
							Date dateBefore = myFormat.parse(dateBeforeString);
						    Date dateAfter = myFormat.parse(dateAfterString);
						    long difference = dateAfter.getTime() - dateBefore.getTime();
						    int daysBetween = (int)(difference / (1000*60*60*24));
						    if(daysBetween<365) {
						    	if(daysBetween<=1 && map.get("IsCurrentPrice").equalsIgnoreCase("1")) {
						    		dbRawValue = "check";
						    	} else {
						    		dbRawValue = "eye-slash";
						    	}
						    }
						}
						_helperClass.compareUiDb(unitName, unitValue, dbRawValue, node);
					} catch(Exception e) {
						
					}
				}
	 		}
		} else {
			node.log(Status.INFO, "There are no competitors available in this market");
		}
	}
}

/* Previous method to get unit values */
//Date dbValue = _databaseUtils.getDate(_queries.gridUnitValue(unitName, Integer.valueOf(gridHeaderValue.get("StoreID"))));
//Date maxDatePrice = _queries.maxDatePrice;
//if(dbValue != null) {
//	duration = maxDatePrice.getTime() - dbValue.getTime();
//	diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
//	dbRawValue = (diffInDays==0) ? "check" : (diffInDays<=365) ? "eye-slash" : "times"; 
//}

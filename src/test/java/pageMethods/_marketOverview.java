package pageMethods;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import objRepository._marketOverviewPage;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._testData;
import pageUtilities._utils;

public class _marketOverview extends _marketOverviewPage {
	
	ExtentTest test = _base.test;
	
	public _marketOverview() {
		super();
		_utils.waitForElementInVisibleByLocator(loader);
		
	}
	
	public void headerOverview() {
		try {
			for(int i=1; i<=$headerList.size(); i++) {
				String uiLabel = $headerLabel(_base.driver, i).getText();
				String uiValue = $headerValue(_base.driver, i).getText();
				String dbValue = "";
				switch(uiLabel) {
				
				case "Total Rentable Square Footage":
//					uiLabel = $headerLabel(_base.driver, i).getText();
//					uiValue = $headerValue(_base.driver, i).getText();
					dbValue = _databaseUtils.getStringValue(_testData.queryIns.overTotalRentSqFo());
					_helperClass.compareUiDb(uiLabel, uiValue, dbValue, test);
					break;
					
				case "Sq Ft/Capita":
//					uiLabel = $headerLabel(_base.driver, i).getText();
//					uiValue = $headerValue(_base.driver, i).getText();
					dbValue = _databaseUtils.getStringValue(_testData.queryIns.overSqCapita());
					_helperClass.compareUiDb(uiLabel, uiValue, dbValue, test);
					break;
					
				case "No. of Stores in Market":
//					uiLabel = $headerLabel(_base.driver, i).getText();
//					uiValue = $headerValue(_base.driver, i).getText();
					dbValue = _databaseUtils.getStringValue(_testData.queryIns.overNoOfStores());
					_helperClass.compareUiDb(uiLabel, uiValue, dbValue, test);
					break;
					
				case "Avg. Rate/Sq Ft":
//					uiLabel = $headerLabel(_base.driver, i).getText();
//					uiValue = $headerValue(_base.driver, i).getText();
					int noOfUnitValues = $noOfAvgSqFtValues(_base.driver, i);
					
					if(noOfUnitValues==1) {
						dbValue = _databaseUtils.getStringValue(_testData.queryIns.overAvgRateSqFt());
						_helperClass.compareUiDb(uiLabel, uiValue, dbValue, test);
					} else {
						for(int j=2; j<=noOfUnitValues+1; j++) {
							String unitType = $sqFtValues(_base.driver, i, j).getText();
							String unitLabel = unitType.substring(0, unitType.indexOf(':'));
							String unitValue = unitType.substring(unitType.indexOf(':')+2).trim();
							switch(unitLabel.toLowerCase()) {
							case "all units":
								dbValue = _databaseUtils.getStringValue(_testData.queryIns.overAvgRateSqFtAllUnits());
								_helperClass.compareUiDb(unitLabel, unitValue, dbValue, test);
								break;
							case "non-cc":
								dbValue = _databaseUtils.getStringValue(_testData.queryIns.overAvgRateSqFtNonCC());
								_helperClass.compareUiDb(unitLabel, unitValue, dbValue, test);
								break;
							case "cc":
								dbValue = _databaseUtils.getStringValue(_testData.queryIns.overAvgRateSqFtCC());
								_helperClass.compareUiDb(unitLabel, unitValue, dbValue, test);
								break;
							}
						}
					}
					break;
					
				case "Avg. Inventory Availability":
//					uiLabel = $headerLabel(_base.driver, i).getText();
//					uiValue = $headerValue(_base.driver, i).getText();
					/* No query
					dbValue = _databaseUtils.getStringValue(_queries.overNoOfStores());
					_helperClass.compareUiDb(uiLabel, uiValue, dbValue, test);
					*/
					break;
					
				case "Expected Supply Growth Next Year":
					uiLabel = $headerLabel(_base.driver, i).getText();
					uiValue = $headerValue(_base.driver, i).getText();
					/* No query
					dbValue = _databaseUtils.getStringValue(_queries.overNoOfStores());
					_helperClass.compareUiDb(uiLabel, uiValue, dbValue, test);
					*/
					break;
				}
			}
		} catch (Exception e) {
			test.log(Status.ERROR, "Exception: "+e);
		}
		
	}
	
	
	
//	public void compareUiDb(String header, String rawUi, String rawDb) {
//		String uiValue = rawUi.replace(",", "").replace("$", "").replace("%", "").replace(" Sq.ft", "");
//		String dbValue = rawDb;
//		
//		if(uiValue.equals(dbValue)) {
//			test.log(Status.PASS, header+" - Site: "+uiValue+"  Db: "+dbValue);
//		} else {
//			test.log(Status.FAIL, header+" - Site: "+uiValue+"  Db: "+dbValue);
//		}
//	}

}

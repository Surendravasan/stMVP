package pageMethods;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import objRepository._marketOverviewPage;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._queries;
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
				String uiLabel = "";
				String uiValue = "";
				String dbValue = "";
				switch(i) {
				case 1:
					uiLabel = $headerLabel(_base.driver, i).getText();
					uiValue = $headerValue(_base.driver, i).getText();
					dbValue = _databaseUtils.getStringValue(_testData.queryIns.overTotalRentSqFo());
					_helperClass.compareUiDb(uiLabel, uiValue, dbValue, test);
					break;
				case 2:
					uiLabel = $headerLabel(_base.driver, i).getText();
					uiValue = $headerValue(_base.driver, i).getText();
					dbValue = _databaseUtils.getStringValue(_testData.queryIns.overSqCapita());
					_helperClass.compareUiDb(uiLabel, uiValue, dbValue, test);
					break;
				case 3:
					uiLabel = $headerLabel(_base.driver, i).getText();
					uiValue = $headerValue(_base.driver, i).getText();
					dbValue = _databaseUtils.getStringValue(_testData.queryIns.overNoOfStores());
					_helperClass.compareUiDb(uiLabel, uiValue, dbValue, test);
					break;
				case 4:
					uiLabel = $headerLabel(_base.driver, i).getText();
					uiValue = $headerValue(_base.driver, i).getText();
					dbValue = _databaseUtils.getStringValue(_testData.queryIns.overAvgRateSqFt());
					_helperClass.compareUiDb(uiLabel, uiValue, dbValue, test);
					break;
				case 5:
					uiLabel = $headerLabel(_base.driver, i).getText();
					uiValue = $headerValue(_base.driver, i).getText();
					/* No query
					dbValue = _databaseUtils.getStringValue(_queries.overNoOfStores());
					_helperClass.compareUiDb(uiLabel, uiValue, dbValue, test);
					*/
					break;
				case 6:
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

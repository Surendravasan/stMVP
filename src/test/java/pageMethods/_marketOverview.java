package pageMethods;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import objRepository._marketOverviewPage;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._queries;
import pageUtilities._utils;

public class _marketOverview extends _marketOverviewPage {
	
	ExtentTest test = _base.test;
	
	public _marketOverview() {
		super();
		_utils.waitForElementInVisibleByLocator(loader);
		
	}
	
	public void headerOverview() {
		for(int i=1; i<=$headerList.size(); i++) {
			String uiLabel = "";
			String uiValue = "";
			String dbValue = "";
			switch(i) {
			case 1:
				uiLabel = $headerLabel(_base.driver, i).getText();
				uiValue = $headerValue(_base.driver, i).getText();
				dbValue = _databaseUtils.getStringValue(_queries.overTotalRentSqFo());
				compareUiDb(uiLabel, uiValue, dbValue);
				break;
			case 2:
				uiLabel = $headerLabel(_base.driver, i).getText();
				uiValue = $headerValue(_base.driver, i).getText();
				dbValue = _databaseUtils.getStringValue(_queries.overSqCapita());
				compareUiDb(uiLabel, uiValue, dbValue);
				break;
			case 3:
				uiLabel = $headerLabel(_base.driver, i).getText();
				uiValue = $headerValue(_base.driver, i).getText();
				dbValue = _databaseUtils.getStringValue(_queries.overNoOfStores());
				compareUiDb(uiLabel, uiValue, dbValue);
				break;
			case 4:
				uiLabel = $headerLabel(_base.driver, i).getText();
				uiValue = $headerValue(_base.driver, i).getText();
				dbValue = _databaseUtils.getStringValue(_queries.overAvgRateSqFt());
				compareUiDb(uiLabel, uiValue, dbValue);
				break;
			case 5:
				uiLabel = $headerLabel(_base.driver, i).getText();
				uiValue = $headerValue(_base.driver, i).getText();
//				dbValue = _databaseUtils.getStringValue(_queries.overNoOfStores());
				compareUiDb(uiLabel, uiValue, dbValue);
				break;
			case 6:
				uiLabel = $headerLabel(_base.driver, i).getText();
				uiValue = $headerValue(_base.driver, i).getText();
//				dbValue = _databaseUtils.getStringValue(_queries.overNoOfStores());
				compareUiDb(uiLabel, uiValue, dbValue);
				break;
			}
		}
	}
	
	
	
	public void compareUiDb(String header, String rawUi, String rawDb) {
		String uiValue = rawUi.replace(",", "").replace("$", "").replace("%", "").replace(" Sq.ft", "");
		String dbValue = rawDb;
		
		if(uiValue.equals(dbValue)) {
			test.log(Status.PASS, header+" - Site: "+uiValue+"  Db: "+dbValue);
		} else {
			test.log(Status.FAIL, header+" - Site: "+uiValue+"  Db: "+dbValue);
		}
		
	}

}

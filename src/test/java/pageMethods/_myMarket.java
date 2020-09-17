package pageMethods;  

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import objRepository._myMarketPage;
import pageUtilities._base;
import pageUtilities._testData;
import pageUtilities._utils;

public class _myMarket extends _myMarketPage {
	
	ExtentTest test = _base.test;
	
	public _myMarket() {
		super();
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.waitForElementVisibleByLocator($tableData);
		_utils.waitForElementInVisibleByLocator(loader);
	}
	
	public void addMarket() {
		_utils.waitForElementVisibleByLocator($addMarketLabel);
		_utils.waitClick($addMarketBtn);
	}
	
	/* not used */
	public void gotoDashboard(int userStore) {
//		_utils.submit($dashboardLink(_base.driver, userStore));
		_utils.waitForElementInVisibleByLocator(loader);
	}
	

	public void marketView(int uStoreId) {
		if(uStoreId!=0) {
			_utils.waitClick($marketname(_base.driver, uStoreId));
			} else {
				String link = $marketname.getAttribute("href");
				
				/* Format of URL changed from
				 * prev url ends with userstoreid
				 * recent changes includes search id at the end */
//				int userStoreId = Integer.valueOf(link.substring(link.lastIndexOf("/")+1));
				
				String stringIds = link.substring(link.indexOf("palette")+8);
				int userStoreId = Integer.valueOf(stringIds.substring(0, stringIds.indexOf("/")));
				
				
				_testData.setUserStoreId(userStoreId);
				_utils.waitClick($marketname);
				test.log(Status.INFO, "UserStoreID: "+_testData.userStoreId);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}

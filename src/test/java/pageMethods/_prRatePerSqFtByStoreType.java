package pageMethods;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import objRepository._prRatePerSqFtByStoreTypePage;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._testData;
import pageUtilities._utils;

public class _prRatePerSqFtByStoreType extends _prRatePerSqFtByStoreTypePage {
	
	ExtentTest test = _base.test;
	ExtentTest node;
	HashMap<String, String> marketDetails;
	_helperClass hc = new _helperClass();
	List<String> comparedMarkets = new LinkedList<>();
	
	
	public _prRatePerSqFtByStoreType() {
		super();
		_utils.waitForElementInVisibleByLocator(loader);
		_helperClass.closeOpenWidgets();
		_utils.selectDropDownByVisibleText($widgetsDisplay, "Show Widgets by Category");
		_utils.clickJs($pricingRentalExpand);
		_utils.clickJs($rateByStoreTypeLink);
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.clickJs($addWidget);
		_utils.waitForElementInVisibleByLocator(loader);
	}
	
	
	
	
	
	
	/* Rate per Square Feet by Store Type - This Market */
	
	public void thisMarketRateByStoreType() {
		HashMap<String, String> dbValues;
		node = test.createNode($title.getText()+" - This Market");
		
		try {
			dbValues = _databaseUtils.mapStrFl(_testData.queryIns.thisMarketRateByStoreType());
			for(int i=1; i<=$noOfColumns.size(); i++) {
				try {
					String header = $rateStoreTypeHeader(_base.driver, i).getText();
					String uiValue = $rateStoreTypeValues(_base.driver, 1, i).getText();
					String dbValue = dbValues.get(header);
					_helperClass.compareUiDb(header, uiValue, dbValue, node);
				} catch(Exception e) {
					node.log(Status.ERROR, "Exception: "+e);
				}
			}
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
	}
	
	
	
	
	
	
	
	/* Rate per Square Feet by Store Type - Green Values */
	
	public void greenRateByStoreType() {
		if(!hc.getGreenMarket().contains("-- None --")) {
			HashMap<String, String> dbValues;
			node = test.createNode($title.getText()+" - "+hc.getGreenMarket());
			
			try {
				if(hc.getGreenMarket().contains("National Totals and Averages")){
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.nationalRateByStoreType());
				} else {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.greenBlueRateByStoreType(marketDetails));
				}
				for(int i=1; i<=$noOfColumns.size(); i++) {
					try {
						String header = $rateStoreTypeHeader(_base.driver, i).getText();
						String uiValue = $rateStoreTypeValues(_base.driver, 2, i).getText();
						String dbValue = dbValues.get(header);
						_helperClass.compareUiDb(header, uiValue, dbValue, node);
					} catch(Exception e) {
						node.log(Status.ERROR, "Exception: "+e);
					}
				}
			} catch(Exception e) {
				node.log(Status.ERROR, "Exception: "+e);
			}
		}
	}
	
	
	
	
	
	
	
	/* Rate per Square Feet by Store Type - Blue Values */
	
	public void blueRateByStoreType() {
		if(!hc.getBlueMarket().contains("-- None --")) {
			HashMap<String, String> dbValues;
			node = test.createNode($title.getText()+" - "+hc.getBlueMarket());
			
			try {
				if(hc.getBlueMarket().contains("State Total and Averages")) {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.stateRateByStoreType());
				} else {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.greenBlueRateByStoreType(marketDetails));
				}
				for(int i=1; i<=$noOfColumns.size(); i++) { 
					try {
						String header = $rateStoreTypeHeader(_base.driver, i).getText();
						String uiValue = $rateStoreTypeValues(_base.driver, 3, i).getText();
						String dbValue = dbValues.get(header);
						_helperClass.compareUiDb(header, uiValue, dbValue, node);
					} catch(Exception e) {
						node.log(Status.ERROR, "Exception: "+e);
					}
				}
			} catch(Exception e) {
				node.log(Status.ERROR, "Exception: "+e);
			}
		}
	}
	
	
	
	
	
	/* Select Markets and verify the values */
	
	public void compareMarket() {
		node = test.createNode("Compare Selected Markets");
		
		comparedMarkets.add("-- None --");
		comparedMarkets.add(hc.getGreenMarket());
		comparedMarkets.add(hc.getBlueMarket());
		
		try {
			hc.compareMarket();
			
			node.log(Status.INFO, MarkupHelper.createLabel(hc.greenStoreName, ExtentColor.GREEN));
			node.log(Status.INFO, MarkupHelper.createLabel(hc.blueStoreName, ExtentColor.BLUE));
			
			if(comparedMarkets.contains(hc.getGreenMarket())==false) {
				marketDetails = hc.getGreenDetails();
				greenRateByStoreType();
			}
			
			if(comparedMarkets.contains(hc.getBlueMarket())==false) {
				marketDetails = hc.getBlueDetails();
				blueRateByStoreType();
			}
			
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
		
	}
	
	
	
	/* Reset Compared Markets */
	
	public void resetComparedMarket() {
		hc.resetCompMarket();
	}
	
	
}		
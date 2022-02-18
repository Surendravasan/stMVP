package pageMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import objRepository._storesInMarketStoreTypesPage;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._testData;
import pageUtilities._utils;

public class _storesInMarketStoreTypes extends _storesInMarketStoreTypesPage {
	
	ExtentTest test = _base.test;
	ExtentTest node;
	HashMap<String, String> marketDetails;
	_helperClass hc = new _helperClass();
	List<String> comparedMarkets = new LinkedList<>();
	
	
	public _storesInMarketStoreTypes() {
		super();
		_utils.waitForElementInVisibleByLocator(loader);
		_helperClass.closeOpenWidgets();
		_utils.selectDropDownByVisibleText($widgetsDisplay, "Show Widgets by Category");
		_utils.clickJs($storesInMarketExpand);
		_utils.clickJs($storeTypesLink);
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.clickJs($addWidget);
		_utils.waitForElementInVisibleByLocator(loader);
	}
	
	
	
	
	
	/* Store Types - This Market */
	
	public void thisMarketStoreTypes() {
		HashMap<String, String> dbValues;
		node = test.createNode($title.getText()+" - This Market");
		
		try {
			dbValues = _databaseUtils.mapStrFl(_testData.queryIns.thisMarketStoreTypes());
			ArrayList<String> as = new ArrayList<String>(dbValues.values());
			int a = 1;
			for(int i=1; i<=$noOfColumns.size(); i++) {
				String header = $storeTypesHeader(_base.driver, i).getText();
				int innerColCount = (i==1) ? 1 : 2;
				for(int j=1; j<=innerColCount; j++) {
					try {
						String uiValue = $rateStoreTypeValues(_base.driver, 1, a).getText();
						String dbValue = as.get(a-1);
						if(j==2) {
							header = header+"%";
						}
						_helperClass.compareUiDb(header, uiValue, dbValue, node);
						a+=1;
					} catch(Exception e) {
						node.log(Status.ERROR, "Exception: "+e);
					}
				}
			}
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
	}
	
	
	
	
	
	/* Store Types - Green Values */
	
	public void greenStoreTypes() {
		if(!hc.getGreenMarket().contains("-- None --")) {
			HashMap<String, String> dbValues;
			node = test.createNode($title.getText()+" - "+hc.getGreenMarket());
			
			try {
//				if(marketType.equalsIgnoreCase("National")){
				if(hc.getGreenMarket().contains("National Totals and Averages")){
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.nationalStoreTypes());
				} else {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.greenBlueStoreTypes(marketDetails));
				}
				ArrayList<String> as = new ArrayList<String>(dbValues.values());
				int a = 1;
				for(int i=1; i<=$noOfColumns.size(); i++) {
					String header = $storeTypesHeader(_base.driver, i).getText();
					int innerColCount = (i==1) ? 1 : 2;
					for(int j=1; j<=innerColCount; j++) {
						try {
							String uiValue = $rateStoreTypeValues(_base.driver, 2, a).getText();
							String dbValue = as.get(a-1);
							if(j==2) {
								header = header+"%";
							}
							_helperClass.compareUiDb(header, uiValue, dbValue, node);
							a+=1;
						} catch(Exception e) {
							node.log(Status.ERROR, "Exception: "+e);
						}
					}
				}
			} catch(Exception e) {
				node.log(Status.ERROR, "Exception: "+e);
			}
		}
	}
	
	
	
	
	/* Store Types - Blue Values */
	
	public void blueStoreTypes() {
//		if(_testData.regId==3 && !marketType.equalsIgnoreCase("State")) {
		if(!hc.getBlueMarket().contains("-- None --")) {
			HashMap<String, String> dbValues;
			node = test.createNode($title.getText()+" - "+hc.getBlueMarket());
			
			try {
//				if(marketType.equalsIgnoreCase("State")){
				if(hc.getBlueMarket().contains("State Total and Averages")) {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.stateStoreTypes());
				} else {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.greenBlueStoreTypes(marketDetails));
				}
				ArrayList<String> as = new ArrayList<String>(dbValues.values());
				int a = 1;
				for(int i=1; i<=$noOfColumns.size(); i++) {
					String header = $storeTypesHeader(_base.driver, i).getText();
					int innerColCount = (i==1) ? 1 : 2;
					for(int j=1; j<=innerColCount; j++) {
						try {
							String uiValue = $rateStoreTypeValues(_base.driver, 3, a).getText();
							String dbValue = as.get(a-1);
							if(j==2) {
								header = header+"%";
							}
							_helperClass.compareUiDb(header, uiValue, dbValue, node);
							a+=1;
						} catch(Exception e) {
							node.log(Status.ERROR, "Exception: "+e);
						}
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
			
//			if(!greenSelection.contains("National Totals and Averages")) {
			if(comparedMarkets.contains(hc.getGreenMarket())==false) {
				marketDetails = hc.getGreenDetails();
				greenStoreTypes();
			}
			
//			if(!blueSelection.contains("State Total and Averages")) {
			if(comparedMarkets.contains(hc.getBlueMarket())==false) {
				marketDetails = hc.getBlueDetails();
				blueStoreTypes();
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
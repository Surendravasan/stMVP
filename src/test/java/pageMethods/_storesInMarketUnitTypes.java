package pageMethods;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import objRepository._storesInMarketUnitTypesPage;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._testData;
import pageUtilities._utils;

public class _storesInMarketUnitTypes extends _storesInMarketUnitTypesPage {
	
	ExtentTest test = _base.test;
	ExtentTest node;
	_helperClass hc = new _helperClass();
	HashMap<String, String> marketDetails;
	List<String> comparedMarkets = new LinkedList<>();
	
	
	
	public _storesInMarketUnitTypes() {
		super();
		_utils.waitForElementInVisibleByLocator(loader);
		_helperClass.closeOpenWidgets();
		_utils.selectDropDownByVisibleText($widgetsDisplay, "Show Widgets by Category");
		_utils.clickJs($pricingRentalExpand);
		_utils.clickJs($unitTypesOfferedLink);
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.clickJs($addWidget);
		_utils.waitForElementInVisibleByLocator(loader);
	}

	
	
	
	
	/* Unit Types Offered - This Market */
	
	public void thisMarketRateByUnitType() {
		System.out.println();
		node = test.createNode($title.getText()+" - This Market");
		
		/* query output values not matching for below units */
		List<String> skippedUnits = Arrays.asList("");
		
		try {
			for(int i=1; i<=$unitList.size(); i++) {
				try {
					String unitName = $unitLabel(_base.driver, i).getText();
					if(skippedUnits.contains(unitName.toUpperCase())==false) {
						String unitValueUi = $unitValue(_base.driver, 1, i).getText();
						String unitValueDb = _databaseUtils.getStringValue(_testData.queryIns.thisMarketUnitTypes(unitName));
						_helperClass.compareUiDb(unitName, unitValueUi, unitValueDb, node);
					}
				} catch(Exception e) {
					node.log(Status.ERROR, "Exception: "+e);
				}
			}
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
	}
	
	
	
	
	
	
	/* Unit Types Offered - Green Values */
	
	public void greenRateByUnitType() {
		if(!hc.getGreenMarket().contains("-- None --")) {
			node = test.createNode($title.getText()+" - "+hc.getGreenMarket());
			String unitName = "";
			String unitValueUi = "";
			String unitValueDb = "";
			
			/* query output values not matching for below units */
			List<String> skippedUnits = Arrays.asList("");
			
			try {
				for(int i=1; i<=$unitList.size(); i++) {
					try {
						unitName = $unitLabel(_base.driver, i).getText();
						if(skippedUnits.contains(unitName.toUpperCase())==false) {
							unitValueUi = $unitValue(_base.driver, 2, i).getText();
//							if(marketType.equalsIgnoreCase("National")){
							if(hc.getGreenMarket().contains("National Totals and Averages")) {
								unitValueDb = _databaseUtils.getStringValue(_testData.queryIns.nationalUnitTypes(unitName));
							} else {
								unitValueDb = _databaseUtils.getStringValue(_testData.queryIns.greenBlueUnitTypes(unitName, marketDetails));
							}
							_helperClass.compareUiDb(unitName, unitValueUi, unitValueDb, node);
						}
					} catch(Exception e) {
						node.log(Status.ERROR, "Exception: "+e);
					}
				}
			} catch(Exception e) {
				node.log(Status.ERROR, "Exception: "+e);
			}
		}
	}
	
	
	
	
	
	/* Unit Types Offered - Blue Values */
	
	public void blueRateByUnitType() {
//		if(_testData.regId==3 && !marketType.equalsIgnoreCase("State")) {
		if(!hc.getBlueMarket().contains("-- None --")) {
			node = test.createNode($title.getText()+" - "+hc.getBlueMarket());
			String unitName = "";
			String unitValueUi = "";
			String unitValueDb = "";
			
			/* query output values not matching for below units */
			List<String> skippedUnits = Arrays.asList("");
			
			try {
				for(int i=1; i<=$unitList.size(); i++) {
					try {
						unitName = $unitLabel(_base.driver, i).getText();
						if(skippedUnits.contains(unitName.toUpperCase())==false) {
							unitValueUi = $unitValue(_base.driver, 3, i).getText();
//							if(marketType.equalsIgnoreCase("State")){
							if(hc.getBlueMarket().contains("State Total and Averages")) {
								unitValueDb = _databaseUtils.getStringValue(_testData.queryIns.stateUnitTypes(unitName));
							} else {
								unitValueDb = _databaseUtils.getStringValue(_testData.queryIns.greenBlueUnitTypes(unitName, marketDetails));
							}
							_helperClass.compareUiDb(unitName, unitValueUi, unitValueDb, node);
						}
					} catch(Exception e) {
						node.log(Status.ERROR, "Exception: "+e);
					}
				}
			} catch(Exception e) {
				node.log(Status.ERROR, "Exception: "+e);
			}
		}
	}
	
	
	
	
	
	/* Compare Markets */
	
	public void compareMarket() {
		node = test.createNode("Compare Markets");
		
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
				greenRateByUnitType();
			}
			
//			if(!blueSelection.contains("State Total and Averages")) {
			if(comparedMarkets.contains(hc.getBlueMarket())==false) {
				marketDetails = hc.getBlueDetails();
				blueRateByUnitType();
			}
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
	}
	
	
	
	
	/* Reset Selected Markets  in Compare Market Drop Down */
	
	public void resetComparedMarket() {
		hc.resetCompMarket();
	}
	
	
	
}

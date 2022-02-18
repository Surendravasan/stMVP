package pageMethods;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import objRepository._prRatePerSqFtByUnitTypePage;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._testData;
import pageUtilities._utils;

public class _prRatePerSqFtByUnitType extends _prRatePerSqFtByUnitTypePage {
	
	ExtentTest test = _base.test;
	ExtentTest node;
	_helperClass hc = new _helperClass();
	HashMap<String, String> marketDetails;
	List<String> comparedMarkets = new LinkedList<>();
	
	
	public _prRatePerSqFtByUnitType() {
		super();
		_utils.waitForElementInVisibleByLocator(loader);
		_helperClass.closeOpenWidgets();
		_utils.selectDropDownByVisibleText($widgetsDisplay, "Show Widgets by Category");
		_utils.clickJs($pricingRentalExpand);
		_utils.clickJs($rateByUnitTypeLink);
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.clickJs($addWidget);
		_utils.waitForElementInVisibleByLocator(loader);
	}

	
	
	
	
	/* Rate per Square Feet by Unit Type - This Market */
	
	public void thisMarketRateByUnitType() {
		
		HashMap<String, String> dbValues;
		node = test.createNode($title.getText()+" - This Market");
		
		/* query output values not matching for below units */
		List<String> skippedUnits = Arrays.asList("");
		
		try {
			dbValues = _databaseUtils.getMapString(_testData.queryIns.thisMarketRateByUnitType());
			for(int i=1; i<=$unitList.size(); i++) {
				try {
					String unitName = $unitLabel(_base.driver, i).getText();
					if(skippedUnits.contains(unitName.toUpperCase())==false) {
						String unitValueUi = $unitValue(_base.driver, 1, i).getText();
						String unitValueDb = dbValues.get(unitName.toLowerCase());
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
	
	
	
	
	
	
	
	/* Rate per Square Feet by Unit Type - Green */
	
	public void greenRateByUnitType() {
		if(!hc.getGreenMarket().contains("-- None --")) {
			HashMap<String, String> dbValues;
			node = test.createNode($title.getText()+" - "+hc.getGreenMarket());
			
			/* query output values not matching for below units */
			List<String> skippedUnits = Arrays.asList("");
			
			try {
				if(hc.getGreenMarket().contains("National Totals and Averages")){
					dbValues = _databaseUtils.getMapString(_testData.queryIns.nationalRateByUnitType());
				} else {
					dbValues = _databaseUtils.getMapString(_testData.queryIns.greenBlueRateByUnitType(marketDetails));
				}
				for(int i=1; i<=$unitList.size(); i++) {
					try {
						String unitName = $unitLabel(_base.driver, i).getText();
						if(skippedUnits.contains(unitName.toUpperCase())==false) {
							String unitValueUi = $unitValue(_base.driver, 2, i).getText();
							String unitValueDb = dbValues.get(unitName.toLowerCase());
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
	
	
	
	
	
	
	/* Rate per Square Feet by Unit Type - Blue */
	
	public void blueRateByUnitType() {
		if(!hc.getBlueMarket().contains("-- None --")) {
			HashMap<String, String> dbValues;
			node = test.createNode($title.getText()+" - "+hc.getBlueMarket());
			
			/* query output values not matching for below units */
			List<String> skippedUnits = Arrays.asList("");
			
			try {
				if(hc.getBlueMarket().contains("State Total and Averages")) {
					dbValues = _databaseUtils.getMapString(_testData.queryIns.stateRateByUnitType());
				} else {
					dbValues = _databaseUtils.getMapString(_testData.queryIns.greenBlueRateByUnitType(marketDetails));
				}
				for(int i=1; i<=$unitList.size(); i++) {
					try {
						String unitName = $unitLabel(_base.driver, i).getText();
						if(skippedUnits.contains(unitName.toUpperCase())==false) {
							String unitValueUi = $unitValue(_base.driver, 3, i).getText();
							String unitValueDb = dbValues.get(unitName.toLowerCase());
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
			
			if(comparedMarkets.contains(hc.getGreenMarket())==false) {
				marketDetails = hc.getGreenDetails();
				greenRateByUnitType();
			}
			
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

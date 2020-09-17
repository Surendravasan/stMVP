package pageMethods;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.support.ui.Select;

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
	
	public void greenRateByUnitType(String marketType) {
		HashMap<String, String> dbValues;
		String market = (marketType.equalsIgnoreCase("National")) ? marketType : hc.greenStoreName;
		node = test.createNode($title.getText()+" - "+market);
		
		/* query output values not matching for below units */
		List<String> skippedUnits = Arrays.asList("");
		
		try {
			if(marketType.equalsIgnoreCase("National")){
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
	
	
	
	
	
	
	/* Rate per Square Feet by Unit Type - Blue */
	
	public void blueRateByUnitType(String marketType) {
		if(_testData.regId==3 && !marketType.equalsIgnoreCase("State")) {
			HashMap<String, String> dbValues;
			String market = (marketType.equalsIgnoreCase("State")) ? marketType : hc.blueStoreName;
			node = test.createNode($title.getText()+" - "+market);
			
			/* query output values not matching for below units */
			List<String> skippedUnits = Arrays.asList("");
			
			try {
				if(marketType.equalsIgnoreCase("State")){
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
		try {
			hc.compareMarket();
			
			node.log(Status.INFO, MarkupHelper.createLabel(hc.greenStoreName, ExtentColor.GREEN));
			node.log(Status.INFO, MarkupHelper.createLabel(hc.blueStoreName, ExtentColor.BLUE));
			
			String greenSelection = new Select($greenDropDown).getFirstSelectedOption().getText();
			String blueSelection = new Select($blueDropDown).getFirstSelectedOption().getText();
			
			if(!greenSelection.contains("National Totals and Averages")) {
				marketDetails = hc.getGreenDetails();
				greenRateByUnitType("compare");
			}
			
			if(!blueSelection.contains("State Total and Averages")) {
				marketDetails = hc.getBlueDetails();
				blueRateByUnitType("compare");
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

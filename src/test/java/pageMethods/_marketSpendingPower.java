package pageMethods;

import java.util.HashMap;

import org.openqa.selenium.support.ui.Select;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import objRepository._marketSpendingPowerPage;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._testData;
import pageUtilities._utils;

public class _marketSpendingPower extends _marketSpendingPowerPage {
	
	ExtentTest test = _base.test;
	ExtentTest node;
	HashMap<String, String> marketDetails;
	_helperClass hc = new _helperClass();
	
	
	public _marketSpendingPower() {
		super();
		_utils.waitForElementInVisibleByLocator(loader);
		_helperClass.closeOpenWidgets();
		_utils.selectDropDownByVisibleText($widgetsDisplay, "Show Widgets by Category");
		_utils.clickJs($mktSpenExpand);
		_utils.clickJs($marketSpendLink);
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.clickJs($addWidget);
		_utils.waitForElementInVisibleByLocator(loader);
	}
	
	
	
	
	
	/* Household Income/Store - This Market */
	
	public void thisMarketHouseholdIncome() {
		HashMap<String, String> dbValues;
		System.out.println();
		node = test.createNode($titleHousehold.getText()+" - This Market");
		
		try {
			dbValues = _databaseUtils.mapStrFl(_testData.queryIns.houholdIncMarket());
			for(int i=1; i<=$noOfColunns.size(); i++) {
				try {
					String header = $houHolIncLabel(_base.driver, i).getText();
					String uiValue = $houHolIncValues(_base.driver, 1, i).getText();
					String dbValue = _helperClass.convertCurrency(dbValues.get(header));
					_helperClass.compareUiDb(header, uiValue, dbValue, node);
				} catch(Exception e) {
					node.log(Status.ERROR, "Exception: "+e);
				}
			}
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
	}
	
	
	
	
	
	/* Household Income/Store - Green Values */
	
	public void greenHouseholdIncome(String marketType) {
		HashMap<String, String> dbValues;
		String market = (marketType.equalsIgnoreCase("National")) ? marketType : hc.greenStoreName;
		node = test.createNode($titleHousehold.getText()+" - "+market);
		
		try {
			if(marketType.equalsIgnoreCase("National")){
				dbValues = _databaseUtils.mapStrFl(_testData.queryIns.houholIncNational());
			} else {
				dbValues = _databaseUtils.mapStrFl(_testData.queryIns.householdGreenBlue(marketDetails));
			}
			for(int i=1; i<=$noOfColunns.size(); i++) {
				try {
					String header = $houHolIncLabel(_base.driver, i).getText();
					String uiValue = $houHolIncValues(_base.driver, 2, i).getText();
					String dbValue = _helperClass.convertCurrency(dbValues.get(header));
					_helperClass.compareUiDb(header, uiValue, dbValue, node);
				} catch(Exception e) {
					node.log(Status.ERROR, "Exception: "+e);
				}
			}
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
	}
	
	
	
	
	
	/* Household Income/Store - Blue Values */
	
	public void blueHouseholdIncome(String marketType) {
		if(_testData.regId==3 && !marketType.equalsIgnoreCase("State")) {
			HashMap<String, String> dbValues;
			String market = (marketType.equalsIgnoreCase("State")) ? marketType : hc.blueStoreName;
			node = test.createNode($titleHousehold.getText()+" - "+market);
			
			try {
				if(marketType.equalsIgnoreCase("State")){
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.houholIncState());
				} else {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.householdGreenBlue(marketDetails));
				}
				for(int i=1; i<=$noOfColunns.size(); i++) {
					try {
						String header = $houHolIncLabel(_base.driver, i).getText();
						String uiValue = $houHolIncValues(_base.driver, 3, i).getText();
						String dbValue = _helperClass.convertCurrency(dbValues.get(header));
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
	
	
	
	
	
	
	/* Average Property Value - This Market */
	
	public void thisMarketAverageProperty() {
		HashMap<String, String> dbValues;
		node = test.createNode($titleAvgProp.getText()+" - This Market");
		
		try {
			dbValues = _databaseUtils.mapStrFl(_testData.queryIns.avgPropMarket());
			String header = $avgPropLabel().getText();
			String uiValue = $avgPropValueMarket().getText();
			String dbValue = dbValues.get(header);
			_helperClass.compareUiDb(header, uiValue, dbValue, node);
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
	}
	
	
	
	
	
	/* Average Property Value - Green Values */
	
	public void greenAverageProperty(String marketType) {
		HashMap<String, String> dbValues;
		String market = (marketType.equalsIgnoreCase("National")) ? marketType : hc.greenStoreName;
		node = test.createNode($titleAvgProp.getText()+" - "+market);
		
		try {
			if(marketType.equalsIgnoreCase("National")){
				dbValues = _databaseUtils.mapStrFl(_testData.queryIns.avgPropNational());
			} else {
				dbValues = _databaseUtils.mapStrFl(_testData.queryIns.avgPropGreenBlue(marketDetails));
			}
			String header = $avgPropLabel().getText();
			String uiValue = $avgPropValueGreen().getText();
			String dbValue = dbValues.get(header);
			_helperClass.compareUiDb(header, uiValue, dbValue, node);
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
	}
	
	
	
	
	
	/* Average Property Value - Blue Values */
	
	public void blueAverageProperty(String marketType) {
		if(_testData.regId==3 && !marketType.equalsIgnoreCase("State")) {
			HashMap<String, String> dbValues;
			String market = (marketType.equalsIgnoreCase("State")) ? marketType : hc.blueStoreName;
			node = test.createNode($titleAvgProp.getText()+" - "+market);
			
			try {
				if(marketType.equalsIgnoreCase("State")){
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.avgPropState());
				} else {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.avgPropGreenBlue(marketDetails));
				}
					String header = $avgPropLabel().getText();
					String uiValue = $avgPropValueBlue().getText();
					String dbValue = dbValues.get(header);
					_helperClass.compareUiDb(header, uiValue, dbValue, node);
			} catch(Exception e) {
				node.log(Status.ERROR, "Exception: "+e);
			}
		}
	}
	
	
	
	
	
	
	/* Average Rental Costs - This Market */
	
	public void thisMarketAverageRental() {
		HashMap<String, String> dbValues;
		node = test.createNode($titleAvgRent.getText()+" - This Market");
		
		try {
			dbValues = _databaseUtils.mapStrFl(_testData.queryIns.rentalPropMarket());
			String header = $avgRentLabel().getText();
			String uiValue = $avgRentMarket().getText();
			String dbValue = dbValues.get(header);
			_helperClass.compareUiDb(header, uiValue, dbValue, node);
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
		
	}
	
	
	
	
	
	/* Average Rental Costs - Green Values */
	
	public void greenAverageRental(String marketType) {
		HashMap<String, String> dbValues;
		String market = (marketType.equalsIgnoreCase("National")) ? marketType : hc.greenStoreName;
		node = test.createNode($titleAvgRent.getText()+" - "+market);
		
		try {
			if(marketType.equalsIgnoreCase("National")){
				dbValues = _databaseUtils.mapStrFl(_testData.queryIns.avgRentNational());
			} else {
				dbValues = _databaseUtils.mapStrFl(_testData.queryIns.avgRentGreenBlue(marketDetails));
			}
				String header = $avgRentLabel().getText();
				String uiValue = $avgRentGreen().getText();
				String dbValue = dbValues.get(header);
				_helperClass.compareUiDb(header, uiValue, dbValue, node);
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
		
	}
	
	
	
	
	
	/* Average Rental Costs - Blue Values */
	
	public void blueAverageRental(String marketType) {
		if(_testData.regId==3 && !marketType.equalsIgnoreCase("State")) {
			HashMap<String, String> dbValues;
			String market = (marketType.equalsIgnoreCase("State")) ? marketType : hc.blueStoreName;
			node = test.createNode($titleAvgRent.getText()+" - "+market);
			
			try {
				if(marketType.equalsIgnoreCase("State")){
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.rentalPropState());
				} else {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.avgRentGreenBlue(marketDetails));
				}
					String header = $avgRentLabel().getText();
					String uiValue = $avgRentBlue().getText();
					String dbValue = dbValues.get(header);
					_helperClass.compareUiDb(header, uiValue, dbValue, node);
			} catch(Exception e) {
				node.log(Status.ERROR, "Exception: "+e);
			}
		}
	}
	
	
	
	
	
	
	/* Compare values of the selected Markets */
	
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
				greenHouseholdIncome("Compare");
				greenAverageProperty("Compare");
				greenAverageRental("Compare");
			}
			
			if(!blueSelection.contains("State Total and Averages")) {
				marketDetails = hc.getBlueDetails();
				blueHouseholdIncome("Compare");
				blueAverageProperty("Compare");
				blueAverageRental("Compare");
			}
			
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
	}
	

	
	
	
	/* Reset Selected Markets in Compare Market Drop Down */
	
	public void resetComparedMarket() {
		hc.resetCompMarket();
	}
}

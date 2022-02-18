package pageMethods;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import objRepository._marketSupplyMetricsPage;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._testData;
import pageUtilities._utils;

public class _marketSupplyMetrics extends _marketSupplyMetricsPage {
	
	ExtentTest test = _base.test;
	ExtentTest node;
	HashMap<String, String> marketDetails;
	_helperClass hc = new _helperClass();
	List<String> comparedMarkets = new LinkedList<>();
	
	
	public _marketSupplyMetrics() {
		super();
		_utils.waitForElementInVisibleByLocator(loader);
		_helperClass.closeOpenWidgets();
		_utils.selectDropDownByVisibleText($widgetsDisplay, "Show Widgets by Category");
		_utils.clickJs($mktSupplyExpand);
		_utils.clickJs($marketSupplyLink);
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.clickJs($addWidget);
		_utils.waitForElementInVisibleByLocator(loader);
	}
	
	
	
	
	
	/* Total Rentable Square Ft/Capita - This Market */
	
	public void thisMarketCapita() {
		HashMap<String, String> dbValues;
		node = test.createNode($titleCapita.getText()+" - This Market");
		
		try {
			dbValues = _databaseUtils.mapStrFl(_testData.queryIns.thisMarketCapita());
			for(int i=1; i<=$noOfColsCapita.size(); i++) {
				try {
					String header = $capitaHeaderLabel(_base.driver, i).getText();
					String uiValue = $capitaValues(_base.driver, 1, i).getText();
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
	
	
	
	
	
	/* Total Rentable Square Ft/Capita - Green Values */
	
	public void greenCapita() {
		if(!hc.getGreenMarket().contains("-- None --")) {
			HashMap<String, String> dbValues;
			node = test.createNode($titleCapita.getText()+" - "+hc.getGreenMarket());
			
			try {
				if(hc.getGreenMarket().contains("National Totals and Averages")){
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.nationalCapita());
				} else {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.greenBlueCapita(marketDetails));
				}
				for(int i=1; i<=3; i++) {
					try {
						String header = $capitaHeaderLabel(_base.driver, i).getText();
						String uiValue = $capitaValues(_base.driver, 2, i).getText();
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
	
	
	
	
	
	/* Total Rentable Square Ft/Capita - Blue Values */
	
	public void blueCapita() {
		if(!hc.getBlueMarket().contains("-- None --")) {
			HashMap<String, String> dbValues;
			node = test.createNode($titleCapita.getText()+" - "+hc.getBlueMarket());
			
			try {
				if(hc.getBlueMarket().contains("State Total and Averages")) {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.stateCapita());
				} else {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.greenBlueCapita(marketDetails));
				}
				for(int i=1; i<=3; i++) { 
					try {
						String header = $capitaHeaderLabel(_base.driver, i).getText();
						String uiValue = $capitaValues(_base.driver, 3, i).getText();
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
	
	
	
	
	
	/* Total Rentable Square Ft/Household - This Market */
	
	public void thisMarketHousehold() {
		HashMap<String, String> dbValues;
		node = test.createNode($titleHousehold.getText()+" - This Market");
		
		try {
			dbValues = _databaseUtils.mapStrFl(_testData.queryIns.thisMarketHousehold());
			for(int i=1; i<=3; i++) {
				try {
					String header = $householdHeaderLabel(_base.driver, i).getText();
					String uiValue = $householdValues(_base.driver, 1, i).getText();
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
	
	
	
	
	
	/* Total Rentable Square Ft/Household - Green Values */
	
	public void greenHousehold() {
		if(!hc.getGreenMarket().contains("-- None --")) {
			HashMap<String, String> dbValues;
			node = test.createNode($titleHousehold.getText()+" - "+hc.getGreenMarket());
			
			try {
				if(hc.getGreenMarket().contains("National Totals and Averages")){
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.nationalHousehold());
				} else {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.greenBlueHoushold(marketDetails));
				}
				for(int i=1; i<=3; i++) {
					try {
						String header = $householdHeaderLabel(_base.driver, i).getText();
						String uiValue = $householdValues(_base.driver, 2, i).getText();
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
	
	
	
	
	
	
	/* Total Rentable Square Ft/Household - Blue Values */
	
	public void blueHousehold() {
		if(!hc.getBlueMarket().contains("-- None --")) {
			HashMap<String, String> dbValues;
			node = test.createNode($titleHousehold.getText()+" - "+hc.getBlueMarket());
			
			try {
				if(hc.getBlueMarket().contains("State Total and Averages")) {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.stateHousehold());
				} else {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.greenBlueHoushold(marketDetails));
				}
				for(int i=1; i<=3; i++) {
					try {
						String header = $householdHeaderLabel(_base.driver, i).getText();
						String uiValue = $householdValues(_base.driver, 3, i).getText();
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
	
	
	
	
	
	
	/* Total Rentable Square Ft/Rental Property - This Market */
	
	public void thisMarketRentalProp() {
		HashMap<String, String> dbValues;
		node = test.createNode($titleRental.getText()+" - This Market");
		
		try {
			dbValues = _databaseUtils.mapStrFl(_testData.queryIns.thisMarketRentalProp());
			for(int i=1; i<=3; i++) {
				try {
					String header = $rentalPropHeaderLabel(_base.driver, i).getText();
					String uiValue = $rentalPropValues(_base.driver, 1, i).getText();
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
	
	
	
	
	
	
	/* Total Rentable Square Ft/Rental Property - Green Values */
	
	public void greenRentalProp() {
		if(!hc.getGreenMarket().contains("-- None --")) {
			HashMap<String, String> dbValues;
			node = test.createNode($titleRental.getText()+" - "+hc.getGreenMarket());
			
			try {
				if(hc.getGreenMarket().contains("National Totals and Averages")){
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.nationalRentProp());
				} else {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.greenBlueRentalProp(marketDetails));
				}
				for(int i=1; i<=3; i++) {
					try {
						String header = $rentalPropHeaderLabel(_base.driver, i).getText();
						String uiValue = $rentalPropValues(_base.driver, 2, i).getText();
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
	
	
	
	
	
	
	/* Total Rentable Square Ft/Rental Property - Blue Values */
	
	public void blueRentalProp() {
		if(!hc.getBlueMarket().contains("-- None --")) {
			HashMap<String, String> dbValues;
			node = test.createNode($titleRental.getText()+" - "+hc.getBlueMarket());
			
			try {
				if(hc.getBlueMarket().contains("State Total and Averages")) {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.stateRentProp());
				} else {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.greenBlueRentalProp(marketDetails));
				}
				for(int i=1; i<=3; i++) {
					try {
						String header = $rentalPropHeaderLabel(_base.driver, i).getText();
						String uiValue = $rentalPropValues(_base.driver, 3, i).getText();
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
				greenCapita();
				greenHousehold();
				greenRentalProp();
			}
			
			if(comparedMarkets.contains(hc.getBlueMarket())==false) {
				marketDetails = hc.getBlueDetails();
				blueCapita();
				blueHousehold();
				blueRentalProp();
			}
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
		
	}
	
	
	
	
	
	/* Reset Selected Markets to compare */
	
	public void resetComparedMarket() {
		hc.resetCompMarket();
	}
	
	
}		
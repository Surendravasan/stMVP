package pageMethods;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import objRepository._demographicsPage;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._testData;
import pageUtilities._utils;

public class _demographics extends _demographicsPage {
	
	ExtentTest test = _base.test;
	ExtentTest node;
	HashMap<String, String> marketDetails;
	_helperClass hc = new _helperClass();
	List<String> comparedMarkets = new LinkedList<>();
	
	
	public _demographics() {
		super();
		_utils.waitForElementInVisibleByLocator(loader);
		_helperClass.closeOpenWidgets();
		_utils.selectDropDownByVisibleText($widgetsDisplay, "Show Widgets by Category");
		_utils.clickJs($demoExpand);
		_utils.clickJs($sizeOfMarketLink);
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.clickJs($addWidget);
		_utils.waitForElementInVisibleByLocator(loader);
	}
	
	
	
	
	
	/* Population/Store - This Market */
	
	public void thisMarketPopulation() {
		HashMap<String, String> dbValues = new HashMap<String, String>();
		node = test.createNode($titlePopulation.getText()+" - This Market");
		try {
			dbValues = _databaseUtils.mapStrFl(_testData.queryIns.popMarket());
			for(int i=1; i<=$noOfColPopulation.size(); i++) {
				String header = $popColLabel(_base.driver, i).getText();
				String uiValue = $popValues(_base.driver, 1, i).getText();
				String dbValue = dbValues.get(header);
				_helperClass.compareUiDb(header, uiValue, dbValue, node);
			}
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
	}
	
	
	
	
	
	/* Population/Store - Green Values */
	
	public void greenPopulation() {
		
		if(!hc.getGreenMarket().contains("-- None --")) {
			HashMap<String, String> dbValues = new HashMap<String, String>();
			node = test.createNode($titlePopulation.getText()+" - "+hc.getGreenMarket());
			
			try {
				if(hc.getGreenMarket().contains("National Totals and Averages")){
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.popNational());
				} else {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.popGreenBlue(marketDetails));
				}
				
				for(int i=1; i<=3; i++) {
					try {
						String header = $popColLabel(_base.driver, i).getText();
						String uiValue = $popValues(_base.driver, 2, i).getText();
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
	
	
	
	
	
	/* Population/Store - Blue Values */
	
	public void bluePopulation() {
		
		if(!hc.getBlueMarket().contains("-- None --")) {
			HashMap<String, String> dbValues = new HashMap<String, String>();
			node = test.createNode($titlePopulation.getText()+" - "+hc.getBlueMarket());
			
			try {
				if(hc.getBlueMarket().contains("State Total and Averages")) {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.popState());
				} else {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.popGreenBlue(marketDetails));
				}
				for(int i=1; i<=3; i++) {
					try {
						String header = $popColLabel(_base.driver, i).getText();
						String uiValue = $popValues(_base.driver, 3, i).getText();
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
	
	
	
	
	
	/* Households/Store - This Market */
	
	public void thisMarketHouseholds() {
		HashMap<String, String> dbValues = new HashMap<String, String>();
		node = test.createNode($titleHousehold.getText()+" - This Market");
		
		try {
			dbValues = _databaseUtils.mapStrFl(_testData.queryIns.houHolMarket());
			for(int i=1; i<=$noOfColHousehold.size(); i++) {
				try {
					String header = $houholColLabel(_base.driver, i).getText();
					String uiValue = $houHolValues(_base.driver, 1, i).getText();
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
	
	
	
	
	
	/* Households/Store - Green Values */
	
	public void greenHouseholds() {
		
		if(!hc.getGreenMarket().contains("-- None --")) {
			HashMap<String, String> dbValues = new HashMap<String, String>();
			node = test.createNode($titleHousehold.getText()+" - "+hc.getGreenMarket());
			
			try {
				if(hc.getGreenMarket().contains("National Totals and Averages")){
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.houHolNational());
				} else {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.houHolGreenBlue(marketDetails));
				}
				for(int i=1; i<=$noOfColHousehold.size(); i++) {
					try {
						String header = $houholColLabel(_base.driver, i).getText();
						String uiValue = $houHolValues(_base.driver, 2, i).getText();
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
		
	
	
	
	
	
	
	/* Households/Store - Blue Values */
	
	public void blueHouseholds() {
		
		if(!hc.getBlueMarket().contains("-- None --")) {
			HashMap<String, String> dbValues = new HashMap<String, String>();
			node = test.createNode($titleHousehold.getText()+" - "+hc.getBlueMarket());
			
			try {
				if(hc.getBlueMarket().contains("State Total and Averages")) {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.houHolState());
				} else {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.houHolGreenBlue(marketDetails));
				}
				for(int i=1; i<=$noOfColHousehold.size(); i++) {
					try {
						String header = $houholColLabel(_base.driver, i).getText();
						String uiValue = $houHolValues(_base.driver, 3, i).getText();
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
	
	
	
	
	
	/*  Rental Properties/Store - This Market */
	
	public void thisMarketRentalProperties() {
		HashMap<String, String> dbValues = new HashMap<String, String>();
		node = test.createNode($titleRental.getText()+" - This Market");
		
		try {
			dbValues = _databaseUtils.mapStrFl(_testData.queryIns.renPropMarket());
			for(int i=1; i<=$noOfColRental.size(); i++) {
				try {
					String header = $renProColLabel(_base.driver, i).getText();
					String uiValue = $renProValues(_base.driver, 1, i).getText();
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
	
	
	
	
	
	/*  Rental Properties/Store - Green Values */
	
	public void greenRentalProperties() {

		if(!hc.getGreenMarket().contains("-- None --")) {
			HashMap<String, String> dbValues = new HashMap<String, String>();
			node = test.createNode($titleRental.getText()+" - "+hc.getGreenMarket());
			
			try {
				if(hc.getGreenMarket().contains("National Totals and Averages")){
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.renPropNational());
				} else {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.rentalGreenBlue(marketDetails));
				}
				for(int i=1; i<=$noOfColRental.size(); i++) {
					String header = $renProColLabel(_base.driver, i).getText();
					String uiValue = $renProValues(_base.driver, 2, i).getText();
					String dbValue = dbValues.get(header);
					_helperClass.compareUiDb(header, uiValue, dbValue, node);
				}
			} catch(Exception e) {
				node.log(Status.ERROR, "Exception: "+e);
			}
		}
	}
		
	
	
	
	
	
	
	/*  Rental Properties/Store - Blue Values */
	
	public void blueRentalProperties() {
		
		if(!hc.getBlueMarket().contains("-- None --")) {
			HashMap<String, String> dbValues = new HashMap<String, String>();
			node = test.createNode($titleRental.getText()+" - "+hc.getBlueMarket());
			
			try {
				if(hc.getBlueMarket().contains("State Total and Averages")) {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.renPropState());
				} else {
					dbValues = _databaseUtils.mapStrFl(_testData.queryIns.rentalGreenBlue(marketDetails));
				}
				for(int i=1; i<=$noOfColRental.size(); i++) {
					try {
						String header = $renProColLabel(_base.driver, i).getText();
						String uiValue = $renProValues(_base.driver, 3, i).getText();
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
	
	
	
	
	
	
	/* Compare values of the selected Markets */
	
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
				greenPopulation();
				greenHouseholds();
				greenRentalProperties();
			}
			
			if(comparedMarkets.contains(hc.getBlueMarket())==false) {
				marketDetails = hc.getBlueDetails();
				bluePopulation();
				blueHouseholds();
				blueRentalProperties();
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

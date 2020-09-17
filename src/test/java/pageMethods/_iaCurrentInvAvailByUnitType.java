package pageMethods;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.support.ui.Select;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import objRepository._iaCurrentInvAvailByUnitTypePage;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._queries;
import pageUtilities._testData;
import pageUtilities._utils;

public class _iaCurrentInvAvailByUnitType extends _iaCurrentInvAvailByUnitTypePage {
	
	ExtentTest test = _base.test;
	ExtentTest node;
	DecimalFormat decimalFormat = new DecimalFormat("0");
	_helperClass hc = new _helperClass();
	HashMap<String, String> marketDetails;
	
	public _iaCurrentInvAvailByUnitType() {
		super();
		_utils.waitForElementInVisibleByLocator(loader);
		_helperClass.closeOpenWidgets();
		_utils.selectDropDownByVisibleText($widgetsDisplay, "Show Widgets by Category");
		_utils.clickJs($inventoryExpand);
		_utils.clickJs($currentInvLink);
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.clickJs($addWidget);
		_utils.waitForElementInVisibleByLocator(loader);
	}
	
	
	
	
	
	/* Current Inventory Availability by Unit Type - This Market */

	public void thisMarketCurrentInventory() {
		node = test.createNode($title.getText()+" - This Market");
		HashMap<String, String> dbValues;
		
		/* query output values not matching for below units */
		List<String> skippedUnits = Arrays.asList("");
		
		try {
			int a=1;
			for(int i=1; i<=$unitList.size(); i++) {
				try {
					String unitName = $unitLabel(_base.driver, i).getText();
					if(skippedUnits.contains(unitName.toUpperCase())==false) {
						String onMarketUi = $unitValue(_base.driver, 1, a).getText().replace(",", "");
						String offMarketUi = $unitValue(_base.driver, 1, a+=1).getText().replace(",", "");
						a+=1;
						
						dbValues = _databaseUtils.getColumnValues(_testData.queryIns.currInvUnitValue(unitName));
						int onMarkDb = Integer.valueOf(dbValues.get("onmarket"));
						int offMarkDb = Integer.valueOf(dbValues.get("offmarket"));
						int total = Integer.valueOf(dbValues.get("total"));
						
						String onMarketDb = "";
						String offMarketDb = "";
						
						if(total!=0) {
							onMarketDb = onMarkDb+"("+decimalFormat.format(((onMarkDb*1.0)/total)*100)+"%)";
							offMarketDb = offMarkDb+"("+decimalFormat.format(((offMarkDb*1.0)/total)*100)+"%)";
						} else {
							onMarketDb = "N/A";
							offMarketDb = "N/A";
						}
						_helperClass.compareUiDb(unitName+" (On Market)"+"@skip@", onMarketUi, onMarketDb, node);
						_helperClass.compareUiDb(unitName+" (Off Market)"+"@skip@", offMarketUi, offMarketDb, node);
					}
				} catch(Exception e) {
					node.log(Status.ERROR, "Exception: "+e);
				}
			}
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
	}
	
	
	
	
	
	
	/* Current Inventory Availability by Unit Type - Green Values */
	
	public void greenCurrentInventory(String marketType) {
		
		HashMap<String, String> dbValues;
		String market = (marketType.equalsIgnoreCase("National")) ? marketType : hc.greenStoreName;
		node = test.createNode($title.getText()+" - "+market);
		
		/* query output values not matching for below units*/
		List<String> skippedUnits = Arrays.asList("");
		
		
			int a=1;
			for(int i=1; i<=$unitList.size(); i++) {
				try {
					String unitName = $unitLabel(_base.driver, i).getText();
					
					String onMarketUi = $unitValue(_base.driver, 2, a).getText().replace(",", "");
					String offMarketUi = $unitValue(_base.driver, 2, a+=1).getText().replace(",", "");
					a+=1;
					if(skippedUnits.contains(unitName.toUpperCase())==false) {
						if(marketType.equalsIgnoreCase("National")){
							dbValues = _databaseUtils.getColumnValues(_testData.queryIns.currInvNational(unitName));
						} else {
							dbValues = _databaseUtils.getColumnValues(_testData.queryIns.currInvGreenBlueVal(unitName, marketDetails));
						}
						int onMarkDb = Integer.valueOf(dbValues.get("onmarket"));
						int offMarkDb = Integer.valueOf(dbValues.get("offmarket"));
						int total = Integer.valueOf(dbValues.get("total"));
						
						String onMarketDb = "";
						String offMarketDb = "";
						
						if(total!=0) {
							onMarketDb = onMarkDb+"("+decimalFormat.format(((onMarkDb*1.0)/total)*100)+"%)";
							offMarketDb = offMarkDb+"("+decimalFormat.format(((offMarkDb*1.0)/total)*100)+"%)";
						} else {
							onMarketDb = "N/A";
							offMarketDb = "N/A";
						}
						_helperClass.compareUiDb(unitName+"@skip@", onMarketUi, onMarketDb, node);
						_helperClass.compareUiDb(unitName+"@skip@", offMarketUi, offMarketDb, node);
					}
				} catch(Exception e) {
						node.log(Status.ERROR, "Exception: "+e);
				}
			}
	}
					
	
	
	
	/* Current Inventory Availability by Unit Type - Blue Values */
	
	public void blueCurrentInventory(String marketType) {
		if(_testData.regId==3 && !marketType.equalsIgnoreCase("State")) {
			HashMap<String, String> dbValues;
			String market = (marketType.equalsIgnoreCase("State")) ? marketType : hc.blueStoreName;
			node = test.createNode($title.getText()+" - "+market);
			
			/* query output values not matching for below units */
			List<String> skippedUnits = Arrays.asList("");
			
			
				int a=1;
				for(int i=1; i<=$unitList.size(); i++) {
					try {
						String unitName = $unitLabel(_base.driver, i).getText();
						if(skippedUnits.contains(unitName.toUpperCase())==false) {
							String onMarketUi = $unitValue(_base.driver, 3, a).getText().replace(",", "");
							String offMarketUi = $unitValue(_base.driver, 3, a+=1).getText().replace(",", "");
							a+=1;
							
							if(marketType.equalsIgnoreCase("State")){
								dbValues = _databaseUtils.getColumnValues(_testData.queryIns.currInvState(unitName));
							} else {
								dbValues = _databaseUtils.getColumnValues(_testData.queryIns.currInvGreenBlueVal(unitName, marketDetails));
							}
							int onMarkDb = Integer.valueOf(dbValues.get("onmarket"));
							int offMarkDb = Integer.valueOf(dbValues.get("offmarket"));
							int total = Integer.valueOf(dbValues.get("total"));
							
							String onMarketDb = "";
							String offMarketDb = "";
							
							if(total!=0) {
								onMarketDb = onMarkDb+"("+decimalFormat.format(((onMarkDb*1.0)/total)*100)+"%)";
								offMarketDb = offMarkDb+"("+decimalFormat.format(((offMarkDb*1.0)/total)*100)+"%)";
							} else {
								onMarketDb = "N/A";
								offMarketDb = "N/A";
							}
							_helperClass.compareUiDb(unitName+"@skip@", onMarketUi, onMarketDb, node);
							_helperClass.compareUiDb(unitName+"@skip@", offMarketUi, offMarketDb, node);
						}
					} catch(Exception e) {
						node.log(Status.ERROR, "Exception: "+e);
					} 
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
				greenCurrentInventory("compare");
			}
			
			if(!blueSelection.contains("State Total and Averages")) {
				marketDetails = hc.getBlueDetails();
				blueCurrentInventory("compare");
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

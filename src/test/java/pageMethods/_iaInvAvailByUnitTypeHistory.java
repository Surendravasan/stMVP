package pageMethods;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import objRepository._iaInvAvailByUnitTypeHistoryPage;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._queries;
import pageUtilities._utils;

public class _iaInvAvailByUnitTypeHistory extends _iaInvAvailByUnitTypeHistoryPage {
	
	ExtentTest test = _base.test;
	ExtentTest node, nchild;
	HashMap<String, String> greenMarketDetails = new HashMap<>();
	HashMap<String, String> blueMarketDetails = new HashMap<>();
	_helperClass hc = new _helperClass();
	List<String> verifiedDateRangeList = new LinkedList<>();
	List<String> noOfUnitsToTest;
	
	
	public _iaInvAvailByUnitTypeHistory() {
		super();
		_utils.waitForElementInVisibleByLocator(loader);
		_helperClass.closeOpenWidgets();
		_utils.selectDropDownByVisibleText($widgetsDisplay, "Show Widgets by Category");
		_utils.clickJs($inventoryExpand);
		_utils.clickJs($inventoryHistoryLink);
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.clickJs($addWidget);
		_utils.waitForElementInVisibleByLocator(loader);
	}
	
	
	
	
	/* Inventory By History Graph */
	
	public void avgRateHistory() {
		dateRange3Months();
		getRandomUnits(3);
		unitsValidation();
		
		setRandomDateRange();
		getRandomUnits(3);
		unitsValidation();
	}
	
	
	
	
	/* select Last 3 Months Date Range */
	
	void dateRange3Months() {
		_utils.selectDropDownByValue($dateRangeDropDown, "1");
		_utils.waitForElementInVisibleByLocator(loader);
		String selectedDateRange = new Select($dateRangeDropDown).getFirstSelectedOption().getText();
		node = test.createNode(selectedDateRange);
	}
	
	
	
	
	/* Random no of units added to the common List  */
	
	void getRandomUnits(int noOfUnits) {
		int unitsToVerify = (noOfUnits<=0 || noOfUnits>$unitTypeLists.size()) ? $unitTypeLists.size() : noOfUnits;
		List<WebElement> webElementsList = new Select($unitTypeDropDown).getOptions();
		
		//list of Units not included in test
		List<String> unitsNotAllowed = Arrays.asList("All Reg", "All CC", "10x15 CC", "10x20 CC", "10x30 CC");
		noOfUnitsToTest = new LinkedList<>();
		 
		for(int i=1; i<=unitsToVerify; i++) {
			try {
				String unit;
				do {
					int randomNo = _utils.getRandNumber(webElementsList.size())-1;
					unit = webElementsList.get(randomNo).getText();
				} while(unitsNotAllowed.contains(unit)==true || noOfUnitsToTest.contains(unit)==true);
				noOfUnitsToTest.add(unit);
			} catch (Exception e) {
				node.log(Status.ERROR, "Exception: "+e);
			}
		}
	}

	
	
	
	/* Select Unit from common List and Verify */
	
	void unitsValidation() {
		
		for(int k=0; k<noOfUnitsToTest.size(); k++) {
			String unitName = noOfUnitsToTest.get(k);
			nchild = node.createNode(unitName);
			_utils.selectDropDownByVisibleText($unitTypeDropDown, unitName);
			System.out.println(unitName);
			
			try {
				int isNoGraph = $noGraphData.size();
				if(isNoGraph!=1) {
					for(int i=1; i<=1; i++) {
						String dateValue, uiMarketValue;
						int count = 0;
						do {
							count++;
							do {
								mouseAction();
								dateValue = $dateValue.getText();
							} while(dateValue.equalsIgnoreCase("- - -"));
							uiMarketValue = $uiMarketValue.getText().replace("[", "").replace("]", "");
							if(uiMarketValue.equals("N/A")!=true) {
								count=3;
							}
						} while(count!=3);
						
						String dateLabel = $dateLabel.getText();
						nchild.log(Status.INFO, MarkupHelper.createLabel(dateLabel+dateValue, ExtentColor.BLUE));
						thisMarket(uiMarketValue, unitName, dateValue);
						greenValue(unitName, dateValue);
						blueValue(unitName, dateValue);
						
					}
				} else {
					nchild.log(Status.INFO, "No Data Found for this Unit Type");
				}
			} catch (Exception e) {
				nchild.log(Status.ERROR, "Exception: "+e);
			}
		}
	}
	
	
	
	
	/* Validate Market value for specific UnitName and Date */
	
	void thisMarket(String marketValueUi, String unitName, String dateValue) {
		
		String marketLabel = $marketLabel.getText();
		String marketValueDb = "";  //_databaseUtils.getStringValue(_queries.thisMarketAvgRateHistory(unitName, dateValue));
		_helperClass.compareUiDb(marketLabel, marketValueUi, marketValueDb, nchild);
	}
	
	
	
	
	/* Validate Green value for specific UnitName and Date */
	
	void greenValue(String unitName, String dateValue) {
		String nationalLabel = $nationalLabel.getText();
		String nationalValue = $nationalValue.getText().replace("[", "").replace("]", "");
		String dbGreenValue;
		if(greenMarketDetails.isEmpty()==true) {
			dbGreenValue = "";  //_databaseUtils.getStringValue(_queries.nationalAvgRateHistory(unitName, dateValue)); 
		} else {
			dbGreenValue = "";  //_databaseUtils.getStringValue(_queries.greenBlueAvgRateHistory(unitName, dateValue, greenMarketDetails));
		}
		_helperClass.compareUiDb(nationalLabel, nationalValue, dbGreenValue, nchild);
	}
	
	
	
	
	/* Validate Blue value for specific UnitName and Date */
	
	void blueValue(String unitName, String dateValue) {
		String stateLabel = $stateLabel.getText();
		String stateValue = $stateValue.getText().replace("[", "").replace("]", "");
		String dbBlueValue;
		if(blueMarketDetails.isEmpty()==true) {
			dbBlueValue = "";  //_databaseUtils.getStringValue(_queries.stateAvgRateHistory(unitName, dateValue)); 
		} else {
			dbBlueValue = "";  //_databaseUtils.getStringValue(_queries.greenBlueAvgRateHistory(unitName, dateValue, blueMarketDetails));;
		}
		_helperClass.compareUiDb(stateLabel, stateValue, dbBlueValue, nchild);
	}
	
	
	
	
	/* Random mouse movement within the specified WebElement */
	
	void mouseAction() {
		int eleXNegBorder = -535;
		int eleXPosBorder = 535;
		Random rand = new Random();
		int randomXPoint = rand.nextInt(eleXPosBorder-eleXNegBorder)+eleXNegBorder;
		
        Actions act = new Actions(_base.driver);
		act.moveToElement($graph, randomXPoint, 0);
		act.build().perform();
	}
	
	
	
	
	/* Select random Date Range from the drop down */
	
	void setRandomDateRange() {
		try {
			List<WebElement> webElementsList = new Select($dateRangeDropDown).getOptions();
			if(webElementsList.size()==verifiedDateRangeList.size()) {
				verifiedDateRangeList.clear();
			}
			verifiedDateRangeList.add("Last 3 months");
			String dateRange;
			do {
				int randomNo = _utils.getRandNumber(webElementsList.size())-1;
				dateRange = webElementsList.get(randomNo).getText();
			} while(verifiedDateRangeList.contains(dateRange)==true);
			
			verifiedDateRangeList.add(dateRange);
			_utils.selectDropDownByVisibleText($dateRangeDropDown, dateRange);
			_utils.waitForElementInVisibleByLocator(loader);
			String selectedDateRange = new Select($dateRangeDropDown).getFirstSelectedOption().getText();
			System.out.println(selectedDateRange);
			node = test.createNode(selectedDateRange);
		} catch (Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
	}
	
	
	
	
	/* Select Markets for comparison and verify the values */
	
	public void compareMarket() {
		node = test.createNode("Compare Selected Markets");
		try {
			hc.compareMarket();
			
			node.log(Status.INFO, MarkupHelper.createLabel(hc.greenStoreName, ExtentColor.GREEN));
			node.log(Status.INFO, MarkupHelper.createLabel(hc.blueStoreName, ExtentColor.BLUE));
			
			String greenSelection = new Select($greenDropDown).getFirstSelectedOption().getText();
			String blueSelection = new Select($blueDropDown).getFirstSelectedOption().getText();
			
			if(!greenSelection.contains("National Totals and Averages") || !blueSelection.contains("State Total and Averages")) {
				greenMarketDetails = hc.getGreenDetails();
				blueMarketDetails = hc.getBlueDetails();
				dateRange3Months();
				getRandomUnits(3);
				unitsValidation();

				setRandomDateRange();
				getRandomUnits(3);
				unitsValidation();
			}
			
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
	}
	
	
	
	
	/* Reset selected markets to compare */
	
	public void resetComparedMarket() {
		hc.resetCompMarket();
	}
	
}

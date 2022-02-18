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
import objRepository._prAverageRatesHistoryPage;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._testData;
import pageUtilities._utils;

public class _prAverageRatesHistory extends _prAverageRatesHistoryPage {
	
	ExtentTest test = _base.test;
	ExtentTest node, nchild;
	HashMap<String, String> greenMarketDetails = new HashMap<>();
	HashMap<String, String> blueMarketDetails = new HashMap<>();
	_helperClass hc = new _helperClass();
	List<String> verifiedDateRangeList = new LinkedList<>();
	List<String> noOfUnitsToTest;
	List<String> comparedMarkets = new LinkedList<>();
	
	
	public _prAverageRatesHistory() {
		super();
		_utils.waitForElementInVisibleByLocator(loader);
		_helperClass.closeOpenWidgets();
		_utils.selectDropDownByVisibleText($widgetsDisplay, "Show Widgets by Category");
		_utils.clickJs($pricingRentalExpand);
		_utils.clickJs($avgRateHistoryLink);
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.clickJs($addWidget);
		_utils.waitForElementInVisibleByLocator(loader);
	}
	
	
	
	
	/* Average Rates History Graph */
	
	public void avgRateHistory() {
		dateRange3Months();
		getRandomUnits(8);
		unitsValidation(10);
		
//		setRandomDateRange();
//		getRandomUnits(3);
//		unitsValidation(10);
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
		List<String> unitsNotAllowed = Arrays.asList("");
		noOfUnitsToTest = new LinkedList<>();
		 
		for(int i=1; i<=unitsToVerify; i++) {
			try {
				String unit;
				do {
					int randomNo = _utils.getRandNumber(webElementsList.size())-1;
					unit = webElementsList.get(randomNo).getText();
				} while(unitsNotAllowed.contains(unit.toUpperCase())==true || noOfUnitsToTest.contains(unit)==true);
				noOfUnitsToTest.add(unit);
			} catch (Exception e) {
				node.log(Status.ERROR, "Exception: "+e);
			}
		}
	}

	
	
	
	/* Select Unit from common List locate the mouse point */
	
	void unitsValidation(int noOfValuesToCheck) {
		
		for(int k=0; k<noOfUnitsToTest.size(); k++) {
			String unitName = noOfUnitsToTest.get(k);
			nchild = node.createNode(unitName);
			_utils.selectDropDownByVisibleText($unitTypeDropDown, unitName);
			System.out.println(unitName);
			
			try {
				int isNoGraph = $noGraphData.size();
				if(isNoGraph!=1) {
					for(int i=1; i<=noOfValuesToCheck; i++) {
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
						
						System.out.println(dateValue);
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
		String marketValueDb = _databaseUtils.getStringValue(_testData.queryIns.thisMarketAvgRateHistory(unitName, dateValue));
		_helperClass.compareUiDb(marketLabel, marketValueUi, marketValueDb, nchild);
	}
	
	
	
	
	/* Validate Green value for specific UnitName and Date */
	
	void greenValue(String unitName, String dateValue) {
		if(!hc.getGreenMarket().contains("-- None --")) {
			String greenMarketLabel = $nationalLabel.getText();
			String greenMarketValue = $nationalValue.getText().replace("[", "").replace("]", "");
			String dbGreenMarketValue;
//			if(greenMarketDetails.isEmpty()==true) {
			if(hc.getGreenMarket().contains("National Totals and Averages")){
				dbGreenMarketValue = _databaseUtils.getStringValue(_testData.queryIns.nationalAvgRateHistory(unitName, dateValue)); 
			} else {
				dbGreenMarketValue = _databaseUtils.getStringValue(_testData.queryIns.greenBlueAvgRateHistory(unitName, dateValue, greenMarketDetails));
			}
			_helperClass.compareUiDb(greenMarketLabel, greenMarketValue, dbGreenMarketValue, nchild);
		}
	}
	
	
	
	
	/* Validate Blue value for specific UnitName and Date */
	
	void blueValue(String unitName, String dateValue) {
		if(!hc.getBlueMarket().contains("-- None --")) {
			String blueMarketLabel = $stateLabel.getText();
			String blueMarketValue = $stateValue.getText().replace("[", "").replace("]", "");
			String dbBlueMarketValue;
//			if(blueMarketDetails.isEmpty()==true) {
			if(hc.getBlueMarket().contains("State Total and Averages")) {
				dbBlueMarketValue = _databaseUtils.getStringValue(_testData.queryIns.stateAvgRateHistory(unitName, dateValue)); 
			} else {
				dbBlueMarketValue = _databaseUtils.getStringValue(_testData.queryIns.greenBlueAvgRateHistory(unitName, dateValue, blueMarketDetails));;
			}
			_helperClass.compareUiDb(blueMarketLabel, blueMarketValue, dbBlueMarketValue, nchild);
		}
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
		
		comparedMarkets.add("-- None --");
		comparedMarkets.add(hc.getGreenMarket());
		comparedMarkets.add(hc.getBlueMarket());
		
		try {
			hc.compareMarket();
			
			node.log(Status.INFO, MarkupHelper.createLabel(hc.greenStoreName, ExtentColor.GREEN));
			node.log(Status.INFO, MarkupHelper.createLabel(hc.blueStoreName, ExtentColor.BLUE));
			
//			if(!greenSelection.contains("National Totals and Averages") || !blueSelection.contains("State Total and Averages")) {
			if((comparedMarkets.contains(hc.getGreenMarket())==false) || (comparedMarkets.contains(hc.getBlueMarket())==false)) {
				greenMarketDetails = hc.getGreenDetails();
				blueMarketDetails = hc.getBlueDetails();
				dateRange3Months();
				getRandomUnits(8);
				unitsValidation(10);

//				setRandomDateRange();
//				getRandomUnits(8);
//				unitsValidation(10);
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

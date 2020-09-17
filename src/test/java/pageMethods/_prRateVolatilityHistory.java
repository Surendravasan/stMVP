package pageMethods;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import objRepository._prRateVolatilityHistoryPage;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._testData;
import pageUtilities._utils;

public class _prRateVolatilityHistory extends _prRateVolatilityHistoryPage {
	
	ExtentTest test = _base.test;
	ExtentTest node, nchild;
	HashMap<String, String> greenMarketDetails = new HashMap<>();
	HashMap<String, String> blueMarketDetails = new HashMap<>();
	_helperClass hc = new _helperClass();
	List<String> verifiedDateRangeList = new LinkedList<>();
	List<String> noOfUnitsToTest;
	
	
	public _prRateVolatilityHistory() {
		super();
		_utils.waitForElementInVisibleByLocator(loader);
		_helperClass.closeOpenWidgets();
		_utils.selectDropDownByVisibleText($widgetsDisplay, "Show Widgets by Category");
		_utils.clickJs($pricingRentalExpand);
		_utils.clickJs($rateVolatilityHistoryLink);
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.clickJs($addWidget);
		_utils.waitForElementInVisibleByLocator(loader);
	}
	
	
	
	
	/* Average Rates History Graph */
	
	public void rateVolatilityHistory() {
		dateRange3Months();
		getRandomUnits(8);
		unitsValidation();
		
//		setRandomDateRange();
//		getRandomUnits(3);
//		unitsValidation();
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

	
	
	
	/* Select Unit from common List and Validate */
	
	void unitsValidation() {
		
		for(int k=0; k<noOfUnitsToTest.size(); k++) {
			try {
				String unitName = noOfUnitsToTest.get(k);
				nchild = node.createNode(unitName);
				_utils.selectDropDownByVisibleText($unitTypeDropDown, unitName);
				System.out.println(unitName);
				for(int i=1; i<=1; i++) {
					thisMarket(unitName);
					greenValue(unitName);
					
					String blueSelection = new Select($blueDropDown).getFirstSelectedOption().getText();
					if(!blueSelection.contains("-- None --")) {
						blueValue(unitName);
					}
				}
			} catch (Exception e) {
				nchild.log(Status.ERROR, "Exception: "+e);
			}
		}
	}
	
	
	
	
	/* Verify this Market Values */
	
	void thisMarket(String unitName) {
		try {
			int mrandom = _utils.getRandNumber($noOfMonthsList.size());
			
			do {
				_utils.moveToElement(marketValue(_base.driver, mrandom));
				_utils.waitForElementVisible($tooltip(_base.driver, $toolTipsSize.size()));
			} while($toolTipsSize.size()==0);
			
			String uiValue = $tooltip(_base.driver, $toolTipsSize.size()).getText().replace("Volatility: ", "");
			System.out.println(uiValue);
			String uiMonthYear[] = monthValue(_base.driver, mrandom).getText().split("'");
			_utils.clickAction($legend);
			
			LinkedList<String>list = _utils.getFirstLastDate(uiMonthYear[0], uiMonthYear[1]);
			String dbValue = _databaseUtils.getStringValue(_testData.queryIns.marketRateVolatilityHistory(unitName, list.getFirst(), list.getLast()));
			_helperClass.compareUiDb("This Market ("+uiMonthYear[0]+"-"+uiMonthYear[1]+")", uiValue, dbValue, nchild);
		} catch (Exception e) {
			node.log(Status.ERROR, e);
		}
	}
	
	
	
	
	/* Verify Green Values */
	
	void greenValue(String unitName) {
		try {
			String header = "";
			int nrandom = _utils.getRandNumber($noOfMonthsList.size());
			
			do {
				System.out.println();
				_utils.moveToElement(greenValue(_base.driver, nrandom));
				_utils.waitForElementVisible($tooltip(_base.driver, $toolTipsSize.size()));
			} while($toolTipsSize.size()==0);
			
			String greenValue = $tooltip(_base.driver, $toolTipsSize.size()).getText().replace("Volatility: ", "");
			System.out.println(greenValue);
			String uiMonthYear[] = monthValue(_base.driver, nrandom).getText().split("'");
			_utils.clickAction($legend);
			
			LinkedList<String>list = _utils.getFirstLastDate(uiMonthYear[0].trim(), uiMonthYear[1].trim());
			String dbValue = "";
			if(greenMarketDetails.isEmpty()==true) {
				header = "National";
				dbValue = _databaseUtils.getStringValue(_testData.queryIns.nationalRateVolatilityHistory(unitName, list.getFirst(), list.getLast()));
			} else {
				header = "Green Market ";
				dbValue = _databaseUtils.getStringValue(_testData.queryIns.greenBlueRateVolatilityHistory(unitName, list.getFirst(), list.getLast(), greenMarketDetails));
			}
			_helperClass.compareUiDb(header+" ("+uiMonthYear[0]+"-"+uiMonthYear[1]+")", greenValue, dbValue, nchild);
		} catch (Exception e) {
			node.log(Status.ERROR, e);
		}
	}
	
	
	
	
	/* Verify Blue Values */
	
	void blueValue(String unitName) {
//		if(_testData.regId==3 && !marketType.equalsIgnoreCase("State")) {
			try {
				String header = "";
				int srandom = _utils.getRandNumber($noOfMonthsList.size());
				
				do {
					_utils.moveToElement(blueValue(_base.driver, srandom));
					_utils.waitForElementVisible($tooltip(_base.driver, $toolTipsSize.size()));
				} while($toolTipsSize.size()==0);
				
				
				String blueValue = $tooltip(_base.driver, $toolTipsSize.size()).getText().replace("Volatility: ", "");
				System.out.println(blueValue);
				String uiMonthYear[] = monthValue(_base.driver, srandom).getText().split("'");
				_utils.clickAction($legend);
				
				LinkedList<String>list = _utils.getFirstLastDate(uiMonthYear[0].trim(), uiMonthYear[1].trim());
				String dbValue = "";
				if(blueMarketDetails.isEmpty()==true) {
					header = "State";
					dbValue = _databaseUtils.getStringValue(_testData.queryIns.stateRateVolatilityHistory(unitName, list.getFirst(), list.getLast()));
				} else {
					header = "Blue Market ";
					dbValue = _databaseUtils.getStringValue(_testData.queryIns.greenBlueRateVolatilityHistory(unitName, list.getFirst(), list.getLast(), blueMarketDetails));
				}
				_helperClass.compareUiDb(header+" ("+uiMonthYear[0]+"-"+uiMonthYear[1]+")", blueValue, dbValue, nchild);
			} catch (Exception e) {
				node.log(Status.ERROR, e);
			}
//		}
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
				getRandomUnits(8);
				unitsValidation();

//				setRandomDateRange();
//				getRandomUnits(3);
//				unitsValidation();
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

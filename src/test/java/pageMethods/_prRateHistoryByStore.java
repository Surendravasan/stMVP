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
import objRepository._prRateHistoryByStorePage;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._testData;
import pageUtilities._utils;

public class _prRateHistoryByStore extends _prRateHistoryByStorePage {
	
	ExtentTest test = _base.test;
	ExtentTest node1, node2, node3;
	HashMap<String, String> greenMarketDetails = new HashMap<>();
	HashMap<String, String> blueMarketDetails = new HashMap<>();
	_helperClass hc = new _helperClass();
	List<String> verifiedDateRangeList = new LinkedList<>();
	List<String> noOfUnitsToTest;
	List<Integer> userStoreIdList = new LinkedList<>();
	int compOneStoreId = 0;
	int compTwoStoreId = 0;
	int compThreeStoreId = 0;
	
	
	public _prRateHistoryByStore() {
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
	
	public void rateHistoryByStore() {
		setPremiumMenu();
		setCompetitorStoreOne();
		setCompetitorStoreTwo();
		setCompetitorStoreThree();
		
		dateRange3Months();
		getRandomUnits(3);
		unitsValidation(10);
		
//		setRandomDateRange();
//		getRandomUnits(3);
//		unitsValidation(10);
		
		setValueMenu();
		dateRange3Months();
		getRandomUnits(3);
		unitsValidation(10);
		
//		setRandomDateRange();
//		getRandomUnits(3);
//		unitsValidation(10);
	}
	
	
	
	
	/* select Last 3 Months Date Range */
	
	void dateRange3Months() {
		_utils.selectDropDownByValue($dateRangeDropDown, "1");
		_utils.waitForElementInVisibleByLocator(loader);
	}
	
	
	
	
	/* Set drop-down to Premium */
	
	void setPremiumMenu() {
		_utils.selectDropDownByValue($menuDropDown, "1");
		_utils.waitForElementInVisibleByLocator(loader);
	}
	
	
	
	/* Set drop-down the Value */
	
	void setValueMenu() {
		_utils.selectDropDownByValue($menuDropDown, "2");
		_utils.waitForElementInVisibleByLocator(loader);
		
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
				test.log(Status.ERROR, "Exception: "+e);
			}
		}
	}
	
	
	
	
	/* Add filter values in the Report */

	void reportMainNode(ExtentTest node) {
		String menu = new Select($menuDropDown).getFirstSelectedOption().getText();
		String dateRange = new Select($dateRangeDropDown).getFirstSelectedOption().getText();
		String competitor1 = new Select($compOneDDL).getFirstSelectedOption().getText();
		String competitor2 = new Select($compTwoDDL).getFirstSelectedOption().getText();
		String competitor3 = new Select($compThreeDDL).getFirstSelectedOption().getText();
		node.log(Status.INFO, menu);
		node.log(Status.INFO, dateRange);
		node.log(Status.INFO, competitor1);
		node.log(Status.INFO, competitor2);
		node.log(Status.INFO, competitor3);
	}
	
	
	
	
	/* Select Unit from common List locate the mouse point */
	
	void unitsValidation(int noOfUnits) {
		
		node1 = test.createNode("Unit Validation Details");
		reportMainNode(node1);
		
		for(int k=0; k<noOfUnitsToTest.size(); k++) {
			String unitName = noOfUnitsToTest.get(k);
			node2 = node1.createNode(unitName);
			
			try {
				
			String valueType = new Select($menuDropDown).getFirstSelectedOption().getText();
			_utils.selectDropDownByVisibleText($unitTypeDropDown, unitName);
			_utils.waitForElementInVisibleByLocator(loader);
			System.out.println(unitName);
			
			int isNoGraph = $noGraphData.size();
			System.out.println();
			if(isNoGraph!=1) {
				for(int i=1; i<=noOfUnits; i++) {
					String dateValue;
					do {
						mouseAction();
						dateValue = $dateValue.getText();
					} while(dateValue.equalsIgnoreCase("- - -"));
					String dateLabel = $dateLabel.getText();
					node2.log(Status.INFO, MarkupHelper.createLabel(dateLabel+dateValue, ExtentColor.BLUE));
					HashMap<String, String> hm = _databaseUtils.mapStrFl(_testData.queryIns.marketRateHistory(valueType, unitName, dateValue));

					for(int a=1; a<=$marketStoreList.size(); a++) {
						try {
							String uiLabel = $marketStoreLabel(_base.driver, a).getText();
							String uiValue = $marketStoreValue(_base.driver, a).getText();
							String dbValue = "", symbol = "";
							switch(a) {
							case 1:
								dbValue =  hm.get("low");
								break;
							case 2:
								dbValue =  hm.get("average");
								break;
							case 3:
								dbValue =  hm.get("high");
								break;
							case 4:
								symbol = $compStoreColor(_base.driver, a).getAttribute("style");
								System.out.println(symbol);
								
								dbValue =  _databaseUtils.getStringValue(_testData.queryIns.storeRateHistory(valueType, competitorMatching(symbol), unitName, dateValue));
								break;
							case 5:
								symbol = $compStoreColor(_base.driver, a).getAttribute("style");
								System.out.println(symbol);
								dbValue =  _databaseUtils.getStringValue(_testData.queryIns.storeRateHistory(valueType, competitorMatching(symbol), unitName, dateValue));
								break;
							case 6:
								symbol = $compStoreColor(_base.driver, a).getAttribute("style");
								System.out.println(symbol);
								dbValue =  _databaseUtils.getStringValue(_testData.queryIns.storeRateHistory(valueType, competitorMatching(symbol), unitName, dateValue));
								break;
							}
							_helperClass.compareUiDb(uiLabel, uiValue, dbValue, node2);
						} catch (Exception e) {
							node2.log(Status.ERROR, "Exception: "+e);
						}
					}
				}
			} else {
				node2.log(Status.INFO, "No Data Found for this Unit Type");
			}
			} catch (Exception e) {
				node2.log(Status.ERROR, "Exception: "+e);
			}
		}
	}
	
	
	
	
	/* Get store id with text color */
	
	int competitorMatching(String symbol) {
		int competitorStoreid = 0;
		if(symbol.endsWith("rgb(31, 223, 226);")) {
			competitorStoreid = compOneStoreId; 
		} else if(symbol.endsWith("rgb(32, 41, 228);")) {
			competitorStoreid = compTwoStoreId; 
		} else if(symbol.endsWith("rgb(239, 54, 165);")) {
			competitorStoreid = compThreeStoreId; 
		}
		return competitorStoreid;
	}
	
	
	
	
	/* Set Competitor Store One */
	
	void setCompetitorStoreOne() {
		int random = 0;
		int noOfStoresAvail = $compOneDDLList.size();
		boolean available;
		if(noOfStoresAvail>1) {
			do {
				random = _utils.getRandNumber($compOneDDLList.size());
				compOneStoreId = Integer.valueOf($compOneDDLOption(_base.driver, random).getAttribute("value"));
				available = userStoreIdList.contains(compOneStoreId);
			} while(available==true);
			_utils.selectDropDownByValue($compOneDDL, String.valueOf(compOneStoreId));
			_utils.waitForElementInVisibleByLocator(loader);
			userStoreIdList.add(compOneStoreId);
		}
		String selectedStore = new Select($compOneDDL).getFirstSelectedOption().getText();
		System.out.println(selectedStore);
		System.out.println(compOneStoreId);
	}
	
	
	
	/* Set Competitor Store Two */
	
	void setCompetitorStoreTwo() {
		int random = 0;
		int noOfStoresAvail = $compTwoDDLList.size();
		boolean available;
		if(noOfStoresAvail>0) {
			do {
				random = _utils.getRandNumber($compTwoDDLList.size());
				compTwoStoreId = Integer.valueOf($compTwoDDLOption(_base.driver, random).getAttribute("value"));
				available = userStoreIdList.contains(compTwoStoreId);
			} while(available==true);
			_utils.selectDropDownByValue($compTwoDDL, String.valueOf(compTwoStoreId));
			_utils.waitForElementInVisibleByLocator(loader);
			userStoreIdList.add(compTwoStoreId);
		}
		
		String selectedStore = new Select($compTwoDDL).getFirstSelectedOption().getText();
		System.out.println(selectedStore);
		System.out.println(compTwoStoreId);
	}
	
	
	
	/* Set Competitor Store Three */
	
	void setCompetitorStoreThree() {
		int random = 0;
		int noOfStoresAvail = $compThreeDDLList.size();
		boolean available;
		if(noOfStoresAvail>0) {
			do {
				random = _utils.getRandNumber($compThreeDDLList.size());
				compThreeStoreId = Integer.valueOf($compThreeDDLOption(_base.driver, random).getAttribute("value"));
				available = userStoreIdList.contains(compThreeStoreId);
			} while(available==true);
		}
		_utils.selectDropDownByValue($compThreeDDL, String.valueOf(compThreeStoreId));
		_utils.waitForElementInVisibleByLocator(loader);
		userStoreIdList.add(compThreeStoreId);
		String selectedStore = new Select($compThreeDDL).getFirstSelectedOption().getText();
		System.out.println(selectedStore);
		System.out.println(compThreeStoreId);
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
		} catch (Exception e) {
			test.log(Status.ERROR, "Exception: "+e);
		}
	}
}

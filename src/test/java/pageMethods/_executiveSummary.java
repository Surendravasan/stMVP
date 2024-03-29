package pageMethods;

import org.jsoup.Jsoup;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import objRepository._executiveSummaryPage;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._testData;
import pageUtilities._utils;

public class _executiveSummary extends _executiveSummaryPage {
	
	ExtentTest test = _base.test;
	ExtentTest node;
	
	
	public _executiveSummary() {
		super();
		_utils.waitForElementInVisibleByLocator(loader);
		_helperClass.closeOpenWidgets();
		_utils.clickJs($execSummaryLink);
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.clickJs($addWidget);
		_utils.waitForElementInVisibleByLocator(loader);
	}
	
	
	
	
	/* No of Stores in Market */
	
	public void noOfStores() {
		node = test.createNode("No of Stores in Market");
		
		try {
			int count = $noOfStoresList.size();
			for(int i=1; i<=count; i++) {
				String uiLabel = $noOfStoresLabel(_base.driver, i).getText();
				String uiValue = $noOfStoresValue(_base.driver, i).getText();
				String dbValue = "";
				if(uiLabel.toLowerCase().contains("market")) {
					dbValue = _databaseUtils.getStringValue(_testData.queryIns.execSumThisMarket());
				} else if(uiLabel.toLowerCase().contains("national")) {
					dbValue = _databaseUtils.getStringValue(_testData.queryIns.execSumNational());
				} else if(uiLabel.toLowerCase().contains(_testData.queryIns.getStateFullName().toLowerCase())) {
					dbValue = _databaseUtils.getStringValue(_testData.queryIns.execSumState());
				}
				_helperClass.compareUiDb(uiLabel, uiValue, dbValue, node);
			}
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
	}
	
	
	
	
	/* Current Supply to Market */
	
	public void currentSupply() {
		node = test.createNode("Current Supply in Market");
		
		try {
			_utils.waitClick($currentSupplyLink);
			String marComp = Jsoup.parse($inventoryText.getText()).text();
			String getValues[] = marComp.replace(". ","").replaceAll("[A-z ,()']", "").split("%");
			
			Float marVal = Float.valueOf(getValues[0]);
			Float natVal = Float.valueOf(getValues[1]);
			Float staVal = 0.0f;
			
			if(_testData.regId==1) {
				staVal = Float.valueOf(getValues[2]);
			}
			

			String expRes = "";
			String statResultUi = "";
			
			if(_testData.regId==1) {
				if(marVal > natVal && marVal > staVal) {
					expRes = _uiConstants.currSupHighUS;
				} else if(marVal < natVal && marVal < staVal) {
					expRes = _uiConstants.currSupLowUS;
				} else if(marVal > natVal && marVal <= staVal || marVal < natVal && marVal >= staVal) {
					expRes = _uiConstants.currSupMixUS;
				} else if(marVal >= natVal && marVal < staVal || marVal <= natVal && marVal > staVal) {
					expRes = _uiConstants.currSupMixUS;
				} else if(marVal.equals(natVal) && marVal.equals(staVal)) {
					expRes = _uiConstants.currSupNeuUS;
				}
				statResultUi = marComp.substring(marComp.indexOf("Therefore, "));
				
			} else if(_testData.regId==3) {
				if(marVal > natVal) {
					expRes = _uiConstants.currSupHighUK;
				} else if(marVal < natVal) {
					expRes = _uiConstants.currSupLowUK;
				} else if(marVal.equals(natVal) && marVal.equals(staVal)) {
					expRes = _uiConstants.currSupNeuUK;
				}
				statResultUi = marComp.substring(marComp.indexOf("comparing against national average, "));
			}
			
			
			_helperClass.compareUiDb("Market Comparison"+"@skip@", statResultUi, expRes, node);
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
	}
	
	
	
	
	/* Rate Trends */
	
	public void rateTrend() {
		node = test.createNode("Rate Trends");
		System.out.println();
		try {
			_utils.waitClick($rateTrendsLink);
			String rateTrendsUi = Jsoup.parse($rateTrendsText.getText()).text();
			String rateTrendsValues[] = rateTrendsUi.replace("3 months", "").replace(". ","").replaceAll("[A-z ,()']", "").split("%");
			Float marketRate = Float.valueOf(rateTrendsValues[0]);
			Float nationalRate = Float.valueOf(rateTrendsValues[1]);
			Float stateRate = 0.0f;
			if(_testData.regId==1) {
				stateRate = Float.valueOf(rateTrendsValues[2]);
			}

			String marketTrend = (marketRate>0.0) ? "up" : "low";
			String demand = (marketRate>0.0) ? "increased" : "decreased";
			String stateTrend = "";
			if(_testData.regId==1) {
				stateTrend = (stateRate>0.0) ? "up" : "down";
			}
			
			String fullStateName = _testData.queryIns.getStateFullName();
			
			String rateTrendsExpected = "";
			if(_testData.regId==1) {
				rateTrendsExpected = _uiConstants.rateTrendUS(marketTrend, marketRate, demand, nationalRate, fullStateName, stateTrend, stateRate);
			} else if(_testData.regId==3) {
				rateTrendsExpected = _uiConstants.rateTrendUK(marketTrend, marketRate, demand, nationalRate);
			}
			
			
			_helperClass.compareUiDb("Rate Trends"+"@skip@", rateTrendsUi, rateTrendsExpected, node);
			
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
	}
	
	
	
	
	/* New Developments */
	
	public void newDevelopments() {
		node = test.createNode("New Developments");
		
		try {
			_utils.waitClick($newDevelopmentsLink);
			String marketDevelopmentsUi = Jsoup.parse($newDevelopmentsText.getText()).text();
			String marketDevelopmentsCount = _databaseUtils.getStringValue(_testData.queryIns.execSumMarketDevs());
			String marketDevelopmentsExp = _uiConstants.marketNewDevelopments(Integer.valueOf(marketDevelopmentsCount));
			_helperClass.compareUiDb("Known Developments"+"@skip@", marketDevelopmentsUi, marketDevelopmentsExp, node);
			
			String totalDevelopmentsUi = Jsoup.parse($totalDevelopmentsText.getText()).text();
			String totalDevelopmentsExp = _uiConstants.totalNewDevelopments(Integer.valueOf(_databaseUtils.getStringValue(_testData.queryIns.execSumTotalDevs()))); 
			_helperClass.compareUiDb("Total Developments"+"@skip@", totalDevelopmentsUi, totalDevelopmentsExp, node);
			
		} catch(Exception e) {
			node.log(Status.ERROR, "Exception: "+e);
		}
	}
	
}

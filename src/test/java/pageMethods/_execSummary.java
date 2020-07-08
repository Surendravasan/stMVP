package pageMethods;

import org.jsoup.Jsoup;

import com.aventstack.extentreports.ExtentTest;

import objRepository._execSummaryPage;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._queries;
import pageUtilities._utils;

public class _execSummary extends _execSummaryPage {
	
	ExtentTest test = _base.test;
	ExtentTest node;
	
	
	public _execSummary() {
		super();
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.closeOpenWidgets();
		_utils.waitClick($execSummaryLink);
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.waitClick($addWidget);
		_utils.waitForElementInVisibleByLocator(loader);
	}
	
	public void noOfStores() {
		node = test.createNode("No of Stores in Market");
		int count = $noOfStoresList.size();
		for(int i=1; i<=count; i++) {
			String uiLabel = $noOfStoresLabel(_base.driver, i).getText();
			String uiValue = $noOfStoresValue(_base.driver, i).getText();
			String dbValue = _databaseUtils.getStringValue(_queries.execSummNoOfStores(i));
			_utils.compareUiDb(uiLabel, uiValue, dbValue, node);
		}
	}
	
	
	public void currentSupply() {
		_utils.waitClick($currentSupplyLink);
		
		String s = Jsoup.parse($inventoryText.getText()).text();
		String a[] = s.replace(". ","").replaceAll("[A-z ,()']", "").split("%");
		Float marketValue = Float.valueOf(a[0]);
		System.out.println(marketValue);
		Float nationalValue = Float.valueOf(a[1]);
		System.out.println(nationalValue);
		Float stateValue = Float.valueOf(a[2]);
		System.out.println(stateValue);

		String q="";
		if(marketValue > nationalValue && marketValue > stateValue) {
			q = "high";
		} else if(marketValue < nationalValue && marketValue < stateValue) {
			q = "low";
		} else if(marketValue > nationalValue && marketValue <= stateValue || marketValue < nationalValue && marketValue >= stateValue) {
			q = "mixed";
		} else if(marketValue >= nationalValue && marketValue < stateValue || marketValue <= nationalValue && marketValue > stateValue) {
			q = "mixed";
		} else if(marketValue.equals(nationalValue) && marketValue.equals(stateValue)) {
			q = "level";
		}
		System.out.println(q);
		
	}

}

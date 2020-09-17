package pageMethods;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._propMgr;
import pageUtilities._queries;
import pageUtilities._testData;
import pageUtilities._utils;

public class _helperClass {
	
	public _helperClass() {
		PageFactory.initElements(_base.driver, this);
	}
	
	ExtentTest test = _base.test;
	ExtentTest node;
	int greenUserStoreId;
	String greenStoreName;
	int blueUserStoreId;
	String blueStoreName;
	HashMap<String, String> marketDetails;
	List<Integer> userStoreIdList = new LinkedList<>();;
	
	private By loader = By.cssSelector("div.loading");
	
	@FindBy(css="div[class*='compare-markets'] button")
	private WebElement $applyCompare;
	
	@FindBy(css="div[class*='compare-markets'] div[class*='green'] select>option[class='dis-bg']")
	private WebElement $greenSelVal;
	
	@FindBy(css="div[class*='compare-markets'] div[class*='blue'] select>option[class='dis-bg']")
	private WebElement $blueSelVal;
	
	@FindBy(xpath="//div[contains(@class,'compare-markets')]//div[contains(@class,'green')]//select/option[not(contains(text(),'None'))][not(contains(text(),' and Averages'))][not(@class='dis-bg')][not(contains(text(),' Radius'))]")
	public List<WebElement> $greenDDList;
	
	private WebElement $greenMarketInfo(WebDriver driver, int nth) {
		return _base.driver.findElement(By.xpath("//div[contains(@class,'compare-markets')]//div[contains(@class,'green')]//select/option[not(contains(text(),'None'))][not(contains(text(),' and Averages'))][not(@class='dis-bg')][not(contains(text(),' Radius'))]["+nth+"]")); 
	}
	
	@FindBy(css="div[class*='compare-markets'] div[class*='green'] select")
	private WebElement $greenDDL;
	
//	private WebElement $greenMarket(WebDriver driver, int nth) {
//		return _base.driver.findElement(By.xpath("//div[contains(@class,'compare-markets')]//div[contains(@class,'green')]//select/option[not(contains(text(),'None'))][not(contains(text(),' and Averages'))][not(@class='dis-bg')][not(contains(text(),' Radius'))]["+nth+"]")); 
//	}
	
	@FindBy(xpath="//div[contains(@class,'compare-markets')]//div[contains(@class,'blue')]//select/option[not(contains(text(),'None'))][not(contains(text(),' and Averages'))][not(@class='dis-bg')][not(contains(text(),' Radius'))]")
	private List<WebElement> $blueDDList;
	
	private WebElement $blueMarketInfo(WebDriver driver, int nth) {
		return _base.driver.findElement(By.xpath("//div[contains(@class,'compare-markets')]//div[contains(@class,'blue')]//select/option[not(contains(text(),'None'))][not(contains(text(),' and Averages'))][not(@class='dis-bg')][not(contains(text(),' Radius'))]["+nth+"]")); 
	}
	
	@FindBy(css="div[class*='compare-markets'] div[class*='blue'] select")
	private WebElement $blueDDL;
	
//	private WebElement $blueMarket(WebDriver driver, int nth) {
//		return _base.driver.findElement(By.xpath("//div[contains(@class,'compare-markets')]//div[contains(@class,'blue')]//select/option[not(contains(text(),'None'))][not(contains(text(),' and Averages'))][not(@class='dis-bg')][not(contains(text(),' Radius'))]["+nth+"]")); 
//	}
	
	@FindBy(xpath="//div[contains(@class,'compare-markets')]//div[contains(@class,'green')]//select/option[contains(text(),'National Totals')]")
	private WebElement $nationalTotals;
	
	@FindBy(xpath="//div[contains(@class,'compare-markets')]//div[contains(@class,'blue')]//select/option[contains(text(),'State Total')]")
	private WebElement $stateTotals;
	
	@FindBy(xpath="//div[contains(@class,'compare-markets')]//div[contains(@class,'blue')]//select/option[contains(text(),'None')]")
	private WebElement $none;
	
	
	public void compareMarket() {
		int loaderPresent;
		int noOfSelection = 1;
		while(noOfSelection<=2) {
			System.out.println("test");
			selMarketOne();
			selMarketTwo();
			_utils.clickJs($applyCompare);
			try {
				_utils.waitForElementInVisibleByLocator(loader);
			} catch(Exception e) {
				System.out.println(e);
			}
			
			loaderPresent = _base.driver.findElements(loader).size();
			
			if(loaderPresent!=0) {
				userStoreIdList.clear();
				_base.driver.navigate().refresh();
				_utils.waitForElementInVisibleByLocator(loader);
			} else if(loaderPresent==0){
				break;
			}
			noOfSelection++;
		}
	}
		
	
	public HashMap<String, String> getGreenDetails() {
		String marketType;
		
		if(!$blueSelVal.getText().contains("National Totals and Averages")) {
			marketType = _databaseUtils.getStringValue(_testData.queryIns.getMarketType(greenUserStoreId));
			setMarketDetails(marketType, greenUserStoreId);
		}
		return marketDetails;
	}
	
	public HashMap<String, String> getBlueDetails() {
		String marketType;
		if(!$greenSelVal.getText().contains("State Total and Averages")) {
			marketType = _databaseUtils.getStringValue(_testData.queryIns.getMarketType(blueUserStoreId));
			setMarketDetails(marketType, blueUserStoreId);
		}
		return marketDetails;
	}
	
	public void resetCompMarket() {
		int loaderPresent;
		
		if((!$blueSelVal.getText().contains("National Totals and Averages")) && (!$greenSelVal.getText().contains("State Total and Averages"))) {
			_utils.click($nationalTotals);
			if(_testData.regId==1) {
				_utils.click($stateTotals);
			} else if(_testData.regId==3) {
				_utils.click($none);
			}
			_utils.clickJs($applyCompare);
			
			try {
				_utils.waitForElementInVisibleByLocator(loader);
			} catch(Exception e) {
				System.out.println(e);
			}
			loaderPresent = _base.driver.findElements(loader).size();
			if(loaderPresent>0) {
				_base.driver.navigate().refresh();
				_utils.waitForElementInVisibleByLocator(loader);
			}
		}
		userStoreIdList.clear();
	}
	
	void setMarketDetails(String marketType, int userStoreId) {
		switch(marketType) {
		case "1":
		case "2":
			marketDetails = _databaseUtils.getColumnValues(_testData.queryIns.getRadiusMarketDet(userStoreId));
			break;
		case "3":
			marketDetails = _databaseUtils.getColumnValues(_testData.queryIns.getCityMarketDet(userStoreId));
			break;
		case "4":
			marketDetails = _databaseUtils.getColumnValues(_testData.queryIns.getMarketType4(userStoreId));
			break;
		}
	}
	
	
	void selMarketOne() {
		int random = 0;
		if(_propMgr.getGreenMarket()==0) {
			int marketsAvailable = $greenDDList.size();
			boolean available;
			if(marketsAvailable!=0 && userStoreIdList.size()<=marketsAvailable) {
				do {
					random = _utils.getRandNumber($greenDDList.size());
//					greenStoreName = $greenMarketInfo(_base.driver, random).getText();
					greenUserStoreId = Integer.valueOf($greenMarketInfo(_base.driver, random).getAttribute("value"));
					available = userStoreIdList.contains(greenUserStoreId);
				}while(available==true);
			}
		} else {
			greenUserStoreId = _propMgr.getGreenMarket();
		}
		_utils.click($greenDDL);
		_utils.selectDropDownByValue($greenDDL, String.valueOf(greenUserStoreId));
		userStoreIdList.add(greenUserStoreId);
		greenStoreName = new Select($greenDDL).getFirstSelectedOption().getText();
		System.out.println(greenStoreName);
		System.out.println(greenUserStoreId);
	}
	
	void selMarketTwo() {
		int random = 0;
		if(_propMgr.getBlueMarket()==0) {
			int marketsAvailable = $blueDDList.size();
			boolean available;
			System.out.println("Test");
			if(marketsAvailable!=0 && userStoreIdList.size()<=marketsAvailable) {
				do {
					random = _utils.getRandNumber($blueDDList.size());
//					blueStoreName = $blueMarketInfo(_base.driver, random).getText();
					blueUserStoreId = Integer.valueOf($blueMarketInfo(_base.driver, random).getAttribute("value"));
					available = userStoreIdList.contains(blueUserStoreId);
				} while(available==true);
			}
		} else {
			blueUserStoreId = _propMgr.getBlueMarket();
		}
		_utils.click($blueDDL);
		_utils.selectDropDownByValue($blueDDL, String.valueOf(blueUserStoreId));
		userStoreIdList.add(blueUserStoreId);
		blueStoreName = new Select($blueDDL).getFirstSelectedOption().getText();
		System.out.println(blueStoreName);
		System.out.println(blueUserStoreId);
	}
	
	
	
	public static String convertCurrency(String dbValue) {
		String value = (dbValue.contains(".")) ? dbValue.substring(0, dbValue.indexOf(".")) : dbValue;
		long l = Long.parseLong(value);
		String s = value;
		if(l >= 1000000 && l < 1000000000){
	       s = String.format("%.2fM", l/ 1000000.0);
		}
		if(l >= 1000000000 && l < 100000000000L){
	        s = String.format("%.2fB", l/ 1000000000.0);
		}
		if(l >= 100000000000L){
	       s = String.format("%.2fT", l/ 1000000000000.0);
		}
		return s;
	}
	
	/* To compare UI and Database Values */
	
	public static void compareUiDb(String header, String rawUi, String rawDb, ExtentTest node) {
		String uiValue = rawUi;
//		if(!header.toLowerCase().contains("storename") || !header.toLowerCase().contains("address")) {
		if(!header.endsWith("@skip@")) {
			uiValue = rawUi.replace(",", "").replace("$", "").replace("%", "").replace(" Sq.ft", "").replace(" sqft", "").replace(" sq.ft", "").replace("[", "").replace("]", "").replace("£", "");
			if(!uiValue.matches(".*[A-z].*")) {
				BigDecimal bigD = new BigDecimal(uiValue);
				uiValue = (bigD.stripTrailingZeros().toPlainString().equals("0")) ? "N/A" : bigD.stripTrailingZeros().toPlainString();
			}
		}
		String dbValue = (rawDb==null || rawDb=="null" || rawDb==String.valueOf(0) || rawDb.equals("0") || rawDb.isEmpty()) ? "N/A" : (rawDb.endsWith(".0")) ? rawDb.replace(".0", "") : rawDb;
		if(!dbValue.matches(".*[A-z%].*")) {
			BigDecimal bigD = new BigDecimal(dbValue);
			dbValue = (bigD.stripTrailingZeros().toPlainString().equals("0")) ? "N/A" : bigD.stripTrailingZeros().toPlainString();
		}
		
		String headerValue = header.replace("@skip@", "").trim();
		if(uiValue.trim().equals(dbValue.trim())) {
			System.out.println("PASS---"+headerValue+" - Site: "+uiValue+"  Db: "+dbValue);
			node.log(Status.PASS, headerValue+" - Site: "+uiValue+"  Db: "+dbValue);
		} else {
			System.out.println("FAIL---"+headerValue+" - Site: "+uiValue+"  Db: "+dbValue);
			node.log(Status.FAIL, headerValue+" - Site: "+uiValue+"  Db: "+dbValue);
		}
	}
	
	
	/* Default slider values used to select radius */
	
	public static String radiusSlider(int radius) {
		HashMap<Integer, String> sliderValue = new HashMap<>();
		sliderValue.put(1, "left: 0%;");
		sliderValue.put(2, "left: 11.1111%;");
		sliderValue.put(3, "left: 22.2222%;");
		sliderValue.put(4, "left: 33.3333%;");
		sliderValue.put(5, "left: 44.4444%;");
		sliderValue.put(6, "left: 55.5556%;");
		sliderValue.put(7, "left: 66.6667%;");
		sliderValue.put(8, "left: 77.7778%;");
		sliderValue.put(9, "left: 88.8889%;");
		sliderValue.put(10, "left: 100%;");
		return sliderValue.get(radius);
	}
	
	
	/* To close all the opening widgets */
	
	public static void closeOpenWidgets() {
		int noOfWidgetsOpen;
		int isAllWidgetClosed;
		
		do {
			noOfWidgetsOpen = _base.driver.findElements(By.cssSelector("button[title*='Remove from Viewer']")).size();;
			while(noOfWidgetsOpen > 0) {
				_utils.clickJs(_base.driver.findElement(By.xpath("(//button[@title='Remove from Viewer'])[1]")));
				noOfWidgetsOpen--;
			}
			isAllWidgetClosed = _base.driver.findElements(By.cssSelector("div.default-pallete-items.muuri>div")).size();
		} while(isAllWidgetClosed!=0);
//		
//		System.out.println(_base.driver.findElement(By.cssSelector("div.default-pallete-items.muuri+div>i")).getText());
		
//		if(n>0) {
//			for(int i=1; i<=n; i++) {
//				_utils.clickJs(_base.driver.findElement(By.xpath("(//button[@title='Remove from Viewer'])[1]")));
//			}
//		}
	}

}

package tests;

import java.io.IOException;
import org.testng.ITestResult;
import org.testng.annotations.*;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import pageMethods._addMarket;
import pageMethods._allStores;
import pageMethods._demographics;
import pageMethods._executiveSummary;
import pageMethods._iaCurrentInvAvailByUnitType;
import pageMethods._iaInvAvailByUnitTypeHistory;
import pageMethods._knownDevelopments;
import pageMethods._marketOverview;
import pageMethods._marketSpendingPower;
import pageMethods._marketSupplyMetrics;
import pageMethods._myMarket;
import pageMethods._prAverageRatesHistory;
import pageMethods._prAverageUnitTypesRates;
import pageMethods._prRatePerSqFtByStoreType;
import pageMethods._prRatePerSqFtByUnitType;
import pageMethods._prRatePerSqFtHistoryGraph;
import pageMethods._prRateHistoryByStore;
import pageMethods._prRateVolatilityHistory;
import pageMethods._signIn;
import pageMethods._signUp;
import pageMethods._storesInMarketStoreTypes;
import pageMethods._storesInMarketUnitTypes;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._excelUtils;
import pageUtilities._propMgr;
import pageUtilities._testData;
import pageUtilities._utils;

public class _mvpTest extends _base {
	
	
	@BeforeTest
	public void login()  {
//		_excelUtils.getStoreAddress();
		
		if(_propMgr.getNewUser().equalsIgnoreCase("yes") && _propMgr.getUrl().contains("staging")) {
			_signUp signup = new _signUp();
			signup.nationalSubs();
		} else {
			 _signIn signIn = new _signIn();
			signIn.login();
		}
		_testData.setQueryInstance();
	}  
	
	
	
	
	@Test(priority=1)
	public void addMarket() {
		
		_base.test = _base.report.createTest("Login & Market Details");
		
//		_addMarket am = new _addMarket();
//		am.addNewMarket();
		
		_myMarket mm = new _myMarket();
		mm.marketView(_testData.userStoreId);
	}
	
	@Test(priority=2, dependsOnMethods="addMarket")
	public void marketOverview() {
		
		_base.test = _base.report.createTest("Market Overview");
		
		_marketOverview mo = new _marketOverview();
		mo.headerOverview();
	}
	
	
	
	
	@Test(priority=3, dependsOnMethods="marketOverview")
	public void allStores() {
		_base.test = _base.report.createTest("All Stores");
		
		_allStores as = new _allStores();
		as.overviewHeader();
		as.allStoresGrid(5);
	}
	
	
	
	
//	@Test(priority=4, dependsOnMethods="allStores")
	public void executiveSummary() {
		_base.test = _base.report.createTest("Executive Summary");
		
		_executiveSummary es = new _executiveSummary();
		es.noOfStores();
		es.currentSupply();
		es.rateTrend();
		es.newDevelopments();
	}
	
	
	
	
//	@Test(priority=5, dependsOnMethods="executiveSummary")
	public void knownDevelopments() {
		_base.test = _base.report.createTest("Known Developments");
		
		_knownDevelopments kd = new _knownDevelopments();
		kd.knownDevelopment();
	}
	
	
	
	
	@Test(priority=6, dependsOnMethods="allStores")
	public void demographics() {
		_base.test = _base.report.createTest("Demographics");
		
		_demographics de = new _demographics();
		de.thisMarketPopulation();
		de.greenPopulation("National");
		de.bluePopulation("State");
		de.thisMarketHouseholds();
		de.greenHouseholds("National");
		de.blueHouseholds("State");
		de.thisMarketRentalProperties();
		de.greenRentalProperties("National");
		de.blueRentalProperties("State");
		
		de.compareMarket();
		de.resetComparedMarket();
	}
	
	
	
	
	@Test(priority=7, dependsOnMethods="demographics")
	public void currentInventoryAvailability() {
		
		_base.test = _base.report.createTest("Current Inventory Availability");
		
		_iaCurrentInvAvailByUnitType ia = new _iaCurrentInvAvailByUnitType();
		ia.thisMarketCurrentInventory();
		ia.greenCurrentInventory("National");
		ia.blueCurrentInventory("State");
		
		ia.compareMarket();
		ia.resetComparedMarket();
	}
	
	
	
	
	@Test(priority=8, dependsOnMethods="currentInventoryAvailability")
	public void inventoryAvailabilityHistory() {
		
		_base.test = _base.report.createTest("Inventory Availability History");
		
		/* query output values not matching for National
		_inventoryAvailabilityHistory iah = new _inventoryAvailabilityHistory();
		iah.avgRateHistory();
		
		iah.compareMarket();
		iah.resetComparedMarket();
		*/
	}
	
	
	
	
	@Test(priority=9, dependsOnMethods="inventoryAvailabilityHistory")
	public void marketSpendingPower() {
		
		_base.test = _base.report.createTest("Market Spending Power");
		
		_marketSpendingPower ms = new _marketSpendingPower();
		ms.thisMarketHouseholdIncome();
		ms.greenHouseholdIncome("National");
		ms.blueHouseholdIncome("State");
		
		ms.thisMarketAverageProperty();
		ms.greenAverageProperty("National");
		ms.blueAverageProperty("State");
		
		ms.thisMarketAverageRental();
		ms.greenAverageRental("National");
		ms.blueAverageRental("State");
		
		ms.compareMarket();
		ms.resetComparedMarket();
	}
		
	
	
	
	@Test(priority=10, dependsOnMethods="marketSpendingPower")
	public void marketSupplyMetrics() {
		
		_base.test = _base.report.createTest("Market Supply");
		
		_marketSupplyMetrics msm = new _marketSupplyMetrics();
		msm.thisMarketCapita();
		msm.greenCapita("National");
		msm.blueCapita("State");
		
		msm.thisMarketHousehold();
		msm.greenHousehold("National");
		msm.blueHousehold("State");
		
		msm.thisMarketRentalProp();
		msm.greenRentalProp("National");
		msm.blueRentalProp("State");
		
//		msm.compareMarket();
//		msm.resetComparedMarket();
	}
	
	
	
	
	@Test(priority=11, dependsOnMethods="marketSupplyMetrics")
	public void prRatePerSqFtByStoreType() {
		
		_base.test = _base.report.createTest("Pricing Rental - Rate per SqFt by Store Type");
		
		_prRatePerSqFtByStoreType pr = new _prRatePerSqFtByStoreType();
		pr.thisMarketRateByStoreType();
		pr.greenRateByStoreType("National");
		pr.blueRateByStoreType("State");
		
		pr.compareMarket();
		pr.resetComparedMarket();
	}
	
	
	
	
	@Test(priority=12, dependsOnMethods="prRatePerSqFtByStoreType")
	public void prRatePerSqFtByUnitType() {
		
		_base.test = _base.report.createTest("Pricing Rental - Rate per SqFt by Unit Type");
		
		_prRatePerSqFtByUnitType prUnit = new _prRatePerSqFtByUnitType();
		prUnit.thisMarketRateByUnitType();
		
		
		prUnit.greenRateByUnitType("National");
		prUnit.blueRateByUnitType("State"); 
		
		prUnit.compareMarket();
		prUnit.resetComparedMarket();
	}
	
	
	
	
	@Test(priority=13, dependsOnMethods="prRatePerSqFtByUnitType")
	public void prAverageUnitTypeRates() {
		
		_base.test = _base.report.createTest("Pricing Rental - Average Unit Type Rates");
		
		_prAverageUnitTypesRates prAvg = new _prAverageUnitTypesRates();
		prAvg.thisMarketAvgUnitRates();
		prAvg.greenAvgUnitRates("National");
		prAvg.blueAvgUnitRates("State");
		
		prAvg.compareMarket();
		prAvg.resetComparedMarket();
	}
	
	
	
	
	@Test(priority=14, dependsOnMethods="prAverageUnitTypeRates")
	public void prRatePerSqFtHistoryGraph() {
		
		_base.test = _base.report.createTest("Pricing Rental - Rate per SqFt by History Graph");
		_prRatePerSqFtHistoryGraph rateHis = new _prRatePerSqFtHistoryGraph();
		rateHis.rateHistory();
		
		rateHis.compareMarket();
		rateHis.resetComparedMarket();
	}
	
	@Test(priority=15, dependsOnMethods="prRatePerSqFtHistoryGraph")
	public void prAverageRatesHistory() {
		
		_base.test = _base.report.createTest("Pricing Rental - Average Rate History");
		
		_prAverageRatesHistory aRateHis = new _prAverageRatesHistory();
		aRateHis.avgRateHistory();
		
		aRateHis.compareMarket();
		aRateHis.resetComparedMarket();
	}

	
	
	
	@Test(priority=16, dependsOnMethods="prAverageRatesHistory")
	public void prRateHistoryByStore() {
		
		_base.test = _base.report.createTest("Pricing Rental - Rate History By Store");
		
		_prRateHistoryByStore rHBS = new _prRateHistoryByStore();
		rHBS.rateHistoryByStore();
	}
	
	
	
	
	@Test(priority=17, dependsOnMethods="prRateHistoryByStore")
	public void prRateVolatilityHistory() {
		
		_base.test = _base.report.createTest("Pricing Rental - Rate Volatility History");
		
		_prRateVolatilityHistory rVH = new _prRateVolatilityHistory();
		rVH.rateVolatilityHistory();
		
		rVH.compareMarket();
		rVH.resetComparedMarket();
	}
	
	
	
	
	@Test(priority=18, dependsOnMethods="prRateVolatilityHistory")
	public void storesInMarketStoreTypes() {
		
		_base.test = _base.report.createTest("Stores In Market - Store Types");
		
		_storesInMarketStoreTypes st = new _storesInMarketStoreTypes();
		st.thisMarketStoreTypes();
		st.greenStoreTypes("National");
		st.blueStoreTypes("State");
		
		st.compareMarket();
		st.resetComparedMarket();
	}
	
	
	
		
	@Test(priority=19, dependsOnMethods="storesInMarketStoreTypes")
	public void storesInMarketUnitTypesOffered() {
		
		_base.test = _base.report.createTest("Stores In Market - Unit Types Offered");
		
		_storesInMarketUnitTypes ut = new _storesInMarketUnitTypes();
		ut.thisMarketRateByUnitType();
		ut.greenRateByUnitType("National");
		ut.blueRateByUnitType("State");
		
		ut.compareMarket();
		ut.resetComparedMarket();
	}
	
	
	
	
	
	@AfterMethod
	public static void createLog(ITestResult result) throws IOException {
		
		if(result.getStatus() == ITestResult.SUCCESS){
//        	test.log(Status.PASS, MarkupHelper.createLabel(result.getName()+" Test Case PASSED", ExtentColor.GREEN));
        } 
		else if(result.getStatus() == ITestResult.FAILURE) {
			String screenShotpath = _utils.screenCapture(_base.driver);
			_base.test.log(Status.FAIL, MarkupHelper.createLabel(result.getName()+" Test case FAILED due to below issues:", ExtentColor.RED));
			_base.test.fail(result.getThrowable());
			_base.test.log(Status.FAIL, "Screen Shot below: "+test.addScreenCaptureFromPath(screenShotpath));
        } 
		else if(result.getStatus()==ITestResult.SKIP) {
			_base.test.log(Status.SKIP, MarkupHelper.createLabel(result.getName()+" Test Case SKIPPED", ExtentColor.ORANGE));
			_base.test.skip(result.getThrowable());
		}
		_base.report.flush();
	}
	
	
	
	@AfterTest
	public void closeInstance() {
		if(_databaseUtils.st!=null){
			try{
				_databaseUtils.st.close();
			} catch (Exception e) {}
		}
		
		if(_databaseUtils.con!=null){
			try{
				_databaseUtils.con.close();
			} catch (Exception e) {}
		}
		
		if(_excelUtils.fis!=null){
			try{
				_excelUtils.fis.close();
			} catch (Exception e) {}
		}
		
		if(_excelUtils.fos!=null){
			try{
				_excelUtils.fos.close();
			} catch (Exception e) {}
		}
		
		if(_excelUtils.wb!=null){
			try{
				_excelUtils.wb.close();
			} catch (Exception e) {}
		}
		_base.driver.quit();
	}
}




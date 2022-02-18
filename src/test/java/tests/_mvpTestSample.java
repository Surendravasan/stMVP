package tests;

import java.io.IOException;

import org.openqa.selenium.By;
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

public class _mvpTestSample extends _base {
	
	
	@BeforeTest
	public void login()  {
		_excelUtils.getStoreAddress();
		
		
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
		
		_addMarket am = new _addMarket();
		am.addNewMarket();
		
		_myMarket mm = new _myMarket();
		mm.marketView(_testData.userStoreId);
	}
	
	
	
	
	@Test(priority=2, dependsOnMethods="addMarket")
	public void allStores() {
		_base.test = _base.report.createTest("All Stores");
		
		_allStores as = new _allStores();
		as.overviewHeader();
		as.allStoresGrid(1);
	}
	
	
	@Test(priority=6, dependsOnMethods="allStores")
	public void demographics() {
		_base.test = _base.report.createTest("Demographics");
		
		_demographics de = new _demographics();
		de.thisMarketPopulation();
		de.greenPopulation();
		de.bluePopulation();   
		de.thisMarketHouseholds();
		de.greenHouseholds();
		de.blueHouseholds();
		de.thisMarketRentalProperties();
		de.greenRentalProperties();
		de.blueRentalProperties();
		
		de.compareMarket();
		de.resetComparedMarket();
	}
	
	
	
	
	@Test(priority=7, dependsOnMethods="demographics")
	public void currentInventoryAvailability() {
		
		_base.test = _base.report.createTest("Current Inventory Availability");
		
		_iaCurrentInvAvailByUnitType ia = new _iaCurrentInvAvailByUnitType();
		
		ia.thisMarketCurrentInventory();
		ia.greenCurrentInventory();
		ia.blueCurrentInventory();
		
		ia.compareMarket();
		ia.resetComparedMarket();
	}
	
	
	
	@Test(priority=9, dependsOnMethods="currentInventoryAvailability")
	public void marketSpendingPower() {
		
		_base.test = _base.report.createTest("Market Spending Power");
		
		_marketSpendingPower ms = new _marketSpendingPower();
		ms.thisMarketHouseholdIncome();
		ms.greenHouseholdIncome();
		ms.blueHouseholdIncome();
		
		ms.thisMarketAverageProperty();
		ms.greenAverageProperty();
		ms.blueAverageProperty();
		
		ms.thisMarketAverageRental();
		ms.greenAverageRental();
		ms.blueAverageRental();
		
		ms.compareMarket();
		ms.resetComparedMarket();
	}
		
	
	
	
	@Test(priority=10, dependsOnMethods="marketSpendingPower")
	public void marketSupplyMetrics() {
		
		_base.test = _base.report.createTest("Market Supply");
		
		_marketSupplyMetrics msm = new _marketSupplyMetrics();
		msm.thisMarketCapita();
		msm.greenCapita();
		msm.blueCapita();
		
		msm.thisMarketHousehold();
		msm.greenHousehold();
		msm.blueHousehold();
		
		msm.thisMarketRentalProp();
		msm.greenRentalProp();
		msm.blueRentalProp();
		
		msm.compareMarket();
		msm.resetComparedMarket();
	}
	
	
	
	
	@Test(priority=11, dependsOnMethods="marketSupplyMetrics")
	public void prRatePerSqFtByStoreType() {
		
		_base.test = _base.report.createTest("Pricing Rental - Rate per SqFt by Store Type");
		
		_prRatePerSqFtByStoreType pr = new _prRatePerSqFtByStoreType();
		pr.thisMarketRateByStoreType();
		pr.greenRateByStoreType();
		pr.blueRateByStoreType();
		
		pr.compareMarket();
		pr.resetComparedMarket();
	}
	
	
	@Test(priority=12, dependsOnMethods="prRatePerSqFtByStoreType")
	public void prRatePerSqFtByUnitType() {
		
		_base.test = _base.report.createTest("Pricing Rental - Rate per SqFt by Unit Type");
		
		_prRatePerSqFtByUnitType prUnit = new _prRatePerSqFtByUnitType();
		System.out.println();
		prUnit.thisMarketRateByUnitType();
		prUnit.greenRateByUnitType();
		prUnit.blueRateByUnitType(); 
		
		prUnit.compareMarket();
		prUnit.resetComparedMarket();
	}
	
	
	
	
	@Test(priority=13, dependsOnMethods="prRatePerSqFtByUnitType")
	public void prAverageUnitTypeRates() {
		
		_base.test = _base.report.createTest("Pricing Rental - Average Unit Type Rates");
		
		_prAverageUnitTypesRates prAvg = new _prAverageUnitTypesRates();
		prAvg.thisMarketAvgUnitRates();
		prAvg.greenAvgUnitRates();
		prAvg.blueAvgUnitRates();
		
		prAvg.compareMarket();
		prAvg.resetComparedMarket();
	}
	
	
	
	@Test(priority=16, dependsOnMethods="prAverageUnitTypeRates")
	public void prRateHistoryByStore() {
		
		_base.test = _base.report.createTest("Pricing Rental - Rate History By Store");
		
		_prRateHistoryByStore rHBS = new _prRateHistoryByStore();
		rHBS.rateHistoryByStore();
	}
	
	

	@Test(priority=18, dependsOnMethods="prRateHistoryByStore")
	public void storesInMarketStoreTypes() {
		
		_base.test = _base.report.createTest("Stores In Market - Store Types");
		
		_storesInMarketStoreTypes st = new _storesInMarketStoreTypes();
		st.thisMarketStoreTypes();
		st.greenStoreTypes();
		st.blueStoreTypes();
		
		st.compareMarket();
		st.resetComparedMarket();
	}
	
	
	
		
	@Test(priority=19, dependsOnMethods="storesInMarketStoreTypes")
	public void storesInMarketUnitTypesOffered() {
		
		_base.test = _base.report.createTest("Stores In Market - Unit Types Offered");
		
		_storesInMarketUnitTypes ut = new _storesInMarketUnitTypes();
		ut.thisMarketRateByUnitType();
		ut.greenRateByUnitType();
		ut.blueRateByUnitType();
		
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




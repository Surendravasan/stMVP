package tests;

import java.io.IOException;
import org.testng.ITestResult;
import org.testng.annotations.*;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import pageMethods._addMarket;
import pageMethods._allStores;
import pageMethods._execSummary;
import pageMethods._marketOverview;
import pageMethods._myMarket;
import pageMethods._signIn;
import pageMethods._signUp;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._excelUtils;
import pageUtilities._propMgr;
import pageUtilities._testData;
import pageUtilities._utils;

public class _mvpTest extends _base {
	
	
	@BeforeTest
	public void login()  {
//		_testData.setMarketType();
//		_excelUtils.getMarket();
		
		if(_propMgr.getNewUser().equalsIgnoreCase("yes") && _propMgr.getUrl().contains("staging")) {
			_signUp signup = new _signUp();
			signup.nationalSubs();
		} else {
			 _signIn signIn = new _signIn();
			signIn.login();
		}
	}  
	
//	@Test(priority=1)
	public void addMarket() {
		
		_base.test = _base.report.createTest("Login & Market Details");
		
		_addMarket am = new _addMarket();
		am.addNewMarket();
		
	}
	
	@Test(priority=2)
	public void marketOverview() {
		
		_myMarket mm = new _myMarket();
		mm.marketView(_testData.userStoreId);
		
		_base.test = _base.report.createTest("Market Overview");
		
//		_marketOverview mo = new _marketOverview();
//		mo.headerOverview();
	}
	
//	@Test(priority=3, dependsOnMethods="marketOverview")
	public void allStores() {
		_base.test = _base.report.createTest("All Stores");
		
		_allStores as = new _allStores();
		System.out.println("Step 6");
		as.overviewHeader();
		System.out.println("Step 7");
		as.gridValues();
		System.out.println("Step 8");
	}
	
	@Test(priority=4, dependsOnMethods="marketOverview")
	public void executiveSummary() {
		_base.test = _base.report.createTest("Executive Summary");
		
		_execSummary es = new _execSummary();
		es.noOfStores();
		es.currentSupply();
	}
		
	
	
	@AfterMethod
	public void createLog(ITestResult result) throws IOException {
		
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




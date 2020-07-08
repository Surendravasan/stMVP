package tests;

import org.testng.annotations.*;

import pageMethods._signIn;
import pageUtilities._base;
import pageUtilities._propMgr;

public class _smokeTests extends _base {
	
	@BeforeClass
    public void beforeMethod() {
		_propMgr.getInstance(); 
		_signIn signIn = new _signIn();
		signIn.login();
		
        long id = Thread.currentThread().getId();
        System.out.println("Before test-method. Thread id is: " + id);
    }
 
    @Test
    public void testMethodsOne() {
        long id = Thread.currentThread().getId();
        System.out.println("Simple test-method One. Thread id is: " + id);
    }
 
    @Test
    public void testMethodsTwo() {
        long id = Thread.currentThread().getId();
        System.out.println("Simple test-method Two. Thread id is: " + id);
    }
 
    @AfterClass
    public void afterMethod() {
    	_base.driver.quit();
    	
        long id = Thread.currentThread().getId();
        System.out.println("After test-method. Thread id is: " + id);
    }
	
}

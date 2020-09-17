package pageMethods;

import objRepository._signUpPage;
import pageUtilities._base;
import pageUtilities._propMgr;
import pageUtilities._testData;
import pageUtilities._utils;

public class _signUp extends _signUpPage {
	
	public _signUp() {
		super();
	}
	public static String loginEmail;
	
	public void nationalSubs() {
		_base.driver.get(_propMgr.getSignupUrl());
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.waitClick($nationalSubs);
		_utils.waitForElementInVisibleByLocator(loader);
		addUserDetails();
		_utils.waitForElementInVisibleByLocator(loader);
		addBillingAddress();
		_utils.waitForElementInVisibleByLocator(loader);
		addCardDetails();
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.waitClick($gotoDash);
		_propMgr.setUsername(loginEmail);
	}
	
	private void addUserDetails() {
		loginEmail = _utils.getDateTime()+"@"+_testData.regId+"autotest.com";
		String phone = (_testData.regId==1) ? "(123)-456-7890" : "(123)-4567-8901";
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.fillData($emailAddress, loginEmail); 
		_utils.fillData($password, "365media");
		_utils.fillData($confirmPassword, "365media");
		_utils.fillData($firstName, "media");
		_utils.fillData($lastName, "media");
		_utils.fillData($company, "365media");
		_utils.fillData($phone, phone);
		_utils.waitClick($userDetailsNext);
	}
	
	private void addBillingAddress() {
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.fillData($address1, "address1");
		_utils.fillData($city, "city");
		if(_testData.regId!=3) {
			_utils.selectDropDownByIndex($state, 3);
		}
		_utils.fillData($zipCode, "12345");
		_utils.waitClick($billingPayNow);
	}
	
	private void addCardDetails() {
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.fillData($cardName, "test");
		_utils.fillData($cardNumber, "4111111111111111");
		_utils.fillData($cardExpiry, _utils.getMonthYearMMYY(1, 0, 0));
		_utils.fillData($cardSecurityCode, "123");
		_utils.click($agreeCheck);
		_utils.waitClick($paymentNow);
	}
}

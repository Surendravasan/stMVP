package pageMethods;

import objRepository._signInPage;
import pageUtilities._base;
import pageUtilities._propMgr;
import pageUtilities._utils;

public class _signIn extends _signInPage {
	
	public _signIn() {
		super();
	}
	
	public void login() {
		_base.driver.navigate().to(_propMgr.getUrl()+"/signin.html");
		_utils.waitForElementClickable($username);
		_utils.fillData($username, _propMgr.getUsername());
		_utils.fillData($password, _propMgr.getPassword());
		_utils.click($mvpRadio);
		_utils.waitClick($loginBtn);
	}

}

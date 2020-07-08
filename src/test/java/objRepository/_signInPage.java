package objRepository;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtilities._base;

public class _signInPage {
	
	public _signInPage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	protected By loader = By.cssSelector("div.loading");
	
	@FindBy(id="usernme")
	protected WebElement $username;
	
	@FindBy(id="pass")
	protected WebElement $password;
	
	@FindBy(id="mvp")
	protected WebElement $mvpRadio;
	
	@FindBy(id="btn")
	protected WebElement $loginBtn;


}

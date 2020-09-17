package objRepository;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtilities._base;

public class _marketDetailsPage {
	
	public _marketDetailsPage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	protected By loader = By.cssSelector("div.loading");
	protected String state = "//select[@name='state']/option";
	
	@FindBy(id="MarketName")
	protected WebElement $marketName;
	
	@FindBy(id="address")
	protected WebElement $address;
	
	@FindBy(id="city")
	protected WebElement $city;
	
	@FindBy(xpath="//select[@name='state']/option")
	protected List<WebElement> $state;
	
	@FindBy(id="zipcode")
	protected WebElement $zipCode;
	
	@FindBy(xpath="//b[contains(text(),'Market Type')]/ancestor::div[contains(@id,'title')]/following-sibling::div//div[@class='step_btn']//button[1]")
	protected WebElement $saveAddress;
	
	@FindBy(css="select[id='state-radio']>option")
	protected List<WebElement> $stateList;
	
	@FindBy(id="state-radio")
	protected WebElement $selectState;
	
	@FindBy(css="input[name='sel_lm_state']")
	protected List<WebElement> $cityList;
	
	protected WebElement $selectCity(WebDriver driver, int nth) {
		return driver.findElement(By.xpath("//fieldset/div/div["+nth+"]//input[@name='sel_lm_state']"));
	}
	
	
	
	
}

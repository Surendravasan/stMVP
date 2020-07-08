package objRepository;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtilities._base;

public class _marketTypePage {
	
	public _marketTypePage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	@FindBy(css="b.h-pop-head")
	protected WebElement $headerText;
	
	@FindBy(css="div[class*='user-details'] h5")
	protected WebElement $marketSelectionLabel;
	
	@FindBy(css="div[class*='user-details'] input[value*='GeoLocation' i]")
	protected WebElement $geoLocationRadio;
	
	@FindBy(css="div[class*='user-details'] input[value*='SelectListMap' i]")
	protected WebElement $cityListRadio;
	
	@FindBy(xpath="//div[@class='step_btn']//button[contains(text(),'Save')]")
	protected WebElement $saveBtn;
	
	
	
	
	
	
	
	

}

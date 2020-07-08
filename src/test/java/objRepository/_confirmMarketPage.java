package objRepository;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtilities._base;

public class _confirmMarketPage {
	
	public _confirmMarketPage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	protected By loader = By.cssSelector("div.loading");

	@FindBy(css="div.market-success button")
	protected WebElement $goToMyMarkets;
	
	

}

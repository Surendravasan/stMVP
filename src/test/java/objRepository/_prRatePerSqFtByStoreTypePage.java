package objRepository;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtilities._base;

public class _prRatePerSqFtByStoreTypePage {
	
	public _prRatePerSqFtByStoreTypePage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	protected By loader = By.cssSelector("div.loading");
	
	@FindBy(css="select[name=sortCat]")
	protected WebElement $widgetsDisplay;
	
	@FindBy(xpath="//span[text()='Pricing/Rental Rates']/../following-sibling::div")
	protected WebElement $pricingRentalExpand;
	
	@FindBy(xpath="//span[contains(text(),'Square Feet by Store Type')]/ancestor::a")
	protected WebElement $rateByStoreTypeLink;
	
	@FindBy(xpath="//button[contains(text(),'Add widget')]")
	protected WebElement $addWidget;
	
	
	/* Rate per Square Feet by Store Type */
	
	@FindBy(css="h4.drag-container")
	protected WebElement $title;
	
	@FindBy(css="div[class*='item-content'] thead>tr>th")
	protected List<WebElement> $noOfColumns;

	protected WebElement $rateStoreTypeHeader(WebDriver driver, int col) {
		return _base.driver.findElement(By.cssSelector("div[class*='item-content'] thead>tr>th:nth-child("+col+")")); 
	}
	
	protected WebElement $rateStoreTypeValues(WebDriver driver, int row, int col) {
		return _base.driver.findElement(By.cssSelector("div[class*='item-content'] tbody>tr:nth-child("+row+")>td:nth-child("+col+")")); 
	}
	
	
	/* Compare */
	
	@FindBy(css="div[class*='compare-markets'] div[class*='green'] select")
	protected WebElement $greenDropDown;
	
	@FindBy(css="div[class*='compare-markets'] div[class*='blue'] select")
	protected WebElement $blueDropDown;
	
}

package objRepository;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtilities._base;

public class _prRatePerSqFtHistoryGraphPage {
	
	public _prRatePerSqFtHistoryGraphPage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	protected By loader = By.cssSelector("div.loading");
	
	@FindBy(css="select[name=sortCat]")
	protected WebElement $widgetsDisplay;
	
	@FindBy(xpath="//span[text()='Pricing/Rental Rates']/../following-sibling::div")
	protected WebElement $pricingRentalExpand;
	
	@FindBy(xpath="//span[contains(text(),'Square Feet History')]/ancestor::a")
	protected WebElement $rateByHistoryLink;
	
	@FindBy(xpath="//button[contains(text(),'Add widget')]")
	protected WebElement $addWidget;
	
	
	/* Rate per Square Feet by History Graph */
	
	@FindBy(css="h6.drag-container")
	protected WebElement $title;
	
	@FindBy(css="select#hisactivedur")
	protected WebElement $unitTypeDropDown;
	
	@FindBy(css="select#hisactivedur>option:not([value*='All'])")
	protected List<WebElement> $unitTypeLists;
	
	protected WebElement $unitTypeSelection(int nth) {
		return _base.driver.findElement(By.cssSelector("select#hisactivedur>option:nth-child("+nth+"+1)"));
	}
	
	@FindBy(css="select#dateRangeChange")
	protected WebElement $dateRangeDropDown;
	
	@FindBy(css="div.mainhisGraphdiv.col-md-12>div>p")
	protected List<WebElement> $noGraphData;
	
	@FindBy(css="div[class*='wid-legend']>div:nth-child(1)>b")
	protected WebElement $dateLabel;
	
	@FindBy(css="div[class*='wid-legend']>div:nth-child(1)>span")
	protected WebElement $dateValue;
	
	@FindBy(css="div[class*='wid-legend']>div:nth-child(2)>div[class*='name']")
	protected WebElement $marketLabel;
	
	@FindBy(css="div[class*='wid-legend']>div:nth-child(2)>div[class*='price']")
	protected WebElement $uiMarketValue;
	
	@FindBy(css="div[class*='wid-legend']>div:nth-child(3)>div[class*='name']")
	protected WebElement $nationalLabel;
	
	@FindBy(css="div[class*='wid-legend']>div:nth-child(3)>div[class*='price']")
	protected WebElement $nationalValue;
	
	@FindBy(css="div[class*='wid-legend']>div:nth-child(4)>div[class*='name']")
	protected WebElement $stateLabel;
	
	@FindBy(css="div[class*='wid-legend']>div:nth-child(4)>div[class*='price']")
	protected WebElement $stateValue;

	
}

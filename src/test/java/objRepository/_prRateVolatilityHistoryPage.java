package objRepository;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtilities._base;

public class _prRateVolatilityHistoryPage {
	
	public _prRateVolatilityHistoryPage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	protected By loader = By.cssSelector("div.loading");
	
	@FindBy(css="select[name=sortCat]")
	protected WebElement $widgetsDisplay;
	
	@FindBy(xpath="//span[text()='Pricing/Rental Rates']/../following-sibling::div")
	protected WebElement $pricingRentalExpand;
	
	@FindBy(xpath="//span[contains(text(),'Rate Volatility History')]/ancestor::a")
	protected WebElement $rateVolatilityHistoryLink;
	
	@FindBy(xpath="//button[contains(text(),'Add widget')]")
	protected WebElement $addWidget;
	
	
	/* Rate Volatility History Graph */
	
	@FindBy(css="h6.drag-container")
	protected WebElement $title;
	
	@FindBy(id="unitTypeChange")
	protected WebElement $unitTypeDropDown;
	
	@FindBy(css="select#unitTypeChange>option")
	protected List<WebElement> $unitTypeLists;
	
	@FindBy(css="select#dateRangeChange")
	protected WebElement $dateRangeDropDown;
	
	@FindBy(xpath="//div[@id='Price-Volatility']//*[@class='x axis']/*[@class='tick']")
	protected List<WebElement> $noOfMonthsList;
	
	protected WebElement marketValue(WebDriver driver, int nth) {
		return _base.driver.findElement(By.xpath("(//div[@id='Price-Volatility']//*[@class='lines']/*[not(@class='line-group')][1]/*[@class='circle'])["+nth+"]"));
	}
	
	protected WebElement greenValue(WebDriver driver, int nth) {
		return _base.driver.findElement(By.xpath("(//div[@id='Price-Volatility']//*[@class='lines']/*[not(@class='line-group')][2]/*[@class='circle'])["+nth+"]"));
	}
	
	protected WebElement blueValue(WebDriver driver, int nth) {
		return _base.driver.findElement(By.xpath("(//div[@id='Price-Volatility']//*[@class='lines']/*[not(@class='line-group')][3]/*[@class='circle'])["+nth+"]"));
	}
	
	@FindBy(xpath="//div[@class='tooltip'][contains(@style,'display: unset')]")
	protected List<WebElement> $toolTipsSize;
	
	protected WebElement $tooltip(WebDriver driver, int nth) {
		return _base.driver.findElement(By.xpath("//div[@class='tooltip'][contains(@style,'display: unset')]["+nth+"]"));
	}
	
	protected WebElement monthValue(WebDriver driver, int nth) {
		return _base.driver.findElement(By.xpath("//div[@id='Price-Volatility']//*[@class='x axis']/*[@class='tick']["+nth+"]/*[@fill='currentColor']"));
	}
	
	@FindBy(css="div.legend.pv-wid-legend")
	protected WebElement $legend;
	

	
	/* Compare */
	
	@FindBy(css="div[class*='compare-markets'] div[class*='green'] select")
	protected WebElement $greenDropDown;
	
	@FindBy(css="div[class*='compare-markets'] div[class*='blue'] select")
	protected WebElement $blueDropDown;
	
}

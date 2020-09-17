package objRepository;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtilities._base;

public class _prRateHistoryByStorePage {
	
	public _prRateHistoryByStorePage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	protected By loader = By.cssSelector("div.loading");
	
	@FindBy(css="select[name=sortCat]")
	protected WebElement $widgetsDisplay;
	
	@FindBy(xpath="//span[text()='Pricing/Rental Rates']/../following-sibling::div")
	protected WebElement $pricingRentalExpand;
	
	@FindBy(xpath="//span[contains(text(),'Rate History by Store')]/ancestor::a")
	protected WebElement $avgRateHistoryLink;
	
	@FindBy(xpath="//button[contains(text(),'Add widget')]")
	protected WebElement $addWidget;
	
	
	/*  */
	
	@FindBy(css="h6.drag-container")
	protected WebElement $title;
	
	
	/* Filters */
	
	@FindBy(id="hisactiveMenu")
	protected WebElement $menuDropDown;
	
	@FindBy(id="hisactiveUnit")
	protected WebElement $unitTypeDropDown;
	
	@FindBy(css="select#hisactiveUnit>option")
	protected List<WebElement> $unitTypeLists;
	
	protected WebElement $unitTypeSelection(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("select#hisactiveUnit>option:nth-child("+nth+"+1)"));
	}
	
	@FindBy(id="hisactiveDur")
	protected WebElement $dateRangeDropDown;
	
	@FindBy(id="compStore1")
	protected WebElement $compOneDDL;
	
	@FindBy(css="select#compStore1>option:not([class*='dis-bg']):not([value='0'])")
	protected List<WebElement> $compOneDDLList;
	
	protected WebElement $compOneDDLOption(WebDriver driver, int nth) {
		return _base.driver.findElement(By.xpath("//select[@id='compStore1']/option[not(contains(@class,'dis-bg'))][not(@value='0')]["+nth+"]"));
	}
	
	@FindBy(id="compStore2")
	protected WebElement $compTwoDDL;
	
	@FindBy(css="select#compStore2>option:not([class*='dis-bg']):not([value='0'])")
	protected List<WebElement> $compTwoDDLList;
	
	protected WebElement $compTwoDDLOption(WebDriver driver, int nth) {
		return _base.driver.findElement(By.xpath("//select[@id='compStore2']/option[not(contains(@class,'dis-bg'))][not(@value='0')]["+nth+"]"));
	}
	
	@FindBy(id="compStore3")
	protected WebElement $compThreeDDL;
	
	@FindBy(css="select#compStore3>option:not([class*='dis-bg']):not([value='0'])")
	protected List<WebElement> $compThreeDDLList;
	
	protected WebElement $compThreeDDLOption(WebDriver driver, int nth) {
		return _base.driver.findElement(By.xpath("//select[@id='compStore3']/option[not(contains(@class,'dis-bg'))][not(@value='0')]["+nth+"]"));
	}
	
	/* Graph */
	
	@FindBy(css="div[class*='hisGraph']>div>p")
	protected List<WebElement> $noGraphData;
	
	@FindBy(css="g.lines")
	protected WebElement $graph;
	
	
	/* Legend */
	
	@FindBy(css="div[class*='wid-legend']>div:nth-child(1)>b")
	protected WebElement $dateLabel;
	
	@FindBy(css="div[class*='wid-legend']>div:nth-child(1)>span")
	protected WebElement $dateValue;
	
	
	@FindBy(css="div[class*='wid-legend']>div:not([class*='date'])")
	protected List<WebElement> $marketStoreList;
	
	protected WebElement $marketStoreLabel(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("div[class*='wid-legend']>div:nth-child("+nth+")+div:not([class*='date'])>div[class*='name']"));
	}
	
	protected WebElement $marketStoreValue(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("div[class*='wid-legend']>div:nth-child("+nth+")+div:not([class*='date'])>div[class*='price']"));
	}
	
	protected WebElement $compStoreColor(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("div[class*='wid-legend']>div:nth-child("+nth+")+div:not([class*='date'])>div[class*='symbol']"));
	}

}

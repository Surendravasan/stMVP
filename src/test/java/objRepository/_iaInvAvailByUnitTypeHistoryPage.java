package objRepository;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtilities._base;

public class _iaInvAvailByUnitTypeHistoryPage {
	
	public _iaInvAvailByUnitTypeHistoryPage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	
	protected By loader = By.cssSelector("div.loading");
	
	@FindBy(css="select[name=sortCat]")
	protected WebElement $widgetsDisplay;
	
	@FindBy(xpath="//span[text()='Inventory Availability']/../following-sibling::div")
	protected WebElement $inventoryExpand;
	
	@FindBy(xpath="//span[contains(text(),'Inventory')][contains(text(),'History')]/ancestor::a")
	protected WebElement $inventoryHistoryLink;
	
	@FindBy(xpath="//button[contains(text(),'Add widget')]")
	protected WebElement $addWidget;
	
	
	/* Rate per Square Feet by History Graph */
	
	@FindBy(css="h6.drag-container")
	protected WebElement $title;
	
	@FindBy(id="unitTypeChange")
	protected WebElement $unitTypeDropDown;
	
	@FindBy(css="select#unitTypeChange>option")
	protected List<WebElement> $unitTypeLists;
	
	@FindBy(id="dateRangeChange")
	protected WebElement $dateRangeDropDown;
	
	@FindBy(css="div[class*='mainhisGraph']>div>p")
	protected List<WebElement> $noGraphData;
	
	@FindBy(css="g.lines")
	protected WebElement $graph;
	
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

	
	/* Compare */
	
	@FindBy(css="div[class*='compare-markets'] div[class*='green'] select")
	protected WebElement $greenDropDown;
	
	@FindBy(css="div[class*='compare-markets'] div[class*='blue'] select")
	protected WebElement $blueDropDown;
	
	

}

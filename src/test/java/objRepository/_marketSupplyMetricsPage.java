package objRepository;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtilities._base;

public class _marketSupplyMetricsPage {
	
	public _marketSupplyMetricsPage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	protected By loader = By.cssSelector("div.loading");
	
	@FindBy(css="select[name=sortCat]")
	protected WebElement $widgetsDisplay;
	
	@FindBy(xpath="//span[text()='Market Supply']/../following-sibling::div")
	protected WebElement $mktSupplyExpand;
	
	@FindBy(xpath="//span[contains(text(),'Market Supply Metrics')]/ancestor::a")
	protected WebElement $marketSupplyLink;
	
	@FindBy(xpath="//button[contains(text(),'Add widget')]")
	protected WebElement $addWidget;
	
	
	/* Total Square Feet/Capita */
	
	@FindBy(css="div[class*='row-style']>div:nth-child(1) b.barTitle")
	protected WebElement $titleCapita;
	
	@FindBy(css="div[class*='row-style']>div:nth-child(1) thead>tr>th")
	protected List<WebElement> $noOfColsCapita;

	protected WebElement $capitaHeaderLabel(WebDriver driver, int col) {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(1) thead th:nth-child("+col+")")); 
	}
	
	protected WebElement $capitaValues(WebDriver driver, int row, int col) {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(1) tbody>tr:nth-child("+row+")>td:nth-child("+col+")")); 
	}
	
	
	/* Total Square Feet/Household */
	
	@FindBy(css="div[class*='row-style']>div:nth-child(2) b.barTitle")
	protected WebElement $titleHousehold;
	
	@FindBy(css="div[class*='row-style']>div:nth-child(1) thead>tr>th")
	protected List<WebElement> $noOfColsHousehold;
	
	protected WebElement $householdHeaderLabel(WebDriver driver, int col) {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(2) thead th:nth-child("+col+")")); 
	}
	
	protected WebElement $householdValues(WebDriver driver, int row, int col) {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(2) tbody>tr:nth-child("+row+")>td:nth-child("+col+")")); 
	}
	
	
	/* Total Square Feet/Rental Property */
	
	@FindBy(css="div[class*='row-style']>div:nth-child(3) b.barTitle")
	protected WebElement $titleRental;
	
	@FindBy(css="div[class*='row-style']>div:nth-child(1) thead>tr>th")
	protected List<WebElement> $noOfColsRental;
	
	protected WebElement $rentalPropHeaderLabel(WebDriver driver, int col) {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(3) thead th:nth-child("+col+")")); 
	}
	
	protected WebElement $rentalPropValues(WebDriver driver, int row, int col) {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(3) tbody>tr:nth-child("+row+")>td:nth-child("+col+")")); 
	}
	
	
	/* Compare */
	
	@FindBy(css="div[class*='compare-markets'] div[class*='green'] select")
	protected WebElement $greenDropDown;
	
	@FindBy(css="div[class*='compare-markets'] div[class*='blue'] select")
	protected WebElement $blueDropDown;
	
}

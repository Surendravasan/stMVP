package objRepository;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtilities._base;

public class _storesInMarketStoreTypesPage {
	
	public _storesInMarketStoreTypesPage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	protected By loader = By.cssSelector("div.loading");
	
	@FindBy(css="select[name=sortCat]")
	protected WebElement $widgetsDisplay;
	
	@FindBy(xpath="//span[text()='Stores in Market']/../following-sibling::div")
	protected WebElement $storesInMarketExpand;
	
	@FindBy(xpath="//span[contains(text(),'Store Types')]/ancestor::a")
	protected WebElement $storeTypesLink;
	
	@FindBy(xpath="//button[contains(text(),'Add widget')]")
	protected WebElement $addWidget;
	
	
	/* Store Types */
	
	@FindBy(css="h4.drag-container")
	protected WebElement $title;
	
	@FindBy(css="div[class*='item-content'] thead>tr:nth-child(1)>th")
	protected List<WebElement> $noOfColumns;

	protected WebElement $storeTypesHeader(WebDriver driver, int col) {
		return _base.driver.findElement(By.cssSelector("div[class*='item-content'] thead>tr:nth-child(1)>th:nth-child("+col+")")); 
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

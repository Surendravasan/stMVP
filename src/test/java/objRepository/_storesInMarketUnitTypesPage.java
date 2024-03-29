package objRepository;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtilities._base;

public class _storesInMarketUnitTypesPage {
	
	public _storesInMarketUnitTypesPage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	
	protected By loader = By.cssSelector("div.loading");
	
	@FindBy(css="select[name=sortCat]")
	protected WebElement $widgetsDisplay;
	
	@FindBy(xpath="//span[text()='Stores in Market']/../following-sibling::div")
	protected WebElement $pricingRentalExpand;
	
	@FindBy(xpath="//span[contains(text(),'Unit Types Offered')]/ancestor::a")
	protected WebElement $unitTypesOfferedLink;
	
	@FindBy(xpath="//button[contains(text(),'Add widget')]")
	protected WebElement $addWidget;
	
	
	/* Rate per Square Feet by Unit Type */
	
	@FindBy(css="h4.drag-container")
	protected WebElement $title;
	
	@FindBy(css="table>thead>tr:nth-child(1)>th")
	protected List<WebElement> $unitList;
	
	protected WebElement $unitLabel(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("table>thead>tr:nth-child(1)>th:nth-child("+nth+")"));
	}
	
	protected WebElement $unitValue(WebDriver driver, int row, int cell) {
		return _base.driver.findElement(By.cssSelector("table>tbody>tr:nth-child("+row+")>td:nth-child("+cell+")"));
	}
}
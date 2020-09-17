package objRepository;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtilities._base;

public class _iaCurrentInvAvailByUnitTypePage {
	
	public _iaCurrentInvAvailByUnitTypePage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	
	protected By loader = By.cssSelector("div.loading");
	
	@FindBy(css="select[name=sortCat]")
	protected WebElement $widgetsDisplay;
	
	@FindBy(xpath="//span[text()='Inventory Availability']/../following-sibling::div")
	protected WebElement $inventoryExpand;
	
	@FindBy(xpath="//span[contains(text(),'Current')][contains(text(),'Inventory')]/ancestor::a")
	protected WebElement $currentInvLink;
	
	@FindBy(xpath="//button[contains(text(),'Add widget')]")
	protected WebElement $addWidget;
	
	
	/* Current Inventory By Unit Type */
	
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
	
	/* Compare */
	
	@FindBy(css="div[class*='compare-markets'] div[class*='green'] select")
	protected WebElement $greenDropDown;
	
	@FindBy(css="div[class*='compare-markets'] div[class*='blue'] select")
	protected WebElement $blueDropDown;

}

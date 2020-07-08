package objRepository;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtilities._base;

public class _allStoresPage {
	
	public _allStoresPage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	protected By loader = By.cssSelector("div.loading");
	
	
	@FindBy(css="a[href*='all-store']")
	protected WebElement $allStoresLink;
	
	@FindBy(css="a[href*='all-store'][class*='btn']")
	protected WebElement $allStoresShowWidgetBtn;
	
//	@FindBy(css="button[title*='Remove from Viewer']")
//	protected List<WebElement> $openWidgetsList;
	
//	@FindBy(xpath="(//button[@title='Remove from Viewer'])[1]")
//	protected WebElement $removeWidget;
	
	protected WebElement $removeWidget(WebDriver driver) {
		return _base.driver.findElement(By.xpath("(//button[@title='Remove from Viewer'])[1]"));
	}
	
	

	
	/* Overview Header */
	
	@FindBy(css="div[class*='overview-header'] div[class*='comoverlist']>div")
	protected List<WebElement> $overHeaderList;
	
	protected WebElement $overHeaderLabel(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("div[class*='overview-header'] div[class*='comoverlist']>div:nth-child("+nth+") h4"));
	}
	
	protected WebElement $overHeaderValue(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("div[class*='overview-header'] div[class*='comoverlist']>div:nth-child("+nth+") h5"));
	}
	
	
	/* Grid Header */
	
	@FindBy(css="table.comp-view-grid.table>tbody>tr")
	protected List<WebElement> $storeList;
	
	protected WebElement $address(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("table.comp-view-grid.table>tbody>tr:nth-child("+nth+") tbody p[class*='address-line']"));
	}
	
	protected WebElement $storeName(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("table.comp-view-grid.table>tbody>tr:nth-child("+nth+") tbody td[class*='first-td'] h2>a"));
	}
	
	protected WebElement $totalSqFtValue(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("table.comp-view-grid.table>tbody>tr:nth-child("+nth+") tbody td[class*='sec-td']>div:nth-child(1) span"));
	}
	
	protected WebElement $rentSqFtValue(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("table.comp-view-grid.table>tbody>tr:nth-child("+nth+") tbody td[class*='sec-td']>div:nth-child(2) span"));
	}
	
	protected WebElement $ownedByValue(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("table.comp-view-grid.table>tbody>tr:nth-child("+nth+") tbody td[class*='third-td']>div:nth-child(1) span"));
	}
	
	protected WebElement $operatedByValue(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("table.comp-view-grid.table>tbody>tr:nth-child("+nth+") tbody td[class*='third-td']>div:nth-child(2) span"));
	}
	
	protected WebElement $openedValue(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("table.comp-view-grid.table>tbody>tr:nth-child("+nth+") tbody td[class*='fourth-td']>div:nth-child(1) span"));
	}
	
	protected WebElement $rateVolValue(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("table.comp-view-grid.table>tbody>tr:nth-child("+nth+") tbody td[class*='fourth-td']>div:nth-child(2) span"));
	}
	
	/* Grid Unit */
	
	@FindBy(css="table.comp-view-grid.table>tbody>tr:nth-child(2)>td>table[class*='custom'] td")
	protected List<WebElement> $unitList;
	
	
	protected WebElement $unitName(WebDriver driver, int store, int unit) {
		return _base.driver.findElement(By.cssSelector("table.comp-view-grid.table>tbody>tr:nth-child("+store+")>td>table[class*='custom'] td:nth-child("+unit+")>p:nth-child(1)"));
	}
	
	protected WebElement $unitValue(WebDriver driver, int store, int unit) {
		return _base.driver.findElement(By.cssSelector("table.comp-view-grid.table>tbody>tr:nth-child("+store+")>td>table[class*='custom'] td:nth-child("+unit+")>p:nth-child(2)>svg"));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

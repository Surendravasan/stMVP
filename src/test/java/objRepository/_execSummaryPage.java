package objRepository;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtilities._base;

public class _execSummaryPage {
	
	public _execSummaryPage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	protected By loader = By.cssSelector("div.loading");
	
	
	@FindBy(xpath="//span[contains(text(),'Executive Summary')]/ancestor::a")
	protected WebElement $execSummaryLink;
	
	@FindBy(xpath="//p[text()='Executive Summary']/../button")
	protected WebElement $addWidget;
	
	/* No of Stores in Market */
	
	@FindBy(css="div.excutive-summary table tr+tr>td")
	protected List<WebElement> $noOfStoresList;
	
	protected WebElement $noOfStoresLabel(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("div.excutive-summary table tr>th:nth-child("+nth+")"));
	}
	
	protected WebElement $noOfStoresValue(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("div.excutive-summary table tr+tr>td:nth-child("+nth+")"));
	}
	
	
	/* Current Supply to Market */
	
	@FindBy(xpath="//a[contains(text(),'Current Supply')]")
	protected WebElement $currentSupplyLink;
	
	
	@FindBy(css="div.tab-pane.active p:nth-child(2)")
	protected WebElement $inventoryText;
	

}

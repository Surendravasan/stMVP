package objRepository;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtilities._base;

public class _demographicsPage {
	
	public _demographicsPage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	protected By loader = By.cssSelector("div.loading");
	
	@FindBy(css="select[name=sortCat]")
	protected WebElement $widgetsDisplay;
	
	
	@FindBy(xpath="//span[text()='Demographics']/../following-sibling::div")
	protected WebElement $demoExpand;
	
	@FindBy(xpath="//span[contains(text(),'Size of Market')]/ancestor::a")
	protected WebElement $sizeOfMarketLink;
	
	@FindBy(xpath="//button[contains(text(),'Add widget')]")
	protected WebElement $addWidget;
	
	
	/* Population/Store */
	
	@FindBy(css="div[class*='row-style']>div:nth-child(1) b[class='barTitle']")
	protected WebElement $titlePopulation;
	
	@FindBy(css="div[class*='row-style']>div:nth-child(1) thead th")
	protected List<WebElement> $noOfColPopulation;
	
	protected WebElement $popColLabel(WebDriver driver, int col) {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(1) thead th:nth-child("+col+")"));
	}
	
	protected WebElement $popValues(WebDriver driver, int row, int col) {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(1) tbody>tr:nth-child("+row+")>td:nth-child("+col+")")); 
	}
	
	
	/* Households/Store */

	@FindBy(css="div[class*='row-style']>div:nth-child(2) b[class='barTitle']")
	protected WebElement $titleHousehold;
	
	@FindBy(css="div[class*='row-style']>div:nth-child(2) thead th")
	protected List<WebElement> $noOfColHousehold;
	
	protected WebElement $houholColLabel(WebDriver driver, int col) {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(2) thead th:nth-child("+col+")")); 
	}
	
	protected WebElement $houHolValues(WebDriver driver, int row, int col) {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(2) tbody>tr:nth-child("+row+")>td:nth-child("+col+")")); 
	}
	
	/* Rental Properties/Store */
	
	@FindBy(css="div[class*='row-style']>div:nth-child(3) b[class='barTitle']")
	protected WebElement $titleRental;

	@FindBy(css="div[class*='row-style']>div:nth-child(3) thead th")
	protected List<WebElement> $noOfColRental;
	
	protected WebElement $renProColLabel(WebDriver driver, int col) {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(3) thead th:nth-child("+col+")")); 
	}
	
	protected WebElement $renProValues(WebDriver driver, int row, int col) {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(3) tbody>tr:nth-child("+row+")>td:nth-child("+col+")")); 
	}
	
	
}

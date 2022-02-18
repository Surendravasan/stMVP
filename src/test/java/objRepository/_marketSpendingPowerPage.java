package objRepository;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtilities._base;

public class _marketSpendingPowerPage {
	
	public _marketSpendingPowerPage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	protected By loader = By.cssSelector("div.loading");
	
	@FindBy(css="select[name=sortCat]")
	protected WebElement $widgetsDisplay;
	
	
	@FindBy(xpath="//span[text()='Market Spending Power']/../following-sibling::div")
	protected WebElement $mktSpenExpand;
	
	@FindBy(xpath="//span[contains(text(),'Market Spending Power')]/ancestor::a")
	protected WebElement $marketSpendLink;
	
	@FindBy(xpath="//button[contains(text(),'Add widget')]")
	protected WebElement $addWidget;
	
	
	/* Household Income/Store */
	
	@FindBy(css="div[class*='row-style']>div:nth-child(1) b.barTitle")
	protected WebElement $titleHousehold;
	
	@FindBy(css="div[class*='row-style']>div:nth-child(1) thead>tr>th")
	protected List<WebElement> $noOfColunns;
	
	protected WebElement $houHolIncLabel(WebDriver driver, int col) {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(1) thead th:nth-child("+col+")")); 
	}
	
	protected WebElement $houHolIncValues(WebDriver driver, int row, int col) {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(1) tbody>tr:nth-child("+row+")>td:nth-child("+col+")")); 
	}
	
	
	/* Average Property Value */
	
	@FindBy(css="div[class*='row-style']>div:nth-child(2) b.barTitle")
	protected WebElement $titleAvgProp;
	
	protected WebElement $avgPropLabel() {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(2) thead th")); 
	}
	
	protected WebElement $avgPropValueMarket() {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(2) tbody tr:nth-child(1)>td")); 
	}
	
	protected WebElement $avgPropValueGreen() {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(2) tbody tr:nth-child(2)>td")); 
	}
	
	protected WebElement $avgPropValueBlue() {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(2) tbody tr:nth-child(3)>td")); 
	}
	
		
	/* Average Rental Costs */
	
	@FindBy(css="div[class*='row-style']>div:nth-child(3) b.barTitle")
	protected WebElement $titleAvgRent;
	
	protected WebElement $avgRentLabel() {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(3) thead th")); 
	}
	
	protected WebElement $avgRentMarket() {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(3) tbody tr:nth-child(1)>td")); 
	}
	
	protected WebElement $avgRentGreen() {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(3) tbody tr:nth-child(2)>td")); 
	}
	
	protected WebElement $avgRentBlue() {
		return _base.driver.findElement(By.cssSelector("div[class*='row-style']>div:nth-child(3) tbody tr:nth-child(3)>td")); 
	}

}

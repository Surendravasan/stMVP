package objRepository;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtilities._base;

public class _knownDevPage {
	
	public _knownDevPage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	protected By loader = By.cssSelector("div.loading");
	
	@FindBy(xpath="//span[contains(text(),'Known Developments')]/ancestor::a")
	protected WebElement $knowDevLink;
	
	@FindBy(xpath="//p[contains(text(),'Known Develop')]/../a")
	protected WebElement $showWidget;
	
	@FindBy(css="div[class*='table'] tbody>tr:nth-child(1)>td")
	protected List<WebElement> $firstRowCell;
	
	@FindBy(css="div[class*='table'] tbody>tr")
	protected List<WebElement> $noOfDevelopments;
	
	protected WebElement $address(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("div[class*='table'] tbody>tr:nth-child("+nth+")>td:nth-child(7)"));
	}
	
	protected WebElement $headerLabel(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("div[class*='table'] thead>tr>th:nth-child("+nth+")>span"));
	}
	
	protected WebElement $rowValues(WebDriver driver, int row, int cell) {
		return _base.driver.findElement(By.cssSelector("div[class*='table'] tbody>tr:nth-child("+row+")>td:nth-child("+cell+")"));
	}
	
	
	

}

package objRepository;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import pageUtilities._base;

public class _marketOverviewPage {
	
	public _marketOverviewPage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	protected By loader = By.cssSelector("div.loading");
	
	@FindBy(css="div[class*='overview-header'] div[class*='height'][class*='row'] div[class*='label']:not([class*='btm'])")
	protected List<WebElement> $headerList;
	
	
	protected WebElement $headerLabel(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("div[class*='overview-header'] div[class*='height'][class*='row']>div:nth-child("+nth+") div[class*='label']:not([class*='btm'])"));
	}
	
	protected WebElement $headerValue(WebDriver driver, int nth) {
		return _base.driver.findElement(By.cssSelector("div[class*='overview-header'] div[class*='height'][class*='row']>div:nth-child("+nth+") div[class*='value']"));
	}
	

}

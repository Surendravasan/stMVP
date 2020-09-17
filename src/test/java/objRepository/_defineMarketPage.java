package objRepository;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pageUtilities._base;

public class _defineMarketPage {
	
	public _defineMarketPage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	protected By loader = By.cssSelector("div.loading");
	
	@FindBy(css="div.loading")
	protected WebElement $loading;
	
	protected int $minRadius() {
		int a = Integer.valueOf(_base.driver.findElement(By.cssSelector("span[class*='slider']>span[role*='slider']")).getAttribute("aria-valuemin"));
		return a; 
	}
	
	protected int $maxRadius() {
		int a = Integer.valueOf(_base.driver.findElement(By.cssSelector("span[class*='slider']>span[role*='slider']")).getAttribute("aria-valuemax"));
		return a; 
	}
	
	@FindBy(css="span[class*='slider']>span[role*='slider']")
	protected WebElement $slider;
	
//	@FindBy(xpath="//span[contains(@class,'slider')]")
//	protected WebElement $slider;
//	
//	@FindBy(xpath="//span[contains(@class,'slider')]/span[contains(@class,'markActive')]")
//	protected List<WebElement> $sliderPoints;
		
//	protected WebElement $radius(WebDriver driver, int nth) {
//		return driver.findElement(By.xpath("//span[contains(@class,'slider')]/span[contains(@class,'markLabel')][text()="+nth+"]"));
//	}
	
//	protected WebElement $selectMiles(WebDriver driver, int nth) {
//		return driver.findElement(By.xpath("//span[contains(@class,'slider')]/span[contains(@class,'markLabel')][text()="+nth+"]"));
//	}
	
	
	@FindBy(xpath="//b[text()='Define Market']/ancestor::div[contains(@id,'title')]/following-sibling::div//button[contains(text(),'Save')]")
	protected WebElement $saveNxtBtn;
	

}

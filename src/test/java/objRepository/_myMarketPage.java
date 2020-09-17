package objRepository;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pageUtilities._base;

public class _myMarketPage {
	
	public _myMarketPage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	protected By loader = By.cssSelector("div.loading");
	protected By $tableData = By.cssSelector("table tbody tr");
	protected By $addMarketLabel = By.cssSelector("button[class*='add']");

	@FindBy(css="button[class*='add']")
	protected WebElement $addMarketBtn;
	
	@FindBy(css="div.market-div tbody tr:nth-child(1)>td[class*='market-name']>a")
	protected WebElement $marketname;
	
	protected WebElement $marketname(WebDriver driver, int userStoreId) {
		return driver.findElement(By.xpath("//div[@class='market-div']//tbody/tr//a[contains(@href,'market-view-palette/"+userStoreId+"/')]"));
	}
	
	@FindBy(css="button[title*='Remove from Viewer']")
	protected List<WebElement> $openWidgetsList;
	
	protected WebElement $removeWidget(WebDriver driver, int nth) {
		return _base.driver.findElement(By.xpath("(//button[@title='Remove from Viewer'])["+nth+"]"));
	}
	
}

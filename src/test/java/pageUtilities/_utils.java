package pageUtilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

 public class _utils {
	
	private static String letters = "abcdefghijklmnopqrstuvwxyz";
	private static char[] alphaNumericString = (letters + letters.toUpperCase() + "0123456789").toCharArray();
	static WebDriverWait wait = new WebDriverWait(_base.driver, 180);
	
	public static void fillData(WebElement element, String text) {
		element.clear();
		element.sendKeys(text);
	}

	
	public static void waitClick(WebElement element) {
		int cnt=0;
		do {
			if(element.isDisplayed()==true && element.isEnabled()==true && element!=null) {
                waitForElementClickable(element);
      			element.click();
				cnt++;
			}
		} while(cnt==0);
	}
	
	public static void select(WebElement element) {
		element.click();
	}
	
	public static void clickAction(WebElement ele) {
		Actions action = new Actions(_base.driver);
		action.moveToElement(ele).click().perform();
	}
	
	public static void clickJs(WebElement ele) {
		JavascriptExecutor executor = (JavascriptExecutor)_base.driver;
		executor.executeScript("arguments[0].click();", ele);
	}
	
	public static void selectDropDownByValue(WebElement ele, String text) {
		Select dropDown = new Select(ele);
		dropDown.selectByValue(text);
	}
	
	public static void selectDropDownByIndex(WebElement ele, int index) {
		Select dropDown = new Select(ele);
		dropDown.selectByIndex(index);
	}
	
	public static void selectDropDownByVisibleText(WebElement ele, String text) {
		Select dropDown = new Select(ele);
		dropDown.selectByVisibleText(text);
	}
	
	
	public static String getDateMinddmm() {
		Date currentDate = new Date();
		SimpleDateFormat dateForm = new SimpleDateFormat("ddmm");
		return dateForm.format(currentDate);
	}
	
	public static String getDateTime() {
		Date currentDate = new Date();
		SimpleDateFormat dateForm = new SimpleDateFormat("ddMMHHmmss");
		return dateForm.format(currentDate);
	}
	
	public static String getMonthYearMMYY(int year, int month, int date) {
		Date currentDate = new Date();
		SimpleDateFormat dateForm = new SimpleDateFormat("MMYY");
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		
		cal.add(Calendar.YEAR, year);
		cal.add(Calendar.MONTH, month);
		cal.add(Calendar.DATE, date);
		
		Date nextMonth = cal.getTime();
		return dateForm.format(nextMonth);
	}
	
	
	public static String getRandString(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(alphaNumericString[new Random().nextInt(alphaNumericString.length)]);
		}
		return sb.toString();
	}

	public static int getRandNumber(int length) {

		Random rand = new Random();
		return rand.nextInt(length)+1;
	}

	public static void waitForElementClickable(WebElement elmt) {
		wait.until(ExpectedConditions.elementToBeClickable(elmt));
	}
	
	public static void waitForElementInVisibleByLocator(By by) {
		wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
	}
	
	public static void waitForElementVisibleByLocator(By by) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}
	
	
	public static String screenCapture(WebDriver driver)  {
		TakesScreenshot ts = (TakesScreenshot)driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String dest = System.getProperty("user.dir")+"\\target\\"+_utils.getDateTime()+".png";
		File destination = new File(dest);
		try {
			FileUtils.copyFile(source, destination);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dest;
	}
	
	/* To close all the opening widgets */
	
	public static void closeOpenWidgets() {
		int n = _base.driver.findElements(By.cssSelector("button[title*='Remove from Viewer']")).size();
		if(n>0) {
			for(int i=1; i<=n; i++) {
				_utils.select(_base.driver.findElement(By.xpath("(//button[@title='Remove from Viewer'])[1]")));
			}
		}
	}
	
	/* To compare UI and Database Values */
	
	public static void compareUiDb(String header, String rawUi, String rawDb, ExtentTest node) {
		String uiValue = rawUi;
		if(!header.toLowerCase().contains("storename")) {
			uiValue = rawUi.replace(",", "").replace("$", "").replace("%", "").replace(" Sq.ft", "").replace(" sqft", "");
		}
		String dbValue = (rawDb==null || rawDb==String.valueOf(0)) ? "N/A" : rawDb;
		
		if(uiValue.trim().equals(dbValue.trim())) {
			System.out.println("PASS---"+header+" - Site: "+uiValue+"  Db: "+dbValue);
			node.log(Status.PASS, header+" - Site: "+uiValue+"  Db: "+dbValue);
		} else {
			System.out.println("FAIL---"+header+" - Site: "+uiValue+"  Db: "+dbValue);
			node.log(Status.FAIL, header+" - Site: "+uiValue+"  Db: "+dbValue);
		}
		
	}
	

}

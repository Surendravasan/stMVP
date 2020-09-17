package pageUtilities;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
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
	
	String letters = "abcdefghijklmnopqrstuvwxyz";
	char[] alphaNumericString = (letters + letters.toUpperCase() + "0123456789").toCharArray();
	WebDriverWait wait = new WebDriverWait(_base.driver, 120);
	
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
	
	public static void click(WebElement element) {
		element.click();
	}
	
	public static void clickAction(WebElement ele) {
		Actions action = new Actions(_base.driver);
		action.moveToElement(ele).click().perform();
	}
	
	public static void moveToElement(WebElement ele) {
		Actions action = new Actions(_base.driver);
		action.moveToElement(ele);
		action.build().perform();
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
		_utils u = new _utils();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(u.alphaNumericString[new Random().nextInt(u.alphaNumericString.length)]);
		}
		return sb.toString();
	}

	public static int getRandNumber(int length) {
		Random rand = new Random();
		return rand.nextInt(length)+1;
	}
	
	

	public static void waitForElementClickable(WebElement elmt) {
		_utils u = new _utils();
		u.wait.until(ExpectedConditions.elementToBeClickable(elmt));
	}
	
	public static void waitForElementVisible(WebElement elmt) {
		_utils u = new _utils();
		u.wait.until(ExpectedConditions.visibilityOf(elmt));
	}
	
	public static void waitForElementInVisibleByLocator(By by) {
		_utils u = new _utils();
		u.wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
	}
	
	public static void waitForElementVisibleByLocator(By by) {
		_utils u = new _utils();
		u.wait.until(ExpectedConditions.visibilityOfElementLocated(by));
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
	
	
	/* Convert Digits to Words */
	
	public static String convert(int n)
	{
		// for storing the word representation of given number
		StringBuilder res = new StringBuilder();

//		// add digits at ten millions & hundred millions place
//		res.append(convertToDigit((n / 1000000000) % 100, "Billion, "));
//
//		// add digits at ten millions & hundred millions place
//		res.append(convertToDigit((n / 10000000) % 100, "Crore, "));
//
//		// add digits at hundred thousands & one millions place
//		res.append(convertToDigit(((n / 100000) % 100), "Lakh, "));
//
//		// add digits at thousands & tens thousands place
//		res.append(convertToDigit(((n / 1000) % 100), "Thousand "));
//
//		// add digit at hundreds place
//		res.append(convertToDigit(((n / 100) % 10), "Hundred "));
//
//		if ((n > 100) && (n % 100 != 0)) {
//			res.append("and ");
//		}

		// add digits at ones & tens place
		res.append(convertToDigit((n % 100), ""));

		return res.toString();
	}
	
	private static String convertToDigit(int n, String suffix){
		String EMPTY = "";
		String[] X =
			{
				EMPTY, "One ", "Two ", "Three ", "Four ", "Five ", "Six ",
				"Seven ", "Eight ", "Nine ", "Ten ", "Eleven ","Twelve ",
				"Thirteen ", "Fourteen ", "Fifteen ", "Sixteen ",
				"Seventeen ", "Eighteen ", "Nineteen "
			};
		
		String[] Y =
			{
				EMPTY, EMPTY, "Twenty ", "Thirty ", "Forty ", "Fifty ",
				"Sixty ", "Seventy ", "Eighty ", "Ninety "
			};
		
		// if n is zero
		if (n == 0) {
			return "no";
		}

		// split n if it is more than 19
		if (n > 19) {
			return Y[n / 10] + X[n % 10] + suffix;
		}
		else {
			return X[n] + suffix;
		}
	}
	
	
	/* Input: month("Feb") & year("19"), return month firstDate and lastDate as List */
	
	public static LinkedList<String> getFirstLastDate(String uiMonth, String uiYear) {
		LinkedList<String> ll = new LinkedList<>(); 
		
		DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM").withLocale(Locale.ENGLISH);
		TemporalAccessor accessor = parser.parse(StringUtils.capitalize(uiMonth.toLowerCase()));
		
		int monthNo = accessor.get(ChronoField.MONTH_OF_YEAR);
		int fullYear = Integer.valueOf("20"+uiYear);
		
		LocalDate date = LocalDate.of(fullYear, monthNo, 1);
		int noOfDays = date.lengthOfMonth();
		
		ll.add(fullYear+"-"+monthNo+"-01");
		ll.add(fullYear+"-"+monthNo+"-"+noOfDays);
		
		return ll;
	}
	
}

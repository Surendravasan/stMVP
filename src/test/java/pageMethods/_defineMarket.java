package pageMethods;  

import org.openqa.selenium.JavascriptExecutor;
import objRepository._defineMarketPage;
import pageUtilities._base;
import pageUtilities._testData;
import pageUtilities._utils;

public class _defineMarket extends _defineMarketPage {
	
	public _defineMarket() {
		super();
	}
	
	public void radius(int radius) {
		int randRadius = radius;
		_utils.waitForElementInVisibleByLocator(loader);
		
		if(radius<$minRadius() || radius>$maxRadius()) {
			randRadius = _utils.getRandNumber($maxRadius());
		}
		
		JavascriptExecutor jse = (JavascriptExecutor)_base.driver;
		jse.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", $slider, "style", _helperClass.radiusSlider(randRadius));
		
		_utils.clickJs($slider);
		_utils.click($slider);
		
		_utils.waitForElementInVisibleByLocator(loader);
		
		_testData.setRadius(randRadius);
		
	}
	
//	public void radius() {
//		_utils.waitForElementInVisibleByLocator(loader);
		
//		String min = _base.driver.findElement(By.cssSelector("span[class*='slider']>span[role*='slider']")).getAttribute("aria-valuemin");
//		String max = _base.driver.findElement(By.cssSelector("span[class*='slider']>span[role*='slider']")).getAttribute("aria-valuemax");
//		System.out.println(min);
//		System.out.println(max);
		
		
//		WebElement element = _base.driver.findElement(By.cssSelector("span[class*='slider']>span[role*='slider']")); // you can use any locator

//		JavascriptExecutor jse = (JavascriptExecutor)_base.driver;
//		jse.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", element, "style", "left: 0%;"); 
		
//		_base.driver.findElement(By.cssSelector("span[class*='slider']>span[role*='slider']")).click();
//		_utils.waitForElementInVisibleByLocator(loader);
		
//		int i = _utils.getRandNumber($sliderPoints.size());
//		if (i!=10) {
//			_utils.submit($selectMiles(_base.driver, i));
//			_utils.waitForElementInVisibleByLocator(loader);
//		}
//		_testData.setRadius(i);
//	}
	
	public void saveMarket() {
		_utils.waitClick($saveNxtBtn);
	}
	
}

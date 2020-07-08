 package tests;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.annotations.Test;

import pageMethods._allStores;
import pageUtilities._databaseUtils;
import pageUtilities._queries;
import pageUtilities._testData;


public class _testClass {
	
	
	@Test
	public void man() {
		String s = "Across all unit types, there is currently 50% of inventory offline (not available). "
				+ "The US national figure at the moment is 60% of inventory offline "
				+ "and the Arizona state number is 60% offline. "
				+ "Therefore, comparing against national and state averages, "
				+ "this market's supply level could be characterized as relatively low";
		
		String a[] = s.replace(". ","").replaceAll("[A-z ,()']", "").split("%");
		Float marketValue = Float.valueOf(a[0]);
		System.out.println(marketValue);
		Float nationalValue = Float.valueOf(a[1]);
		System.out.println(nationalValue);
		Float stateValue = Float.valueOf(a[2]);
		System.out.println(stateValue);

		String q="";
		if(marketValue > nationalValue && marketValue > stateValue) {
			q = "high";
		} else if(marketValue < nationalValue && marketValue < stateValue) {
			q = "low";
		} else if(marketValue > nationalValue && marketValue <= stateValue || marketValue < nationalValue && marketValue >= stateValue) {
			q = "mixed";
		} else if(marketValue >= nationalValue && marketValue < stateValue || marketValue <= nationalValue && marketValue > stateValue) {
			q = "mixed";
		} else if(marketValue.equals(nationalValue) && marketValue.equals(stateValue)) {
			q = "level";
		}
		System.out.println(q);
	}
	
}
			
	
				

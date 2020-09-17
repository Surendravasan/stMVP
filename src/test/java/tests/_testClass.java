 package tests;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.LinkedList;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

import pageUtilities.Queries;
import pageUtilities._databaseUtils;
import pageUtilities._propMgr;
import pageUtilities._testData;
import pageUtilities._ukQueries;
import pageUtilities._usQueries;

public class _testClass {
	
	@Test
	public void tess() {
		
		_smokeTests obj = new _smokeTests1();
		System.out.println(obj.getMarketType(5));
		System.out.println(obj.overTotalRentSqFo());
		
		
	}
}
		


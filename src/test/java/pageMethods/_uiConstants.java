package pageMethods;

import pageUtilities._testData;
import pageUtilities._utils;

public class _uiConstants {
	
	public static String currSupLowUS = "Therefore, comparing against national and state averages, this market's supply level could be characterized as relatively low.";
	public static String currSupHighUS = "Therefore, comparing against national and state averages, this market's supply level could be characterized as relatively high.";
	public static String currSupMixUS = "Therefore, comparing against national and state averages, this market's supply level could be characterized as a mixed result";
	public static String currSupNeuUS = "Therefore, comparing against national and state averages, this market's supply level could be characterized as near typical across the state and the nation.";
	
	public static String currSupLowUK = "comparing against national average, this market's supply level could be characterized as an under-supply.";
	public static String currSupHighUK = "comparing against national average, this market's supply level could be characterized as an under-supply.";
	public static String currSupNeuUK = "comparing against national average, this market's supply level could be characterized as an under-supply.";
	
	public static String rateTrendUS(String marTrend, Float marRate, String marSupply, Float natRate, String state, String staTrend, Float staRate) {
		String rateTrendValue = "";
		rateTrendValue = "The rate in this market has trended "+marTrend+" in the last 3 months, by "+marRate+"%. This would indicate a "+marSupply+" demand against available supply. The current US national rate trend for the past 3 months is up, by "+natRate+"%. The "+state+" state is showing a rate trend "+staTrend+" for the last 3 months of "+staRate+"%.";
		return rateTrendValue;
	}
	
	public static String rateTrendUK(String marTrend, Float marRate, String marSupply, Float natRate) {
		String rateTrendValue = "";
		rateTrendValue = "The rate in this market has trended "+marTrend+" in the last 3 months, by "+marRate+"%. This would indicate a "+marSupply+" demand against available supply. The current UK national rate trend for the past 3 months is up, by "+natRate+"%.";
		return rateTrendValue;
	}
	
	
	public static String marketNewDevelopments(int noOfDevs) {
		String expText;
		switch(noOfDevs){
		case 0:
		case 1:
			expText = "There is "+_utils.convert(noOfDevs)+" known new development in this market.";
			break;
		default:
			expText = "There are "+_utils.convert(noOfDevs)+" known new developments in this market.";
			break;
		}
		return expText;
	}
	
	public static String totalNewDevelopments(int noOfDevs) {
		String newDevText = "";
		if(_testData.regId==1) {
			newDevText = "StorTrack is currently tracking "+noOfDevs+" new developments across the USA.";
		} else if(_testData.regId==3) {
			newDevText = "StorTrack is currently tracking "+noOfDevs+" new developments across the UK.";
		}
		return newDevText;
	}
	
	
	
	
}

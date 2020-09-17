package pageMethods;

import pageUtilities._utils;

public class _uiConstants {
	
	public static String knoDevCurrSupLow = "Therefore, comparing against national and state averages, this market's supply level could be characterized as relatively low.";
	public static String knoDevCurrSupHigh = "Therefore, comparing against national and state averages, this market's supply level could be characterized as relatively high.";
	public static String knoDevCurrSupMix = "Therefore, comparing against national and state averages, this market's supply level could be characterized as a mixed result";
	public static String knoDevCurrSupNeu = "Therefore, comparing against national and state averages, this market's supply level could be characterized as near typical across the state and the nation.";
	
	public static String knoMix(String marTrend, Float marRate, String marSupply, Float natRate, String state, String staTrend, Float staRate) {
		String query = "";
		query = "The rate in this market has trended "+marTrend+" in the last 3 months, by "+marRate+"%. This would indicate a "+marSupply+" demand against available supply. The current US national rate trend for the past 3 months is up, by "+natRate+"%. The "+state+" state is showing a rate trend "+staTrend+" for the last 3 months of "+staRate+"%.";
		return query;
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
		String query = "StorTrack is currently tracking "+noOfDevs+" new developments across the USA.";
		return query;
	}
	
	
	
	
}

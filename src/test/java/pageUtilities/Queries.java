package pageUtilities;

import java.util.Date;
import java.util.HashMap;

public interface Queries {
	
		
	public int getUnitID(String unitName);
	
	public Date maxDatePrice = _databaseUtils.getDate("select DatePrice from MaxDatePrice");
	
	public String getStateFullName();
	
	public String getCountryFullName();
	
	
	/* Compare Markets */
	
	public  String getMarketType(int userStoreId);
	
	public  String getRadiusMarketDet(int userStoreId);
	
	public  String getCityMarketDet(int userStoreId);
	
	public  String getMarketType4(int userStoreId);
	
	
	
	
	/* Queries - Market Overview */
	
	public  String overTotalRentSqFo();
	
	public  String overSqCapita();
	
	public  String overNoOfStores();

	public String overAvgRateSqFt();
	
	public String overAvgRateSqFtAllUnits();
	
	public String overAvgRateSqFtNonCC();
	
	public String overAvgRateSqFtCC();
	
	
	
	/* All Stores List  - Overview Header*/
	
	public String overNoOfReits();
	
	public String overNoOfMOps();
	
	public String overNoOfSOps();
	
	
	/* All Stores List  - Grid */
	
	public String storesCount();
	
	public String gridHeaderValue(String address);
	
	public String gridUnitValue(String unitName, int storeId);
	
	
	/* Executive Summary - No of Stores */
	
	public String execSumThisMarket();
	
	public String execSumNational();
	
	public String execSumState();
	
	
	/* Executive Summary - New Developments */
	
	public String execSumTotalDevs();
	
	public String execSumMarketDevs();
	
	
	
	
	/* Known Developments */
	
	public String knowDevNoOfDevs();
	
	
	public String knownDevGridVal(String address);
	
	
	/* Demographics - Population */
		
	public String popMarket();
	
	public String popNational();
	
	public String popState();
	
	
	/* Demographics - Household */
	
	public String houHolMarket();
	
	public String houHolNational();
	
	public String houHolState();
	
	/* Demographics - Rental Properties */
	
	public String renPropMarket();
	
	public String renPropNational();
	
	public String renPropState();
	
	
	/* Compare Markets */
	
	public String popGreenBlue(HashMap<String, String> mapDet);
	
	
	public String houHolGreenBlue(HashMap<String, String> mapDet);
	
	public String rentalGreenBlue(HashMap<String, String> mapDet);
	
	
	
	/* Inventory Availability - Current Inventory By Unit Type */
	
	public String currInvUnitValue(String unitName);
	
	public String currInvNational(String unitName);
	
	public String currInvState(String unitName);
	
	/* Compare */
	
	public String currInvGreenBlueVal(String unitName, HashMap<String, String> mapDet);
	

	
	
	/* Inventory Availability - Inventory By Unit Type History */
	
	public String thisMarketInventoryHistory(String unitName, String datePrice);
	

	public String nationalInventoryHistory(String unitName, String datePrice);
	
	public String stateInventoryHistory(String unitName, String datePrice);
	
	/* Compare */
	
	public String greenBlueInventoryHistory(String unitName, String datePrice, HashMap<String, String> mapDet);
	
	
	
	/* Market Spending Power - Household Income */
	
	public String houholdIncMarket();
	
	public String houholIncNational();
	
	public String houholIncState();
	
	
	/* Market Spending Power - Average Property Value */
	
	public String avgPropMarket();
	
	public String avgPropNational();
	
	public String avgPropState();
	
	
	/* Market Spending Power - Average Rental Costs */
	
	public String rentalPropMarket();
	
	public String avgRentNational();
	
	public String rentalPropState();
	
	/* Compare */
	
	public String householdGreenBlue(HashMap<String, String> mapDet);
	
	
	public String avgPropGreenBlue(HashMap<String, String> mapDet);
	
	public String avgRentGreenBlue(HashMap<String, String> mapDet);
	
	
	/* Market Supply Metrics - Capita */
	
	public String thisMarketCapita();
	
	public String nationalCapita();
	
	public String stateCapita();
	
	
	/* Market Supply Metrics - Household */
	
	public String thisMarketHousehold();
	
	public String nationalHousehold();
	
	public String stateHousehold();
	
	/* Market Supply Metrics - Rental Properties */
	
	public String thisMarketRentalProp();
	
	public String nationalRentProp();
	
	public String stateRentProp();
	
	/* Compare */
	
	public String greenBlueCapita(HashMap<String, String> mapDet);
	
	
	public String greenBlueHoushold(HashMap<String, String> mapDet);
	
	public String greenBlueRentalProp(HashMap<String, String> mapDet);
	
	
	
	/* Pricing Rental - Rate Per Square Type by Store Type */
	
	public String thisMarketRateByStoreType();
	
	public String nationalRateByStoreType();
	
	public String stateRateByStoreType();
	
	/* Compare */
	
	public String greenBlueRateByStoreType(HashMap<String, String> mapDet);
	
	
	
	
	
	/* Pricing Rental - Rate Per Square Type by Unit Type */
	
	public String thisMarketRateByUnitType();
	
	public String nationalRateByUnitType();

	public String stateRateByUnitType();
	
	/* Compare */
	public String greenBlueRateByUnitType(HashMap<String, String> mapDet);
	
	
	
	
	
	/* Pricing Rental - Rate Per Square Type by Unit Type */
	
	public String thisMarketAvgUnitRates();
	
	public String nationalAvgUnitRates();
	
	public String stateAvgUnitRates();
	
	public String greenBlueAvgUnitRates(HashMap<String, String> mapDet);
	
	
	
	
	
	/* Pricing Rental - Rate Per Square Feet History Graph */
	
	public String thisMarketUnitRateHistory(String unitName, String datePrice);
	
	public String nationalUnitRateHistory(String unitName, String datePrice);
	
	public String stateUnitRateHistory(String unitName, String datePrice);
	
	
	/* Compare */
	
	public String greenBlueUnitRateHistory(String unitName, String datePrice, HashMap<String, String> mapDet);
	
	
	
	
	/* Pricing Rental - Average Rates History */
	
	public String thisMarketAvgRateHistory(String unitName, String datePrice);
	public String nationalAvgRateHistory(String unitName, String datePrice);
	public String stateAvgRateHistory(String unitName, String datePrice);
	
	/* Compare */
	
	public String greenBlueAvgRateHistory(String unitName, String datePrice, HashMap<String, String> mapDet);
	
	
	/* Pricing Rental - Rate History By Store */
	
	public String marketRateHistory(String valueType, String unitName, String datePrice);
	public String storeRateHistory(String valueType, int storeId, String unitName, String datePrice);
	
	
	
	
	
	/* Pricing Rental - Rate Volatility History */
	
	public String marketRateVolatilityHistory(String unitName, String dateFrom, String dateTo);
	public String nationalRateVolatilityHistory(String unitName, String dateFrom, String dateTo);
	public String stateRateVolatilityHistory(String unitName, String dateFrom, String dateTo);
	public String greenBlueRateVolatilityHistory(String unitName, String dateFrom, String dateTo, HashMap<String, String> mapDet);
	
	
	
	/* Stores in Market */
	/* Store Types */
	
	public String thisMarketStoreTypes();
	public String nationalStoreTypes();
	public String stateStoreTypes();
	public String greenBlueStoreTypes(HashMap<String, String> mapDet);
	
	
	
	/* Unit Types Offered */
	
	public String thisMarketUnitTypes(String unitName);
	public String nationalUnitTypes(String unitName);
	public String stateUnitTypes(String unitName);
	public String greenBlueUnitTypes(String unitName, HashMap<String, String> mapDet);

}

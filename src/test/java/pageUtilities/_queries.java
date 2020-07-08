package pageUtilities;

import java.util.Date;
import java.util.HashMap;

public class _queries {
	
	
	public static int getUnitID(String unitName) {
		HashMap<String, String> unitIdName;
		String query = null;
		
		switch(_testData.regId) {
		case 1:
			if(!unitName.contains("m²")){
				query = "select UnitType, ID from DefaultUnitSizes where countryid like '1,2' and id between 2 and 17";
			} else { 
 				query = "select UnitType, ID from DefaultUnitSizes where countryid like '3,4,5'";
			}
			break;
		case 3:
			query = "select UnitType, ID from DefaultUnitSizes where RegionId = 3 and id between 39 and 48";
			break;
		}
		unitIdName = _databaseUtils.getMapString(query);
		return Integer.valueOf(unitIdName.get(unitName));
	}

	public static Date maxDatePrice = _databaseUtils.getDate("select DatePrice from MaxDatePrice");
	
	
	
	/* Queries - Market Overview */
	
	public static String overTotalRentSqFo() {
		String query = "";
		if(_testData.marketTypeId==1) {
			query = "select sum(cast (b.RentableSqFt as int)) as RentableSqFt from MarketViewUserStoreCompSet a join stores b on a.StoreId=b.StoreID join RadiusWiseCensusData c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+_testData.userStoreId+" group by totalpopulation";
		} else if(_testData.marketTypeId==3) {
			query = "SELECT sum(COALESCE(s.RentableSqFt,0)) as RentableSqFt FROM ZipcodeWiseCensusData zwc with (nolock) JOIN stores s ON s.zipcode=zwc.zipcode and s.state='"+_testData.state+"' and s.StoreModFlag!=3 and s.City='"+_testData.city+"' JOIN uscities c ON c.City=s.city and  c.State_id=s.State";
		}
		return query;
	}
	
	public static String overSqCapita() {
		String query = "";
		if(_testData.marketTypeId==1) {
			query = "select round(sum(cast (b.RentableSqFt as int))/c.totalpopulation,2) as SqFtCapita from MarketViewUserStoreCompSet a join stores b on a.StoreId=b.StoreID join RadiusWiseCensusData c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+_testData.userStoreId+" group by totalpopulation";
		} else if(_testData.marketTypeId==3){
			query = "Select ROUND((RentableSqFt/population),2) from (SELECT 1 as rsf, sum(COALESCE(s.RentableSqFt,0)) as RentableSqFt FROM ZipcodeWiseCensusData zwc with (nolock) JOIN stores s ON s.zipcode=zwc.zipcode and s.state='"+_testData.state+"' and s.StoreModFlag!=3 and s.City='"+_testData.city+"' JOIN uscities c ON c.City=s.city and  c.State_id=s.State) f join (select 1 as pop, sum(TotalPopulation) as population from ZipcodeWiseCensusData where Zipcode in (Select Zipcode from Zipcodes where City ='"+_testData.city+"' and abbr ='"+_testData.state+"')) s on f.rsf = s.pop";
		}
		return query;
	}
	
	public static String overNoOfStores() {
		String query = "";
		if(_testData.marketTypeId==1) {
			query = "select count(a.storeid) as stores from MarketViewUserStoreCompSet a join stores b on a.StoreId=b.StoreID join RadiusWiseCensusData c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+_testData.userStoreId+" group by totalpopulation";
		} else if(_testData.marketTypeId==3){
			query = "select count(*) from MarketViewUserStoreCompSet with (nolock) where UserStoreId = "+_testData.userStoreId;
		}
		return query;
	}
	
	public static String overAvgRateSqFt() {
		String query = "select round(AVG(value),2) as OVERALLMARKETAVERAGE from (SELECT sp.onlineprice/ nullif(((CASE WHEN ABS(S.width)=0 THEN 1 ELSE ABS(S.width) END)*(CASE WHEN ABS(S.length)=0 THEN 1 ELSE ABS(S.length) END)),0) as Value from SpacePrice sp WITH (NOLOCK) join stores st with (nolock) on sp.StoreID=st.StoreId Join MarketViewUserStoreCompSet mv with (nolock) on mv.storeid=St.StoreId and mv.userstoreid="+_testData.userStoreId+" join space s with (nolock) on s.SpaceID=sp.SpaceID JOIN DefaultUnitSizes d with (nolock) ON  d.id=s.DefaultUnitId and  d.CountryId='1,2' and  d.IsDefault=0 where st.StoreModFlag!=3 and st.RegionId=1 and st.countryid in (1,2) and sp.onlineprice!=0 and sp.IsCurrentPrice=1) as Avg";
		return query;
	}
	
	
	/* All Stores List  - Overview Header*/
	
//	public static String overAllStoNoOfStores() {
//		String query = "select count(*) from stores (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet (nolock) where userstoreid = "+_testData.userStoreId;
//		return query;
//	}
	
	
	public static String overNoOfReits() {
		String query = "select count(*) from stores (nolock) where REIT=1 and storeid in (select storeid from MarketViewUserStoreCompSet (nolock) where userstoreid = "+_testData.userStoreId+")";
		return query;
	}
	
	public static String overNoOfMOps() {
		String query = "select count(*) from stores (nolock) where multioperator=1 and storeid in (select storeid from MarketViewUserStoreCompSet (nolock) where userstoreid = "+_testData.userStoreId+")";
		return query;
	}
	
	public static String overNoOfSOps() {
		String query = "select count(*) from stores (nolock) where singleoperator=1 and storeid in (select storeid from MarketViewUserStoreCompSet (nolock) where userstoreid = "+_testData.userStoreId+")";
		return query;
	}
	
	
	/* All Stores List  - Grid */
	
	public static String gridHeaderValue(String address) {
		String query = "select StoreName, squarefootage, RentableSqFt, ownedby, operatedby, yearbuild, StoreID from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and (address+', '+city+', '+state+' '+ZipCode) = '"+address+"'";
		return query;
	}
	
	
//	public static String gridUnitValue(String unitName, int storeId) {
//		String query = "select top 1 sp.dateprice from SpacePriceHistory SP with (nolock) JOIN Space S ON S.SpaceID=SP.SpaceID JOIN DefaultUnitSizes d ON d.id=S.DefaultUnitId and s.defaultunitid="+getUnitID(unitName)+" Where StoreID in (select storeid from Stores where storeid = "+storeId+") and SP.SpaceId in (Select SpaceID from Space where defaultunitid in (select id from DefaultUnitSizes where countryid in ('1,2'))) and sp.onlineprice>1 and sp.onlineprice<1000 order by 1 desc";
//		return query;
//	}
	
	public static String gridUnitValue(String unitName, int storeId) {
		String query = "";
		if(!unitName.toLowerCase().contains("rv")) {
			query = "select top 1 sp.dateprice from SpacePriceHistory SP with (nolock) JOIN Space S ON S.SpaceID=SP.SpaceID JOIN DefaultUnitSizes d ON d.id=S.DefaultUnitId and s.defaultunitid="+getUnitID(unitName)+" Where StoreID in (select storeid from Stores where storeid = "+storeId+") and SP.SpaceId in (Select SpaceID from Space where defaultunitid in (select id from DefaultUnitSizes where countryid in ('1,2'))) and sp.onlineprice>1 and sp.onlineprice<1000 order by 1 desc";
		} else {
			query = "select top 1 sp.dateprice from SpacePriceHistory SP with (nolock) JOIN Space S ON S.SpaceID=SP.SpaceID JOIN DefaultUnitSizes d ON d.id=S.DefaultUnitId and s.defaultunitid=18 Where StoreID in (select storeid from Stores where storeid = "+storeId+") and SP.SpaceId in (Select SpaceID from Space where defaultunitid in (select id from DefaultUnitSizes where countryid in ('1,2'))) and sp.onlineprice>1 and sp.onlineprice<1000 order by 1 desc";
		}
		return query;
	}
	
	
	/* Executive Summary - No of Stores */
	
	public static String execSummNoOfStores(int i) {
		String query = "";
		if(i==1) {
			query = execSumThisMarket();
		} else if(i==2) {
			query = execSumNational();
		} else if(i==3) {
			query = execSumState();
		}
		return query;
	}
	
	
	public static String execSumThisMarket() {
		String query = "select COUNT(*) as Storesinmarket from stores  with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet with (nolock) where userstoreid = "+_testData.userStoreId+") and storemodflag!= 3";
		return query;
	}
	
	public static String execSumNational() {
		String query = "select COUNT(*) as Storesinnation from stores with (nolock) where storemodflag != 3 and CountryId=2 and State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1)";
		return query;
	}
	
	public static String execSumState() {
		String query = "select COUNT(*) as Storesinstate from stores with (nolock) where storemodflag != 3 and CountryId=2 and State='"+_testData.state+"'";
		return query;
	}
	
	
	
	
}

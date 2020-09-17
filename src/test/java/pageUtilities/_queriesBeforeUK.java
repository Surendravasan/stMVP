package pageUtilities;

import java.util.Date;
import java.util.HashMap;

public class _queriesBeforeUK {
	
	
	public static int getUnitID(String unitName) {
		HashMap<String, String> unitIdName;
		String query = null;
		switch(_testData.regId) {
		case 1:
			if(!unitName.contains("m²")){
//				query = "select LOWER(UnitType), ID from DefaultUnitSizes where countryid like '1,2' and id between 2 and 17";
				query = "select LOWER(UnitType), ID from DefaultUnitSizes where countryid like '1,2'";
			} else { 
 				query = "select LOWER(UnitType), ID from DefaultUnitSizes where countryid like '3,4,5'";
			}
			break;
		case 3:
			query = "select LOWER(UnitType), ID from DefaultUnitSizes where countryid like '7,8,9,10'";
			break;
		}
		unitIdName = _databaseUtils.getMapString(query);
		return Integer.valueOf(unitIdName.get(unitName.toLowerCase()));
	}
	

	public static Date maxDatePrice = _databaseUtils.getDate("select DatePrice from MaxDatePrice");
	
	public static String getStateFullName() {
		return "select Name from LookupStates where id = '"+_testData.state+"'";
	}
	
	
	/* Compare Markets */
	
	public static String getMarketType(int userStoreId) {
		String query = "select MarketChoice from MarketViewUserStores where userstoreid = "+userStoreId;
		return query;
	}
	
	public static String getRadiusMarketDet(int userStoreId) {
//		String query = "select UserStoreId, MarketChoice, LEFT(ZoneCoverage, CHARINDEX(' ', ZoneCoverage)-1) as zone from MarketViewUserStores where userstoreid="+userStoreId;
		String query = "select UserStoreId, MarketChoice, City, State, LEFT(ZoneCoverage, CHARINDEX(' ', ZoneCoverage)-1) as zone from MarketViewUserStores where userstoreid="+userStoreId;
		return query;
	}
	
	public static String getCityMarketDet(int userStoreId) {
//		String query = "Select UserStoreId, MarketChoice, Substring(City, 1,Charindex(',', City)-1) as City, Substring(City, Charindex(',', City)+1, LEN(City)) as State from MarketViewUserStores where userstoreid = "+userStoreId;
		String query = "Select UserStoreId, MarketChoice, Substring(City, 1,Charindex(',', City)-1) as City, State, ZoneCoverage from MarketViewUserStores where userstoreid="+userStoreId;
		return query;
	}
	
	public static String getMarketType4(int userStoreId) {
		String query = "select UserStoreId, MarketChoice, LEFT(ZoneCoverage, CHARINDEX(',', ZoneCoverage)-1) as City, State, ZoneCoverage from MarketViewUserStores where userstoreid="+userStoreId;
		return query;
	}
	
	
	
	
	/* Queries - Market Overview */
	
	public static String overTotalRentSqFo() {
		String query = "";
		if(_testData.regId==1) {
			if(_testData.marketTypeId==1) {
				query = "select sum(cast (b.RentableSqFt as int)) as RentableSqFt from MarketViewUserStoreCompSet a join stores b on a.StoreId=b.StoreID join RadiusWiseCensusData c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+_testData.userStoreId+" group by totalpopulation";
			} else if(_testData.marketTypeId==3) {
				query = "SELECT sum(COALESCE(s.RentableSqFt,0)) as RentableSqFt FROM ZipcodeWiseCensusData zwc with (nolock) JOIN stores s ON s.zipcode=zwc.zipcode and s.state='"+_testData.state+"' and s.StoreModFlag!=3 and s.City='"+_testData.city+"' JOIN uscities c ON c.City=s.city and  c.State_id=s.State";
			}
		} else if(_testData.regId==3) {
			query = "select sum(cast (RentableSqFt as int)) as TotalSqFt from stores with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet with (nolock) where userstoreid = "+_testData.userStoreId+")";
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
		if(_testData.regId==1) {
			if(_testData.marketTypeId==1) {
				query = "select count(a.storeid) as stores from MarketViewUserStoreCompSet a join stores b on a.StoreId=b.StoreID join RadiusWiseCensusData c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+_testData.userStoreId+" group by totalpopulation";
			} else if(_testData.marketTypeId==3){
				query = "select count(*) from MarketViewUserStoreCompSet with (nolock) where UserStoreId = "+_testData.userStoreId;
			}
		} else if(_testData.regId==3) {
			query = "select count(*) as NoOfStores from stores with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet with (nolock) where userstoreid = "+_testData.userStoreId+")";
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
	
	public static String storesCount() {
		String query = "select count(*) as stores from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+")";
		return query;
	}
	
	public static String gridHeaderValue(String address) {
		String query = "select StoreName, squarefootage, RentableSqFt, ownedby, operatedby, yearbuild, StoreID from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and (address+', '+city+', '+state+' '+ZipCode) = '"+address+"'";
		return query;
	}
	
	
//	public static String gridUnitValue(String unitName, int storeId) {
//		String query = "";
//		if(!unitName.toLowerCase().contains("rv")) {
//			query = "select top 1 sp.dateprice from SpacePriceHistory SP with (nolock) JOIN Space S ON S.SpaceID=SP.SpaceID JOIN DefaultUnitSizes d ON d.id=S.DefaultUnitId and s.defaultunitid="+getUnitID(unitName)+" Where StoreID in (select storeid from Stores where storeid = "+storeId+") and SP.SpaceId in (Select SpaceID from Space where defaultunitid in (select id from DefaultUnitSizes where countryid in ('1,2'))) and sp.onlineprice>1 and sp.onlineprice<1000 order by 1 desc";
//		} else {
//			query = "select top 1 sp.dateprice from SpacePriceHistory SP with (nolock) JOIN Space S ON S.SpaceID=SP.SpaceID JOIN DefaultUnitSizes d ON d.id=S.DefaultUnitId and s.defaultunitid=18 Where StoreID in (select storeid from Stores where storeid = "+storeId+") and SP.SpaceId in (Select SpaceID from Space where defaultunitid in (select id from DefaultUnitSizes where countryid in ('1,2'))) and sp.onlineprice>1 and sp.onlineprice<1000 order by 1 desc";
//		}
//		return query;
//	}
	
	public static String gridUnitValue(String unitName, int storeId) {
		String query = "";
		if(_testData.regId==1) {
			if(!unitName.toLowerCase().contains("rv")) {
				query = "select top 1 sp.dateprice, spaceprice.IsCurrentPrice from SpacePriceHistory SP with (nolock) JOIN Space S with (nolock) ON S.SpaceID=SP.SpaceID JOIN DefaultUnitSizes d with (nolock) ON d.id=S.DefaultUnitId and s.defaultunitid="+getUnitID(unitName)+" JOIN stores with (nolock) on sp.StoreID = stores.StoreID and stores.storeid = "+storeId+" join space with (nolock) on sp.SpaceID = space.SpaceID join DefaultUnitSizes de with (nolock) on space.DefaultUnitId = de.ID left join SpacePrice with (nolock) on sp.SpaceID = spaceprice.SpaceID where de.countryid in ('1,2') and sp.onlineprice>1 and sp.onlineprice<1000 order by 1 desc, 2 desc";
			} else {
				query = "select top 1 sp.dateprice, spaceprice.IsCurrentPrice from SpacePriceHistory SP with (nolock) JOIN Space S with (nolock) ON S.SpaceID=SP.SpaceID JOIN DefaultUnitSizes d with (nolock) ON d.id=S.DefaultUnitId and s.defaultunitid=18 JOIN stores with (nolock) on sp.StoreID = stores.StoreID and stores.storeid = "+storeId+" join space with (nolock) on sp.SpaceID = space.SpaceID join DefaultUnitSizes de with (nolock) on space.DefaultUnitId = de.ID left join SpacePrice with (nolock) on sp.SpaceID = spaceprice.SpaceID where de.countryid in ('1,2') and sp.onlineprice>1 and sp.onlineprice<1000 order by 1 desc, 2 desc";
			}
		} else if(_testData.regId==3) {
			if(!unitName.toLowerCase().contains("rv")) {
				String unitName1 = unitName.replace("Sq.ft", " Sq.ft"); 
				query = "select top 1 sp.dateprice, spaceprice.IsCurrentPrice from SpacePriceHistory SP with (nolock) JOIN Space S with (nolock) ON S.SpaceID=SP.SpaceID JOIN DefaultUnitSizes d with (nolock) ON d.id=S.DefaultUnitId and s.defaultunitid="+getUnitID(unitName1)+" JOIN stores with (nolock) on sp.StoreID = stores.StoreID and stores.storeid = "+storeId+" join space with (nolock) on sp.SpaceID = space.SpaceID join DefaultUnitSizes de with (nolock) on space.DefaultUnitId = de.ID left join SpacePrice with (nolock) on sp.SpaceID = spaceprice.SpaceID where de.countryid in ('7,8,9,10') and sp.onlineprice>1 and sp.onlineprice<1000 order by 1 desc, 2 desc";
			} else {
				query = "select top 1 sp.dateprice, spaceprice.IsCurrentPrice from SpacePriceHistory SP with (nolock) JOIN Space S with (nolock) ON S.SpaceID=SP.SpaceID JOIN DefaultUnitSizes d with (nolock) ON d.id=S.DefaultUnitId and s.defaultunitid=18 JOIN stores with (nolock) on sp.StoreID = stores.StoreID and stores.storeid = "+storeId+" join space with (nolock) on sp.SpaceID = space.SpaceID join DefaultUnitSizes de with (nolock) on space.DefaultUnitId = de.ID left join SpacePrice with (nolock) on sp.SpaceID = spaceprice.SpaceID where de.countryid in ('7,8,9,10') and sp.onlineprice>1 and sp.onlineprice<1000 order by 1 desc, 2 desc";
			}
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
	
	
	/* Executive Summary - New Developments */
	
	public static String execSumTotalDevs() {
		String query = "select count(*) from StoresKnownDevelopement";
		return query;
	}
	
	public static String execSumMarketDevs() {
		String query = "select count(*) from StoresKnownDevelopement where city = '"+_testData.city+"' and state = '"+_testData.state+"'";
		return query;
	}
	
	
	
	
	/* Known Developments */
	
	public static String knowDevNoOfDevs() {
		String query = "select count(*) as noOfdev from StoresKnownDevelopement where state='"+_testData.state+"' and City='"+_testData.city+"'";
		return query;
	}
	
	
	public static String knownDevGridVal(String address) {
		String query = "select STAGE, PropertyType as 'PROJECT TYPE', StoreName as 'STORE NAME', OwnerName as 'OWNER NAME', OperatorManagementCompanyName as 'OPERATOR/MANAGEMENT', OperatorType as 'OPERATOR TYPE', (StreetAddress+', '+City+', '+State+' '+ZipCode) as ADDRESS, propertyAcres as ACRES, propertyBuildingSquareFootage as 'BUILDING SQUARE FOOTAGE', propertyFloors as FLOORS, propertyNumberOfUnits as 'NUMBER OF UNITS', propertyNumberOfBuildings as 'NUMBER OF BUILDINGS', propertyEstimatedValue as 'ESTIMATED VALUE', FORMAT((CAST(propertyExpectedToOpen as date)),'dd-MMM-yyyy') as 'EXPECTED OPEN DATE', propertyAdditionalPropertyInfo as 'ADDITIONAL PROPERTY INFO', SourceUrl as 'SOURCE URL', NOTES, FORMAT(ModifiedDate, 'dd-MMM-yyyy') as 'LAST CHECKED DATE' from StoresKnownDevelopement where state='"+_testData.state+"' and City='"+_testData.city+"' and (StreetAddress+', '+City+', '+State+' '+ZipCode) = '"+address+"'";
		return query;
	}
	
	
	/* Demographics - Population */
		
	public static String popMarket() {
		String query = "";
		if(_testData.regId==1) {
			if(_testData.marketTypeId==1) {
				query = "select c.TotalPopulation as 'POPULATION SERVED', round((cast(c.TotalPopulation as int))/(c.LandArea),2) as 'POPULATION DENSITY (PER SQ MI)', round((cast(c.TotalPopulation as float))/COUNT(distinct a.storeid),0) as 'POPULATION/STORE' from MarketViewUserStoreCompSet a join RadiusWiseCensusData c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+_testData.userStoreId+" and c.ZoneCoverage="+_testData.radius+" group by TotalPopulation, LandArea";
			} else if(_testData.marketTypeId==3){
				query = "select populationserved as 'POPULATION SERVED', populationdensity as 'POPULATION DENSITY (PER SQ MI)', round(populationserved/storecount,0) as 'POPULATION/STORE' from (SELECT 1 as tomap1, SUM(zwc.TotalPopulation) as populationserved, round(sum((zwc.TotalPopulation))/sum(zwc.LandArea),2) as populationdensity FROM ZipcodeWiseCensusData zwc with (nolock) JOIN ZipCodes z on z.zipcode=zwc.Zipcode where z.City='"+_testData.city+"' and z.abbr='"+_testData.state+"') as serden join (select 1 as tomap2, count(*) as storecount from MarketViewUserStoreCompSet with (nolock) where UserStoreId = "+_testData.userStoreId+") as sto on serden.tomap1 = sto.tomap2";
			}
		} else if(_testData.regId==3) {
			if(_testData.marketTypeId==1) {
				query = "select c.TotalPopulation as 'POPULATION SERVED', round((cast(c.TotalPopulation as int))/(c.LandArea),2) as 'POPULATION DENSITY (PER SQ MI)', round((cast(c.TotalPopulation as float))/COUNT(distinct a.storeid),0) as 'POPULATION/STORE' from MarketViewUserStoreCompSet a join RadiusWiseCensusDataUK c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+_testData.userStoreId+" and c.ZoneCoverage="+_testData.radius+" group by TotalPopulation, LandArea";
			} else if(_testData.marketTypeId==3){
				query = "select populationserved as 'POPULATION SERVED', populationdensity as 'POPULATION DENSITY (PER SQ MI)', round(populationserved/storecount,0) as 'POPULATION/STORE' from (SELECT 1 as tomap1, SUM(TotalPopulation) as populationserved, round(sum((TotalPopulation))/sum(LandArea),2) as populationdensity FROM CityWiseCensusDataUK cwc with (nolock) where City='"+_testData.city+"') as serden join (select 1 as tomap2, count(*) as storecount from MarketViewUserStoreCompSet with (nolock) where UserStoreId = "+_testData.userStoreId+") as sto on serden.tomap1=sto.tomap2";
			}
		}
		return query;
	}
	
	public static String popNational() {
		String query = "";
		if(_testData.regId==1) {
			query = "select populationserved as 'POPULATION SERVED', populationdensity as 'POPULATION DENSITY (PER SQ MI)', ROUND(populationserved/Storesinnation,0) as 'POPULATION/STORE' from (SELECT 1 as tomap1, SUM(zwc.TotalPopulation) as populationserved FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2 and IsUnionTerritories is null)) as ser join (SELECT 1 as tomap2, round(sum((zwc.TotalPopulation))/sum(zwc.LandArea),2) as populationdensity FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2 and IsUnionTerritories is null)) as den on ser.tomap1 = den.tomap2 join (select 1 as tomap3, COUNT(*) as Storesinnation from stores where storemodflag != 3 and CountryId=2 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) as sto on den.tomap2 = sto.tomap3";
		} else if(_testData.regId==3) {
			query = "select populationserved as 'POPULATION SERVED', populationdensity as 'POPULATION DENSITY (PER SQ MI)', ROUND(populationserved/Storesinnation,0) as 'POPULATION/STORE' from (SELECT 1 as tomap1, SUM(zwc.TotalPopulation) as populationserved FROM NationalWiseCensusDataUK zwc WITH(NOLOCK)) as ser join (SELECT 1 as tomap2, round(sum((zwc.TotalPopulation))/sum(zwc.LandArea),2) as populationdensity FROM NationalWiseCensusDataUK zwc  WITH(NOLOCK)) as den on ser.tomap1 = den.tomap2 join (select 1 as tomap3, COUNT(*) as Storesinnation from stores where storemodflag != 3 and CountryId in (7,8,9,10) and State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) as sto on den.tomap2 = sto.tomap3";
		}
		
		return query;
	}
	
	public static String popState() {
		String query = "select populationserved as 'POPULATION SERVED', populationdensity as 'POPULATION DENSITY (PER SQ MI)', ROUND(populationserved/Storesinstate,0) as 'POPULATION/STORE' from (SELECT 1 as tomap1, SUM(zwc.TotalPopulation) as populationserved FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2 and id='"+_testData.state+"')) as ser join (SELECT 1 as tomap2, round(sum((zw.TotalPopulation))/sum(zw.LandArea),2) as populationdensity FROM ZipcodeWiseCensusData zw  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zw.ZipCode and z.abbr in (Select id from LookupStates where countryid=2 and id='"+_testData.state+"')) as den on tomap1=tomap2 join (select 1 as tomap3, COUNT(*) as Storesinstate from stores where storemodflag != 3 and CountryId=2 and State='"+_testData.state+"') as sto on tomap2 = tomap3";
		return query;
	}
	
	
	/* Demographics - Household */
	
	public static String houHolMarket() {
		String query = "";
		if(_testData.regId==1) {
			if(_testData.marketTypeId==1) {
				query = "select c.TotalHouseHolds as HOUSEHOLDS, round((cast(c.TotalHouseHolds as int))/(c.LandArea),2) as 'HOUSEHOLD DENSITY (PER SQ MI)', round((cast(c.TotalHouseHolds as float))/COUNT(distinct a.storeid),0) as 'HOUSEHOLDS/STORE' from MarketViewUserStoreCompSet a join RadiusWiseCensusData c on a.UserStoreId=c.UserStoreID where a.UserStoreId= "+_testData.userStoreId+" and c.ZoneCoverage="+_testData.radius+" group by TotalHouseHolds, LandArea";
			} else if(_testData.marketTypeId==3){
				query = "select households as HOUSEHOLDS, hhdensity as 'HOUSEHOLD DENSITY (PER SQ MI)', round(households/storecount,0) as 'HOUSEHOLDS/STORE' from (SELECT 1 as tomap1, SUM(zwc.TotalHouseholds) as households, round(sum((zwc.TotalHouseholds))/sum(zwc.LandArea),2) as hhdensity FROM ZipcodeWiseCensusData zwc with (nolock) JOIN ZipCodes z on z.zipcode=zwc.Zipcode where z.City='"+_testData.city+"' and z.abbr='"+_testData.state+"') as serden join (select 1 as tomap2, count(*) as storecount from MarketViewUserStoreCompSet with (nolock) where UserStoreId = "+_testData.userStoreId+") as sto on serden.tomap1 = sto.tomap2";
			}
		} else if(_testData.regId==3) {
			if(_testData.marketTypeId==1) {
				query = "select c.TotalHouseHolds as HOUSEHOLDS, round((cast(c.TotalHouseHolds as int))/(c.LandArea),2) as 'HOUSEHOLD DENSITY (PER SQ MI)', round((cast(c.TotalHouseHolds as float))/COUNT(distinct a.storeid),0) as 'HOUSEHOLDS/STORE' from MarketViewUserStoreCompSet a join RadiusWiseCensusDataUK c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+_testData.userStoreId+" and c.ZoneCoverage="+_testData.radius+" group by TotalHouseHolds, LandArea";
			} else if(_testData.marketTypeId==3){
				query = "select households as HOUSEHOLDS, populationdensity as 'HOUSEHOLD DENSITY (PER SQ MI)', round(households/storecount,0) as 'HOUSEHOLDS/STORE' from (SELECT 1 as tomap1, SUM(TotalHouseHolds) as households, round(sum((TotalHouseHolds))/sum(LandArea),2) as populationdensity FROM CityWiseCensusDataUK cwc with (nolock) where City='"+_testData.city+"') as serden join (select 1 as tomap2, count(*) as storecount from MarketViewUserStoreCompSet with (nolock) where UserStoreId = "+_testData.userStoreId+") as sto on serden.tomap1=sto.tomap2";
			}
		}
		
		return query;
	}
	
	public static String houHolNational() {
		String query = "";
		if(_testData.regId==1) {
			query = "select households as HOUSEHOLDS, hhdensity as 'HOUSEHOLD DENSITY (PER SQ MI)', ROUND(households/Storesinnation,0) as 'HOUSEHOLDS/STORE' from  (SELECT 1 as tomap1, SUM(zwc.TotalHouseholds) as households FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2 and IsUnionTerritories is null)) as hh join (SELECT 1 as tomap2, round(sum((zwc.TotalHouseholds))/sum(zwc.LandArea),2) as hhdensity FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2 and IsUnionTerritories is null)) as den on hh.tomap1 = den.tomap2 join (select 1 as tomap3, COUNT(*) as Storesinnation from stores where storemodflag != 3 and CountryId=2 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) as sto on den.tomap2 = sto.tomap3";
		} else if(_testData.regId==3) {
			query = "select households as HOUSEHOLDS, hhdensity as 'HOUSEHOLD DENSITY (PER SQ MI)', ROUND(households/Storesinnation,0) as 'HOUSEHOLDS/STORE' from (SELECT 1 as tomap1, SUM(zwc.TotalHouseholds) as households FROM NationalWiseCensusDataUK zwc WITH(NOLOCK)) as ser join (SELECT 1 as tomap2, round(sum((zwc.TotalHouseholds))/sum(zwc.LandArea),2) as hhdensity FROM NationalWiseCensusDataUK zwc  WITH(NOLOCK)) as den on ser.tomap1 = den.tomap2 join (select 1 as tomap3, COUNT(*) as Storesinnation from stores where storemodflag != 3 and CountryId in (7,8,9,10) and State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) as sto on den.tomap2 = sto.tomap3";
		}
		return query;
	}
	
	public static String houHolState() {
		String query = "select household as HOUSEHOLDS, hhdensity as 'HOUSEHOLD DENSITY (PER SQ MI)', ROUND(household/Storesinstate,0) as 'HOUSEHOLDS/STORE'  from (SELECT 1 as tomap1, SUM(zwc.TotalHouseholds) as household FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2 and id= '"+_testData.state+"')) as hh join (SELECT 1 as tomap2, round(sum((zwc.TotalHouseholds))/sum(zwc.LandArea),2) as hhdensity FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2 and id= '"+_testData.state+"')) as den on hh.tomap1 = den.tomap2 join (select 1 as tomap3, COUNT(*) as Storesinstate from stores where storemodflag != 3 and CountryId=2 and State= '"+_testData.state+"') as sto on sto.tomap3 = den.tomap2";
		return query;
	}
	
	/* Demographics - Rental Properties */
	
	public static String renPropMarket() {
		String query = "";
		if(_testData.regId==1) {
			if(_testData.marketTypeId==1) {
				query = "select c.TotalRenterOccupied as 'RENTAL PROPERTIES', round((cast(c.TotalRenterOccupied as int))/(c.LandArea),2) as 'RENTAL PROPERTIES DENSITY (PER SQ MI)', round((cast(c.TotalRenterOccupied as float))/COUNT(distinct a.storeid),0) as 'RENTAL PROPERTIES/STORE' from MarketViewUserStoreCompSet a join RadiusWiseCensusData c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+_testData.userStoreId+"  and c.ZoneCoverage="+_testData.radius+" group by TotalRenterOccupied, LandArea";
			} else if(_testData.marketTypeId==3){
				query = "select rentalproperties as 'RENTAL PROPERTIES', rendensity as 'RENTAL PROPERTIES DENSITY (PER SQ MI)', round(rentalproperties/storecount,0) as 'RENTAL PROPERTIES/STORE' from (SELECT 1 as tomap1, SUM(zwc.TotalRenterOccupied) as rentalproperties, round(sum((zwc.TotalRenterOccupied))/sum(zwc.LandArea),2) as rendensity FROM ZipcodeWiseCensusData zwc with (nolock) JOIN ZipCodes z on z.zipcode=zwc.Zipcode where z.City='"+_testData.city+"' and z.abbr='"+_testData.state+"') as serden join (select 1 as tomap2, count(*) as storecount from MarketViewUserStoreCompSet with (nolock) where UserStoreId = "+_testData.userStoreId+") as sto on serden.tomap1 = sto.tomap2";
			}
		} else if(_testData.regId==3) {
			if(_testData.marketTypeId==1) {
				query = "select c.TotalRenterOccupied as 'RENTAL PROPERTIES', round((cast(c.TotalRenterOccupied as int))/(c.LandArea),2) as 'RENTAL PROPERTIES DENSITY (PER SQ MI)', round((cast(c.TotalRenterOccupied as float))/COUNT(distinct a.storeid),0) as 'RENTAL PROPERTIES/STORE' from MarketViewUserStoreCompSet a join RadiusWiseCensusDataUK c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+_testData.userStoreId+" and c.ZoneCoverage="+_testData.radius+" group by TotalRenterOccupied, LandArea";
			} else if(_testData.marketTypeId==3){
				query = "select rentalproperties as 'RENTAL PROPERTIES', populationdensity as 'RENTAL PROPERTIES DENSITY (PER SQ MI)', round(rentalproperties/storecount,0) as 'RENTAL PROPERTIES/STORE' from (SELECT 1 as tomap1, SUM(TotalRenterOccupied) as rentalproperties, round(sum((TotalRenterOccupied))/sum(LandArea),2) as populationdensity FROM CityWiseCensusDataUK cwc with (nolock) where City='"+_testData.city+"') as serden join (select 1 as tomap2, count(*) as storecount from MarketViewUserStoreCompSet with (nolock) where UserStoreId = "+_testData.userStoreId+") as sto on serden.tomap1=sto.tomap2";
			}
		}
		
		return query;
	}
	
	public static String renPropNational() {
		String query = "";
		if(_testData.regId==1) {
			query = "select rentalproperties as 'RENTAL PROPERTIES', rendensity as 'RENTAL PROPERTIES DENSITY (PER SQ MI)', ROUND(rentalproperties/Storesinnation,0) as 'RENTAL PROPERTIES/STORE' from (SELECT 1 as tomap1, SUM(zwc.TotalRenterOccupied) as rentalproperties FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2 and IsUnionTerritories is null)) as ren join (SELECT 1 as tomap2, round(sum((zwc.TotalRenterOccupied))/sum(zwc.LandArea),2) as rendensity FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2 and IsUnionTerritories is null)) as den on ren.tomap1 = den.tomap2 join (select 1 as tomap3, COUNT(*) as Storesinnation from stores where storemodflag != 3 and CountryId=2 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) as sto on den.tomap2 = sto.tomap3"; 
		} else if(_testData.regId==3) {
			query = "select rentalproperties as 'RENTAL PROPERTIES', rendensity as 'RENTAL PROPERTIES DENSITY (PER SQ MI)', ROUND(rentalproperties/Storesinnation,0) as 'RENTAL PROPERTIES/STORE' from (SELECT 1 as tomap1, SUM(zwc.TotalRenterOccupied) as rentalproperties FROM NationalWiseCensusDataUK zwc WITH(NOLOCK)) as ser join (SELECT 1 as tomap2, round(sum((zwc.TotalRenterOccupied))/sum(zwc.LandArea),2) as rendensity FROM NationalWiseCensusDataUK zwc  WITH(NOLOCK)) as den on ser.tomap1 = den.tomap2 join (select 1 as tomap3, COUNT(*) as Storesinnation from stores where storemodflag != 3 and CountryId in (7,8,9,10) and State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) as sto on den.tomap2 = sto.tomap3";
		}
		return query;
	}
	
	public static String renPropState() {
		String query = "select rentalproperties as 'RENTAL PROPERTIES', rendensity as 'RENTAL PROPERTIES DENSITY (PER SQ MI)', ROUND(rentalproperties/Storesinstate,0) as 'RENTAL PROPERTIES/STORE' from (SELECT 1 as tomap1, SUM(zwc.TotalRenterOccupied) as rentalproperties FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2 and id= '"+_testData.state+"')) as ren join (SELECT 1 as tomap2, round(sum((zwc.TotalRenterOccupied))/sum(zwc.LandArea),2) as rendensity FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2 and id= '"+_testData.state+"')) as den on ren.tomap1 = den.tomap2 join (select 1 as tomap3, COUNT(*) as Storesinstate from stores where storemodflag != 3 and CountryId=2 and State= '"+_testData.state+"') as sto on den.tomap2 = sto.tomap3";
		return query;
	}
	
	
	/* Compare Markets */
	
	public static String popGreenBlue(HashMap<String, String> mapDet) {
		String query = "";
		if(_testData.regId==1) {
			if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
				query = "select c.TotalPopulation as 'POPULATION SERVED', round((cast(c.TotalPopulation as int))/(c.LandArea),2) as 'POPULATION DENSITY (PER SQ MI)', round((cast(c.TotalPopulation as float))/COUNT(distinct a.storeid),0) as 'POPULATION/STORE' from MarketViewUserStoreCompSet a join RadiusWiseCensusData c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+mapDet.get("UserStoreId")+" and c.ZoneCoverage="+mapDet.get("zone")+" group by TotalPopulation, LandArea";
			} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
				query = "select populationserved as 'POPULATION SERVED', populationdensity as 'POPULATION DENSITY (PER SQ MI)', round(populationserved/storecount,0) as 'POPULATION/STORE' from (SELECT 1 as tomap1, SUM(zwc.TotalPopulation) as populationserved, round(sum((zwc.TotalPopulation))/sum(zwc.LandArea),2) as populationdensity FROM ZipcodeWiseCensusData zwc with (nolock) JOIN ZipCodes z on z.zipcode=zwc.Zipcode where z.City='"+mapDet.get("City")+"' and z.abbr='"+mapDet.get("State")+"') as serden join (select 1 as tomap2, count(*) as storecount from MarketViewUserStoreCompSet with (nolock) where UserStoreId = "+mapDet.get("UserStoreId")+") as sto on serden.tomap1 = sto.tomap2";
			}
		} else if(_testData.regId==3) {
			if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
				query = "select c.TotalHouseHolds as HOUSEHOLDS, round((cast(c.TotalHouseHolds as int))/(c.LandArea),2) as 'HOUSEHOLD DENSITY (PER SQ MI)', round((cast(c.TotalHouseHolds as float))/COUNT(distinct a.storeid),0) as 'HOUSEHOLDS/STORE' from MarketViewUserStoreCompSet a join RadiusWiseCensusDataUK c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+mapDet.get("UserStoreId")+" and c.ZoneCoverage="+mapDet.get("zone")+" group by TotalHouseHolds, LandArea";
			} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
				query = "select households as HOUSEHOLDS, populationdensity as 'HOUSEHOLD DENSITY (PER SQ MI)', round(households/storecount,0) as 'HOUSEHOLDS/STORE' from (SELECT 1 as tomap1, SUM(TotalHouseHolds) as households, round(sum((TotalHouseHolds))/sum(LandArea),2) as populationdensity FROM CityWiseCensusDataUK cwc with (nolock) where City='"+mapDet.get("City")+"') as serden join (select 1 as tomap2, count(*) as storecount from MarketViewUserStoreCompSet with (nolock) where UserStoreId = "+mapDet.get("UserStoreId")+") as sto on serden.tomap1=sto.tomap2";
			}
		}
		return query;
	}
	
	
	public static String houHolGreenBlue(HashMap<String, String> mapDet) {
		String query = "";
		if(_testData.regId==1) {
			if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
				query = "select c.TotalHouseHolds as HOUSEHOLDS, round((cast(c.TotalHouseHolds as int))/(c.LandArea),2) as 'HOUSEHOLD DENSITY (PER SQ MI)', round((cast(c.TotalHouseHolds as float))/COUNT(distinct a.storeid),0) as 'HOUSEHOLDS/STORE' from MarketViewUserStoreCompSet a join RadiusWiseCensusData c on a.UserStoreId=c.UserStoreID where a.UserStoreId= "+mapDet.get("UserStoreId")+" and c.ZoneCoverage="+mapDet.get("zone")+" group by TotalHouseHolds, LandArea";
			} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
				query = "select households as HOUSEHOLDS, hhdensity as 'HOUSEHOLD DENSITY (PER SQ MI)', round(households/storecount,0) as 'HOUSEHOLDS/STORE' from (SELECT 1 as tomap1, SUM(zwc.TotalHouseholds) as households, round(sum((zwc.TotalHouseholds))/sum(zwc.LandArea),2) as hhdensity FROM ZipcodeWiseCensusData zwc with (nolock) JOIN ZipCodes z on z.zipcode=zwc.Zipcode where z.City='"+mapDet.get("City")+"' and z.abbr='"+mapDet.get("State")+"') as serden join (select 1 as tomap2, count(*) as storecount from MarketViewUserStoreCompSet with (nolock) where UserStoreId = "+mapDet.get("UserStoreId")+") as sto on serden.tomap1 = sto.tomap2";
			}
		} else if(_testData.regId==3) {
			if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
				query = "select c.TotalHouseHolds as HOUSEHOLDS, round((cast(c.TotalHouseHolds as int))/(c.LandArea),2) as 'HOUSEHOLD DENSITY (PER SQ MI)', round((cast(c.TotalHouseHolds as float))/COUNT(distinct a.storeid),0) as 'HOUSEHOLDS/STORE' from MarketViewUserStoreCompSet a join RadiusWiseCensusDataUK c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+mapDet.get("UserStoreId")+" and c.ZoneCoverage="+mapDet.get("zone")+" group by TotalHouseHolds, LandArea";
			} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
				query = "select households as HOUSEHOLDS, populationdensity as 'HOUSEHOLD DENSITY (PER SQ MI)', round(households/storecount,0) as 'HOUSEHOLDS/STORE' from (SELECT 1 as tomap1, SUM(TotalHouseHolds) as households, round(sum((TotalHouseHolds))/sum(LandArea),2) as populationdensity FROM CityWiseCensusDataUK cwc with (nolock) where City='"+mapDet.get("City")+"') as serden join (select 1 as tomap2, count(*) as storecount from MarketViewUserStoreCompSet with (nolock) where UserStoreId = "+mapDet.get("UserStoreId")+") as sto on serden.tomap1=sto.tomap2";
			}
		}
		return query;
	}
	
	public static String rentalGreenBlue(HashMap<String, String> mapDet) {
		String query = "";
		if(_testData.regId==1) {
			if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
				query = "select c.TotalRenterOccupied as 'RENTAL PROPERTIES', round((cast(c.TotalRenterOccupied as int))/(c.LandArea),2) as 'RENTAL PROPERTIES DENSITY (PER SQ MI)', round((cast(c.TotalRenterOccupied as float))/COUNT(distinct a.storeid),0) as 'RENTAL PROPERTIES/STORE' from MarketViewUserStoreCompSet a join RadiusWiseCensusData c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+mapDet.get("UserStoreId")+" and c.ZoneCoverage="+mapDet.get("zone")+" group by TotalRenterOccupied, LandArea";
			} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
				query = "select rentalproperties as 'RENTAL PROPERTIES', rendensity as 'RENTAL PROPERTIES DENSITY (PER SQ MI)', round(rentalproperties/storecount,0) as 'RENTAL PROPERTIES/STORE' from (SELECT 1 as tomap1, SUM(zwc.TotalRenterOccupied) as rentalproperties, round(sum((zwc.TotalRenterOccupied))/sum(zwc.LandArea),2) as rendensity FROM ZipcodeWiseCensusData zwc with (nolock) JOIN ZipCodes z on z.zipcode=zwc.Zipcode where z.City='"+mapDet.get("City")+"' and z.abbr='"+mapDet.get("State")+"') as serden join (select 1 as tomap2, count(*) as storecount from MarketViewUserStoreCompSet with (nolock) where UserStoreId = "+mapDet.get("UserStoreId")+") as sto on serden.tomap1 = sto.tomap2";
			}
		} else if(_testData.regId==3) {
			if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
				query = "select c.TotalRenterOccupied as 'RENTAL PROPERTIES', round((cast(c.TotalRenterOccupied as int))/(c.LandArea),2) as 'RENTAL PROPERTIES DENSITY (PER SQ MI)', round((cast(c.TotalRenterOccupied as float))/COUNT(distinct a.storeid),0) as 'RENTAL PROPERTIES/STORE' from MarketViewUserStoreCompSet a join RadiusWiseCensusDataUK c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+mapDet.get("UserStoreId")+" and c.ZoneCoverage="+mapDet.get("zone")+" group by TotalRenterOccupied, LandArea";
			} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
				query = "select rentalproperties as 'RENTAL PROPERTIES', populationdensity as 'RENTAL PROPERTIES DENSITY (PER SQ MI)', round(rentalproperties/storecount,0) as 'RENTAL PROPERTIES/STORE' from (SELECT 1 as tomap1, SUM(TotalRenterOccupied) as rentalproperties, round(sum((TotalRenterOccupied))/sum(LandArea),2) as populationdensity FROM CityWiseCensusDataUK cwc with (nolock) where City='"+mapDet.get("City")+"') as serden join (select 1 as tomap2, count(*) as storecount from MarketViewUserStoreCompSet with (nolock) where UserStoreId = "+mapDet.get("UserStoreId")+") as sto on serden.tomap1=sto.tomap2";
			}
		}
		return query;
	}
	
	
	
	/* Inventory Availability - Current Inventory By Unit Type */
	
	public static String currInvUnitValue(String unitName) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0))) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0)))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0))) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0)))) ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0)))) as tot on ofm.tomap2 = tot.tomap3";
		} else if(unitName.toLowerCase().contains("all reg")) {
			query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0 and CC=0))) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0 and cc=0)))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0 and CC=0))) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0 and cc=0)))) ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0 and cc=0)))) as tot on ofm.tomap2 = tot.tomap3";
		} else if(unitName.toLowerCase().contains("all cc")) {
			query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0 and CC=1))) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0 and cc=1)))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0 and CC=1))) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0 and cc=1)))) ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0 and cc=1)))) as tot on ofm.tomap2 = tot.tomap3";
		} else if(unitName.toLowerCase().contains("rv park")) {
			if(_testData.regId==1) {
				query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=18 or (defaultunitid=17 and RV=1))) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where defaultunitid=18 or (defaultunitid=17 and RV=1)))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=18 or (defaultunitid=17 and RV=1))) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where defaultunitid=18 or (defaultunitid=17 and RV=1)))) as ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=18 or (defaultunitid=17 and RV=1)))) as tot on ofm.tomap2 = tot.tomap3";
			} else if(_testData.regId==3) {
				query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=78 or (defaultunitid=77 and RV=1))) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where defaultunitid=78 or (defaultunitid=77 and RV=1)))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=78 or (defaultunitid=77 and RV=1))) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where defaultunitid=78 or (defaultunitid=77 and RV=1)))) as ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=78 or (defaultunitid=77 and RV=1)))) as tot on ofm.tomap2 = tot.tomap3";
			}
		} else {
			int unitId = getUnitID(unitName);
			query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId = "+unitId+")) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId = "+unitId+"))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId = "+unitId+")) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId = "+unitId+"))) ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId = "+unitId+"))) as tot on ofm.tomap2 = tot.tomap3";
		}
		return query;
	}
	
	public static String currInvNational(String unitName) {
		String query;
		if(unitName.toLowerCase().contains("all units")) {
			query = "select onmarket, (total-onmarket) as offmarket, total from (SELECT 1 as tomap1, COUNT(DISTINCT S.MasterId) as total FROM SpacePriceHistory SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.RegionId = 1 AND DU.IsDefault = 0 WHERE S.CountryId = 2 and s.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1)) as tot join (SELECT 1 as tomap2, COUNT(DISTINCT S.MasterId) as onmarket FROM Spaceprice SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.RegionId = 1 WHERE SP.IsCurrentPrice=1 AND DU.IsDefault = 0 and S.CountryId = 2 and s.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1)) as onm on tot.tomap1 = onm.tomap2";
		} else if(unitName.toLowerCase().contains("all reg")) {
			query = "select onmarket, (total-onmarket) as offmarket, total from (SELECT 1 as tomap1, COUNT(DISTINCT S.MasterId) as total FROM SpacePriceHistory SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.RegionId = 1 AND DU.IsDefault = 0 AND DU.CC=0 WHERE S.CountryId = 2 and s.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1)) as tot join (SELECT 1 as tomap2, COUNT(DISTINCT S.MasterId) as onmarket FROM Spaceprice SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.RegionId = 1 WHERE SP.IsCurrentPrice=1 AND DU.IsDefault = 0 AND DU.CC=0 and S.CountryId = 2 and s.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1)) as onm on tot.tomap1 = onm.tomap2";
		} else if(unitName.toLowerCase().contains("all cc")) {
			query = "select onmarket, (total-onmarket) as offmarket, total from (SELECT 1 as tomap1, COUNT(DISTINCT S.MasterId) as total FROM SpacePriceHistory SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.RegionId = 1 AND DU.IsDefault = 0 AND DU.CC=1 WHERE S.CountryId = 2 and s.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1)) as tot join (SELECT 1 as tomap2, COUNT(DISTINCT S.MasterId) as onmarket FROM Spaceprice SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.RegionId = 1 WHERE SP.IsCurrentPrice=1 AND DU.IsDefault = 0 AND DU.CC=1 and S.CountryId = 2 and s.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1)) as onm on tot.tomap1 = onm.tomap2";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select onmarket, (total-onmarket) as offmarket, total from (SELECT 1 as tomap1, COUNT(*) as total from (SELECT DISTINCT S.State, SPC.DefaultUnitId, S.MasterId FROM SpacePriceHistory SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.IsDefault = 0 AND DU.RegionId = 1 WHERE (spc.DefaultUnitId=18 or (spc.DefaultUnitId=17 and spc.RV=1)) and S.CountryId IN (SELECT Id FROM LookupCountries WHERE Code = 'US')) as t) as tot join (SELECT 1 as tomap2, COUNT(*) as onmarket from (SELECT DISTINCT S.State, SPC.DefaultUnitId, S.MasterId FROM Spaceprice SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.IsDefault = 0 AND DU.RegionId = 1 WHERE SP.IsCurrentPrice=1 and (spc.DefaultUnitId=18 or (spc.DefaultUnitId=17 and spc.RV=1)) and S.CountryId IN (SELECT Id FROM LookupCountries WHERE Code = 'US')) as onmarket) as onm on tot.tomap1 = onm.tomap2";
		} else {
			int unitId = getUnitID(unitName);
			query = "select onmarket, (total-onmarket) as offmarket, total from (SELECT 1 as tomap1, COUNT(*) as total from (SELECT DISTINCT S.State, SPC.DefaultUnitId, S.MasterId FROM SpacePriceHistory SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.IsDefault = 0 AND DU.RegionId = 1 WHERE DU.id="+unitId+" and S.CountryId IN (SELECT Id FROM LookupCountries WHERE Code = 'US') and s.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1))as t) as tot join (SELECT 1 as tomap2, COUNT(*) as onmarket from (SELECT DISTINCT S.State, SPC.DefaultUnitId, S.MasterId FROM Spaceprice SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.IsDefault = 0 AND DU.RegionId = 1 WHERE SP.IsCurrentPrice=1 and DU.id="+unitId+" and S.CountryId IN (SELECT Id FROM LookupCountries WHERE Code = 'US') and s.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1)) as onmarket) as onm on tot.tomap1 = onm.tomap2";
		}
		return query;
	}
	
	public static String currInvState(String unitName) {
		String query;
		if(unitName.toLowerCase().contains("all units")) {
			query = "select onmarket, (total-onmarket) as offmarket, total from (SELECT 1 as tomap1, COUNT(DISTINCT S.MasterId) as total FROM SpacePriceHistory SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.RegionId = 1 AND DU.IsDefault = 0 WHERE S.state='"+_testData.state+"' and S.CountryId=2 and s.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1)) as tot join (SELECT 1 as tomap2, COUNT(DISTINCT S.MasterId) as onmarket FROM SpacePrice SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.RegionId = 1 AND SP.IsCurrentPrice = 1 AND DU.IsDefault = 0 WHERE S.state='"+_testData.state+"' and S.CountryId=2 and s.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1)) as onm on tot.tomap1 = onm.tomap2";
		} else if(unitName.toLowerCase().contains("all reg")) {
			query = "select onmarket, (total-onmarket) as offmarket, total from (SELECT 1 as tomap1, COUNT(DISTINCT S.MasterId) as total FROM SpacePriceHistory SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.RegionId = 1 AND DU.IsDefault = 0 AND DU.CC=0 WHERE S.state='"+_testData.state+"' and S.CountryId=2 and s.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1)) as tot join (SELECT 1 as tomap2, COUNT(DISTINCT S.MasterId) as onmarket FROM SpacePrice SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.RegionId = 1 AND SP.IsCurrentPrice = 1 AND DU.IsDefault = 0 AND DU.CC=0 WHERE S.state='"+_testData.state+"' and S.CountryId=2 and s.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1)) as onm on tot.tomap1 = onm.tomap2";
		} else if(unitName.toLowerCase().contains("all cc")) {
			query = "select onmarket, (total-onmarket) as offmarket, total from (SELECT 1 as tomap1, COUNT(DISTINCT S.MasterId) as total FROM SpacePriceHistory SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.RegionId = 1 AND DU.IsDefault = 0 AND DU.CC=1 WHERE S.state='"+_testData.state+"' and S.CountryId=2 and s.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1)) as tot join (SELECT 1 as tomap2, COUNT(DISTINCT S.MasterId) as onmarket FROM SpacePrice SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.RegionId = 1 AND SP.IsCurrentPrice = 1 AND DU.IsDefault = 0 AND DU.CC=1 WHERE S.state='"+_testData.state+"' and S.CountryId=2 and s.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1)) as onm on tot.tomap1 = onm.tomap2";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select onmarket, (total-onmarket) as offmarket, total from (SELECT 1 as tomap1, COUNT(*) as total from (select DISTINCT S.State, SPC.DefaultUnitId, S.MasterId FROM SpacePriceHistory SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.IsDefault = 0 AND DU.RegionId = 1 WHERE (spc.DefaultUnitId=18 or (spc.DefaultUnitId=17 and spc.RV=1)) and S.state='"+_testData.state+"' and S.CountryId=2) as t) as tot join (SELECT 1 as tomap2, COUNT(*) as onmarket from (SELECT DISTINCT S.State, SPC.DefaultUnitId, S.MasterId FROM Spaceprice SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.IsDefault = 0 AND DU.RegionId = 1 WHERE SP.IsCurrentPrice=1 and (spc.DefaultUnitId=18 or (spc.DefaultUnitId=17 and spc.RV=1)) and S.state='"+_testData.state+"' and S.CountryId=2) as onmarket) as onm on tot.tomap1 = onm.tomap2";
		} else {
			int unitId = getUnitID(unitName);
			query = "select onmarket, (total-onmarket) as offmarket, total from (SELECT 1 as tomap1, COUNT(*) as total from (select DISTINCT S.State, SPC.DefaultUnitId, S.MasterId FROM SpacePriceHistory SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.IsDefault = 0 AND DU.RegionId = 1 WHERE DU.id="+unitId+" and S.state='"+_testData.state+"' and S.CountryId=2) as t) as tot join (SELECT 1 as tomap2, COUNT(*) as onmarket from (SELECT DISTINCT S.State, SPC.DefaultUnitId, S.MasterId FROM Spaceprice SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.IsDefault = 0 AND DU.RegionId = 1 WHERE SP.IsCurrentPrice=1 and DU.id="+unitId+" and S.state='"+_testData.state+"' and S.CountryId=2) as onmarket) as onm on tot.tomap1 = onm.tomap2";
		}
		return query;
	}
	
	/* Compare */
	
	public static String currInvGreenBlueVal(String unitName, HashMap<String, String> mapDet) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0))) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0)))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0))) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0)))) ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0)))) as tot on ofm.tomap2 = tot.tomap3";
		} else if(unitName.toLowerCase().contains("all reg")) {
			query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0 and CC=0))) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0 and cc=0)))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0 and CC=0))) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0 and cc=0)))) ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0 and cc=0)))) as tot on ofm.tomap2 = tot.tomap3";
		} else if(unitName.toLowerCase().contains("all cc")) {
			query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0 and CC=1))) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0 and cc=1)))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0 and CC=1))) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0 and cc=1)))) ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0 and cc=1)))) as tot on ofm.tomap2 = tot.tomap3";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=18 or (defaultunitid=17 and RV=1))) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where defaultunitid=18 or (defaultunitid=17 and RV=1)))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=18 or (defaultunitid=17 and RV=1))) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where defaultunitid=18 or (defaultunitid=17 and RV=1)))) as ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=18 or (defaultunitid=17 and RV=1)))) as tot on ofm.tomap2 = tot.tomap3";
		} else {
			query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId = "+getUnitID(unitName)+")) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId = "+getUnitID(unitName)+"))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId = "+getUnitID(unitName)+")) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId = "+getUnitID(unitName)+"))) ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId = "+getUnitID(unitName)+"))) as tot on ofm.tomap2 = tot.tomap3";
		}
		return query;
	}
	
//	public static String currInvGreenVal(String unitName, HashMap<String, String> mapDet) {
//		String query = "";
//			if(!unitName.contains("RV")) {
//				query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId = "+getUnitID(unitName)+")) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId = "+getUnitID(unitName)+"))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId = "+getUnitID(unitName)+")) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId = "+getUnitID(unitName)+"))) ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId = "+getUnitID(unitName)+"))) as tot on ofm.tomap2 = tot.tomap3";
//			} else {
//				query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=18 or (defaultunitid=17 and RV=1))) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where defaultunitid=18 or (defaultunitid=17 and RV=1)))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=18 or (defaultunitid=17 and RV=1))) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where defaultunitid=18 or (defaultunitid=17 and RV=1)))) as ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=18 or (defaultunitid=17 and RV=1)))) as tot on ofm.tomap2 = tot.tomap3";
//			}
//		return query;
//	}
//	
//	public static String currInvBlueVal(String unitName, HashMap<String, String> mapDet) {
//		String query = "";
//			if(!unitName.contains("RV")) {
//				query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId = "+getUnitID(unitName)+")) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId = "+getUnitID(unitName)+"))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId = "+getUnitID(unitName)+")) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId = "+getUnitID(unitName)+"))) ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId = "+getUnitID(unitName)+"))) as tot on ofm.tomap2 = tot.tomap3";
//			} else {
//				query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=18 or (defaultunitid=17 and RV=1))) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where defaultunitid=18 or (defaultunitid=17 and RV=1)))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=18 or (defaultunitid=17 and RV=1))) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where defaultunitid=18 or (defaultunitid=17 and RV=1)))) as ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=18 or (defaultunitid=17 and RV=1)))) as tot on ofm.tomap2 = tot.tomap3";
//			}
//		return query;
//	}
	
	
	/* Inventory Availability - Inventory By Unit Type History */
	
	public static String thisMarketInventoryHistory(String unitName, String datePrice) {
		String query = "";
		if(!unitName.toLowerCase().contains("rv")) {
			if(!unitName.toLowerCase().contains("all units")) {
				query = "";
			} else {
				query = "";
			}
		} else {
			query = "";
		}
		return query;
	}
	

	public static String nationalInventoryHistory(String unitName, String datePrice) {
		String query = "";
		if(!unitName.toLowerCase().contains("rv")) {
			if(!unitName.toLowerCase().contains("all units")) {
				query = "";
			} else {
				query = "";
			}
		} else {
			query = "";
		}
		return query;
	}
	
	public static String stateInventoryHistory(String unitName, String datePrice) {
		String query = "";
		if(!unitName.toLowerCase().contains("rv")) {
			if(!unitName.toLowerCase().contains("all units")) {
				query = "";
			} else {
				query = "";
			}
		} else {
			query = "";
		}
		return query;
	}
	
	/* Compare */
	
	public static String greenBlueInventoryHistory(String unitName, String datePrice, HashMap<String, String> mapDet) {
		String query = "";
		if(!unitName.toLowerCase().contains("rv")) {
			if(!unitName.toLowerCase().contains("all units")) {
				query = "";
			} else {
				query = "";
			}
		} else {
			query = "";
		}
		return query;
	}
	
	
	
	/* Market Spending Power - Household Income */
	
	public static String houholdIncMarket() {
		String query = "";
		if(_testData.marketTypeId==1) {
			query = "select MedianHouseholdIncome as 'MEDIAN HOUSEHOLD INCOME', AggregateHouseholdIncome as 'AGGREGATE HOUSEHOLD INCOME', (AggregateHouseholdIncome/stores) as 'HOUSEHOLD INCOME/STORE' from (select 1 as tomap1, MedianHouseholdIncome from RadiusWiseCensusData where UserStoreID="+_testData.userStoreId+" and ZoneCoverage="+_testData.radius+") as hh join (select 1 as tomap2, (MedianHouseholdIncome*TotalHouseHolds) as AggregateHouseholdIncome from RadiusWiseCensusData where UserStoreID="+_testData.userStoreId+" and ZoneCoverage="+_testData.radius+") as agg on hh.tomap1 = agg.tomap2 join (select 1 as tomap3, count(*) as stores from MarketViewUserStoreCompSet where UserStoreID="+_testData.userStoreId+") as sto on agg.tomap2 = sto.tomap3";
		} else if(_testData.marketTypeId==3){
			query = "select MedianHouseholdIncome as 'MEDIAN HOUSEHOLD INCOME', AggregateHouseholdIncome as 'AGGREGATE HOUSEHOLD INCOME', (AggregateHouseholdIncome/stores) as 'HOUSEHOLD INCOME/STORE' from (SELECT 1 as tomap1, ROUND(avg(zwc.MedianHouseholdIncome),0) as MedianHouseholdIncome FROM ZipcodeWiseCensusData zwc with (nolock) JOIN ZipCodes z on z.zipcode=zwc.Zipcode where z.City='"+_testData.city+"' and z.abbr='"+_testData.state+"') as mhh join (SELECT 1 as tomap2, sum(zwc.MedianHouseholdIncome*zwc.TotalHouseholds) as AggregateHouseholdIncome FROM ZipcodeWiseCensusData zwc with (nolock) JOIN ZipCodes z on z.zipcode=zwc.Zipcode where z.City='"+_testData.city+"' and z.abbr='"+_testData.state+"') as agg on mhh.tomap1 = agg.tomap2 join (select 1 as tomap3, COUNT(*) as stores from MarketViewUserStoreCompSet where UserStoreId="+_testData.userStoreId+") as sto on agg.tomap2 = sto.tomap3";
		}
		return query;
	}
	
	public static String houholIncNational() {
		String query = "select MedianHouseholdIncome as 'MEDIAN HOUSEHOLD INCOME', AggregateHouseholdIncome as 'AGGREGATE HOUSEHOLD INCOME', (AggregateHouseholdIncome/stores) as 'HOUSEHOLD INCOME/STORE' from (SELECT 1 as tomap1, ROUND(avg(zwc.MedianHouseholdIncome),0) as MedianHouseholdIncome FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2)) as hh join (SELECT 1 as tomap2, sum(zwc.MedianHouseholdIncome * zwc.TotalHouseholds) as AggregateHouseholdIncome FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2)) as agg on hh.tomap1 = agg.tomap2 join (Select 1 as tomap3, count(*) as stores from Stores where countryid=2 and storemodflag!=3 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) as sto on agg.tomap2 = sto.tomap3";
		return query;
	}
	
	public static String houholIncState() {
		String query = "select MedianHouseholdIncome as 'MEDIAN HOUSEHOLD INCOME',  AggregateHouseholdIncome as 'AGGREGATE HOUSEHOLD INCOME', (AggregateHouseholdIncome/stores) as 'HOUSEHOLD INCOME/STORE' from (Select 1 as tomap1,  MedianHouseholdIncome from Statewisecensusdata where abbr='"+_testData.state+"') as hh join (Select 1 as tomap2, (MedianHouseholdIncome*TotalHouseHolds) as AggregateHouseholdIncome from Statewisecensusdata where abbr='"+_testData.state+"') as agg on hh.tomap1 = agg.tomap2 join (Select 1 as tomap3, count(*) as stores from Stores where countryid=2 and storemodflag!=3 and state='"+_testData.state+"') as sto on agg.tomap2 = sto.tomap3";
		return query;
	}
	
	
	/* Market Spending Power - Average Property Value */
	
	public static String avgPropMarket() {
		String query = "";
		if(_testData.marketTypeId==1) {
			query = "select MedianPropertyValue as 'AVERAGE PROPERTY VALUE' from RadiusWiseCensusData where UserStoreID="+_testData.userStoreId+" and ZoneCoverage="+_testData.radius+"";
		} else if(_testData.marketTypeId==3){
			query = "SELECT ROUND(avg(zwc.MedianPropertyValue),0) as 'AVERAGE PROPERTY VALUE' FROM ZipcodeWiseCensusData zwc with (nolock) JOIN ZipCodes z on z.zipcode=zwc.Zipcode where z.City='"+_testData.city+"' and z.abbr='"+_testData.state+"'";
		}
		return query;
	}
	
	public static String avgPropNational() {
		String query = "SELECT ROUND(avg(zwc.MedianPropertyValue),0) as 'AVERAGE PROPERTY VALUE' FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2)";
		return query;
	}
	
	public static String avgPropState() {
		String query = "Select MedianPropertyValue as 'AVERAGE PROPERTY VALUE' from Statewisecensusdata where abbr='"+_testData.state+"'";
		return query;
	}
	
	
	/* Market Spending Power - Average Rental Costs */
	
	public static String rentalPropMarket() {
		String query = "";
		if(_testData.marketTypeId==1) {
			query = "select AverageRent as 'AVERAGE RENTAL COSTS' from RadiusWiseCensusData where UserStoreID="+_testData.userStoreId+" and ZoneCoverage="+_testData.radius+"";
		} else if(_testData.marketTypeId==3){
			query = "SELECT ROUND(avg(zwc.AverageRent),0) as 'AVERAGE RENTAL COSTS' FROM ZipcodeWiseCensusData zwc with (nolock) JOIN ZipCodes z on z.zipcode=zwc.Zipcode where z.City='"+_testData.city+"' and z.abbr='"+_testData.state+"'";
		}
		return query;
	}
	
	public static String avgRentNational() {
		String query = "SELECT ROUND(avg(zwc.AverageRent),0) as 'AVERAGE RENTAL COSTS' FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2)";
		return query;
	}
	
	public static String rentalPropState() {
		String query = "Select AverageRent as 'AVERAGE RENTAL COSTS' from Statewisecensusdata where abbr='"+_testData.state+"'";
		return query;
	}
	
	/* Compare */
	
	public static String householdGreenBlue(HashMap<String, String> mapDet) {
		System.out.println(mapDet);
		String query = "";
		if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
			query = "select MedianHouseholdIncome as 'MEDIAN HOUSEHOLD INCOME', AggregateHouseholdIncome as 'AGGREGATE HOUSEHOLD INCOME', (AggregateHouseholdIncome/stores) as 'HOUSEHOLD INCOME/STORE' from (select 1 as tomap1, MedianHouseholdIncome from RadiusWiseCensusData where UserStoreID="+mapDet.get("UserStoreId")+" and ZoneCoverage="+mapDet.get("zone")+") as hh join (select 1 as tomap2, (MedianHouseholdIncome*TotalHouseHolds) as AggregateHouseholdIncome from RadiusWiseCensusData where UserStoreID="+mapDet.get("UserStoreId")+" and ZoneCoverage="+mapDet.get("zone")+") as agg on hh.tomap1 = agg.tomap2 join (select 1 as tomap3, count(*) as stores from MarketViewUserStoreCompSet where UserStoreID="+mapDet.get("UserStoreId")+") as sto on agg.tomap2 = sto.tomap3";
		} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
			query = "select MedianHouseholdIncome as 'MEDIAN HOUSEHOLD INCOME', AggregateHouseholdIncome as 'AGGREGATE HOUSEHOLD INCOME', (AggregateHouseholdIncome/stores) as 'HOUSEHOLD INCOME/STORE' from (SELECT 1 as tomap1, ROUND(avg(zwc.MedianHouseholdIncome),0) as MedianHouseholdIncome FROM ZipcodeWiseCensusData zwc with (nolock) JOIN ZipCodes z on z.zipcode=zwc.Zipcode where z.City='"+mapDet.get("City")+"' and z.abbr='"+mapDet.get("State")+"') as mhh join (SELECT 1 as tomap2, sum(zwc.MedianHouseholdIncome*zwc.TotalHouseholds) as AggregateHouseholdIncome FROM ZipcodeWiseCensusData zwc with (nolock) JOIN ZipCodes z on z.zipcode=zwc.Zipcode where z.City='"+mapDet.get("City")+"' and z.abbr='"+mapDet.get("State")+"') as agg on mhh.tomap1 = agg.tomap2 join (select 1 as tomap3, COUNT(*) as stores from MarketViewUserStoreCompSet where UserStoreId="+mapDet.get("UserStoreId")+") as sto on agg.tomap2 = sto.tomap3";
		}
		return query;
	}
	
	
	public static String avgPropGreenBlue(HashMap<String, String> mapDet) {
		String query = "";
		if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
			query = "select MedianPropertyValue as 'AVERAGE PROPERTY VALUE' from RadiusWiseCensusData where UserStoreID="+mapDet.get("UserStoreId")+" and ZoneCoverage="+mapDet.get("zone");
		} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
				query = "SELECT ROUND(avg(zwc.MedianPropertyValue),0) as 'AVERAGE PROPERTY VALUE' FROM ZipcodeWiseCensusData zwc with (nolock) JOIN ZipCodes z on z.zipcode=zwc.Zipcode where z.City='"+mapDet.get("City")+"' and z.abbr='"+mapDet.get("State")+"'";
		}
		return query;
	}
	
	public static String avgRentGreenBlue(HashMap<String, String> mapDet) {
		String query = "";
		if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
			query = "select AverageRent as 'AVERAGE RENTAL COSTS' from RadiusWiseCensusData where UserStoreID="+mapDet.get("UserStoreId")+" and ZoneCoverage="+mapDet.get("zone");
		} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
			query = "SELECT ROUND(avg(zwc.AverageRent),0) as 'AVERAGE RENTAL COSTS' FROM ZipcodeWiseCensusData zwc with (nolock) JOIN ZipCodes z on z.zipcode=zwc.Zipcode where z.City='"+mapDet.get("City")+"' and z.abbr='"+mapDet.get("State")+"'";
		}
		return query;
	}
	
	
	/* Market Supply Metrics - Capita */
	
	public static String thisMarketCapita() {
		String query = "";
		if(_testData.marketTypeId==1) {
			query = "select sum(cast (b.RentableSqFt as int)) as 'TOTAL RENTABLE SQFT', c.TotalPopulation as 'POPULATION', round(sum(cast(b.RentableSqFt as int))/c.TotalPopulation,2) as 'TOTAL RENTABLE SQ FT/CAPITA' from MarketViewUserStoreCompSet a join stores b on a.StoreId=b.StoreID join RadiusWiseCensusData c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+_testData.userStoreId+" and c.ZoneCoverage="+_testData.radius+" group by TotalPopulation";
		} else if(_testData.marketTypeId==3){
			query = "select RentableSqFt as 'TOTAL RENTABLE SQFT', population as 'POPULATION', Round((RentableSqFt/population),2) as 'TOTAL RENTABLE SQ FT/CAPITA' from (SELECT 1 as tomap1, sum(COALESCE(s.RentableSqFt,0)) as RentableSqFt FROM ZipcodeWiseCensusData zwc with (nolock) JOIN stores s ON s.zipcode=zwc.zipcode and s.state='"+_testData.state+"' and s.StoreModFlag!=3 and s.City='"+_testData.city+"' JOIN uscities c ON c.City=s.city and  c.State_id=s.State) as ren join (select 1 as tomap2, sum(TotalPopulation) as population from ZipcodeWiseCensusData where Zipcode in (Select Zipcode from Zipcodes where City ='"+_testData.city+"' and abbr ='"+_testData.state+"')) as pop on ren.tomap1 = pop.tomap2";
		}
		return query;
	}
	
	public static String nationalCapita() {
		String query = "select RentableSqFt as 'TOTAL RENTABLE SQFT', population as 'POPULATION', Round((RentableSqFt/population), 2) as 'TOTAL RENTABLE SQ FT/CAPITA' from (select 1 as tomap1, sum(COALESCE(RentableSqFt,0)) as RentableSqFt from Stores where StoreModFlag!=3 and CountryId=2 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) as ren join (Select 1 as tomap2, sum(TotalPopulation) as population FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2 and IsUnionTerritories is null)) as pop on ren.tomap1 = pop.tomap2";
		return query;
	}
	
	public static String stateCapita() {
		String query = "select RentableSqFt as 'TOTAL RENTABLE SQFT', population as 'POPULATION', Round((RentableSqFt/population),2) as 'TOTAL RENTABLE SQ FT/CAPITA' from (select 1 as tomap1, sum(COALESCE(RentableSqFt,0)) as RentableSqFt from Stores where StoreModFlag!=3 and CountryId=2 and State='"+_testData.state+"') as ren join (select 1 as tomap2, sum(TotalPopulation) as population FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2 and id='"+_testData.state+"')) as pop on ren.tomap1 = pop.tomap2";
		return query;
	}
	
	
	/* Market Supply Metrics - Household */
	
	public static String thisMarketHousehold() {
		String query = "";
		if(_testData.marketTypeId==1) {
			query = "select sum(cast (b.RentableSqFt as int)) as 'TOTAL RENTABLE SQFT', c.TotalHouseHolds as 'HOUSEHOLDS', round(sum(cast(b.RentableSqFt as int))/c.TotalHouseHolds,2) as 'TOTAL RENTABLE SQ FT/HOUSEHOLD' from MarketViewUserStoreCompSet a join stores b on a.StoreId=b.StoreID join RadiusWiseCensusData c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+_testData.userStoreId+" and c.ZoneCoverage="+_testData.radius+" group by TotalHouseHolds";
		} else if(_testData.marketTypeId==3){
			query = "select RentableSqFt as 'TOTAL RENTABLE SQFT', households as 'HOUSEHOLDS', Round((RentableSqFt/households),2) as 'TOTAL RENTABLE SQ FT/HOUSEHOLD' from (SELECT 1 as tomap1, sum(COALESCE(s.RentableSqFt,0)) as RentableSqFt FROM ZipcodeWiseCensusData zwc with (nolock) JOIN stores s ON s.zipcode=zwc.zipcode and s.state='"+_testData.state+"' and s.StoreModFlag!=3 and s.City='"+_testData.city+"' JOIN uscities c ON c.City=s.city and  c.State_id=s.State) as ren join (select 1 as tomap2, sum(TotalHouseholds) as households from ZipcodeWiseCensusData where Zipcode in (Select Zipcode from Zipcodes where City ='"+_testData.city+"' and abbr ='"+_testData.state+"')) as pop on ren.tomap1 = pop.tomap2";
		}
		return query;
	}
	
	public static String nationalHousehold() {
		String query = "select RentableSqFt as 'TOTAL RENTABLE SQFT', households as 'HOUSEHOLDS', Round((RentableSqFt/households),2) as 'TOTAL RENTABLE SQ FT/HOUSEHOLD' from (select 1 as tomap1, sum(COALESCE(RentableSqFt,0)) as RentableSqFt from Stores where StoreModFlag!=3 and CountryId=2 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) as ren join (select 1 as tomap2, sum(TotalHouseholds) as households FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2 and IsUnionTerritories is null)) as hh on ren.tomap1 = hh.tomap2";
		return query;
	}
	
	public static String stateHousehold() {
		String query = "select RentableSqFt as 'TOTAL RENTABLE SQFT', households as 'HOUSEHOLDS', Round((RentableSqFt/households),2) as 'TOTAL RENTABLE SQ FT/HOUSEHOLD' from (select 1 as tomap1, sum(COALESCE(RentableSqFt,0)) as RentableSqFt from Stores where StoreModFlag!=3 and CountryId=2 and State='"+_testData.state+"') as ren join (select 1 as tomap2, sum(TotalHouseholds) as households FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2 and id='"+_testData.state+"')) as pop on ren.tomap1 = pop.tomap2";
		return query;
	}
	
	/* Market Supply Metrics - Rental Properties */
	
	public static String thisMarketRentalProp() {
		String query = "";
		if(_testData.marketTypeId==1) {
			query = "select sum(cast (b.RentableSqFt as int)) as 'TOTAL RENTABLE SQFT', c.TotalRenterOccupied as 'RENTAL PROPERTIES', round(sum(cast(b.RentableSqFt as int))/c.TotalRenterOccupied,2) as 'TOTAL RENTABLE SQ FT/RENTAL PROPERTIES' from MarketViewUserStoreCompSet a join stores b on a.StoreId=b.StoreID join RadiusWiseCensusData c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+_testData.userStoreId+" and c.ZoneCoverage="+_testData.radius+" group by TotalRenterOccupied";
		} else if(_testData.marketTypeId==3){
			query = "select RentableSqFt as 'TOTAL RENTABLE SQFT', rentalproperties as 'RENTAL PROPERTIES', Round((RentableSqFt/rentalproperties),2) as 'TOTAL RENTABLE SQ FT/RENTAL PROPERTIES' from (SELECT 1 as tomap1, sum(COALESCE(s.RentableSqFt,0)) as RentableSqFt FROM ZipcodeWiseCensusData zwc with (nolock) JOIN stores s ON s.zipcode=zwc.zipcode and s.state='"+_testData.state+"' and s.StoreModFlag!=3 and s.City='"+_testData.city+"' JOIN uscities c ON c.City=s.city and  c.State_id=s.State) as ren join (select 1 as tomap2, SUM(TotalRenterOccupied) as rentalproperties from ZipcodeWiseCensusData where Zipcode in (Select Zipcode from Zipcodes where City ='"+_testData.city+"' and abbr ='"+_testData.state+"')) as pop on ren.tomap1 = pop.tomap2";
		}
		return query;
	}
	
	public static String nationalRentProp() {
		String query = "select RentableSqFt as 'TOTAL RENTABLE SQFT', rentalproperties as 'RENTAL PROPERTIES', Round((RentableSqFt/rentalproperties),2) as 'TOTAL RENTABLE SQ FT/RENTAL PROPERTIES' from (select 1 as tomap1, sum(COALESCE(RentableSqFt,0)) as RentableSqFt from Stores where StoreModFlag!=3 and CountryId=2 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) as ren join (select 1 as tomap2, SUM(TotalRenterOccupied) as rentalproperties FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2 and IsUnionTerritories is null)) as rep on ren.tomap1 = rep.tomap2";
		return query;
	}
	
	public static String stateRentProp() {
		String query = "select RentableSqFt as 'TOTAL RENTABLE SQFT', rentalproperties as 'RENTAL PROPERTIES', Round((RentableSqFt/rentalproperties),2) as 'TOTAL RENTABLE SQ FT/RENTAL PROPERTIES' from (select 1 as tomap1, sum(COALESCE(RentableSqFt,0)) as RentableSqFt from Stores where StoreModFlag!=3 and CountryId=2 and State='"+_testData.state+"') as ren join (select 1 as tomap2, SUM(TotalRenterOccupied) as rentalproperties FROM ZipcodeWiseCensusData zwc  WITH(NOLOCK) JOIN ZipCodes z ON z.ZipCode=zwc.ZipCode and z.abbr in (Select id from LookupStates where countryid=2 and id='"+_testData.state+"')) as pop on ren.tomap1 = pop.tomap2";
		return query;
	}
	
	/* Compare */
	
	public static String greenBlueCapita(HashMap<String, String> mapDet) {
		String query = "";
		if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
			query = "select sum(cast (b.RentableSqFt as int)) as 'TOTAL RENTABLE SQFT', c.TotalPopulation as 'POPULATION', round(sum(cast(b.RentableSqFt as int))/c.TotalPopulation,2) as 'TOTAL RENTABLE SQ FT/CAPITA' from MarketViewUserStoreCompSet a join stores b on a.StoreId=b.StoreID join RadiusWiseCensusData c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+mapDet.get("UserStoreId")+" and c.ZoneCoverage="+mapDet.get("zone")+" group by TotalPopulation";
		} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
			query = "select RentableSqFt as 'TOTAL RENTABLE SQFT', population as 'POPULATION', Round((RentableSqFt/population),2) as 'TOTAL RENTABLE SQ FT/CAPITA' from (SELECT 1 as tomap1, sum(COALESCE(s.RentableSqFt,0)) as RentableSqFt FROM ZipcodeWiseCensusData zwc with (nolock) JOIN stores s ON s.zipcode=zwc.zipcode and s.state='"+mapDet.get("State")+"' and s.StoreModFlag!=3 and s.City='"+mapDet.get("City")+"' JOIN uscities c ON c.City=s.city and  c.State_id=s.State) as ren join (select 1 as tomap2, sum(TotalPopulation) as population from ZipcodeWiseCensusData where Zipcode in (Select Zipcode from Zipcodes where City ='"+mapDet.get("City")+"' and abbr ='"+mapDet.get("State")+"')) as pop on ren.tomap1 = pop.tomap2";
		}
		return query;
	}
	
	
	public static String greenBlueHoushold(HashMap<String, String> mapDet) {
		String query = "";
		if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
			query = "select sum(cast (b.RentableSqFt as int)) as 'TOTAL RENTABLE SQFT', c.TotalHouseHolds as 'HOUSEHOLDS', round(sum(cast(b.RentableSqFt as int))/c.TotalHouseHolds,2) as 'TOTAL RENTABLE SQ FT/HOUSEHOLD' from MarketViewUserStoreCompSet a join stores b on a.StoreId=b.StoreID join RadiusWiseCensusData c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+mapDet.get("UserStoreId")+" and c.ZoneCoverage="+mapDet.get("zone")+" group by TotalHouseHolds";
		} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
				query = "select RentableSqFt as 'TOTAL RENTABLE SQFT', households as 'HOUSEHOLDS', Round((RentableSqFt/households),2) as 'TOTAL RENTABLE SQ FT/HOUSEHOLD' from (SELECT 1 as tomap1, sum(COALESCE(s.RentableSqFt,0)) as RentableSqFt FROM ZipcodeWiseCensusData zwc with (nolock) JOIN stores s ON s.zipcode=zwc.zipcode and s.state='"+mapDet.get("State")+"' and s.StoreModFlag!=3 and s.City='"+mapDet.get("City")+"' JOIN uscities c ON c.City=s.city and  c.State_id=s.State) as ren join (select 1 as tomap2, sum(TotalHouseholds) as households from ZipcodeWiseCensusData where Zipcode in (Select Zipcode from Zipcodes where City ='"+mapDet.get("City")+"' and abbr ='"+mapDet.get("State")+"')) as pop on ren.tomap1 = pop.tomap2";
		}
		return query;
	}
	
	public static String greenBlueRentalProp(HashMap<String, String> mapDet) {
		String query = "";
		if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
			query = "select sum(cast (b.RentableSqFt as int)) as 'TOTAL RENTABLE SQFT', c.TotalRenterOccupied as 'RENTAL PROPERTIES', round(sum(cast(b.RentableSqFt as int))/c.TotalRenterOccupied,2) as 'TOTAL RENTABLE SQ FT/RENTAL PROPERTIES' from MarketViewUserStoreCompSet a join stores b on a.StoreId=b.StoreID join RadiusWiseCensusData c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+mapDet.get("UserStoreId")+" and c.ZoneCoverage="+mapDet.get("zone")+" group by TotalRenterOccupied";
		} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
			query = "select RentableSqFt as 'TOTAL RENTABLE SQFT', rentalproperties as 'RENTAL PROPERTIES', Round((RentableSqFt/rentalproperties),2) as 'TOTAL RENTABLE SQ FT/RENTAL PROPERTIES' from (SELECT 1 as tomap1, sum(COALESCE(s.RentableSqFt,0)) as RentableSqFt FROM ZipcodeWiseCensusData zwc with (nolock) JOIN stores s ON s.zipcode=zwc.zipcode and s.state='"+mapDet.get("State")+"' and s.StoreModFlag!=3 and s.City='"+mapDet.get("City")+"' JOIN uscities c ON c.City=s.city and  c.State_id=s.State) as ren join (select 1 as tomap2, SUM(TotalRenterOccupied) as rentalproperties from ZipcodeWiseCensusData where Zipcode in (Select Zipcode from Zipcodes where City ='"+mapDet.get("City")+"' and abbr ='"+mapDet.get("State")+"')) as pop on ren.tomap1 = pop.tomap2";
		}
		return query;
	}
	
	
	
	/* Pricing Rental - Rate Per Square Type by Store Type */
	
	public static String thisMarketRateByStoreType() {
		String query = "select OVERALLMARKETAVERAGE as 'OVERALL MARKET AVERAGE', REITS, MIDOPS as 'MID OPS', SMALLOPS as 'SMALL OPS' from (select 1 as tomap1, round(AVG(value),2) as OVERALLMARKETAVERAGE from (SELECT sp.onlineprice/ nullif(((CASE WHEN ABS(S.width)=0 THEN 1 ELSE ABS(S.width) END)*(CASE WHEN ABS(S.length)=0 THEN 1 ELSE ABS(S.length) END)),0) as Value from SpacePrice sp WITH (NOLOCK) join stores st with (nolock) on sp.StoreID=st.StoreId Join MarketViewUserStoreCompSet mv on mv.storeid=St.StoreId and mv.userstoreid="+_testData.userStoreId+" join space s with (nolock) on s.SpaceID=sp.SpaceID JOIN DefaultUnitSizes d ON  d.id=s.DefaultUnitId and  d.CountryId='1,2' and  d.IsDefault=0 where st.StoreModFlag!=3 and st.RegionId=1 and st.countryid in (1,2) and sp.onlineprice!=0 and sp.IsCurrentPrice=1) as Avg) as ma join (select 1 as tomap2, round(AVG(value),2) as REITS from (SELECT sp.onlineprice/ nullif(((CASE WHEN ABS(S.width)=0 THEN 1 ELSE ABS(S.width) END)*(CASE WHEN ABS(S.length)=0 THEN 1 ELSE ABS(S.length) END)),0) as Value from SpacePrice sp WITH (NOLOCK) join stores st with (nolock) on sp.StoreID=st.StoreId and st.REIT=1 Join MarketViewUserStoreCompSet mv on mv.storeid=St.StoreId and mv.userstoreid="+_testData.userStoreId+" join space s with (nolock) on s.SpaceID=sp.SpaceID JOIN DefaultUnitSizes d ON  d.id=s.DefaultUnitId and  d.CountryId='1,2' and  d.IsDefault=0 where st.StoreModFlag!=3 and st.RegionId=1 and st.countryid in (1,2) and sp.onlineprice!=0 and sp.IsCurrentPrice=1) as Avg) as re on ma.tomap1 = re.tomap2 join (select 1 as tomap3, round(AVG(value),2) as MIDOPS from (SELECT sp.onlineprice/ nullif(((CASE WHEN ABS(S.width)=0 THEN 1 ELSE ABS(S.width) END)*(CASE WHEN ABS(S.length)=0 THEN 1 ELSE ABS(S.length) END)),0) as Value from SpacePrice sp WITH (NOLOCK) join stores st with (nolock) on sp.StoreID=st.StoreId and st.MultiOperator=1 Join MarketViewUserStoreCompSet mv on mv.storeid=St.StoreId and mv.userstoreid="+_testData.userStoreId+" join space s with (nolock) on s.SpaceID=sp.SpaceID JOIN DefaultUnitSizes d ON  d.id=s.DefaultUnitId and  d.CountryId='1,2' and  d.IsDefault=0 where st.StoreModFlag!=3 and st.RegionId=1 and st.countryid in (1,2) and sp.onlineprice!=0 and sp.IsCurrentPrice=1) as Avg) as mi on re.tomap2 = mi.tomap3 join (select 1 as tomap4, round(AVG(value),2) as SMALLOPS from (SELECT sp.onlineprice/ nullif(((CASE WHEN ABS(S.width)=0 THEN 1 ELSE ABS(S.width) END)*(CASE WHEN ABS(S.length)=0 THEN 1 ELSE ABS(S.length) END)),0) as Value from SpacePrice sp WITH (NOLOCK) join stores st with (nolock) on sp.StoreID=st.StoreId and st.SingleOperator=1 Join MarketViewUserStoreCompSet mv on mv.storeid=St.StoreId and mv.userstoreid="+_testData.userStoreId+" join space s with (nolock) on s.SpaceID=sp.SpaceID JOIN DefaultUnitSizes d ON  d.id=s.DefaultUnitId and  d.CountryId='1,2' and  d.IsDefault=0 where st.StoreModFlag!=3 and st.RegionId=1 and st.countryid in (1,2) and sp.onlineprice!=0 and sp.IsCurrentPrice=1) as Avg) as sm on mi.tomap3 = sm.tomap4";
		return query;
	}
	
	public static String nationalRateByStoreType() {
		String query = "select OVERALLMARKETAVERAGE as 'OVERALL MARKET AVERAGE', REITS, MIDOPS as 'MID OPS', SMALLOPS as 'SMALL OPS' from (select 1 as tomap1, round(AVG(value),2) as OVERALLMARKETAVERAGE from (SELECT (Z.onlineprice/ nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)) as Value, S1.StoreID from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID and S1.CountryId=2 JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND   Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 and Z.onlineprice between 2 and 999 Join DefaultUnitSizes du on sa.defaultunitid = du.id and du.countryid ='1,2' and du.isdefault=0 WHERE  S1.StoreModFlag != 3 ) as Avg) as ma join (select 1 as tomap2, round(AVG(value),2) as REITS from (SELECT (Z.onlineprice/ nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)) as Value, S1.StoreID from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID and S1.CountryId=2 and S1.REIT=1 JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND   Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 and Z.onlineprice between 2 and 999 Join DefaultUnitSizes du on sa.defaultunitid = du.id and du.countryid ='1,2' and du.isdefault=0 WHERE  S1.StoreModFlag != 3 ) as Avg) as re on ma.tomap1 = re.tomap2 join (select 1 as tomap3, round(AVG(value),2) as MIDOPS from (SELECT (Z.onlineprice/ nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)) as Value, S1.StoreID from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID and S1.CountryId=2 and S1.MultiOperator=1 JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND   Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 and Z.onlineprice between 2 and 999 Join DefaultUnitSizes du on sa.defaultunitid = du.id and du.countryid ='1,2' and du.isdefault=0 WHERE  S1.StoreModFlag != 3 ) as Avg) as mi on re.tomap2 = mi.tomap3 join (select 1 as tomap4, round(AVG(value),2) as SMALLOPS from (SELECT (Z.onlineprice/ nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)) as Value, S1.StoreID from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID and S1.CountryId=2 and S1.SingleOperator=1 JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND   Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 and Z.onlineprice between 2 and 999 Join DefaultUnitSizes du on sa.defaultunitid = du.id and du.countryid ='1,2' and du.isdefault=0 WHERE  S1.StoreModFlag != 3 ) as Avg) as sm on mi.tomap3 = sm.tomap4";
		return query;
	}
	
	public static String stateRateByStoreType() {
		String query = "select OVERALLMARKETAVERAGE as 'OVERALL MARKET AVERAGE', REITS, MIDOPS as 'MID OPS', SMALLOPS as 'SMALL OPS' from (select 1 as tomap1, round(AVG(value),2) as OVERALLMARKETAVERAGE from (SELECT (Z.onlineprice/ nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)) as Value, S1.StoreID from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID and S1.CountryId=2 and S1.State='"+_testData.state+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND   Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 and Z.onlineprice between 2 and 999 Join DefaultUnitSizes du on sa.defaultunitid = du.id and du.countryid ='1,2' and du.isdefault=0 WHERE  S1.StoreModFlag != 3 ) as Avg) as ma join (select 1 as tomap2, round(AVG(value),2) as REITS from (SELECT (Z.onlineprice/ nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)) as Value, S1.StoreID from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID and S1.CountryId=2 and S1.REIT=1 and S1.State='"+_testData.state+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND   Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 and Z.onlineprice between 2 and 999 Join DefaultUnitSizes du on sa.defaultunitid = du.id and du.countryid ='1,2' and du.isdefault=0 WHERE  S1.StoreModFlag != 3 ) as Avg) as re on ma.tomap1 = re.tomap2 join (select 1 as tomap3, round(AVG(value),2) as MIDOPS from (SELECT (Z.onlineprice/ nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)) as Value, S1.StoreID from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID and S1.CountryId=2 and S1.MultiOperator=1 and S1.State='"+_testData.state+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND   Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 and Z.onlineprice between 2 and 999 Join DefaultUnitSizes du on sa.defaultunitid = du.id and du.countryid ='1,2' and du.isdefault=0 WHERE  S1.StoreModFlag != 3 ) as Avg) as mi on re.tomap2 = mi.tomap3 join (select 1 as tomap4, round(AVG(value),2) as SMALLOPS from (SELECT (Z.onlineprice/ nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)) as Value, S1.StoreID from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID and S1.CountryId=2 and S1.SingleOperator=1 and S1.State='"+_testData.state+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND   Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 and Z.onlineprice between 2 and 999 Join DefaultUnitSizes du on sa.defaultunitid = du.id and du.countryid ='1,2' and du.isdefault=0 WHERE  S1.StoreModFlag != 3 ) as Avg) as sm on mi.tomap3 = sm.tomap4";
		return query;
	}
	
	/* Compare */
	
	public static String greenBlueRateByStoreType(HashMap<String, String> mapDet) {
		String query = "select OVERALLMARKETAVERAGE as 'OVERALL MARKET AVERAGE', REITS, MIDOPS as 'MID OPS', SMALLOPS as 'SMALL OPS' from (select 1 as tomap1, round(AVG(value),2) as OVERALLMARKETAVERAGE from (SELECT sp.onlineprice/ nullif(((CASE WHEN ABS(S.width)=0 THEN 1 ELSE ABS(S.width) END)*(CASE WHEN ABS(S.length)=0 THEN 1 ELSE ABS(S.length) END)),0) as Value from SpacePrice sp WITH (NOLOCK) join stores st with (nolock) on sp.StoreID=st.StoreId Join MarketViewUserStoreCompSet mv on mv.storeid=St.StoreId and mv.userstoreid="+mapDet.get("UserStoreId")+" join space s with (nolock) on s.SpaceID=sp.SpaceID JOIN DefaultUnitSizes d ON  d.id=s.DefaultUnitId and  d.CountryId='1,2' and  d.IsDefault=0 where st.StoreModFlag!=3 and st.RegionId=1 and st.countryid in (1,2) and sp.onlineprice!=0 and sp.IsCurrentPrice=1) as Avg) as ma join (select 1 as tomap2, round(AVG(value),2) as REITS from (SELECT sp.onlineprice/ nullif(((CASE WHEN ABS(S.width)=0 THEN 1 ELSE ABS(S.width) END)*(CASE WHEN ABS(S.length)=0 THEN 1 ELSE ABS(S.length) END)),0) as Value from SpacePrice sp WITH (NOLOCK) join stores st with (nolock) on sp.StoreID=st.StoreId and st.REIT=1 Join MarketViewUserStoreCompSet mv on mv.storeid=St.StoreId and mv.userstoreid="+mapDet.get("UserStoreId")+" join space s with (nolock) on s.SpaceID=sp.SpaceID JOIN DefaultUnitSizes d ON  d.id=s.DefaultUnitId and  d.CountryId='1,2' and  d.IsDefault=0 where st.StoreModFlag!=3 and st.RegionId=1 and st.countryid in (1,2) and sp.onlineprice!=0 and sp.IsCurrentPrice=1) as Avg) as re on ma.tomap1 = re.tomap2 join (select 1 as tomap3, round(AVG(value),2) as MIDOPS from (SELECT sp.onlineprice/ nullif(((CASE WHEN ABS(S.width)=0 THEN 1 ELSE ABS(S.width) END)*(CASE WHEN ABS(S.length)=0 THEN 1 ELSE ABS(S.length) END)),0) as Value from SpacePrice sp WITH (NOLOCK) join stores st with (nolock) on sp.StoreID=st.StoreId and st.MultiOperator=1 Join MarketViewUserStoreCompSet mv on mv.storeid=St.StoreId and mv.userstoreid="+mapDet.get("UserStoreId")+" join space s with (nolock) on s.SpaceID=sp.SpaceID JOIN DefaultUnitSizes d ON  d.id=s.DefaultUnitId and  d.CountryId='1,2' and  d.IsDefault=0 where st.StoreModFlag!=3 and st.RegionId=1 and st.countryid in (1,2) and sp.onlineprice!=0 and sp.IsCurrentPrice=1) as Avg) as mi on re.tomap2 = mi.tomap3 join (select 1 as tomap4, round(AVG(value),2) as SMALLOPS from (SELECT sp.onlineprice/ nullif(((CASE WHEN ABS(S.width)=0 THEN 1 ELSE ABS(S.width) END)*(CASE WHEN ABS(S.length)=0 THEN 1 ELSE ABS(S.length) END)),0) as Value from SpacePrice sp WITH (NOLOCK) join stores st with (nolock) on sp.StoreID=st.StoreId and st.SingleOperator=1 Join MarketViewUserStoreCompSet mv on mv.storeid=St.StoreId and mv.userstoreid="+mapDet.get("UserStoreId")+" join space s with (nolock) on s.SpaceID=sp.SpaceID JOIN DefaultUnitSizes d ON  d.id=s.DefaultUnitId and  d.CountryId='1,2' and  d.IsDefault=0 where st.StoreModFlag!=3 and st.RegionId=1 and st.countryid in (1,2) and sp.onlineprice!=0 and sp.IsCurrentPrice=1) as Avg) as sm on mi.tomap3 = sm.tomap4";
		return query;
	}
	
	
	
	
	
	/* Pricing Rental - Rate Per Square Type by Unit Type */
	
	public static String thisMarketRateByUnitType() {
		String query = "SELECT d.unittype as Unittype, round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and d.id!=18 group by d.unittype UNION SELECT 'RV Parking' as Unittype, round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as RVprice from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and D.ID=18 or (D.ID=17 and sa.RV=1) UNION SELECT 'All Units' as Unittype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and d.IsDefault=0 UNION ALL SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 AND sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A UNION SELECT 'All Reg' as Unittype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and d.IsDefault=0 and d.CC=0 UNION ALL SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 AND sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A UNION SELECT 'All CC' as Unittype, round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 And D.CC=1 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3";
		return query;
	}
	
	public static String nationalRateByUnitType() {
		String query = "SELECT d.unittype as Unittype, round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price from Stores S1 with (nolock) JOIN SpacePrice Z  with (nolock)ON S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN Space sa with (nolock) ON sa.SpaceID=Z.SpaceID AND  Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D with (nolock) ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 WHERE S1.StoreModFlag != 3 and d.ID!=18 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1) group by d.unittype UNION SELECT 'RV Parking' as Unittype, round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as RVprice from Stores S1 with (nolock) JOIN SpacePrice Z with (nolock) ON S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN Space sa with (nolock) ON sa.SpaceID=Z.SpaceID AND  Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D with (nolock) ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 WHERE S1.StoreModFlag != 3 and D.ID=18 or (D.ID=17 and sa.RV=1) and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1) UNION SELECT 'All Units' as Unittype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' and D.IsDefault=0 WHERE S1.StoreModFlag != 3 and d.IsDefault=0 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1) UNION ALL SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 WHERE S1.StoreModFlag != 3 AND sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A UNION SELECT 'All Reg' as Unittype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' and D.IsDefault=0 WHERE S1.StoreModFlag != 3 and d.IsDefault=0 and d.CC=0 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1) UNION ALL SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 WHERE S1.StoreModFlag != 3 AND sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A UNION SELECT 'All CC' as Unittype, round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price from Stores S1 with (nolock) JOIN SpacePrice Z with (nolock) ON S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN Space sa with (nolock) ON sa.SpaceID=Z.SpaceID AND  Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D with (nolock) ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 and D.CC=1 and S1.StoreModFlag != 3 WHERE State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)";
		return query;
	}

	public static String stateRateByUnitType() {
		String query = "SELECT d.unittype as Unitype, round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN Space sa ON sa.SpaceID=Z.SpaceID AND  Z.IsCurrentPrice = 1 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 WHERE d.ID!=18 and S1.state='"+_testData.state+"' group by d.unittype UNION SELECT 'RV Parking' as Unitype, round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as RVprice from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN Space sa ON sa.SpaceID=Z.SpaceID AND  Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 WHERE S1.StoreModFlag != 3 and S1.state='"+_testData.state+"' and D.ID=18 or (D.ID=17 and sa.RV=1) UNION SELECT 'All Units' as Unitype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND   Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' and D.IsDefault=0 WHERE S1.StoreModFlag != 3 and S1.state='"+_testData.state+"' UNION ALL SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND   Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 WHERE S1.StoreModFlag != 3 and S1.state='"+_testData.state+"' AND sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A UNION SELECT 'All Reg' as Unitype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND   Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' and D.IsDefault=0 WHERE S1.StoreModFlag != 3 and S1.state='"+_testData.state+"' and D.CC=0 UNION ALL SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND   Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 WHERE S1.StoreModFlag != 3 and S1.state='"+_testData.state+"' AND sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A UNION SELECT 'All CC' as Unitype, round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND   Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' and D.IsDefault=0 WHERE S1.StoreModFlag != 3 and S1.state='"+_testData.state+"' and D.CC=1";
		return query;
	}
	
	/* Compare */
	public static String greenBlueRateByUnitType(HashMap<String, String> mapDet) {
		String query = "SELECT d.unittype as Unittype, round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and d.id!=18 group by d.unittype UNION SELECT 'RV Parking' as Unittype, round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as RVprice from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and D.ID=18 or (D.ID=17 and sa.RV=1) UNION SELECT 'All Units' as Unittype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and d.IsDefault=0 UNION ALL SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 AND sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A UNION SELECT 'All Reg' as Unittype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and d.IsDefault=0 and d.CC=0 UNION ALL SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 AND sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A UNION SELECT 'All CC' as Unittype, round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 And D.CC=1 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3";
		return query;
	}
	
	
	
	
	
	/* Pricing Rental - Rate Per Square Type by Unit Type */
	
	public static String thisMarketAvgUnitRates() {
		String query = "SELECT d.unittype, round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND   Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and d.id!=1 and d.id!=18 and s1.countryid=2 group by d.unittype UNION SELECT d.unittype, round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and d.id=18 or (d.id=17 and sa.RV=1) and s1.countryid=2 and d.ID !=17 group by d.unittype UNION SELECT 'all units' as unittype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and d.IsDefault=0 UNION ALL SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 AND sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A UNION SELECT 'all reg' as unittype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and d.IsDefault=0 and d.CC=0 UNION ALL SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 AND sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A UNION SELECT 'all cc' as unittype, round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and d.IsDefault=0 and d.CC=1";
		return query;
	}
	
	public static String nationalAvgUnitRates() {
		String query = "SELECT d.unittype, round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice > 1 AND Z.onlineprice < 1000 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' WHERE S1.StoreModFlag != 3 and d.id!=1 and d.id!=18 and s1.countryid=2 AND S1.State NOT IN (SELECT ID FROM LookupStates WHERE RegionId = 1 AND IsUnionTerritories = 1) and d.IsDefault=0 group by d.unittype UNION SELECT d.unittype, round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice > 1 AND Z.onlineprice < 1000 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and d.IsDefault=0 WHERE S1.StoreModFlag != 3 and d.id=18 or (d.id=17 and sa.RV=1) and s1.countryid=2 AND S1.State NOT IN (SELECT ID FROM LookupStates WHERE RegionId = 1 AND IsUnionTerritories = 1) and d.ID !=17 group by d.unittype UNION SELECT 'all units' as unittype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 AND S1.State NOT IN (SELECT ID FROM LookupStates WHERE RegionId = 1 AND IsUnionTerritories = 1) JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice > 1 AND Z.onlineprice < 1000 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 WHERE d.IsDefault=0 UNION ALL SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 AND S1.State NOT IN (SELECT ID FROM LookupStates WHERE RegionId = 1 AND IsUnionTerritories = 1) JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice > 1 AND Z.onlineprice < 1000 WHERE sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A UNION SELECT 'all reg' as unittype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 AND S1.State NOT IN (SELECT ID FROM LookupStates WHERE RegionId = 1 AND IsUnionTerritories = 1) JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice > 1 AND Z.onlineprice < 1000 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 WHERE d.IsDefault=0 and d.CC=0 UNION ALL SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 AND S1.State NOT IN (SELECT ID FROM LookupStates WHERE RegionId = 1 AND IsUnionTerritories = 1) JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice > 1 AND Z.onlineprice < 1000 WHERE sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A UNION SELECT 'all cc' as unittype, round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 AND S1.State NOT IN (SELECT ID FROM LookupStates WHERE RegionId = 1 AND IsUnionTerritories = 1) JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice > 1 AND Z.onlineprice < 1000 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 WHERE d.IsDefault=0 and d.CC=1";
		return query;
	}
	
	public static String stateAvgUnitRates() {
		String query = "SELECT d.unittype, round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice > 1 AND Z.onlineprice < 1000 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' WHERE S1.StoreModFlag != 3 and d.id!=1 and d.id!=18 and s1.countryid=2 and s1.State='"+_testData.state+"' and d.isdefault=0 group by d.unittype UNION SELECT d.unittype, round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice > 1 AND Z.onlineprice < 1000 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' WHERE S1.StoreModFlag != 3 and (d.id=18 or (d.id=17 and sa.RV=1)) and s1.countryid=2 and s1.State='"+_testData.state+"' and d.ID !=17 group by d.unittype UNION SELECT 'all units' as unittype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID AND s1.CountryId=2 JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice > 1 AND Z.onlineprice < 1000 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' WHERE s1.State='"+_testData.state+"' and D.IsDefault=0 UNION ALL SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 AND S1.State NOT IN (SELECT ID FROM LookupStates WHERE RegionId = 1 AND IsUnionTerritories = 1) JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice > 1 AND Z.onlineprice < 1000 WHERE s1.State='"+_testData.state+"' and sa.SpaceType='parking' and sa.Car=1 and sa.RV=1) A UNION SELECT 'all reg' as unittype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID AND s1.CountryId=2 JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice > 1 AND Z.onlineprice < 1000 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' WHERE s1.State='"+_testData.state+"' and D.IsDefault=0 and D.CC=0 UNION ALL SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 AND S1.State NOT IN (SELECT ID FROM LookupStates WHERE RegionId = 1 AND IsUnionTerritories = 1) JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice > 1 AND Z.onlineprice < 1000 WHERE s1.State='"+_testData.state+"' and sa.SpaceType='parking' and sa.Car=1 and sa.RV=1) A UNION SELECT 'all cc' as unittype, round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID AND s1.CountryId=2 JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice > 1 AND Z.onlineprice < 1000 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' WHERE s1.State='"+_testData.state+"' and D.IsDefault=0 and D.CC=1";
		return query;
	}
	
	public static String greenBlueAvgUnitRates(HashMap<String, String> mapDet) {
		String query = "SELECT d.unittype, round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND   Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and d.id!=1 and d.id!=18 and s1.countryid=2 group by d.unittype UNION SELECT d.unittype, round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and d.id=18 or (d.id=17 and sa.RV=1) and s1.countryid=2 and d.ID !=17 group by d.unittype UNION SELECT 'all units' as unittype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and d.IsDefault=0 UNION ALL SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 AND sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A UNION SELECT 'all reg' as unittype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and d.IsDefault=0 and d.CC=0 UNION ALL SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 AND sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A UNION SELECT 'all cc' as unittype, round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and d.IsDefault=0 and d.CC=1";
		return query;
	}
	
	
	
	
	
	/* Pricing Rental - Rate Per Square Feet History Graph */
	
	public static String thisMarketUnitRateHistory(String unitName, String datePrice) {
		String query = "";
		int unitId = getUnitID(unitName);
		if(unitName.toLowerCase().contains("all units")) {
			query = "Select round(avg (price),2) from (SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE D.IsDefault=0 union all SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 and sa.SpaceType='Parking' and (Car=1 and RV=1) JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+") z GROUP by z.DatePrice"; 
		} else if(unitName.toLowerCase().contains("all reg")) {
			query = "Select round(avg (price),2) from (SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 and D.CC=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE D.IsDefault=0 union all SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 and sa.SpaceType='Parking' and (Car=1 and RV=1) JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+") z GROUP by z.DatePrice";
		} else if(unitName.toLowerCase().contains("all cc")) {
			query = "SELECT round(avg((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0))),2) as price from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 and D.CC=1 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" GROUP by z.DatePrice";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "SELECT round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and D.ID=18 or (D.ID=17 and sa.RV=1) GROUP by z.DatePrice";
		} else {
			query = "SELECT round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as thismarket from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and d.id="+unitId+" GROUP by z.DatePrice";
		}
		return query;
	}
	
	public static String nationalUnitRateHistory(String unitName, String datePrice) {
		String query = "";
		int unitId = getUnitID(unitName);
		if(unitName.toLowerCase().contains("all units")) {
			query = "Select round(avg (price),2) from (SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0  THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' and D.IsDefault=0 WHERE D.IsDefault=0 AND S1.State NOT IN (SELECT ID FROM LookupStates WHERE RegionId = 1 AND IsUnionTerritories = 1) union all SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2)  as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 and sa.SpaceType='Parking' and (Car=1 and RV=1)) z GROUP by z.DatePrice"; 
		} else if(unitName.toLowerCase().contains("all reg")) {
			query = "Select round(avg (price),2) from (SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0  THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' and D.IsDefault=0 and D.CC=0 WHERE D.IsDefault=0 AND S1.State NOT IN (SELECT ID FROM LookupStates WHERE RegionId = 1 AND IsUnionTerritories = 1) union all SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 and sa.SpaceType='Parking' and (Car=1 and RV=1)) z GROUP by z.DatePrice";
		} else if(unitName.toLowerCase().contains("all cc")) {
			query = "SELECT round(avg(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1  ELSE ABS(Sa.length) END)),0)),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' and D.IsDefault=0 and D.CC=1 GROUP by z.DatePrice";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "SELECT round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' and D.IsDefault=0 WHERE S1.StoreModFlag != 3 and D.ID=18 or (D.ID=17 and sa.RV=1) GROUP by z.DatePrice";
		} else {
			query = "SELECT round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as nationalPrice from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' and D.IsDefault=0 WHERE S1.StoreModFlag != 3 and d.ID="+unitId+" GROUP by z.DatePrice";
		}
		return query;
	}
	
	public static String stateUnitRateHistory(String unitName, String datePrice) {
		String query = "";
		int unitId = getUnitID(unitName);
		if(unitName.toLowerCase().contains("all units")) {
			query = "Select round(avg (price),2) from (SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' and S1.State='"+_testData.state+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' and D.IsDefault=0 UNION ALL SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' and S1.State='"+_testData.state+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 and sa.SpaceType='Parking' and (Car=1 and RV=1)) z GROUP by z.DatePrice"; 
		} else if(unitName.toLowerCase().contains("all reg")) {
			query = "Select round(avg (price),2) from (SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' and S1.State='"+_testData.state+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' and D.IsDefault=0 and D.CC=0 UNION ALL SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' and S1.State='"+_testData.state+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 and sa.SpaceType='Parking' and (Car=1 and RV=1)) z GROUP by z.DatePrice";
		} else if(unitName.toLowerCase().contains("all cc")) {
			query = "SELECT round(avg((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0))),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' and S1.State='"+_testData.state+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' and D.IsDefault=0 and D.CC=1 GROUP by z.DatePrice";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "SELECT round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' and D.IsDefault=0 WHERE S1.StoreModFlag != 3 and S1.state='"+_testData.state+"' and D.ID=18 or (D.ID=17 and sa.RV=1) GROUP by z.DatePrice";
		} else {
			query = "SELECT round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as statePrice from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' and D.IsDefault=0 WHERE S1.StoreModFlag != 3 and d.ID="+unitId+" and S1.state='"+_testData.state+"' GROUP by z.DatePrice";
		}
		return query;
	}
	
	/* Compare */
	
	public static String greenBlueUnitRateHistory(String unitName, String datePrice, HashMap<String, String> mapDet) {
		String query = "";
		int unitId = getUnitID(unitName);
		if(unitName.toLowerCase().contains("all units")) {
			query = "Select round(avg (price),2) from (SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE D.IsDefault=0 union all SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 and sa.SpaceType='Parking' and (Car=1 and RV=1) JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+") z GROUP by z.DatePrice"; 
		} else if(unitName.toLowerCase().contains("all reg")) {
			query = "Select round(avg (price),2) from (SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 and D.CC=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE D.IsDefault=0 union all SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 and sa.SpaceType='Parking' and (Car=1 and RV=1) JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+") z GROUP by z.DatePrice";
		} else if(unitName.toLowerCase().contains("all cc")) {
			query = "SELECT round(avg((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0))),2) as price from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and D.IsDefault=0 and D.CC=1 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" GROUP by z.DatePrice";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "SELECT round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and D.ID=18 or (D.ID=17 and sa.RV=1) GROUP by z.DatePrice";
		} else {
			query = "SELECT round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as thismarket from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and d.id="+unitId+" GROUP by z.DatePrice";
		}
		return query;
	}
	
	
	
	
	
	/* Pricing Rental - Average Rates History */
	
	public static String thisMarketAvgRateHistory(String unitName, String datePrice) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "SELECT round(AVG(Z.price),2) as price FROM (SELECT Z.onlineprice as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE d.IsDefault=0 union all SELECT Z.onlineprice as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 and (car=1 and rv=1) and Sa.Spacetype='Parking' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+") z group by z.DatePrice"; 
		} else if(unitName.toLowerCase().contains("all reg")) {
			query = "SELECT round(AVG(Z.price),2) as price FROM (SELECT Z.onlineprice as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE d.IsDefault=0 and d.CC=0 union all SELECT Z.onlineprice as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 and (car=1 and rv=1) and Sa.Spacetype='Parking' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+") z group by z.DatePrice";
		} else if(unitName.toLowerCase().contains("all cc")) {
			query = "SELECT round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and d.IsDefault=0 and d.CC=1 group by z.DatePrice";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and d.id=18 or (d.id=17 and sa.RV=1) group by z.DatePrice";
		} else {
			int unitId = getUnitID(unitName);
			query = "select round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and d.id="+unitId+" group by z.DatePrice";
		}
		return query;
	}
	

	public static String nationalAvgRateHistory(String unitName, String datePrice) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "SELECT round(AVG(Z.price),2) as price FROM (SELECT CASE WHEN  Z.onlineprice!=0 then  Z.onlineprice else Z.regularprice END as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' WHERE d.IsDefault=0 and S1.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1) union all SELECT CASE WHEN  Z.onlineprice!=0 then  Z.onlineprice else Z.regularprice END as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 and sa.car=1 and sa.rv=1 and Sa.Spacetype='Parking' JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' WHERE S1.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1)) z group by z.DatePrice"; 
		} else if(unitName.toLowerCase().contains("all reg")) {
			query = "SELECT round(AVG(Z.price),2) as price FROM (SELECT CASE WHEN  Z.onlineprice!=0 then  Z.onlineprice else Z.regularprice END as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' WHERE d.IsDefault=0 and S1.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1) and d.CC=0 union all SELECT CASE WHEN  Z.onlineprice!=0 then  Z.onlineprice else Z.regularprice END as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 and sa.car=1 and sa.rv=1 and Sa.Spacetype='Parking' JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' WHERE S1.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1)) z group by z.DatePrice";
		} else if(unitName.toLowerCase().contains("all cc")) {
			query = "SELECT round(AVG(Z.price),2) as price FROM (SELECT CASE WHEN Z.onlineprice!=0 then Z.onlineprice else Z.regularprice END as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' and d.IsDefault=0 and d.CC=1 WHERE S1.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1)) Z group by z.DatePrice";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select ROUND(AVG(CASE WHEN Z.onlineprice!=0 then  Z.onlineprice else Z.regularprice END),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' WHERE d.IsDefault=0 and d.id=18 or (d.id=17 and sa.RV=1) and S1.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1) group by z.DatePrice";
		} else {
			int unitId = getUnitID(unitName);
			query = "SELECT ROUND(AVG(CASE WHEN Z.onlineprice!=0 then  Z.onlineprice else Z.regularprice END),2) as price from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' WHERE d.IsDefault=0 and d.ID="+unitId+" and S1.State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1) group by z.DatePrice";
		}
		return query;
	}
	
	public static String stateAvgRateHistory(String unitName, String datePrice) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "SELECT round(AVG(Z.price),2) as price FROM (SELECT Z.onlineprice as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice >1 and  z.onlineprice<1000 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' WHERE s1.State='"+_testData.state+"' and D.IsDefault=0 UNION ALL SELECT Z.onlineprice as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space sa ON sa.SpaceID=Z.SpaceID  AND Z.onlineprice >1 and  z.onlineprice<1000 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' WHERE s1.State='"+_testData.state+"' and sa.SpaceType='Parking' and sa.Car=1 and sa.RV=1) Z group by z.DatePrice"; 
		} else if(unitName.toLowerCase().contains("all reg")) {
			query = "SELECT round(AVG(Z.price),2) as price FROM (SELECT Z.onlineprice as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice >1 and  z.onlineprice<1000 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' WHERE s1.State='"+_testData.state+"' and D.IsDefault=0 and D.CC=0 UNION ALL SELECT Z.onlineprice as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space sa ON sa.SpaceID=Z.SpaceID  AND Z.onlineprice >1 and  z.onlineprice<1000 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' WHERE s1.State='"+_testData.state+"' and sa.SpaceType='Parking' and sa.Car=1 and sa.RV=1) Z group by z.DatePrice";
		} else if(unitName.toLowerCase().contains("all cc")) {
			query = "SELECT round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice >1 and  z.onlineprice<1000 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' WHERE s1.State='"+_testData.state+"' and D.IsDefault=0 and D.CC=1 group by z.DatePrice";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice >1 and  z.onlineprice<1000 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' WHERE D.IsDefault=0 and d.id=18 or (d.id=17 and sa.RV=1) and s1.countryid=2 and s1.State='"+_testData.state+"' group by z.DatePrice";
		} else {
			int unitId = getUnitID(unitName);
			query = "select round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice >1 and  z.onlineprice<1000 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' WHERE s1.State='"+_testData.state+"' and D.IsDefault=0 and D.ID="+unitId+" group by z.DatePrice";
		}
		return query;
	}
	
	/* Compare */
	
	public static String greenBlueAvgRateHistory(String unitName, String datePrice, HashMap<String, String> mapDet) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "SELECT round(AVG(Z.price),2) as price FROM (SELECT Z.onlineprice as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE d.IsDefault=0 union all SELECT Z.onlineprice as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 and (car=1 and rv=1) and Sa.Spacetype='Parking' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+") z group by z.DatePrice"; 
		} else if(unitName.toLowerCase().contains("all reg")) {
			query = "SELECT round(AVG(Z.price),2) as price FROM (SELECT Z.onlineprice as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='1,2' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE d.IsDefault=0 and d.CC=0 union all SELECT Z.onlineprice as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 and (car=1 and rv=1) and Sa.Spacetype='Parking' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+") z group by z.DatePrice";
		} else if(unitName.toLowerCase().contains("all cc")) {
			query = "SELECT round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and d.IsDefault=0 and d.CC=1 group by z.DatePrice";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and d.id=18 or (d.id=17 and sa.RV=1) group by z.DatePrice";
		} else {
			int unitId = getUnitID(unitName);
			query = "select round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='1,2' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and d.id="+unitId+" group by z.DatePrice";
		}
		return query;
	}
	
	
	
	/* Pricing Rental - Rate History By Store */
	
	public static String marketRateHistory(String valueType, String unitName, String datePrice) {
		String minMax = (valueType.equalsIgnoreCase("Premium")) ? "max" : "min";
		String query = "";
		
		if(unitName.toLowerCase().contains("all units")) {
			if(valueType.equalsIgnoreCase("Premium")) {
				query = "select sp.Premium as Price, sp.storeid, sp.DefaultUnitId  into #a from PriceChangesVolatilityDefaultUnits Sp with (nolock) join MarketviewUserStoreCompset mvc with (nolock) on mvc.storeid=sp.StoreID and mvc.userstoreid="+_testData.userStoreId+" join DefaultUnitSizes DU with (nolock) on DU.id=Sp.defaultunitid and DU.ISdefault=0 and DU.CountryId in ('1,2') and sp.DatePrice2='"+datePrice+"' and sp.Premium>1 and sp.Premium<1000 select max(price) as high, ROUND(AVG(Price),2) as average, MIN(Price) as low from #a drop table #a";
			} else {
				query = "select sp.Value as Price, sp.storeid, sp.DefaultUnitId  into #a from PriceChangesVolatilityDefaultUnits Sp with (nolock) join MarketviewUserStoreCompset mvc with (nolock) on mvc.storeid=sp.StoreID and mvc.userstoreid="+_testData.userStoreId+" join DefaultUnitSizes DU with (nolock) on DU.id=Sp.defaultunitid and DU.ISdefault=0 and DU.CountryId in ('1,2') and sp.DatePrice2='"+datePrice+"' and sp.Value>1 and sp.Value<1000 select max(price) as high, ROUND(AVG(Price),2) as average, MIN(Price) as low from #a drop table #a";
			}
		} else if(unitName.toLowerCase().contains("all reg")) {
			if(valueType.equalsIgnoreCase("Premium")) {
				query = "select sp.Premium as Price, sp.storeid, sp.DefaultUnitId  into #a from PriceChangesVolatilityDefaultUnits Sp with (nolock) join MarketviewUserStoreCompset mvc with (nolock) on mvc.storeid=sp.StoreID and mvc.userstoreid="+_testData.userStoreId+" join DefaultUnitSizes DU with (nolock) on DU.id=Sp.defaultunitid and DU.ISdefault=0 and DU.CC=0 and DU.CountryId in ('1,2') and sp.DatePrice2='"+datePrice+"' and sp.Premium>1 and sp.Premium<1000 select max(price) as high, ROUND(AVG(Price),2) as average, MIN(Price) as low from #a drop table #a";
			} else {
				query = "select sp.Value as Price, sp.storeid, sp.DefaultUnitId  into #a from PriceChangesVolatilityDefaultUnits Sp with (nolock) join MarketviewUserStoreCompset mvc with (nolock) on mvc.storeid=sp.StoreID and mvc.userstoreid="+_testData.userStoreId+" join DefaultUnitSizes DU with (nolock) on DU.id=Sp.defaultunitid and DU.ISdefault=0 and DU.CC=0 and DU.CountryId in ('1,2') and sp.DatePrice2='"+datePrice+"' and sp.Value>1 and sp.Value<1000 select max(price) as high, ROUND(AVG(Price),2) as average, MIN(Price) as low from #a drop table #a";
			}
		} else if(unitName.toLowerCase().contains("all cc")) {
			if(valueType.equalsIgnoreCase("Premium")) {
				query = "select sp.Premium as Price, sp.storeid, sp.DefaultUnitId  into #a from PriceChangesVolatilityDefaultUnits Sp with (nolock) join MarketviewUserStoreCompset mvc with (nolock) on mvc.storeid=sp.StoreID and mvc.userstoreid="+_testData.userStoreId+" join DefaultUnitSizes DU with (nolock) on DU.id=Sp.defaultunitid and DU.ISdefault=0 and DU.CC=1 and DU.CountryId in ('1,2') and sp.DatePrice2='"+datePrice+"' and sp.Premium>1 and sp.Premium<1000 select max(price) as high, ROUND(AVG(Price),2) as average, MIN(Price) as low from #a drop table #a";
			} else {
				query = "select sp.Value as Price, sp.storeid, sp.DefaultUnitId  into #a from PriceChangesVolatilityDefaultUnits Sp with (nolock) join MarketviewUserStoreCompset mvc with (nolock) on mvc.storeid=sp.StoreID and mvc.userstoreid="+_testData.userStoreId+" join DefaultUnitSizes DU with (nolock) on DU.id=Sp.defaultunitid and DU.ISdefault=0 and DU.CC=1 and DU.CountryId in ('1,2') and sp.DatePrice2='"+datePrice+"' and sp.Value>1 and sp.Value<1000 select max(price) as high, ROUND(AVG(Price),2) as average, MIN(Price) as low from #a drop table #a";
			}
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select "+minMax+"(onlineprice) as Price, storeid into #a from SpacePriceHistory with (nolock) where storeid in (select storeid from MarketviewUserStoreCompset where userstoreid = "+_testData.userStoreId+") and spaceid in (Select SpaceID from Space where defaultunitid=18 or (defaultunitid=17 and RV=1)) and Dateprice='"+datePrice+"' and Onlineprice >1 and Onlineprice < 1000  group by storeid select MAX(Price) as high, ROUND(AVG(Price),2) as average, MIN(Price) as low from #a Drop Table #a";
		} else {
			int unitId = getUnitID(unitName);
			query = "select "+minMax+"(onlineprice) as Price, storeid into #a from SpacePriceHistory with (nolock) where storeid in (select storeid from MarketviewUserStoreCompset where userstoreid = "+_testData.userStoreId+") and spaceid in (Select SpaceID from Space where defaultunitid="+unitId+") and Dateprice='"+datePrice+"' and Onlineprice >1 and Onlineprice < 1000  group by storeid select MAX(Price) as high, ROUND(AVG(Price),2) as average, MIN(Price) as low from #a Drop Table #a";
		}
		
		return query;
	}
	
	
	public static String storeRateHistory(String valueType, int storeId, String unitName, String datePrice) {
		String minMax = (valueType=="Premium") ? "max" : "min";
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			if(valueType.equalsIgnoreCase("Premium")) {
				query = "select sp.Premium as Price, sp.storeid, sp.DefaultUnitId  into #a from PriceChangesVolatilityDefaultUnits Sp with (nolock) join DefaultUnitSizes DU with (nolock) on DU.id=Sp.defaultunitid and DU.ISdefault=0 and DU.CountryId in ('1,2') and sp.DatePrice2='"+datePrice+"' and sp.Premium>1 and sp.Premium<1000 and sp.StoreId="+storeId+" select ROUND(AVG(Price),2) as average from #a drop table #a";
			} else {
				query = "select sp.Value as Price, sp.storeid, sp.DefaultUnitId  into #a from PriceChangesVolatilityDefaultUnits Sp with (nolock) join DefaultUnitSizes DU with (nolock) on DU.id=Sp.defaultunitid and DU.ISdefault=0 and DU.CountryId in ('1,2') and sp.DatePrice2='"+datePrice+"' and sp.Value>1 and sp.Value<1000 and sp.StoreId="+storeId+" select ROUND(AVG(Price),2) as average from #a drop table #a";
			}
		} else if(unitName.toLowerCase().contains("all reg")) {
			if(valueType.equalsIgnoreCase("Premium")) {
				query = "select sp.Premium as Price, sp.storeid, sp.DefaultUnitId  into #a from PriceChangesVolatilityDefaultUnits Sp with (nolock) join DefaultUnitSizes DU with (nolock) on DU.id=Sp.defaultunitid and DU.ISdefault=0 and DU.CC=0 and DU.CountryId in ('1,2') and sp.DatePrice2='"+datePrice+"' and sp.Premium>1 and sp.Premium<1000 and sp.StoreId="+storeId+" select ROUND(AVG(Price),2) as average from #a drop table #a";
			} else {
				query = "select sp.Value as Price, sp.storeid, sp.DefaultUnitId  into #a from PriceChangesVolatilityDefaultUnits Sp with (nolock) join DefaultUnitSizes DU with (nolock) on DU.id=Sp.defaultunitid and DU.ISdefault=0 and DU.CC=0 and DU.CountryId in ('1,2') and sp.DatePrice2='"+datePrice+"' and sp.Value>1 and sp.Value<1000 and sp.StoreId="+storeId+" select ROUND(AVG(Price),2) as average from #a drop table #a";
			}
		} else if(unitName.toLowerCase().contains("all cc")) {
			if(valueType.equalsIgnoreCase("Premium")) {
				query = "select sp.Premium as Price, sp.storeid, sp.DefaultUnitId  into #a from PriceChangesVolatilityDefaultUnits Sp with (nolock) join DefaultUnitSizes DU with (nolock) on DU.id=Sp.defaultunitid and DU.ISdefault=0 and DU.CC=1 and DU.CountryId in ('1,2') and sp.DatePrice2='"+datePrice+"' and sp.Premium>1 and sp.Premium<1000 and sp.StoreId="+storeId+" select ROUND(AVG(Price),2) as average from #a drop table #a";
			} else {
				query = "select sp.Value as Price, sp.storeid, sp.DefaultUnitId  into #a from PriceChangesVolatilityDefaultUnits Sp with (nolock) join DefaultUnitSizes DU with (nolock) on DU.id=Sp.defaultunitid and DU.ISdefault=0 and DU.CC=1 and DU.CountryId in ('1,2') and sp.DatePrice2='"+datePrice+"' and sp.Value>1 and sp.Value<1000 and sp.StoreId="+storeId+" select ROUND(AVG(Price),2) as average from #a drop table #a";
			}
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select "+minMax+"(onlineprice) as Price from SpacePriceHistory with (nolock) where storeid = "+storeId+" and Dateprice ='"+datePrice+"' and SpaceID in (Select SpaceID from Space where defaultunitid=18 or (defaultunitid=17 and RV=1))";
		} else {
			int unitId = getUnitID(unitName);
			query = "select "+minMax+"(onlineprice) as Price from SpacePriceHistory with (nolock) where storeid = "+storeId+" and Dateprice ='"+datePrice+"' and SpaceID in (Select SpaceID from Space where defaultunitid="+unitId+")";
		}
		return query;
	}
	
	
	
	
	
	
	/* Pricing Rental - Rate Volatility History */
	
	public static String marketRateVolatilityHistory(String unitName, String dateFrom, String dateTo) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a join defaultunitsizes b on a.DefaultUnitId = b.id join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId join MarketViewUserStoreCompSet c on d1.storeid = c.storeid and c.userstoreid = "+_testData.userStoreId+" and c.IsTracked = 1 where a.DatePrice2 between '"+dateFrom+"' and '"+dateTo+"'"; 
		} else if(unitName.toLowerCase().contains("all reg")) {
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a join defaultunitsizes b on a.DefaultUnitId = b.id and b.Isdefault=0 and b.cc=0 join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId join MarketViewUserStoreCompSet c on d1.storeid = c.storeid and c.userstoreid = "+_testData.userStoreId+" and c.IsTracked = 1 where   a.DatePrice2 BETWEEN '"+dateFrom+"' and '"+dateTo+"'";
		} else if(unitName.toLowerCase().contains("all cc")) {
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a join defaultunitsizes b on a.DefaultUnitId = b.id and b.Isdefault=0 and b.cc=1 join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId join MarketViewUserStoreCompSet c on d1.storeid = c.storeid and c.userstoreid = 4179 and c.IsTracked = 1 where   a.DatePrice2 BETWEEN '"+dateFrom+"' and '"+dateTo+"'";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "";
		} else {
			int unitId = getUnitID(unitName);
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a join defaultunitsizes b on a.DefaultUnitId = b.id join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId join MarketViewUserStoreCompSet c on d1.storeid = c.storeid and c.userstoreid = "+_testData.userStoreId+" and c.IsTracked = 1 and b.id="+unitId+" where a.DatePrice2 between '"+dateFrom+"' and '"+dateTo+"'";
		}
		return query;
	}
	
	
	public static String nationalRateVolatilityHistory(String unitName, String dateFrom, String dateTo) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a with (nolock) join defaultunitsizes b on a.DefaultUnitId = b.id join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId where a.DatePrice2 between '"+dateFrom+"' and '"+dateTo+"'"; 
		} else if(unitName.toLowerCase().contains("all reg")) {
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a with (nolock) join defaultunitsizes b on a.DefaultUnitId = b.id and b.IsDefault=0 and b.CC=0 and b.countryid in ('1,2') join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId where a.DatePrice2 between '"+dateFrom+"' and '"+dateTo+"'";
		} else if(unitName.toLowerCase().contains("all cc")) {
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a with (nolock) join defaultunitsizes b on a.DefaultUnitId = b.id and b.IsDefault=0 and b.CC=1 and b.countryid in ('1,2') join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId where a.DatePrice2 between '"+dateFrom+"' and '"+dateTo+"'";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "";
		} else {
			int unitId = getUnitID(unitName);
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a with (nolock) join defaultunitsizes b on a.DefaultUnitId = b.id join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId and b.id="+unitId+" where a.DatePrice2 between '"+dateFrom+"' and '"+dateTo+"'";
		}
		return query;
	}
	
	
	public static String stateRateVolatilityHistory(String unitName, String dateFrom, String dateTo) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a with (nolock) join defaultunitsizes b on a.DefaultUnitId = b.id join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId and d.State='"+_testData.state+"' where a.DatePrice2 between '"+dateFrom+"' and '"+dateTo+"'"; 
		} else if(unitName.toLowerCase().contains("all reg")) {
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a with (nolock) join defaultunitsizes b on a.DefaultUnitId = b.id and b.IsDefault=0 and b.cc=0 join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId and d.State='"+_testData.state+"' where a.DatePrice2 BETWEEN '"+dateFrom+"' and '"+dateTo+"'";
		} else if(unitName.toLowerCase().contains("all cc")) {
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a with (nolock) join defaultunitsizes b on a.DefaultUnitId = b.id and b.IsDefault=0 and b.cc=1 join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId and d.State='"+_testData.state+"' where a.DatePrice2 BETWEEN '"+dateFrom+"' and '"+dateTo+"'";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "";
		} else {
			int unitId = getUnitID(unitName);
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a with (nolock) join defaultunitsizes b on a.DefaultUnitId = b.id join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId and d.State='"+_testData.state+"' and b.id="+unitId+" where a.DatePrice2 between '"+dateFrom+"' and '"+dateTo+"'";
		}
		return query;
	}
	
	
	public static String greenBlueRateVolatilityHistory(String unitName, String dateFrom, String dateTo, HashMap<String, String> mapDet) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a join defaultunitsizes b on a.DefaultUnitId = b.id join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId join MarketViewUserStoreCompSet c on d1.storeid = c.storeid and c.userstoreid = "+mapDet.get("UserStoreId")+" and c.IsTracked = 1 where a.DatePrice2 between '"+dateFrom+"' and '"+dateTo+"'"; 
		} else if(unitName.toLowerCase().contains("all reg")) {
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a join defaultunitsizes b on a.DefaultUnitId = b.id and b.Isdefault=0 and b.cc=0 join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId join MarketViewUserStoreCompSet c on d1.storeid = c.storeid and c.userstoreid = "+mapDet.get("UserStoreId")+" and c.IsTracked = 1 where   a.DatePrice2 BETWEEN '"+dateFrom+"' and '"+dateTo+"'";
		} else if(unitName.toLowerCase().contains("all cc")) {
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a join defaultunitsizes b on a.DefaultUnitId = b.id and b.Isdefault=0 and b.cc=1 join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId join MarketViewUserStoreCompSet c on d1.storeid = c.storeid and c.userstoreid = "+mapDet.get("UserStoreId")+" and c.IsTracked = 1 where   a.DatePrice2 BETWEEN '"+dateFrom+"' and '"+dateTo+"'";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "";
		} else {
			int unitId = getUnitID(unitName);
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a join defaultunitsizes b on a.DefaultUnitId = b.id join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId join MarketViewUserStoreCompSet c on d1.storeid = c.storeid and c.userstoreid = "+mapDet.get("UserStoreId")+" and c.IsTracked = 1 and b.id="+unitId+" where a.DatePrice2 between '"+dateFrom+"' and '"+dateTo+"'";
		}
		return query;
	}
	
	
	/* Stores in Market */
	/* Store Types */
	
	public static String thisMarketStoreTypes() {
		String query = "select Storesinmarket, REITS, Round((REITS*1.0/Storesinmarket)*100,2) as reitspercent, MIDOPS, Round((MIDOPS*1.0/Storesinmarket)*100, 2) as midopspercent, SMALLOPS, Round((SMALLOPS*1.0/Storesinmarket)*100,2) as smallopspercent from (select 1 as tomap1, COUNT(*) as Storesinmarket from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and storemodflag!= 3) as sto join (select 1 as tomap2, COUNT(*) as REITS from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and storemodflag != 3 and REIT=1) as re on sto.tomap1 = re.tomap2 join (select 1 as tomap3, COUNT(*) as MIDOPS from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and storemodflag != 3 and MultiOperator=1) as mi on re.tomap2 = mi.tomap3 join (select 1 as tomap4, COUNT(*) as SMALLOPS from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and storemodflag != 3 and SingleOperator=1) as sm on mi.tomap3 = sm.tomap4";
		return query;
	}
	
	public static String nationalStoreTypes() {
		String query = "select Storesinnation, REITS, Round((REITS*1.0/Storesinnation)*100,2) as reitspercent, MIDOPS, Round((MIDOPS*1.0/Storesinnation)*100, 2) as midopspercent, SMALLOPS, Round((SMALLOPS*1.0/Storesinnation)*100,2) as smallopspercent from (select 1 as tomap1, COUNT(*) as Storesinnation from stores where storemodflag != 3 and CountryId=2 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) as st join (select 1 as tomap2, COUNT(*) as REITS from stores where storemodflag != 3 and CountryId=2 and REIT=1 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) as re on st.tomap1 = re.tomap2 join (select 1 as tomap3, COUNT(*) as MIDOPS from stores where storemodflag != 3 and CountryId=2 and MultiOperator=1 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) as mi on re.tomap2 = mi.tomap3 join (select 1 as tomap4, COUNT(*) as SMALLOPS from stores where storemodflag != 3 and CountryId=2 and SingleOperator=1 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) as sm on mi.tomap3 = sm.tomap4";
		return query;
	}
	
	public static String stateStoreTypes() {
		String query = "select Storesinstate, REITS, Round((REITS*1.0/Storesinstate)*100,2) as reitspercent, MIDOPS, Round((MIDOPS*1.0/Storesinstate)*100, 2) as midopspercent, SMALLOPS, Round((SMALLOPS*1.0/Storesinstate)*100,2) as smallopspercent from (select 1 as tomap1, COUNT(*) as Storesinstate from stores where storemodflag != 3 and CountryId=2 and State='"+_testData.state+"') as st join (select 1 as tomap2, COUNT(*) as REITS from stores where storemodflag != 3 and CountryId=2 and REIT =1 and State='"+_testData.state+"') as re on st.tomap1 = re.tomap2 join (select 1 as tomap3, COUNT(*) as MIDOPS from stores where storemodflag != 3 and CountryId=2 and MultiOperator =1 and State='"+_testData.state+"') as mi on re.tomap2 = mi.tomap3 join (select 1 as tomap4, COUNT(*) as SMALLOPS from stores where storemodflag != 3 and CountryId=2 and SingleOperator =1 and State='"+_testData.state+"') as sm on mi.tomap3 = sm.tomap4";
		return query;
	}
	
	public static String greenBlueStoreTypes(HashMap<String, String> mapDet) {
		String query = "select Storesinmarket, REITS, Round((REITS*1.0/Storesinmarket)*100,2) as reitspercent, MIDOPS, Round((MIDOPS*1.0/Storesinmarket)*100, 2) as midopspercent, SMALLOPS, Round((SMALLOPS*1.0/Storesinmarket)*100,2) as smallopspercent from (select 1 as tomap1, COUNT(*) as Storesinmarket from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and storemodflag!= 3) as sto join (select 1 as tomap2, COUNT(*) as REITS from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and storemodflag != 3 and REIT=1) as re on sto.tomap1 = re.tomap2 join (select 1 as tomap3, COUNT(*) as MIDOPS from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and storemodflag != 3 and MultiOperator=1) as mi on re.tomap2 = mi.tomap3 join (select 1 as tomap4, COUNT(*) as SMALLOPS from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and storemodflag != 3 and SingleOperator=1) as sm on mi.tomap3 = sm.tomap4";
		return query;
	}
	
	
	/* Unit Types Offered */
	
	public static String thisMarketUnitTypes(String unitName) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(*) as totalunits from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and storemodflag != 3) as st join (SELECT 1 as tomap2, count(distinct A.count1) as noofunits FROM (select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid in (select id from DefaultUnitSizes where IsDefault=0)) UNION ALL select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and iscurrentprice = 1 and spaceid in (select spaceid from space where spacetype = 'Parking' AND Car = 1 AND RV = 1)) A) as un on st.tomap1 = un.tomap2"; 
		} else if(unitName.toLowerCase().contains("all reg")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(*) as totalunits from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and storemodflag != 3) as st join (SELECT 1 as tomap2, count(distinct A.count1) as noofunits FROM (select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid in (select id from DefaultUnitSizes where IsDefault=0 and CC=0)) UNION ALL select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and iscurrentprice = 1 and spaceid in (select spaceid from space where spacetype = 'Parking' AND Car = 1 AND RV = 1)) A) as un on st.tomap1 = un.tomap2";
		} else if(unitName.toLowerCase().contains("all cc")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(*) as totalunits from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and storemodflag != 3) as st join (select 1 as tomap2, count(distinct storeid) as noofunits from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid in (select id from DefaultUnitSizes where IsDefault=0 and CC=1))) as un on st.tomap1 = un.tomap2";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(*) as totalunits from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and storemodflag != 3) as st join (select 1 as tomap2, count(distinct storeid) as noofunits from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid=18 or (defaultunitid=17 and RV=1))) as un on st.tomap1 = un.tomap2";
		} else {
			int unitId = getUnitID(unitName);
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(*) as totalunits from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and storemodflag != 3) as st join (select 1 as tomap2, count(distinct storeid) as noofunits from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid = "+unitId+")) as un on st.tomap1 = un.tomap2";
		}
		return query;
	}
	
	
	public static String nationalUnitTypes(String unitName) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(distinct s.storeid) as totalunits from Stores s JOIN SpacePrice sp on s.StoreID=sp.storeid and s.countryid=2 and sp.iscurrentprice=1 JOIN space sh on sh.spaceid=sp.spaceid JOIN DefaultUnitSizes d on sh.defaultunitid=d.ID and d.IsDefault=0 Where s.State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) as tot join (SELECT 1 as tomap2, count(distinct A.count1) as noofunits FROM (select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from stores where storemodflag != 3 and CountryId=2 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid in (select id from DefaultUnitSizes where IsDefault=0)) UNION ALL select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from stores where storemodflag != 3 and CountryId=2 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) and iscurrentprice = 1 and spaceid in (select spaceid from space where SpaceType = 'parking' and car=1 and rv=1)) A) as un on tot.tomap1 = un.tomap2"; 
		} else if(unitName.toLowerCase().contains("all reg")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(distinct s.storeid) as totalunits from Stores s JOIN SpacePrice sp on s.StoreID=sp.storeid and s.countryid=2 and sp.iscurrentprice=1 JOIN space sh on sh.spaceid=sp.spaceid JOIN DefaultUnitSizes d on sh.defaultunitid=d.ID and d.IsDefault=0 Where s.State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) as tot join (SELECT 1 as tomap2, count(distinct A.count1) as noofunits FROM (select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from stores where storemodflag != 3 and CountryId=2 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid in (select id from DefaultUnitSizes where IsDefault=0 and CC=0)) UNION ALL select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from stores where storemodflag != 3 and CountryId=2 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) and iscurrentprice = 1 and spaceid in (select spaceid from space where SpaceType = 'parking' and car=1 and rv=1)) A) as un on tot.tomap1 = un.tomap2";
		} else if(unitName.toLowerCase().contains("all cc")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(distinct s.storeid) as totalunits from Stores s JOIN SpacePrice sp on s.StoreID=sp.storeid and s.countryid=2 and sp.iscurrentprice=1 JOIN space sh on sh.spaceid=sp.spaceid JOIN DefaultUnitSizes d on sh.defaultunitid=d.ID and d.IsDefault=0 Where s.State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) as tot join (SELECT 1 as tomap2, count(distinct storeid) as noofunits from SpacePrice with (nolock) where storeid in (select storeid from stores where storemodflag != 3 and CountryId=2 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid in (select id from DefaultUnitSizes where IsDefault=0 and CC=1))) as un on tot.tomap1 = un.tomap2";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(distinct s.storeid) as totalunits from Stores s JOIN SpacePrice sp on s.StoreID=sp.storeid and s.countryid=2 and sp.iscurrentprice=1 JOIN space sh on sh.spaceid=sp.spaceid JOIN DefaultUnitSizes d on sh.defaultunitid=d.ID and d.IsDefault=0 Where s.State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) as tot join (select 1 as tomap2, count(distinct storeid) as noofunits from SpacePrice with (nolock) where storeid in (select storeid from stores where storemodflag != 3 and CountryId=2 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid=18 or (defaultunitid=17 and RV=1))) as un on tot.tomap1 = un.tomap2";
		} else {
			int unitId = getUnitID(unitName);
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(distinct s.storeid) as totalunits from Stores s JOIN SpacePrice sp on s.StoreID=sp.storeid and s.countryid=2 and sp.iscurrentprice=1 JOIN space sh on sh.spaceid=sp.spaceid JOIN DefaultUnitSizes d on sh.defaultunitid=d.ID and d.IsDefault=0 Where s.State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) as tot join (select 1 as tomap2, count(distinct storeid) as noofunits from SpacePrice with (nolock) where storeid in (select storeid from stores where storemodflag != 3 and CountryId=2 and State not in (select id from LookupStates where CountryId = 2 and IsUnionTerritories = 1)) and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid = "+unitId+")) as un on tot.tomap1 = un.tomap2";
		}
		return query;
	}
	
	
	public static String stateUnitTypes(String unitName) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(distinct s.storeid) as totalunits from Stores s JOIN SpacePrice sp on s.StoreID=sp.storeid and s.countryid=2 and sp.iscurrentprice=1 JOIN space sh on sh.spaceid=sp.spaceid JOIN DefaultUnitSizes d on sh.defaultunitid=d.ID and d.IsDefault=0 and s.State='"+_testData.state+"') as tot join (SELECT 1 as tomap2, count(distinct A.count1) as noofunits FROM (select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from stores where storemodflag != 3 and CountryId=2 and State='"+_testData.state+"') and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid in (select id from DefaultUnitSizes where IsDefault=0)) UNION ALL select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from stores where storemodflag != 3 and CountryId=2 and State='"+_testData.state+"') and iscurrentprice = 1 and spaceid in (select spaceid from space where SpaceType = 'parking' and car=1 and rv=1)) A) as un on tot.tomap1 = un.tomap2"; 
		} else if(unitName.toLowerCase().contains("all reg")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(distinct s.storeid) as totalunits from Stores s JOIN SpacePrice sp on s.StoreID=sp.storeid and s.countryid=2 and sp.iscurrentprice=1 JOIN space sh on sh.spaceid=sp.spaceid JOIN DefaultUnitSizes d on sh.defaultunitid=d.ID and d.IsDefault=0 and s.State='"+_testData.state+"') as tot join (SELECT 1 as tomap2, count(distinct A.count1) as noofunits FROM (select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from stores where storemodflag != 3 and CountryId=2 and State='"+_testData.state+"') and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid in (select id from DefaultUnitSizes where IsDefault=0 and CC=0)) UNION ALL select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from stores where storemodflag != 3 and CountryId=2 and State='"+_testData.state+"') and iscurrentprice = 1 and spaceid in (select spaceid from space where SpaceType = 'parking' and car=1 and rv=1)) A) as un on tot.tomap1 = un.tomap2";
		} else if(unitName.toLowerCase().contains("all cc")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(distinct s.storeid) as totalunits from Stores s JOIN SpacePrice sp on s.StoreID=sp.storeid and s.countryid=2 and sp.iscurrentprice=1 JOIN space sh on sh.spaceid=sp.spaceid JOIN DefaultUnitSizes d on sh.defaultunitid=d.ID and d.IsDefault=0 and s.State='"+_testData.state+"') as tot join (SELECT 1 as tomap2, count(distinct storeid) as noofunits from SpacePrice with (nolock) where storeid in (select storeid from stores where storemodflag != 3 and CountryId=2 and State='"+_testData.state+"') and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid in (select id from DefaultUnitSizes where IsDefault=0 and CC=1))) as un on tot.tomap1 = un.tomap2";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(distinct s.storeid) as totalunits from Stores s JOIN SpacePrice sp on s.StoreID=sp.storeid and s.countryid=2 and sp.iscurrentprice=1 JOIN space sh on sh.spaceid=sp.spaceid JOIN DefaultUnitSizes d on sh.defaultunitid=d.ID and d.IsDefault=0 and s.State='"+_testData.state+"') as st join (select 1 as tomap2, count(distinct storeid) as noofunits from SpacePrice with (nolock) where storeid in (select storeid from stores where storemodflag != 3 and CountryId=2 and State='"+_testData.state+"') and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid=18 or (defaultunitid=17 and RV=1))) as un on st.tomap1 = un.tomap2";
		} else {
			int unitId = getUnitID(unitName);
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(distinct s.storeid) as totalunits from Stores s JOIN SpacePrice sp on s.StoreID=sp.storeid and s.countryid=2 and sp.iscurrentprice=1 JOIN space sh on sh.spaceid=sp.spaceid JOIN DefaultUnitSizes d on sh.defaultunitid=d.ID and d.IsDefault=0 and s.State='"+_testData.state+"') as st join (select 1 as tomap2, count(distinct storeid) as noofunits from SpacePrice with (nolock) where storeid in (select storeid from stores where storemodflag != 3 and CountryId=2 and State='"+_testData.state+"') and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid = "+unitId+")) as un on st.tomap1 = un.tomap2";
		}
		return query;
	}
	
	public static String greenBlueUnitTypes(String unitName, HashMap<String, String> mapDet) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(*) as totalunits from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and storemodflag != 3) as st join (SELECT 1 as tomap2, count(distinct A.count1) as noofunits FROM (select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid in (select id from DefaultUnitSizes where IsDefault=0)) UNION ALL select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and iscurrentprice = 1 and spaceid in (select spaceid from space where spacetype = 'Parking' AND Car = 1 AND RV = 1)) A) as un on st.tomap1 = un.tomap2"; 
		} else if(unitName.toLowerCase().contains("all reg")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(*) as totalunits from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and storemodflag != 3) as st join (SELECT 1 as tomap2, count(distinct A.count1) as noofunits FROM (select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid in (select id from DefaultUnitSizes where IsDefault=0 and CC=0)) UNION ALL select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and iscurrentprice = 1 and spaceid in (select spaceid from space where spacetype = 'Parking' AND Car = 1 AND RV = 1)) A) as un on st.tomap1 = un.tomap2";
		} else if(unitName.toLowerCase().contains("all cc")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(*) as totalunits from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and storemodflag != 3) as st join (select 1 as tomap2, count(distinct storeid) as noofunits from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid in (select id from DefaultUnitSizes where IsDefault=0 and CC=1))) as un on st.tomap1 = un.tomap2";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(*) as totalunits from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and storemodflag != 3) as st join (select 1 as tomap2, count(distinct storeid) as noofunits from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid=18 or (defaultunitid=17 and RV=1))) as un on st.tomap1 = un.tomap2";
		} else {
			int unitId = getUnitID(unitName);
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(*) as totalunits from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and storemodflag != 3) as st join (select 1 as tomap2, count(distinct storeid) as noofunits from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid = "+unitId+")) as un on st.tomap1 = un.tomap2";
		}
		return query;
	}
}














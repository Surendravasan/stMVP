package pageUtilities;

import java.util.Date;
import java.util.HashMap;

public class _ukQueries implements Queries {

	public int getUnitID(String unitName) {
		HashMap<String, String> unitIdName;
		String query = null;
//		query = "select LOWER(UnitType), ID from DefaultUnitSizes where countryid like '7,8,9,10'";
		query = "select Replace(Replace(LOWER(UnitType), ' ', ''), '.', ''), ID from DefaultUnitSizes where countryid like '7,8,9,10'";
		unitIdName = _databaseUtils.getMapString(query);
		return Integer.valueOf(unitIdName.get(unitName.toLowerCase().replace(" ", "").replace(".", "")));
	}
	

	public Date maxDatePrice = _databaseUtils.getDate("select DatePrice from MaxDatePrice");
	
	public String getStateFullName() {
		return "select Name from LookupStates where id = '"+_testData.state+"'";
	}
	
	public String getCountryFullName() {
		return "select Name from LookupCountries where id = "+_testData.countryId;
	}
	
	
	
	/* Compare Markets */
	
	public String getMarketType(int userStoreId) {
		String query = "select MarketChoice from MarketViewUserStores where userstoreid = "+userStoreId;
		return query;
	}
	
	public String getRadiusMarketDet(int userStoreId) {
//		String query = "select UserStoreId, MarketChoice, LEFT(ZoneCoverage, CHARINDEX(' ', ZoneCoverage)-1) as zone from MarketViewUserStores where userstoreid="+userStoreId;
		String query = "select UserStoreId, MarketChoice, City, State, LEFT(ZoneCoverage, CHARINDEX(' ', ZoneCoverage)-1) as zone from MarketViewUserStores where userstoreid="+userStoreId;
		return query;
	}
	
	public String getCityMarketDet(int userStoreId) {
//		String query = "Select UserStoreId, MarketChoice, Substring(City, 1,Charindex(',', City)-1) as City, Substring(City, Charindex(',', City)+1, LEN(City)) as State from MarketViewUserStores where userstoreid = "+userStoreId;
		String query = "Select UserStoreId, MarketChoice, City, State, ZoneCoverage from MarketViewUserStores where userstoreid="+userStoreId;
		return query;
	}
	
	public String getMarketType4(int userStoreId) {
		String query = "select UserStoreId, MarketChoice, LEFT(ZoneCoverage, CHARINDEX(',', ZoneCoverage)-1) as City, State, ZoneCoverage from MarketViewUserStores where userstoreid="+userStoreId;
		return query;
	}
	
	
	
	
	/* Queries - Market Overview */
	
	public String overTotalRentSqFo() {
		String query = "";
		query = "select sum(cast (RentableSqFt as int)) as TotalSqFt from stores with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet with (nolock) where userstoreid = "+_testData.userStoreId+")";
		return query;
	}
	
	public String overSqCapita() {
		String query = "";
		if(_testData.marketTypeId==1) {
			query = "select round(sum(cast(b.RentableSqFt as int))/c.TotalPopulation,2) as 'TOTAL RENTABLE SQ FT/CAPITA' from MarketViewUserStoreCompSet a join stores b on a.StoreId=b.StoreID join RadiusWiseCensusDataUK c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+_testData.userStoreId+" and c.ZoneCoverage="+_testData.radius+" group by TotalPopulation";
		} else if(_testData.marketTypeId==3){
			String countryName = _databaseUtils.getStringValue(getCountryFullName());
			query = "Select Round((RentableSqFt/population),2) as 'TOTAL RENTABLE SQ FT/CAPITA' from (SELECT 1 as tomap1, sum(COALESCE(s.RentableSqFt,0)) as RentableSqFt FROM Stores s join CityWiseCensusDataUK cuk on s.City=cuk.City where s.City='"+_testData.city+"' and s.RegionId=3 and s.CountryId="+_testData.countryId+" and s.StoreModFlag!=3) as ren join (Select 1 as tomap2, sum(TotalPopulation) as population from CityWiseCensusDataUK where  City ='"+_testData.city+"' and Country='"+countryName+"') as pop on ren.tomap1 = pop.tomap2";
		}
		return query;
	}
	
	public String overNoOfStores() {
		String query = "";
		query = "select count(*) as NoOfStores from stores with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet with (nolock) where userstoreid = "+_testData.userStoreId+")";
		return query;
	}

	
	public String overAvgRateSqFt() {
		String query = "select round(AVG(value),2) as OVERALLMARKETAVERAGE from (SELECT sp.onlineprice/nullif(((CASE WHEN ABS(S.width)=0 THEN 1 ELSE ABS(S.width) END)*(CASE WHEN ABS(S.length)=0 THEN 1 ELSE ABS(S.length) END)),0) as Value from SpacePrice sp WITH (NOLOCK) join stores st with (nolock) on sp.StoreID=st.StoreId Join MarketViewUserStoreCompSet mv with (nolock) on mv.storeid=St.StoreId and mv.userstoreid="+_testData.userStoreId+" join space s with (nolock) on s.SpaceID=sp.SpaceID JOIN DefaultUnitSizes d with (nolock) ON  d.id=s.DefaultUnitId and  d.CountryId='7,8,9,10' and  d.IsDefault=0 where st.StoreModFlag!=3 and st.RegionId=3 and st.countryid in (7,8,9,10) and sp.onlineprice!=0 and sp.IsCurrentPrice=1) as Avg";
		return query;
	}
	
	
	/* All Stores List  - Overview Header*/
	
//	public String overAllStoNoOfStores() {
//		String query = "select count(*) from stores (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet (nolock) where userstoreid = "+_testData.userStoreId;
//		return query;
//	}
	
	
	public String overNoOfReits() {
		return null;
	}
	
	public String overNoOfMOps() {
		String query = "select count(*) from stores (nolock) where multioperator=1 and storeid in (select storeid from MarketViewUserStoreCompSet (nolock) where userstoreid = "+_testData.userStoreId+")";
		return query;
	}
	
	public String overNoOfSOps() {
		String query = "select count(*) from stores (nolock) where singleoperator=1 and storeid in (select storeid from MarketViewUserStoreCompSet (nolock) where userstoreid = "+_testData.userStoreId+")";
		return query;
	}
	
	
	/* All Stores List  - Grid */
	
	public String storesCount() {
		String query = "select count(*) as stores from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+")";
		return query;
	}
	
	public String gridHeaderValue(String address) {
		String query = "select StoreName, squarefootage, RentableSqFt, ownedby, operatedby, yearbuild, StoreID from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and (address+', '+city+', '+state+' '+ZipCode) = '"+address+"'";
		return query;
	}
	
	
	public String gridUnitValue(String unitName, int storeId) {
		String query = "";
		if(!unitName.toLowerCase().contains("rv")) {
//			String unitName1 = unitName.replace("Sq.ft", " Sq.ft"); 
			query = "select top 1 sp.dateprice, spaceprice.IsCurrentPrice from SpacePriceHistory SP with (nolock) JOIN Space S with (nolock) ON S.SpaceID=SP.SpaceID JOIN DefaultUnitSizes d with (nolock) ON d.id=S.DefaultUnitId and s.defaultunitid="+getUnitID(unitName)+" JOIN stores with (nolock) on sp.StoreID = stores.StoreID and stores.storeid = "+storeId+" join space with (nolock) on sp.SpaceID = space.SpaceID join DefaultUnitSizes de with (nolock) on space.DefaultUnitId = de.ID left join SpacePrice with (nolock) on sp.SpaceID = spaceprice.SpaceID where de.countryid in ('7,8,9,10') and sp.onlineprice>1 and sp.onlineprice<1000 order by 1 desc, 2 desc";
		} else {
			query = "select top 1 sp.dateprice, spaceprice.IsCurrentPrice from SpacePriceHistory SP with (nolock) JOIN Space S with (nolock) ON S.SpaceID=SP.SpaceID JOIN DefaultUnitSizes d with (nolock) ON d.id=S.DefaultUnitId and s.defaultunitid=18 JOIN stores with (nolock) on sp.StoreID = stores.StoreID and stores.storeid = "+storeId+" join space with (nolock) on sp.SpaceID = space.SpaceID join DefaultUnitSizes de with (nolock) on space.DefaultUnitId = de.ID left join SpacePrice with (nolock) on sp.SpaceID = spaceprice.SpaceID where de.countryid in ('7,8,9,10') and sp.onlineprice>1 and sp.onlineprice<1000 order by 1 desc, 2 desc";
		}	
		return query;
	}
	
	
	/* Executive Summary - No of Stores */
	
	public String execSummNoOfStores(int i) {
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
	
	public String execSumThisMarket() {
		String query = "select COUNT(*) as Storesinmarket from stores  with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet with (nolock) where userstoreid = "+_testData.userStoreId+") and storemodflag!= 3";
		return query;
	}
	
	public String execSumNational() {
		String query = "select COUNT(*) as Storesinnation from stores with (nolock) where storemodflag != 3 and CountryId=2 and State not in (select id from LookupStates with (nolock) where CountryId = 2 and IsUnionTerritories = 1)";
		return query;
	}
	
	public String execSumState() {
		String query = "select COUNT(*) as Storesinstate from stores with (nolock) where storemodflag != 3 and CountryId=2 and State='"+_testData.state+"'";
		return query;
	}
	
	
	/* Executive Summary - New Developments */
	
	public String execSumTotalDevs() {
		String query = "select count(*) from StoresKnownDevelopement where RegionId="+_testData.regId;
		return query;
	}
	
	public String execSumMarketDevs() {
		String query = "select count(*) from StoresKnownDevelopement where city = '"+_testData.city+"' and state = '"+_testData.state+"'";
		return query;
	}
	
	
	
	
	/* Known Developments */
	
	public String knowDevNoOfDevs() {
		String query = "select count(*) as noOfdev from StoresKnownDevelopement where state='"+_testData.state+"' and City='"+_testData.city+"'";
		return query;
	}
	
	
	public String knownDevGridVal(String address) {
		String query = "select STAGE, PropertyType as 'PROJECT TYPE', StoreName as 'STORE NAME', OwnerName as 'OWNER NAME', OperatorManagementCompanyName as 'OPERATOR/MANAGEMENT', OperatorType as 'OPERATOR TYPE', (StreetAddress+', '+City+', '+State+' '+ZipCode) as ADDRESS, propertyAcres as ACRES, propertyBuildingSquareFootage as 'BUILDING SQUARE FOOTAGE', propertyFloors as FLOORS, propertyNumberOfUnits as 'NUMBER OF UNITS', propertyNumberOfBuildings as 'NUMBER OF BUILDINGS', propertyEstimatedValue as 'ESTIMATED VALUE', FORMAT((CAST(propertyExpectedToOpen as date)),'dd-MMM-yyyy') as 'EXPECTED OPEN DATE', propertyAdditionalPropertyInfo as 'ADDITIONAL PROPERTY INFO', SourceUrl as 'SOURCE URL', NOTES, FORMAT(ModifiedDate, 'dd-MMM-yyyy') as 'LAST CHECKED DATE' from StoresKnownDevelopement where state='"+_testData.state+"' and City='"+_testData.city+"' and (StreetAddress+', '+City+', '+State+' '+ZipCode) = '"+address+"'";
		return query;
	}
	
	
	
	
	
	
	/* Demographics - Population */
		
	public String popMarket() {
		String query = "";
		if(_testData.marketTypeId==1) {
			query = "select c.TotalPopulation as 'POPULATION SERVED', round((cast(c.TotalPopulation as int))/(c.LandArea),2) as 'POPULATION DENSITY (PER SQ MI)', round((cast(c.TotalPopulation as float))/COUNT(distinct a.storeid),0) as 'POPULATION/STORE' from MarketViewUserStoreCompSet a join RadiusWiseCensusDataUK c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+_testData.userStoreId+" and c.ZoneCoverage="+_testData.radius+" group by TotalPopulation, LandArea";
		} else if(_testData.marketTypeId==3){
			query = "select populationserved as 'POPULATION SERVED', populationdensity as 'POPULATION DENSITY (PER SQ MI)', round(populationserved/storecount,0) as 'POPULATION/STORE' from (SELECT 1 as tomap1, SUM(TotalPopulation) as populationserved, round(sum((TotalPopulation))/sum(LandArea),2) as populationdensity FROM CityWiseCensusDataUK cwc with (nolock) where City='"+_testData.city+"') as serden join (select 1 as tomap2, count(*) as storecount from MarketViewUserStoreCompSet with (nolock) where UserStoreId = "+_testData.userStoreId+") as sto on serden.tomap1=sto.tomap2";
		}	
		return query;
	}
	
	public String popNational() {
		String query = "";
		query = "select populationserved as 'POPULATION SERVED', populationdensity as 'POPULATION DENSITY (PER SQ MI)', ROUND(populationserved/Storesinnation,0) as 'POPULATION/STORE' from (SELECT 1 as tomap1, SUM(zwc.TotalPopulation) as populationserved FROM NationalWiseCensusDataUK zwc WITH(NOLOCK)) as ser join (SELECT 1 as tomap2, round(sum((zwc.TotalPopulation))/sum(zwc.LandArea),2) as populationdensity FROM NationalWiseCensusDataUK zwc  WITH(NOLOCK)) as den on ser.tomap1 = den.tomap2 join (select 1 as tomap3, COUNT(*) as Storesinnation from stores where storemodflag != 3 and CountryId in (7,8,9,10) and State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) as sto on den.tomap2 = sto.tomap3";
		return query;
	}
	
	public String popState() {
		return null;
	}
	
	
	/* Demographics - Household */
	
	public String houHolMarket() {
		String query = "";
		if(_testData.marketTypeId==1) {
			query = "select c.TotalHouseHolds as HOUSEHOLDS, round((cast(c.TotalHouseHolds as int))/(c.LandArea),2) as 'HOUSEHOLD DENSITY (PER SQ MI)', round((cast(c.TotalHouseHolds as float))/COUNT(distinct a.storeid),0) as 'HOUSEHOLDS/STORE' from MarketViewUserStoreCompSet a join RadiusWiseCensusDataUK c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+_testData.userStoreId+" and c.ZoneCoverage="+_testData.radius+" group by TotalHouseHolds, LandArea";
		} else if(_testData.marketTypeId==3){
			query = "select households as HOUSEHOLDS, populationdensity as 'HOUSEHOLD DENSITY (PER SQ MI)', round(households/storecount,0) as 'HOUSEHOLDS/STORE' from (SELECT 1 as tomap1, SUM(TotalHouseHolds) as households, round(sum((TotalHouseHolds))/sum(LandArea),2) as populationdensity FROM CityWiseCensusDataUK cwc with (nolock) where City='"+_testData.city+"') as serden join (select 1 as tomap2, count(*) as storecount from MarketViewUserStoreCompSet with (nolock) where UserStoreId = "+_testData.userStoreId+") as sto on serden.tomap1=sto.tomap2";
		}	
		return query;
	}
	
	public String houHolNational() {
		String query = "";
		query = "select households as HOUSEHOLDS, hhdensity as 'HOUSEHOLD DENSITY (PER SQ MI)', ROUND(households/Storesinnation,0) as 'HOUSEHOLDS/STORE' from (SELECT 1 as tomap1, SUM(zwc.TotalHouseholds) as households FROM NationalWiseCensusDataUK zwc WITH(NOLOCK)) as ser join (SELECT 1 as tomap2, round(sum((zwc.TotalHouseholds))/sum(zwc.LandArea),2) as hhdensity FROM NationalWiseCensusDataUK zwc  WITH(NOLOCK)) as den on ser.tomap1 = den.tomap2 join (select 1 as tomap3, COUNT(*) as Storesinnation from stores where storemodflag != 3 and CountryId in (7,8,9,10) and State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) as sto on den.tomap2 = sto.tomap3";
		return query;
	}
	
	public String houHolState() {
		return null;
	}
	
	/* Demographics - Rental Properties */
	
	public String renPropMarket() {
		String query = "";
		if(_testData.marketTypeId==1) {
			query = "select c.TotalRenterOccupied as 'RENTAL PROPERTIES', round((cast(c.TotalRenterOccupied as int))/(c.LandArea),2) as 'RENTAL PROPERTIES DENSITY (PER SQ MI)', round((cast(c.TotalRenterOccupied as float))/COUNT(distinct a.storeid),0) as 'RENTAL PROPERTIES/STORE' from MarketViewUserStoreCompSet a join RadiusWiseCensusDataUK c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+_testData.userStoreId+" and c.ZoneCoverage="+_testData.radius+" group by TotalRenterOccupied, LandArea";
		} else if(_testData.marketTypeId==3){
			query = "select rentalproperties as 'RENTAL PROPERTIES', populationdensity as 'RENTAL PROPERTIES DENSITY (PER SQ MI)', round(rentalproperties/storecount,0) as 'RENTAL PROPERTIES/STORE' from (SELECT 1 as tomap1, SUM(TotalRenterOccupied) as rentalproperties, round(sum((TotalRenterOccupied))/sum(LandArea),2) as populationdensity FROM CityWiseCensusDataUK cwc with (nolock) where City='"+_testData.city+"') as serden join (select 1 as tomap2, count(*) as storecount from MarketViewUserStoreCompSet with (nolock) where UserStoreId = "+_testData.userStoreId+") as sto on serden.tomap1=sto.tomap2";
		}	
		return query;
	}
	
	public String renPropNational() {
		String query = "";
		query = "select rentalproperties as 'RENTAL PROPERTIES', rendensity as 'RENTAL PROPERTIES DENSITY (PER SQ MI)', ROUND(rentalproperties/Storesinnation,0) as 'RENTAL PROPERTIES/STORE' from (SELECT 1 as tomap1, SUM(zwc.TotalRenterOccupied) as rentalproperties FROM NationalWiseCensusDataUK zwc WITH(NOLOCK)) as ser join (SELECT 1 as tomap2, round(sum((zwc.TotalRenterOccupied))/sum(zwc.LandArea),2) as rendensity FROM NationalWiseCensusDataUK zwc  WITH(NOLOCK)) as den on ser.tomap1 = den.tomap2 join (select 1 as tomap3, COUNT(*) as Storesinnation from stores where storemodflag != 3 and CountryId in (7,8,9,10) and State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) as sto on den.tomap2 = sto.tomap3";
		return query;
	}
	
	public String renPropState() {
		return null;
	}
	
	
	/* Compare Markets */
	
	public String popGreenBlue(HashMap<String, String> mapDet) {
		String query = "";
		if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
			query = "select c.TotalPopulation as 'POPULATION SERVED', round((cast(c.TotalPopulation as int))/(c.LandArea),2) as 'POPULATION DENSITY (PER SQ MI)', round((cast(c.TotalPopulation as float))/COUNT(distinct a.storeid),0) as 'POPULATION/STORE' from MarketViewUserStoreCompSet a join RadiusWiseCensusDataUK c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+mapDet.get("UserStoreId")+" and c.ZoneCoverage="+mapDet.get("zone")+" group by TotalPopulation, LandArea";
		} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
			query = "select populationserved as 'POPULATION SERVED', populationdensity as 'POPULATION DENSITY (PER SQ MI)', round(populationserved/storecount,0) as 'POPULATION/STORE' from (SELECT 1 as tomap1, SUM(TotalPopulation) as populationserved, round(sum((TotalPopulation))/sum(LandArea),2) as populationdensity FROM CityWiseCensusDataUK cwc with (nolock) where City='"+mapDet.get("City")+"') as serden join (select 1 as tomap2, count(*) as storecount from MarketViewUserStoreCompSet with (nolock) where UserStoreId = "+mapDet.get("UserStoreId")+") as sto on serden.tomap1=sto.tomap2";
		}	
		return query;
	}
	
	
	public String houHolGreenBlue(HashMap<String, String> mapDet) {
		String query = "";
		if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
			query = "select c.TotalHouseHolds as HOUSEHOLDS, round((cast(c.TotalHouseHolds as int))/(c.LandArea),2) as 'HOUSEHOLD DENSITY (PER SQ MI)', round((cast(c.TotalHouseHolds as float))/COUNT(distinct a.storeid),0) as 'HOUSEHOLDS/STORE' from MarketViewUserStoreCompSet a join RadiusWiseCensusDataUK c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+mapDet.get("UserStoreId")+" and c.ZoneCoverage="+mapDet.get("zone")+" group by TotalHouseHolds, LandArea";
		} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
			query = "select households as HOUSEHOLDS, populationdensity as 'HOUSEHOLD DENSITY (PER SQ MI)', round(households/storecount,0) as 'HOUSEHOLDS/STORE' from (SELECT 1 as tomap1, SUM(TotalHouseHolds) as households, round(sum((TotalHouseHolds))/sum(LandArea),2) as populationdensity FROM CityWiseCensusDataUK cwc with (nolock) where City='"+mapDet.get("City")+"') as serden join (select 1 as tomap2, count(*) as storecount from MarketViewUserStoreCompSet with (nolock) where UserStoreId = "+mapDet.get("UserStoreId")+") as sto on serden.tomap1=sto.tomap2";
		}	
		return query;
	}
	
	public String rentalGreenBlue(HashMap<String, String> mapDet) {
		String query = "";
		if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
			query = "select c.TotalRenterOccupied as 'RENTAL PROPERTIES', round((cast(c.TotalRenterOccupied as int))/(c.LandArea),2) as 'RENTAL PROPERTIES DENSITY (PER SQ MI)', round((cast(c.TotalRenterOccupied as float))/COUNT(distinct a.storeid),0) as 'RENTAL PROPERTIES/STORE' from MarketViewUserStoreCompSet a join RadiusWiseCensusDataUK c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+mapDet.get("UserStoreId")+" and c.ZoneCoverage="+mapDet.get("zone")+" group by TotalRenterOccupied, LandArea";
		} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
			query = "select rentalproperties as 'RENTAL PROPERTIES', populationdensity as 'RENTAL PROPERTIES DENSITY (PER SQ MI)', round(rentalproperties/storecount,0) as 'RENTAL PROPERTIES/STORE' from (SELECT 1 as tomap1, SUM(TotalRenterOccupied) as rentalproperties, round(sum((TotalRenterOccupied))/sum(LandArea),2) as populationdensity FROM CityWiseCensusDataUK cwc with (nolock) where City='"+mapDet.get("City")+"') as serden join (select 1 as tomap2, count(*) as storecount from MarketViewUserStoreCompSet with (nolock) where UserStoreId = "+mapDet.get("UserStoreId")+") as sto on serden.tomap1=sto.tomap2";
		}	
		return query;
	}
	
	
	
	
	
	
	
	/* Inventory Availability - Current Inventory By Unit Type */
	
	public String currInvUnitValue(String unitName) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0))) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0)))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0))) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0)))) ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0)))) as tot on ofm.tomap2 = tot.tomap3";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=78 or (defaultunitid=77 and RV=1))) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where defaultunitid=78 or (defaultunitid=77 and RV=1)))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=78 or (defaultunitid=77 and RV=1))) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where defaultunitid=78 or (defaultunitid=77 and RV=1)))) as ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=78 or (defaultunitid=77 and RV=1)))) as tot on ofm.tomap2 = tot.tomap3";
		} else {
			int unitId = getUnitID(unitName);
			query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId = "+unitId+")) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId = "+unitId+"))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId = "+unitId+")) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId = "+unitId+"))) ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId = "+unitId+"))) as tot on ofm.tomap2 = tot.tomap3";
		}
		return query;
	}
	
	public String currInvNational(String unitName) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "select onmarket, (total-onmarket) as offmarket, total from (SELECT 1 as tomap1, COUNT(DISTINCT S.MasterId) as total FROM SpacePriceHistory SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.RegionId = 3 AND DU.IsDefault = 0 WHERE S.CountryId in (7,8,9,10) and s.State not in (select id from LookupStates with (nolock) where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) as tot join (SELECT 1 as tomap2, COUNT(DISTINCT S.MasterId) as onmarket FROM Spaceprice SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.RegionId = 3 WHERE SP.IsCurrentPrice=1 AND DU.IsDefault = 0 and S.CountryId in (7,8,9,10) and s.State not in (select id from LookupStates with (nolock) where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) as onm on tot.tomap1 = onm.tomap2";
		} else if(unitName.toLowerCase().contains("rv park")) {
		    query = "select onmarket, (total-onmarket) as offmarket, total from (SELECT 1 as tomap1, COUNT(distinct masterid) as total from (SELECT DISTINCT SPC.DefaultUnitId, S.MasterId FROM SpacePriceHistory SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.IsDefault = 0 AND DU.RegionId = 3 WHERE (spc.DefaultUnitId=78 or (spc.DefaultUnitId=77 and spc.RV=1)) and S.CountryId in (7,8,9,10) and s.RegionId=3) as t) as tot join (SELECT 1 as tomap2, COUNT(distinct masterid) as onmarket from (SELECT DISTINCT SPC.DefaultUnitId, S.MasterId FROM Spaceprice SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.IsDefault = 0 AND DU.RegionId = 3 WHERE SP.IsCurrentPrice=1 and (spc.DefaultUnitId=78 or (spc.DefaultUnitId=77 and spc.RV=1)) and S.CountryId in (7,8,9,10) and s.RegionId=3) as onmarket) as onm on tot.tomap1 = onm.tomap2";
		} else {
			int unitId = getUnitID(unitName);
			query = "select onmarket, (total-onmarket) as offmarket, total from (SELECT 1 as tomap1, COUNT(*) as total from (SELECT DISTINCT S.State, SPC.DefaultUnitId, S.MasterId FROM SpacePriceHistory SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.IsDefault = 0 AND DU.RegionId = 3 WHERE DU.id="+unitId+" and S.CountryId IN (SELECT Id FROM LookupCountries WHERE regionid = 3) and s.State not in (select id from LookupStates with (nolock) where CountryId in (7,8,9,10) and IsUnionTerritories = 1))as t) as tot join (SELECT 1 as tomap2, COUNT(*) as onmarket from (SELECT DISTINCT S.State, SPC.DefaultUnitId, S.MasterId FROM Spaceprice SP (NOLOCK) JOIN Stores S (NOLOCK) ON SP.StoreID = S.StoreID JOIN Space SPC (NOLOCK) ON SP.SpaceID = SPC.SpaceID JOIN DefaultUnitSizes DU (NOLOCK) ON SPC.DefaultUnitId = DU.ID AND DU.IsDefault = 0 AND DU.RegionId = 3 WHERE SP.IsCurrentPrice=1 and DU.id="+unitId+" and S.CountryId IN (SELECT Id FROM LookupCountries WHERE regionid = 3) and s.State not in (select id from LookupStates with (nolock) where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) as onmarket) as onm on tot.tomap1 = onm.tomap2";
		}
		return query;
	}
	
	public String currInvState(String unitName) {
		return null;
	}
	
	/* Compare */
	
	public String currInvGreenBlueVal(String unitName, HashMap<String, String> mapDet) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0))) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0)))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0))) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0)))) ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId in (select id from DefaultUnitSizes where IsDefault=0)))) as tot on ofm.tomap2 = tot.tomap3";
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=78 or (defaultunitid=77 and RV=1))) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where defaultunitid=78 or (defaultunitid=77 and RV=1)))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=78 or (defaultunitid=77 and RV=1))) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where defaultunitid=78 or (defaultunitid=77 and RV=1)))) as ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space where defaultunitid=78 or (defaultunitid=77 and RV=1)))) as tot on ofm.tomap2 = tot.tomap3";
		} else {
			int unitId = getUnitID(unitName);
			query = "select onmarket, offmarket, total from (select 1 as tomap1, count(distinct StoreId) as onmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId = "+unitId+")) and StoreID in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId = "+unitId+"))) as onm join (select 1 as tomap2, count(distinct StoreId) as offmarket from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from SpacePriceHistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId = "+unitId+")) and StoreID not in (select storeid from SpacePrice with (nolock) Where IscurrentPrice=1 and SpaceId in (Select SpaceID from space with (nolock) where DefaultUnitId = "+unitId+"))) ofm on onm.tomap1 = ofm.tomap2 join (select 1 as tomap3, count(distinct StoreId) as total from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+" and storeid in (select storeid from spacepricehistory with (nolock) where spaceid in (select spaceid from space with (nolock) where DefaultUnitId = "+unitId+"))) as tot on ofm.tomap2 = tot.tomap3";
		}
		return query;
	}
	

	
	
	
	
	/* Inventory Availability - Inventory By Unit Type History */
	
	public String thisMarketInventoryHistory(String unitName, String datePrice) {
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
	

	public String nationalInventoryHistory(String unitName, String datePrice) {
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
	
	public String stateInventoryHistory(String unitName, String datePrice) {
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
	
	public String greenBlueInventoryHistory(String unitName, String datePrice, HashMap<String, String> mapDet) {
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
	
	public String houholdIncMarket() {
		String query = "";
		if(_testData.marketTypeId==1) {
			query = "select MedianHouseholdIncome as 'MEDIAN HOUSEHOLD INCOME', AggregateHouseholdIncome as 'AGGREGATE HOUSEHOLD INCOME', (AggregateHouseholdIncome/storescount) as 'HOUSEHOLD INCOME/STORE' from  (select 1 as tomap1,  MedianHouseholdIncome, (MedianHouseholdIncome*TotalHouseHolds) as AggregateHouseholdIncome from RadiusWiseCensusDataUK where UserStoreID="+_testData.userStoreId+" and ZoneCoverage="+_testData.radius+") as agg join (select 1 as tomap2, count(*) as storescount from MarketViewUserStoreCompSet where UserStoreID="+_testData.userStoreId+") as sto on agg.tomap1 = sto.tomap2";
		} else if(_testData.marketTypeId==3){
			query = "Select MedianHouseholdIncome as 'MEDIAN HOUSEHOLD INCOME', AggregateHouseholdIncome as 'AGGREGATE HOUSEHOLD INCOME', (AggregateHouseholdIncome/storescount) as 'HOUSEHOLD INCOME/STORE' from (SELECT 1 as tomap1, ROUND(avg(MedianHouseholdIncome),0) as MedianHouseholdIncome, sum(MedianHouseholdIncome*TotalHouseholds) as AggregateHouseholdIncome FROM  citywisecensusdatauk with (nolock) where City='"+_testData.city+"') as med join (select 1 as tomap2, COUNT(*) as storescount from MarketViewUserStoreCompSet where UserStoreId="+_testData.userStoreId+") as sto on med.tomap1=sto.tomap2";
		}
		return query;
	}
	
	public String houholIncNational() {
		String query = "select MedianHouseholdIncome as 'MEDIAN HOUSEHOLD INCOME', AggregateHouseholdIncome  as 'AGGREGATE HOUSEHOLD INCOME', (AggregateHouseholdIncome/storescount) as 'HOUSEHOLD INCOME/STORE' from (SELECT 1 as tomap1, ROUND(avg(zwc.MedianHouseholdIncome),0) as MedianHouseholdIncome, sum(zwc.MedianHouseholdIncome * zwc.TotalHouseholds) as AggregateHouseholdIncome FROM NationalWiseCensusDataUK zwc  WITH(NOLOCK)) as agg join (Select 1 as tomap2, count(*) as storescount from Stores where countryid in (7,8,9,10) and storemodflag!=3 and State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) as sto on agg.tomap1 = sto.tomap2";
		return query;
	}
	
	public String houholIncState() {
		return null;
	}
	
	
	/* Market Spending Power - Average Property Value */
	
	public String avgPropMarket() {
		String query = "";
		if(_testData.marketTypeId==1) {
			query = "select ROUND(avg(MedianPropertyValue),0) as 'AVERAGE PROPERTY VALUE' from RadiusWiseCensusDataUK where UserStoreID="+_testData.userStoreId+" and ZoneCoverage="+_testData.radius+"";
		} else if(_testData.marketTypeId==3){
			query = "SELECT ROUND(avg(MedianPropertyValue),0) as 'AVERAGE PROPERTY VALUE' FROM citywisecensusdatauk with (nolock) where City='"+_testData.city+"'";
		}
		return query;
	}
	
	public String avgPropNational() {
		String query = "SELECT ROUND(avg(zwc.MedianPropertyValue),0) as 'AVERAGE PROPERTY VALUE' FROM NationalWiseCensusDataUK zwc WITH(NOLOCK)";
		return query;
	}
	
	public String avgPropState() {
		return null;
	}
	
	
	/* Market Spending Power - Average Rental Costs */
	
	public String rentalPropMarket() {
		String query = "";
		if(_testData.marketTypeId==1) {
			query = "select ROUND(avg(AverageRent),0) as 'AVERAGE RENTAL COSTS' from RadiusWiseCensusDataUK where UserStoreID="+_testData.userStoreId+" and ZoneCoverage="+_testData.radius+"";
		} else if(_testData.marketTypeId==3){
			query = "SELECT ROUND(avg(AverageRent),0) as 'AVERAGE RENTAL COSTS' FROM  citywisecensusdatauk with (nolock) where City='"+_testData.city+"'";
		}
		return query;
	}
	
	public String avgRentNational() {
		String query = "SELECT ROUND(avg(zwc.AverageRent),0) as 'AVERAGE RENTAL COSTS' FROM NationalWiseCensusDataUK zwc WITH (NOLOCK)";
		return query;
	}
	
	public String rentalPropState() {
		return null;
	}
	
	/* Compare */
	
	public String householdGreenBlue(HashMap<String, String> mapDet) {
		System.out.println(mapDet);
		String query = "";
		if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
			query = "select MedianHouseholdIncome as 'MEDIAN HOUSEHOLD INCOME', AggregateHouseholdIncome as 'AGGREGATE HOUSEHOLD INCOME', (AggregateHouseholdIncome/storescount) as 'HOUSEHOLD INCOME/STORE' from  (select 1 as tomap1,  MedianHouseholdIncome, (MedianHouseholdIncome*TotalHouseHolds) as AggregateHouseholdIncome from RadiusWiseCensusDataUK where UserStoreID="+mapDet.get("UserStoreId")+" and ZoneCoverage="+mapDet.get("zone")+") as agg join (select 1 as tomap2, count(*) as storescount from MarketViewUserStoreCompSet where UserStoreID="+mapDet.get("UserStoreId")+") as sto on agg.tomap1 = sto.tomap2";
		} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
			query = "Select MedianHouseholdIncome as 'MEDIAN HOUSEHOLD INCOME', AggregateHouseholdIncome as 'AGGREGATE HOUSEHOLD INCOME', (AggregateHouseholdIncome/storescount) as 'HOUSEHOLD INCOME/STORE' from (SELECT 1 as tomap1, ROUND(avg(MedianHouseholdIncome),0) as MedianHouseholdIncome, sum(MedianHouseholdIncome*TotalHouseholds) as AggregateHouseholdIncome FROM  citywisecensusdatauk with (nolock) where City='"+mapDet.get("City")+"') as med join (select 1 as tomap2, COUNT(*) as storescount from MarketViewUserStoreCompSet where UserStoreId="+mapDet.get("UserStoreId")+") as sto on med.tomap1=sto.tomap2";
		}
		return query;
	}
	
	
	public String avgPropGreenBlue(HashMap<String, String> mapDet) {
		String query = "";
		if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
			query = "select ROUND(avg(MedianPropertyValue),0) as 'AVERAGE PROPERTY VALUE' from RadiusWiseCensusDataUK where UserStoreID="+mapDet.get("UserStoreId")+" and ZoneCoverage="+mapDet.get("zone")+"";
		} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
			query = "SELECT ROUND(avg(MedianPropertyValue),0) as 'AVERAGE PROPERTY VALUE' FROM citywisecensusdatauk with (nolock) where City='"+mapDet.get("City")+"'";
		}
		return query;
	}
	
	public String avgRentGreenBlue(HashMap<String, String> mapDet) {
		String query = "";
		if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
			query = "select ROUND(avg(AverageRent),0) as 'AVERAGE RENTAL COSTS' from RadiusWiseCensusDataUK where UserStoreID="+mapDet.get("UserStoreId")+" and ZoneCoverage="+mapDet.get("zone")+"";
		} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
			query = "SELECT ROUND(avg(AverageRent),0) as 'AVERAGE RENTAL COSTS' FROM  citywisecensusdatauk with (nolock) where City='"+mapDet.get("City")+"'";
		}
		return query;
	}
	
	
	
	
	
	
	
	
	/* Market Supply Metrics - Capita */
	
	public String thisMarketCapita() {
		String query = "";
		if(_testData.marketTypeId==1) {
			query = "select sum(cast (b.RentableSqFt as int)) as 'TOTAL RENTABLE SQFT', c.TotalPopulation as 'POPULATION', round(sum(cast(b.RentableSqFt as int))/c.TotalPopulation,2) as 'TOTAL RENTABLE SQ FT/CAPITA' from MarketViewUserStoreCompSet a join stores b on a.StoreId=b.StoreID join RadiusWiseCensusDataUK c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+_testData.userStoreId+" and c.ZoneCoverage="+_testData.radius+" group by TotalPopulation";
		} else if(_testData.marketTypeId==3){
			String countryName = _databaseUtils.getStringValue(getCountryFullName());
			query = "Select RentableSqFt as 'TOTAL RENTABLE SQFT', population as 'POPULATION', Round((RentableSqFt/population),2) as 'TOTAL RENTABLE SQ FT/CAPITA' from (SELECT 1 as tomap1, sum(COALESCE(s.RentableSqFt,0)) as RentableSqFt FROM Stores s join CityWiseCensusDataUK cuk on s.City=cuk.City where s.City='"+_testData.city+"' and s.RegionId=3 and s.CountryId="+_testData.countryId+" and s.StoreModFlag!=3) as ren join (Select 1 as tomap2, sum(TotalPopulation) as population from CityWiseCensusDataUK where  City ='"+_testData.city+"' and Country='"+countryName+"') as pop on ren.tomap1 = pop.tomap2";
		}
		return query;
	}
	
	public String nationalCapita() {
		String query = "select RentableSqFt as 'TOTAL RENTABLE SQFT', population as 'POPULATION', Round((RentableSqFt/population),2) as 'TOTAL RENTABLE SQ FT/CAPITA' from (SELECT 1 as tomap1, sum(COALESCE(RentableSqFt,0)) as RentableSqFt from Stores where StoreModFlag!=3 and CountryId in (7,8,9,10) and State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) as ren join (Select 1 as tomap2, sum(TotalPopulation) as population FROM UKDemographyCountry zwc  WITH(NOLOCK)) as pop on ren.tomap1 = pop.tomap2";
		return query;
	}
	
	public String stateCapita() {
		return null;
	}
	
	
	/* Market Supply Metrics - Household */
	
	public String thisMarketHousehold() {
		String query = "";
		if(_testData.marketTypeId==1) {
			query = "select sum(cast(b.RentableSqFt as int)) as 'TOTAL RENTABLE SQFT', c.TotalHouseHolds as 'HOUSEHOLDS', round(sum(cast(b.RentableSqFt as int))/c.TotalHouseHolds,2) as 'TOTAL RENTABLE SQ FT/HOUSEHOLD' from MarketViewUserStoreCompSet a join stores b on a.StoreId=b.StoreID join RadiusWiseCensusDataUK c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+_testData.userStoreId+" and c.ZoneCoverage="+_testData.radius+" group by TotalHouseHolds";
		} else if(_testData.marketTypeId==3){
			String countryName = _databaseUtils.getStringValue(getCountryFullName());
			query = "Select RentableSqFt as 'TOTAL RENTABLE SQFT', households as 'HOUSEHOLDS', Round((RentableSqFt/households),2) as 'TOTAL RENTABLE SQ FT/HOUSEHOLD' from (SELECT 1 as tomap1, sum(COALESCE(s.RentableSqFt,0)) as RentableSqFt FROM Stores s join CityWiseCensusDataUK cuk on s.City=cuk.City where s.City='"+_testData.city+"' and s.RegionId=3 and s.CountryId="+_testData.countryId+" and s.StoreModFlag!=3) as ren join (Select 1 as tomap2, sum(TotalHouseholds) as households from [dbo].[UKDemographyCity] where  City ='"+_testData.city+"' and Country='"+countryName+"') as pop on ren.tomap1 = pop.tomap2";
		}
		return query;
	}
	
	public String nationalHousehold() {
		String query = "select RentableSqFt as 'TOTAL RENTABLE SQFT', households as 'HOUSEHOLDS', Round((RentableSqFt/households),2) as 'TOTAL RENTABLE SQ FT/HOUSEHOLD' from (SELECT 1 as tomap1, sum(COALESCE(RentableSqFt,0)) as RentableSqFt from Stores where StoreModFlag!=3 and CountryId in (7,8,9,10) and State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) as ren join (Select 1 as tomap2, sum(TotalHouseholds) as households FROM UKDemographyCountry zwc  WITH(NOLOCK)) as pop on ren.tomap1 = pop.tomap2";
		return query;
	}
	
	public String stateHousehold() {
		return null;
	}
	
	/* Market Supply Metrics - Rental Properties */
	
	public String thisMarketRentalProp() {
		String query = "";
		if(_testData.marketTypeId==1) {
			query = "select sum(cast (b.RentableSqFt as int)) as 'TOTAL RENTABLE SQFT', c.TotalRenterOccupied as 'RENTAL PROPERTIES', round(sum(cast(b.RentableSqFt as int))/c.TotalRenterOccupied,2) as 'TOTAL RENTABLE SQ FT/RENTAL PROPERTIES' from MarketViewUserStoreCompSet a join stores b on a.StoreId=b.StoreID join RadiusWiseCensusDataUK c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+_testData.userStoreId+" and c.ZoneCoverage="+_testData.radius+" group by TotalRenterOccupied";
		} else if(_testData.marketTypeId==3){
			String countryName = _databaseUtils.getStringValue(getCountryFullName());
			query = "Select RentableSqFt as 'TOTAL RENTABLE SQFT', rentalproperties as 'RENTAL PROPERTIES', Round((RentableSqFt/rentalproperties),2) as 'TOTAL RENTABLE SQ FT/RENTAL PROPERTIES' from (SELECT 1 as tomap1, sum(COALESCE(s.RentableSqFt,0)) as RentableSqFt FROM Stores s join CityWiseCensusDataUK cuk on s.City=cuk.City where s.City='"+_testData.city+"' and s.RegionId=3 and s.CountryId="+_testData.countryId+" and s.StoreModFlag!=3) as ren join (Select 1 as tomap2, SUM(RenterOccupied) as rentalproperties from [dbo].[UKDemographyCity] where  City ='"+_testData.city+"' and Country='"+countryName+"') as pop on ren.tomap1 = pop.tomap2";
		}
		return query;
	}
	
	public String nationalRentProp() {
		String query = "select RentableSqFt as 'TOTAL RENTABLE SQFT', rentalproperties as 'RENTAL PROPERTIES', Round((RentableSqFt/rentalproperties),2) as 'TOTAL RENTABLE SQ FT/RENTAL PROPERTIES' from (SELECT 1 as tomap1, sum(COALESCE(RentableSqFt,0)) as RentableSqFt from Stores where StoreModFlag!=3 and CountryId in (7,8,9,10) and State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) as ren join (Select 1 as tomap2, SUM(RentalOccupied) as rentalproperties FROM UKDemographyCountry zwc  WITH(NOLOCK)) as pop on ren.tomap1 = pop.tomap2";
		return query;
	}
	
	public String stateRentProp() {
		return null;
	}
	
	/* Compare */
	
	public String greenBlueCapita(HashMap<String, String> mapDet) {
		String query = "";
		if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
			query = "select sum(cast (b.RentableSqFt as int)) as 'TOTAL RENTABLE SQFT', c.TotalPopulation as 'POPULATION', round(sum(cast(b.RentableSqFt as int))/c.TotalPopulation,2) as 'TOTAL RENTABLE SQ FT/CAPITA' from MarketViewUserStoreCompSet a join stores b on a.StoreId=b.StoreID join RadiusWiseCensusDataUK c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+mapDet.get("UserStoreId")+" and c.ZoneCoverage="+mapDet.get("zone")+" group by TotalPopulation";
		} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
			String countryName = _databaseUtils.getStringValue(getCountryFullName());
			query = "Select RentableSqFt as 'TOTAL RENTABLE SQFT', population as 'POPULATION', Round((RentableSqFt/population),2) as 'TOTAL RENTABLE SQ FT/CAPITA' from (SELECT 1 as tomap1, sum(COALESCE(s.RentableSqFt,0)) as RentableSqFt FROM Stores s join CityWiseCensusDataUK cuk on s.City=cuk.City where s.City='"+mapDet.get("City")+"' and s.RegionId=3 and s.CountryId="+mapDet.get("CountryId")+" and s.StoreModFlag!=3) as ren join (Select 1 as tomap2, sum(TotalPopulation) as population from [dbo].[UKDemographyCity] where  City ='"+mapDet.get("City")+"' and Country='"+countryName+"') as pop on ren.tomap1 = pop.tomap2";
		}
		return query;
	}
	
	
	public String greenBlueHoushold(HashMap<String, String> mapDet) {
		String query = "";
		if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
			query = "select sum(cast (b.RentableSqFt as int)) as 'TOTAL RENTABLE SQFT', c.TotalHouseHolds as 'HOUSEHOLDS', round(sum(cast(b.RentableSqFt as int))/c.TotalHouseHolds,2) as 'TOTAL RENTABLE SQ FT/HOUSEHOLD' from MarketViewUserStoreCompSet a join stores b on a.StoreId=b.StoreID join RadiusWiseCensusData c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+mapDet.get("UserStoreId")+" and c.ZoneCoverage="+mapDet.get("zone")+" group by TotalHouseHolds";
		} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
				query = "select RentableSqFt as 'TOTAL RENTABLE SQFT', households as 'HOUSEHOLDS', Round((RentableSqFt/households),2) as 'TOTAL RENTABLE SQ FT/HOUSEHOLD' from (SELECT 1 as tomap1, sum(COALESCE(s.RentableSqFt,0)) as RentableSqFt FROM ZipcodeWiseCensusData zwc with (nolock) JOIN stores s ON s.zipcode=zwc.zipcode and s.state='"+mapDet.get("State")+"' and s.StoreModFlag!=3 and s.City='"+mapDet.get("City")+"' JOIN uscities c ON c.City=s.city and  c.State_id=s.State) as ren join (select 1 as tomap2, sum(TotalHouseholds) as households from ZipcodeWiseCensusData where Zipcode in (Select Zipcode from Zipcodes where City ='"+mapDet.get("City")+"' and abbr ='"+mapDet.get("State")+"')) as pop on ren.tomap1 = pop.tomap2";
		}
		return query;
	}
	
	public String greenBlueRentalProp(HashMap<String, String> mapDet) {
		String query = "";
		if(mapDet.get("MarketChoice").equalsIgnoreCase("1")) {
			query = "select sum(cast (b.RentableSqFt as int)) as 'TOTAL RENTABLE SQFT', c.TotalRenterOccupied as 'RENTAL PROPERTIES', round(sum(cast(b.RentableSqFt as int))/c.TotalRenterOccupied,2) as 'TOTAL RENTABLE SQ FT/RENTAL PROPERTIES' from MarketViewUserStoreCompSet a join stores b on a.StoreId=b.StoreID join RadiusWiseCensusData c on a.UserStoreId=c.UserStoreID where a.UserStoreId="+mapDet.get("UserStoreId")+" and c.ZoneCoverage="+mapDet.get("zone")+" group by TotalRenterOccupied";
		} else if(mapDet.get("MarketChoice").equalsIgnoreCase("3")){
			query = "select RentableSqFt as 'TOTAL RENTABLE SQFT', rentalproperties as 'RENTAL PROPERTIES', Round((RentableSqFt/rentalproperties),2) as 'TOTAL RENTABLE SQ FT/RENTAL PROPERTIES' from (SELECT 1 as tomap1, sum(COALESCE(s.RentableSqFt,0)) as RentableSqFt FROM ZipcodeWiseCensusData zwc with (nolock) JOIN stores s ON s.zipcode=zwc.zipcode and s.state='"+mapDet.get("State")+"' and s.StoreModFlag!=3 and s.City='"+mapDet.get("City")+"' JOIN uscities c ON c.City=s.city and  c.State_id=s.State) as ren join (select 1 as tomap2, SUM(TotalRenterOccupied) as rentalproperties from ZipcodeWiseCensusData where Zipcode in (Select Zipcode from Zipcodes where City ='"+mapDet.get("City")+"' and abbr ='"+mapDet.get("State")+"')) as pop on ren.tomap1 = pop.tomap2";
		}
		return query;
	}
	
	
	
	
	
	
	
	
	/* Pricing Rental - Rate Per Square Feet by Store Type */
	
	public String thisMarketRateByStoreType() {
		String query = "select OVERALLMARKETAVERAGE as 'OVERALL MARKET AVERAGE', MIDOPS as 'MID OPS', SMALLOPS as 'SMALL OPS' from (select 1 as tomap1, round(AVG(value),2) as OVERALLMARKETAVERAGE from (SELECT sp.onlineprice/ nullif(((CASE WHEN ABS(S.width)=0 THEN 1 ELSE ABS(S.width) END)*(CASE WHEN ABS(S.length)=0 THEN 1 ELSE ABS(S.length) END)),0) as Value from SpacePrice sp WITH (NOLOCK) join stores st with (nolock) on sp.StoreID=st.StoreId Join MarketViewUserStoreCompSet mv on mv.storeid=St.StoreId and mv.userstoreid="+_testData.userStoreId+" join space s with (nolock) on s.SpaceID=sp.SpaceID JOIN DefaultUnitSizes d ON  d.id=s.DefaultUnitId and  d.CountryId='7,8,9,10' and  d.IsDefault=0 where st.StoreModFlag!=3 and st.RegionId=3 and st.countryid in (7,8,9,10) and sp.onlineprice!=0 and sp.IsCurrentPrice=1) as Avg) as ma join (select 1 as tomap2, round(AVG(value),2) as MIDOPS from (SELECT sp.onlineprice/ nullif(((CASE WHEN ABS(S.width)=0 THEN 1 ELSE ABS(S.width) END)*(CASE WHEN ABS(S.length)=0 THEN 1 ELSE ABS(S.length) END)),0) as Value from SpacePrice sp WITH (NOLOCK) join stores st with (nolock) on sp.StoreID=st.StoreId and st.MultiOperator=1 Join MarketViewUserStoreCompSet mv on mv.storeid=St.StoreId and mv.userstoreid="+_testData.userStoreId+" join space s with (nolock) on s.SpaceID=sp.SpaceID JOIN DefaultUnitSizes d ON  d.id=s.DefaultUnitId and  d.CountryId='7,8,9,10' and  d.IsDefault=0 where st.StoreModFlag!=3 and st.RegionId=3 and st.countryid in (7,8,9,10) and sp.onlineprice!=0 and sp.IsCurrentPrice=1) as Avg) as mi on ma.tomap1 = mi.tomap2 join (select 1 as tomap3, round(AVG(value),2) as SMALLOPS from (SELECT sp.onlineprice/ nullif(((CASE WHEN ABS(S.width)=0 THEN 1 ELSE ABS(S.width) END)*(CASE WHEN ABS(S.length)=0 THEN 1 ELSE ABS(S.length) END)),0) as Value from SpacePrice sp WITH (NOLOCK) join stores st with (nolock) on sp.StoreID=st.StoreId and st.SingleOperator=1 Join MarketViewUserStoreCompSet mv on mv.storeid=St.StoreId and mv.userstoreid="+_testData.userStoreId+" join space s with (nolock) on s.SpaceID=sp.SpaceID JOIN DefaultUnitSizes d ON  d.id=s.DefaultUnitId and  d.CountryId='7,8,9,10' and  d.IsDefault=0 where st.StoreModFlag!=3 and st.RegionId=3 and st.countryid in (7,8,9,10) and sp.onlineprice!=0 and sp.IsCurrentPrice=1) as Avg) as sm on mi.tomap2 = sm.tomap3";
		return query;
	}
	
	public String nationalRateByStoreType() {
		String query = "select OVERALLMARKETAVERAGE as 'OVERALL MARKET AVERAGE', MIDOPS as 'MID OPS', SMALLOPS as 'SMALL OPS' from (select 1 as tomap1, round(AVG(value),2) as OVERALLMARKETAVERAGE from (SELECT (Z.onlineprice/ nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)) as Value, S1.StoreID from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID and S1.CountryId in (7,8,9,10) JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND   Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 and Z.onlineprice between 2 and 999 Join DefaultUnitSizes du on sa.defaultunitid = du.id and du.countryid ='7,8,9,10' and du.isdefault=0 WHERE  S1.StoreModFlag != 3 ) as Avg) as ma join (select 1 as tomap2, round(AVG(value),2) as MIDOPS from (SELECT (Z.onlineprice/ nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)) as Value, S1.StoreID from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID and S1.CountryId in (7,8,9,10) and S1.MultiOperator=1 JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND   Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 and Z.onlineprice between 2 and 999 Join DefaultUnitSizes du on sa.defaultunitid = du.id and du.countryid ='7,8,9,10' and du.isdefault=0 WHERE  S1.StoreModFlag != 3 ) as Avg) as mi on ma.tomap1 = mi.tomap2 join (select 1 as tomap3, round(AVG(value),2) as SMALLOPS from (SELECT (Z.onlineprice/ nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)) as Value, S1.StoreID from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID and S1.CountryId in (7,8,9,10) and S1.SingleOperator=1 JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND   Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 and Z.onlineprice between 2 and 999 Join DefaultUnitSizes du on sa.defaultunitid = du.id and du.countryid ='7,8,9,10' and du.isdefault=0 WHERE  S1.StoreModFlag != 3 ) as Avg) as sm on mi.tomap2 = sm.tomap3";
		return query;
	}
	
	public String stateRateByStoreType() {
		return null;
	}
	
	/* Compare */
	
	public String greenBlueRateByStoreType(HashMap<String, String> mapDet) {
		String query = "select OVERALLMARKETAVERAGE as 'OVERALL MARKET AVERAGE', MIDOPS as 'MID OPS', SMALLOPS as 'SMALL OPS' from (select 1 as tomap1, round(AVG(value),2) as OVERALLMARKETAVERAGE from (SELECT sp.onlineprice/ nullif(((CASE WHEN ABS(S.width)=0 THEN 1 ELSE ABS(S.width) END)*(CASE WHEN ABS(S.length)=0 THEN 1 ELSE ABS(S.length) END)),0) as Value from SpacePrice sp WITH (NOLOCK) join stores st with (nolock) on sp.StoreID=st.StoreId Join MarketViewUserStoreCompSet mv on mv.storeid=St.StoreId and mv.userstoreid="+mapDet.get("UserStoreId")+" join space s with (nolock) on s.SpaceID=sp.SpaceID JOIN DefaultUnitSizes d ON  d.id=s.DefaultUnitId and  d.CountryId='7,8,9,10' and  d.IsDefault=0 where st.StoreModFlag!=3 and st.RegionId=3 and st.countryid in (7,8,9,10) and sp.onlineprice!=0 and sp.IsCurrentPrice=1) as Avg) as ma join (select 1 as tomap2, round(AVG(value),2) as MIDOPS from (SELECT sp.onlineprice/ nullif(((CASE WHEN ABS(S.width)=0 THEN 1 ELSE ABS(S.width) END)*(CASE WHEN ABS(S.length)=0 THEN 1 ELSE ABS(S.length) END)),0) as Value from SpacePrice sp WITH (NOLOCK) join stores st with (nolock) on sp.StoreID=st.StoreId and st.MultiOperator=1 Join MarketViewUserStoreCompSet mv on mv.storeid=St.StoreId and mv.userstoreid="+mapDet.get("UserStoreId")+" join space s with (nolock) on s.SpaceID=sp.SpaceID JOIN DefaultUnitSizes d ON  d.id=s.DefaultUnitId and  d.CountryId='7,8,9,10' and  d.IsDefault=0 where st.StoreModFlag!=3 and st.RegionId=3 and st.countryid in (7,8,9,10) and sp.onlineprice!=0 and sp.IsCurrentPrice=1) as Avg) as mi on ma.tomap1 = mi.tomap2 join (select 1 as tomap3, round(AVG(value),2) as SMALLOPS from (SELECT sp.onlineprice/ nullif(((CASE WHEN ABS(S.width)=0 THEN 1 ELSE ABS(S.width) END)*(CASE WHEN ABS(S.length)=0 THEN 1 ELSE ABS(S.length) END)),0) as Value from SpacePrice sp WITH (NOLOCK) join stores st with (nolock) on sp.StoreID=st.StoreId and st.SingleOperator=1 Join MarketViewUserStoreCompSet mv on mv.storeid=St.StoreId and mv.userstoreid="+mapDet.get("UserStoreId")+" join space s with (nolock) on s.SpaceID=sp.SpaceID JOIN DefaultUnitSizes d ON  d.id=s.DefaultUnitId and  d.CountryId='7,8,9,10' and  d.IsDefault=0 where st.StoreModFlag!=3 and st.RegionId=3 and st.countryid in (7,8,9,10) and sp.onlineprice!=0 and sp.IsCurrentPrice=1) as Avg) as sm on mi.tomap2 = sm.tomap3";
		return query;
	}
	
	
	
	
	
	/* Pricing Rental - Rate Per Square Feet by Unit Type */
	
	public String thisMarketRateByUnitType() {
		String query = "select REPLACE(d.unittype, '.', '') as Unittype, round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and d.id!=78 group by d.unittype UNION SELECT 'RV Parking' as Unittype, round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as RVprice from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and D.ID=78 or (D.ID=77 and sa.RV=1) UNION SELECT 'All Units' as Unittype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and d.IsDefault=0 UNION ALL SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 AND sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A";
		return query;
	}
	
	public String nationalRateByUnitType() {
		String query = "SELECT REPLACE(d.unittype, '.', '') as Unittype, round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price from Stores S1 with (nolock) JOIN SpacePrice Z  with (nolock)ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) JOIN Space sa with (nolock) ON sa.SpaceID=Z.SpaceID AND  Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D with (nolock) ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' and D.IsDefault=0 WHERE S1.StoreModFlag != 3 and d.ID!=78 and State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1) group by d.unittype UNION SELECT 'RV Parking' as Unittype, round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as RVprice from Stores S1 with (nolock) JOIN SpacePrice Z with (nolock) ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) JOIN Space sa with (nolock) ON sa.SpaceID=Z.SpaceID AND  Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D with (nolock) ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' and D.IsDefault=0 WHERE S1.StoreModFlag != 3 and D.ID=78 or (D.ID=77 and sa.RV=1) and State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1) UNION SELECT 'All Units' as Unittype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='7,8,9,10' and D.IsDefault=0 WHERE S1.StoreModFlag != 3 and d.IsDefault=0 and State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1) UNION ALL SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 WHERE S1.StoreModFlag != 3 AND sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A";
		return query;
	}

	public String stateRateByUnitType() {
		return null;
	}
	
	/* Compare */
	public String greenBlueRateByUnitType(HashMap<String, String> mapDet) {
		String query = "select REPLACE(d.unittype, '.', '') as Unittype, round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and d.id!=78 group by d.unittype UNION SELECT 'RV Parking' as Unittype, round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as RVprice from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and D.ID=78 or (D.ID=77 and sa.RV=1) UNION SELECT 'All Units' as Unittype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and d.IsDefault=0 UNION ALL SELECT Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0) as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 AND sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A ";
		return query;
	}
	
	
	
	
	
	/* Pricing Rental - Average Unit Type Rates */
	
	public String thisMarketAvgUnitRates() {
		String query = "SELECT REPLACE(d.unittype, '.', ''), round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and s1.countryid in (7,8,9,10) group by d.unittype UNION SELECT 'all units' as unittype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and d.IsDefault=0 UNION ALL SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 AND sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A";
		return query;
	}
	
	public String nationalAvgUnitRates() {
		String query = "SELECT REPLACE(unittype, '.', ''), round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice as price, D.UnitType from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) AND S1.State NOT IN (SELECT ID FROM LookupStates WHERE RegionId = 3 AND IsUnionTerritories = 1) JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice > 1 AND Z.onlineprice < 1000 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' and D.IsDefault=0 WHERE d.IsDefault=0 UNION ALL SELECT Z.onlineprice as price, 'RV Parking' as unittype from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) AND S1.State NOT IN (SELECT ID FROM LookupStates WHERE RegionId = 3 AND IsUnionTerritories = 1) JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice > 1 AND Z.onlineprice < 1000 WHERE sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A group by UnitType UNION SELECT 'All Units' as unitype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) AND S1.State NOT IN (SELECT ID FROM LookupStates WHERE RegionId = 3 AND IsUnionTerritories = 1) JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice > 1 AND Z.onlineprice < 1000 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' and D.IsDefault=0 WHERE d.IsDefault=0 UNION ALL SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) AND S1.State NOT IN (SELECT ID FROM LookupStates WHERE RegionId = 3 AND IsUnionTerritories = 1) JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice > 1 AND Z.onlineprice < 1000 WHERE sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A";
		return query;
	}
	
	public String stateAvgUnitRates() {
		return null;
	}
	
	public String greenBlueAvgUnitRates(HashMap<String, String> mapDet) {
		String query = "SELECT REPLACE(d.unittype, '.', ''), round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePrice Z ON  S1.StoreID=Z.StoreID JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and s1.countryid in (7,8,9,10) group by d.unittype UNION SELECT 'all units' as unittype, round(AVG(A.Price),2) as price FROM (SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and d.IsDefault=0 UNION ALL SELECT Z.onlineprice as price from Stores S1 JOIN SpacePrice Z ON S1.StoreID=Z.StoreID JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.IsCurrentPrice = 1 AND Z.onlineprice !=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 AND sa.spacetype = 'Parking' AND sa.Car = 1 AND sa.RV = 1) A";
		return query;
	}
	
	
	
	
	
	/* Pricing Rental - Rate Per Square Feet History Graph */
	
	public String thisMarketUnitRateHistory(String unitName, String datePrice) {
		String query = "";
		int unitId = getUnitID(unitName);
		if(unitName.toLowerCase().contains("all units")) {
			query = "Select round(avg (price),2) from (SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE D.IsDefault=0 union all SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 and sa.SpaceType='Parking' and (Car=1 and RV=1) JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+") z GROUP by z.DatePrice"; 
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "SELECT round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as thismarket from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='7,8,9,10' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and d.id=78 GROUP by z.DatePrice";
		} else {
			query = "SELECT round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as thismarket from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='7,8,9,10' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and d.id="+unitId+" GROUP by z.DatePrice";
		}
		return query;
	}
	
	public String nationalUnitRateHistory(String unitName, String datePrice) {
		String query = "";
		int unitId = getUnitID(unitName);
		if(unitName.toLowerCase().contains("all units")) {
			query = "Select round(avg (price),2) from (SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0  THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='7,8,9,10' and D.IsDefault=0 WHERE D.IsDefault=0 AND S1.State NOT IN (SELECT ID FROM LookupStates WHERE RegionId = 3 AND IsUnionTerritories = 1) union all SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2)  as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 and sa.SpaceType='Parking' and (Car=1 and RV=1)) z GROUP by z.DatePrice"; 
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "SELECT round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='7,8,9,10' and D.IsDefault=0 WHERE S1.StoreModFlag != 3 and D.ID=78 or (D.ID=77 and sa.RV=1) GROUP by z.DatePrice";
		} else {
			query = "SELECT round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as nationalPrice from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='7,8,9,10' and D.IsDefault=0 WHERE S1.StoreModFlag != 3 and d.ID="+unitId+" GROUP by z.DatePrice";
		}
		return query;
	}
	
	public String stateUnitRateHistory(String unitName, String datePrice) {
		return null;
	}
	
	/* Compare */
	
	public String greenBlueUnitRateHistory(String unitName, String datePrice, HashMap<String, String> mapDet) {
		String query = "";
		int unitId = getUnitID(unitName);
		if(unitName.toLowerCase().contains("all units")) {
			query = "Select round(avg (price),2) from (SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE D.IsDefault=0 union all SELECT round((Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 and sa.SpaceType='Parking' and (Car=1 and RV=1) JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+") z GROUP by z.DatePrice"; 
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "SELECT round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId=2 and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='7,8,9,10' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and D.ID=78 or (D.ID=77 and sa.RV=1) GROUP by z.DatePrice";
		} else {
			query = "SELECT round(AVG(Z.onlineprice/nullif(((CASE WHEN ABS(Sa.width)=0 THEN 1 ELSE ABS(Sa.width) END)*(CASE WHEN ABS(Sa.length)=0 THEN 1 ELSE ABS(Sa.length) END)),0)),2) as thismarket from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 AND Z.onlineprice Between 2 and 999 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='7,8,9,10' and D.IsDefault=0 JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and d.id="+unitId+" GROUP by z.DatePrice";
		}
		return query;
	}
	
	
	
	
	
	
	/* Pricing Rental - Average Rates History */
	
	public String thisMarketAvgRateHistory(String unitName, String datePrice) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "SELECT round(AVG(Z.price),2) as price FROM (SELECT Z.onlineprice as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE d.IsDefault=0 union all SELECT Z.onlineprice as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 and (car=1 and rv=1) and Sa.Spacetype='Parking' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+") z group by z.DatePrice"; 
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='7,8,9,10' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and d.id=78 or (d.id=77 and sa.RV=1) group by z.DatePrice";
		} else {
			int unitId = getUnitID(unitName);
			query = "select round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='7,8,9,10' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+_testData.userStoreId+" WHERE S1.StoreModFlag != 3 and d.id="+unitId+" group by z.DatePrice";
		}
		return query;
	}
	

	public String nationalAvgRateHistory(String unitName, String datePrice) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "SELECT round(AVG(Z.price),2) as price FROM (SELECT CASE WHEN  Z.onlineprice!=0 then  Z.onlineprice else Z.regularprice END as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' WHERE d.IsDefault=0 and S1.State not in (select id from LookupStates with (nolock) where CountryId in (7,8,9,10) and IsUnionTerritories = 1) union all SELECT CASE WHEN  Z.onlineprice!=0 then  Z.onlineprice else Z.regularprice END as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 and sa.car=1 and sa.rv=1 and Sa.Spacetype='Parking' JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' WHERE S1.State not in (select id from LookupStates with (nolock) where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) z group by z.DatePrice"; 
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select ROUND(AVG(CASE WHEN Z.onlineprice!=0 then  Z.onlineprice else Z.regularprice END),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN  Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' WHERE d.IsDefault=0 and d.id=78 or (d.id=77 and sa.RV=1) and S1.State not in (select id from LookupStates with (nolock) where CountryId in (7,8,9,10) and IsUnionTerritories = 1) group by z.DatePrice";
		} else {
			int unitId = getUnitID(unitName);
			query = "SELECT ROUND(AVG(CASE WHEN Z.onlineprice!=0 then  Z.onlineprice else Z.regularprice END),2) as price from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' WHERE d.IsDefault=0 and d.ID="+unitId+" and S1.State not in (select id from LookupStates with (nolock) where CountryId in (7,8,9,10) and IsUnionTerritories = 1) group by z.DatePrice";
		}
		return query;
	}
	
	public String stateAvgRateHistory(String unitName, String datePrice) {
		return null;
	}
	
	/* Compare */
	
	public String greenBlueAvgRateHistory(String unitName, String datePrice, HashMap<String, String> mapDet) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "SELECT round(AVG(Z.price),2) as price FROM (SELECT Z.onlineprice as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 JOIN DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and D.CountryId='7,8,9,10' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE d.IsDefault=0 union all SELECT Z.onlineprice as price, z.DatePrice from Stores S1 JOIN SpacePriceHistory Z ON S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN Space sa ON sa.SpaceID=Z.SpaceID AND z.Onlineprice >1 and z.Onlineprice < 1000 and (car=1 and rv=1) and Sa.Spacetype='Parking' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+") z group by z.DatePrice"; 
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='7,8,9,10' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and d.id=78 or (d.id=77 and sa.RV=1) group by z.DatePrice";
		} else {
			int unitId = getUnitID(unitName);
			query = "select round(AVG(Z.onlineprice),2) as price from Stores S1 JOIN SpacePriceHistory Z ON  S1.StoreID=Z.StoreID AND S1.CountryId in (7,8,9,10) and z.DatePrice = '"+datePrice+"' JOIN  Space  sa ON sa.SpaceID=Z.SpaceID AND Z.onlineprice !=0 JOIN  DefaultUnitSizes D ON D.ID=sa.DefaultUnitId and  D.CountryId='7,8,9,10' JOIN MarketViewUserStoreCompSet mvp on mvp.storeid=s1.storeid and mvp.userstoreid="+mapDet.get("UserStoreId")+" WHERE S1.StoreModFlag != 3 and d.id="+unitId+" group by z.DatePrice";
		}
		return query;
	}
	
	
	
	
	
	
	
	/* Pricing Rental - Rate History By Store */
	
	public String marketRateHistory(String valueType, String unitName, String datePrice) {
		String minMax = (valueType.equalsIgnoreCase("Premium")) ? "max" : "min";
		String query = "";
		
		if(unitName.toLowerCase().contains("all units")) {
			if(valueType.equalsIgnoreCase("Premium")) {
				query = "select sp.Premium as Price, sp.storeid, sp.DefaultUnitId  into #a from PriceChangesVolatilityDefaultUnits Sp with (nolock) join MarketviewUserStoreCompset mvc with (nolock) on mvc.storeid=sp.StoreID and mvc.userstoreid="+_testData.userStoreId+" join DefaultUnitSizes DU with (nolock) on DU.id=Sp.defaultunitid and DU.ISdefault=0 and DU.CountryId in ('7,8,9,10') and sp.DatePrice2='"+datePrice+"' and sp.Premium>1 and sp.Premium<1000 select max(price) as high, ROUND(AVG(Price),2) as average, MIN(Price) as low from #a drop table #a";
			} else {
				query = "select sp.Value as Price, sp.storeid, sp.DefaultUnitId  into #a from PriceChangesVolatilityDefaultUnits Sp with (nolock) join MarketviewUserStoreCompset mvc with (nolock) on mvc.storeid=sp.StoreID and mvc.userstoreid="+_testData.userStoreId+" join DefaultUnitSizes DU with (nolock) on DU.id=Sp.defaultunitid and DU.ISdefault=0 and DU.CountryId in ('7,8,9,10') and sp.DatePrice2='"+datePrice+"' and sp.Value>1 and sp.Value<1000 select max(price) as high, ROUND(AVG(Price),2) as average, MIN(Price) as low from #a drop table #a";
			}
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select "+minMax+"(onlineprice) as Price, storeid into #a from SpacePriceHistory with (nolock) where storeid in (select storeid from MarketviewUserStoreCompset where userstoreid = "+_testData.userStoreId+") and spaceid in (Select SpaceID from Space where defaultunitid=78 or (defaultunitid=77 and RV=1)) and Dateprice='"+datePrice+"' and Onlineprice >1 and Onlineprice < 1000  group by storeid select MAX(Price) as high, ROUND(AVG(Price),2) as average, MIN(Price) as low from #a Drop Table #a";
		} else {
			int unitId = getUnitID(unitName);
			if(valueType.equalsIgnoreCase("Premium")) {
				query = "select sp.Premium as Price, sp.storeid, sp.DefaultUnitId  into #a from PriceChangesVolatilityDefaultUnits Sp with (nolock) join MarketviewUserStoreCompset mvc with (nolock) on mvc.storeid=sp.StoreID and mvc.userstoreid="+_testData.userStoreId+" join DefaultUnitSizes DU with (nolock) on DU.id=Sp.defaultunitid and DU.ID="+unitId+" and DU.CountryId in ('7,8,9,10') and sp.DatePrice2='"+datePrice+"' and sp.Premium>1 and sp.Premium<1000 select max(price) as high, ROUND(AVG(Price),2) as average, MIN(Price) as low from #a drop table #a";
			} else {
				query = "select sp.Value as Price, sp.storeid, sp.DefaultUnitId  into #a from PriceChangesVolatilityDefaultUnits Sp with (nolock) join MarketviewUserStoreCompset mvc with (nolock) on mvc.storeid=sp.StoreID and mvc.userstoreid="+_testData.userStoreId+" join DefaultUnitSizes DU with (nolock) on DU.id=Sp.defaultunitid and DU.ID="+unitId+" and DU.CountryId in ('7,8,9,10') and sp.DatePrice2='"+datePrice+"' and sp.Value>1 and sp.Value<1000 select max(price) as high, ROUND(AVG(Price),2) as average, MIN(Price) as low from #a drop table #a";
			}
		}
		return query;
	}
	
	
	public String storeRateHistory(String valueType, int storeId, String unitName, String datePrice) {
		String minMax = (valueType=="Premium") ? "max" : "min";
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			if(valueType.equalsIgnoreCase("Premium")) {
				query = "select sp.Premium as Price, sp.storeid, sp.DefaultUnitId  into #a from PriceChangesVolatilityDefaultUnits Sp with (nolock) join DefaultUnitSizes DU with (nolock) on DU.id=Sp.defaultunitid and DU.ISdefault=0 and DU.CountryId in ('7,8,9,10') and sp.DatePrice2='"+datePrice+"' and sp.Premium>1 and sp.Premium<1000 and sp.StoreId="+storeId+" select ROUND(AVG(Price),2) as average from #a drop table #a";
			} else {
				query = "lect sp.Value as Price, sp.storeid, sp.DefaultUnitId  into #a from PriceChangesVolatilityDefaultUnits Sp with (nolock) join DefaultUnitSizes DU with (nolock) on DU.id=Sp.defaultunitid and DU.ISdefault=0 and DU.CountryId in ('7,8,9,10') and sp.DatePrice2='"+datePrice+"' and sp.Value>1 and sp.Value<1000 and sp.StoreId="+storeId+" select ROUND(AVG(Price),2) as average from #a drop table #a";
			}
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select "+minMax+"(onlineprice) as Price from SpacePriceHistory with (nolock) where storeid = "+storeId+" and Dateprice ='"+datePrice+"' and SpaceID in (Select SpaceID from Space where defaultunitid=78 or (defaultunitid=77 and RV=1))";
		} else {
			int unitId = getUnitID(unitName);
			query = "select "+minMax+"(onlineprice) as Price from SpacePriceHistory with (nolock) where storeid = "+storeId+" and Dateprice ='"+datePrice+"' and SpaceID in (Select SpaceID from Space where defaultunitid="+unitId+")";
		}
		return query;
	}
	
	
	
	
	
	
	/* Pricing Rental - Rate Volatility History */
	
	public String marketRateVolatilityHistory(String unitName, String dateFrom, String dateTo) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a join defaultunitsizes b on a.DefaultUnitId = b.id and b.Isdefault=0 join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId join MarketViewUserStoreCompSet c on d1.storeid = c.storeid and c.userstoreid = "+_testData.userStoreId+" and c.IsTracked = 1 where a.DatePrice2 BETWEEN '"+dateFrom+"' and '"+dateTo+"'"; 
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a join defaultunitsizes b on a.DefaultUnitId = b.id join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId join MarketViewUserStoreCompSet c on d1.storeid = c.storeid and c.userstoreid = "+_testData.userStoreId+" and c.IsTracked = 1 and b.id=78 where a.DatePrice2 between '"+dateFrom+"' and '"+dateTo+"' group by b.UnitType";
		} else {
			int unitId = getUnitID(unitName);
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a join defaultunitsizes b on a.DefaultUnitId = b.id join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId join MarketViewUserStoreCompSet c on d1.storeid = c.storeid and c.userstoreid = "+_testData.userStoreId+" and c.IsTracked = 1 and b.id="+unitId+" where a.DatePrice2 between '"+dateFrom+"' and '"+dateTo+"' group by b.UnitType";
		}
		return query;
	}
	
	
	public String nationalRateVolatilityHistory(String unitName, String dateFrom, String dateTo) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a with (nolock) join defaultunitsizes b on a.DefaultUnitId = b.id and b.IsDefault=0 and b.CountryId in ('7,8,9,10') join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId where a.DatePrice2 BETWEEN '"+dateFrom+"' and '"+dateTo+"'"; 
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a with (nolock) join defaultunitsizes b on a.DefaultUnitId = b.id join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId and b.id=78 where a.DatePrice2 between '"+dateFrom+"' and '"+dateTo+"' group by b.UnitType";
		} else {
			int unitId = getUnitID(unitName);
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a with (nolock) join defaultunitsizes b on a.DefaultUnitId = b.id join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId and b.id="+unitId+" where a.DatePrice2 between '"+dateFrom+"' and '"+dateTo+"' group by b.UnitType";
		}
		return query;
	}
	
	
	public String stateRateVolatilityHistory(String unitName, String dateFrom, String dateTo) {
		return null;
	}
	
	
	public String greenBlueRateVolatilityHistory(String unitName, String dateFrom, String dateTo, HashMap<String, String> mapDet) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a join defaultunitsizes b on a.DefaultUnitId = b.id and b.Isdefault=0 join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId join MarketViewUserStoreCompSet c on d1.storeid = c.storeid and c.userstoreid = "+mapDet.get("UserStoreId")+" and c.IsTracked = 1 where a.DatePrice2 BETWEEN '"+dateFrom+"' and '"+dateTo+"'"; 
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a join defaultunitsizes b on a.DefaultUnitId = b.id join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId join MarketViewUserStoreCompSet c on d1.storeid = c.storeid and c.userstoreid = "+mapDet.get("UserStoreId")+" and c.IsTracked = 1 and b.id=78 where a.DatePrice2 between '"+dateFrom+"' and '"+dateTo+"' group by b.UnitType";
		} else {
			int unitId = getUnitID(unitName);
			query = "select Round(cast(sum(case when a.Pricerangechangecount = 1 then 1 else 0 END)as float)/cast(count(a.id) as float) *100,2) as PriceVolatility from PriceChangesVolatilityDefaultUnits a join defaultunitsizes b on a.DefaultUnitId = b.id join stores d on a.storeid = d.storeid JOIN stores d1 ON d1.MasterId=d.MasterId join MarketViewUserStoreCompSet c on d1.storeid = c.storeid and c.userstoreid = "+mapDet.get("UserStoreId")+" and c.IsTracked = 1 and b.id="+unitId+" where a.DatePrice2 between '"+dateFrom+"' and '"+dateTo+"' group by b.UnitType";
		}
		return query;
	}
	
	
	
	
	
	
	
	/* Stores in Market */
	/* Store Types */
	
	public String thisMarketStoreTypes() {
		String query = "select Storesinmarket, MIDOPS, Round((MIDOPS*1.0/Storesinmarket)*100, 2) as midopspercent, SMALLOPS, Round((SMALLOPS*1.0/Storesinmarket)*100,2) as smallopspercent from (select 1 as tomap1, COUNT(*) as Storesinmarket from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and storemodflag!= 3) as sto join (select 1 as tomap2, COUNT(*) as MIDOPS from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and storemodflag != 3 and MultiOperator=1) as mi on sto.tomap1 = mi.tomap2 join (select 1 as tomap3, COUNT(*) as SMALLOPS from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and storemodflag != 3 and SingleOperator=1) as sm on mi.tomap2 = sm.tomap3";
		return query;
	}
	
	public String nationalStoreTypes() {
		String query = "select Storesinnation, MIDOPS, Round((MIDOPS*1.0/Storesinnation)*100, 2) as midopspercent, SMALLOPS, Round((SMALLOPS*1.0/Storesinnation)*100,2) as smallopspercent from (select 1 as tomap1, COUNT(*) as Storesinnation from stores where storemodflag != 3 and CountryId in (7,8,9,10) and State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) as st join (select 1 as tomap2, COUNT(*) as MIDOPS from stores where storemodflag != 3 and CountryId in (7,8,9,10) and MultiOperator=1 and State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) as mi on st.tomap1 = mi.tomap2 join (select 1 as tomap3, COUNT(*) as SMALLOPS from stores where storemodflag != 3 and CountryId in (7,8,9,10) and SingleOperator=1 and State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) as sm on mi.tomap2 = sm.tomap3";
		return query;
	}
	
	public String stateStoreTypes() {
		return null;
	}
	
	public String greenBlueStoreTypes(HashMap<String, String> mapDet) {
		String query = "select Storesinmarket, MIDOPS, Round((MIDOPS*1.0/Storesinmarket)*100, 2) as midopspercent, SMALLOPS, Round((SMALLOPS*1.0/Storesinmarket)*100,2) as smallopspercent from (select 1 as tomap1, COUNT(*) as Storesinmarket from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and storemodflag!= 3) as sto join (select 1 as tomap2, COUNT(*) as MIDOPS from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and storemodflag != 3 and MultiOperator=1) as mi on sto.tomap1 = mi.tomap2 join (select 1 as tomap3, COUNT(*) as SMALLOPS from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and storemodflag != 3 and SingleOperator=1) as sm on mi.tomap2 = sm.tomap3";
		return query;
	}
	
	
	
	
	
	
	
	/* Unit Types Offered */
	
	public String thisMarketUnitTypes(String unitName) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(*) as totalunits from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and storemodflag != 3) as st join (SELECT 1 as tomap2, count(distinct A.count1) as noofunits FROM (select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid in (select id from DefaultUnitSizes where IsDefault=0)) UNION ALL select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and iscurrentprice = 1 and spaceid in (select spaceid from space where spacetype = 'Parking' AND Car = 1 AND RV = 1)) A) as un on st.tomap1 = un.tomap2"; 
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(*) as totalunits from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and storemodflag != 3) as st join (select 1 as tomap2, count(distinct storeid) as noofunits from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid=78 or (defaultunitid=77 and RV=1))) as un on st.tomap1 = un.tomap2";
		} else {
			int unitId = getUnitID(unitName);
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(*) as totalunits from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and storemodflag != 3) as st join (select 1 as tomap2, count(distinct storeid) as noofunits from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+_testData.userStoreId+") and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid = "+unitId+")) as un on st.tomap1 = un.tomap2";
		}
		return query;
	}
	
	
	public String nationalUnitTypes(String unitName) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(distinct s.storeid) as totalunits from Stores s JOIN SpacePrice sp on s.StoreID=sp.storeid and s.countryid in (7,8,9,10) and sp.iscurrentprice=1 JOIN space sh on sh.spaceid=sp.spaceid JOIN DefaultUnitSizes d on sh.defaultunitid=d.ID and d.IsDefault=0 Where s.State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) as tot join (SELECT 1 as tomap2, count(distinct A.count1) as noofunits FROM (select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from stores where storemodflag != 3 and CountryId in (7,8,9,10) and State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid in (select id from DefaultUnitSizes where IsDefault=0)) UNION ALL select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from stores where storemodflag != 3 and CountryId in (7,8,9,10) and State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) and iscurrentprice = 1 and spaceid in (select spaceid from space where SpaceType = 'parking' and car=1 and rv=1)) A) as un on tot.tomap1 = un.tomap2"; 
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(distinct s.storeid) as totalunits from Stores s JOIN SpacePrice sp on s.StoreID=sp.storeid and s.countryid in (7,8,9,10) and sp.iscurrentprice=1 JOIN space sh on sh.spaceid=sp.spaceid JOIN DefaultUnitSizes d on sh.defaultunitid=d.ID and d.IsDefault=0 Where s.State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) as tot join (select 1 as tomap2, count(distinct storeid) as noofunits from SpacePrice with (nolock) where storeid in (select storeid from stores where storemodflag != 3 and CountryId in (7,8,9,10) and State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid=78 or (defaultunitid=77 and RV=1))) as un on tot.tomap1 = un.tomap2";
		} else {
			int unitId = getUnitID(unitName);
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(distinct s.storeid) as totalunits from Stores s JOIN SpacePrice sp on s.StoreID=sp.storeid and s.countryid in (7,8,9,10) and sp.iscurrentprice=1 JOIN space sh on sh.spaceid=sp.spaceid JOIN DefaultUnitSizes d on sh.defaultunitid=d.ID and d.IsDefault=0 Where s.State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) as tot join (select 1 as tomap2, count(distinct storeid) as noofunits from SpacePrice with (nolock) where storeid in (select storeid from stores where storemodflag != 3 and CountryId in (7,8,9,10) and State not in (select id from LookupStates where CountryId in (7,8,9,10) and IsUnionTerritories = 1)) and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid = "+unitId+")) as un on tot.tomap1 = un.tomap2";
		}
		return query;
	}
	
	
	public String stateUnitTypes(String unitName) {
		return null;
	}
	
	public String greenBlueUnitTypes(String unitName, HashMap<String, String> mapDet) {
		String query = "";
		if(unitName.toLowerCase().contains("all units")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(*) as totalunits from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and storemodflag != 3) as st join (SELECT 1 as tomap2, count(distinct A.count1) as noofunits FROM (select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid in (select id from DefaultUnitSizes where IsDefault=0)) UNION ALL select distinct storeid as count1 from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and iscurrentprice = 1 and spaceid in (select spaceid from space where spacetype = 'Parking' AND Car = 1 AND RV = 1)) A) as un on st.tomap1 = un.tomap2"; 
		} else if(unitName.toLowerCase().contains("rv park")) {
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(*) as totalunits from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and storemodflag != 3) as st join (select 1 as tomap2, count(distinct storeid) as noofunits from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid=78 or (defaultunitid=77 and RV=1))) as un on st.tomap1 = un.tomap2";
		} else {
			int unitId = getUnitID(unitName);
			query = "select Round(cast(CAST((noofunits*1.0/totalunits*100) as decimal(18,5)) as float),2) from (select 1 as tomap1, COUNT(*) as totalunits from stores where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and storemodflag != 3) as st join (select 1 as tomap2, count(distinct storeid) as noofunits from SpacePrice with (nolock) where storeid in (select storeid from MarketViewUserStoreCompSet where userstoreid = "+mapDet.get("UserStoreId")+") and iscurrentprice = 1 and spaceid in (select spaceid from space where defaultunitid = "+unitId+")) as un on st.tomap1 = un.tomap2";
		}
		return query;
	}
	
	
	

}

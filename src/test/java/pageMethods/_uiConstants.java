package pageMethods;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import pageUtilities._testData;

public class _uiConstants {
	
	public String basicHeader = "Basic Details";
	public String nameLabel = "Unit Type Name";
	public String nameValidation = "Unit Type Name is required";
	
	public String storageTypeLabel = "Storage Type";
	
	public List<String> storageTypeValue() {
		List<String> list = new LinkedList<>();
		list.add("Self Storage");
		list.add("Parking");
		return list;
	}
	
	public String spaceTypeHeader = "Select Space Types";
	public String spaceTypeSelectAll = "Select All";
	
	public List<String> spaceTypeValue() {
		List<String> list = new LinkedList<>();
		if(_testData.regId==1) {
			if(_testData.countryId==1 || _testData.countryId==2) {
				list.addAll(Arrays.asList("Standard Unit", "Locker", "Warehouse", "Wine Storage", "Workshop"));
			} else {
				list.addAll(Arrays.asList("Standard Unit", "Box", "Container", "Locker", "Warehouse", "Wine Storage", "Workshop"));
			}
		} else if(_testData.regId==2 || _testData.regId==4) {
			list.addAll(Arrays.asList("Standard Unit", "Container", "Locker", "Warehouse", "Wine Storage", "Workshop"));
		} else if(_testData.regId==3) {
			list.addAll(Arrays.asList("Standard Unit", "Box", "Container", "Locker", "Warehouse", "Workshop"));
		}
		return list;
	}
	
	public String sizeHeader = "Size";
	public String sizePredefined = "Predefined";
	
	public List<String> sizePredefinedValue() {
		List<String> list = new LinkedList<>();
		if(_testData.regId==1) {
			if(_testData.countryId==1 || _testData.countryId==2) {
				list.addAll(Arrays.asList("5x5", "5x7.5", "5x10", "5x15", "10x10", "10x15", "10x20", "10x25", "10x30", "10x40", "15x15", "15x20", "20x20", "20x30", "30x30"));
			} else {
				list.addAll(Arrays.asList("1m²", "2m²", "3m²", "4m²", "5m²", "6m²", "7m²", "8m²", "9m²", "10m²"));
			}
		} else if(_testData.regId==2 || _testData.regId==4) {
			list.addAll(Arrays.asList("1mx1m", "1.5mx1.5m", "1.5mx2m", "1.5mx3m", "2mx3m", "3mx3m", "3mx4m", "3mx4.5m", "3mx5m", "3mx6m"));
		} else if(_testData.regId==3) {
			list.addAll(Arrays.asList("20 sq.ft", "25 sq.ft", "35 sq.ft", "40 sq.ft", "50 sq.ft", "80 sq.ft", "100 sq.ft", "150 sq.ft", "160 sq.ft", "200 sq.ft"));
		}
		return list;
	}
 	
	public String sizeEMLabel = "Exact Match";
	public String sizeWLLabel = "Width=Length";
	public String sizeSpecLabel = "Specific";
	public String sizeSpecWLLabel = "Width=Length Match (eg: 5 x 10 = 10 x 5)";
	public String sizeSqFootagelabel = (_testData.regId==1 && _testData.countryId==3 || _testData.regId==1 && _testData.countryId==4 || _testData.regId==1 && _testData.countryId==5) ? "Specific" : "Square Footage";
	public String feaHeader = "Unit Features";
	public String feaCCHeader = "Climate Controlled";
	public String feaAlrmHeader = "Alarm";
	public String feaDriveHeader = "Drive Up";
	public String feaPwrHeader = "Power";
	public String feaOutAccHeader = "Outdoor Access";
	public String feaEleHeader = "Elevator";
	public String feaHumHeader = "Humidity Controlled";
	public String feaDoorHeader = "Door Type";
	
	
	public List<String> feaCCValue() {
		List<String> list = new LinkedList<>();
		list.add("-- --");
		list.add("Yes");
		list.add("No");
		return list;
	}
	
	public List<String> feaAlarmValue() {
		List<String> list = new LinkedList<>();
		list.add("-- --");
		list.add("Yes");
		list.add("No");
		return list;
	}
	
	public List<String> feaDriveValue() {
		List<String> list = new LinkedList<>();
		list.add("-- --");
		list.add("Yes");
		list.add("No");
		return list;
	}
	
	public List<String> feaPowerValue() {
		List<String> list = new LinkedList<>();
		list.add("-- --");
		list.add("Yes");
		list.add("No");
		return list;
	}
	
	public List<String> feaOutdoorValue() {
		List<String> list = new LinkedList<>();
		list.add("-- --");
		list.add("Yes");
		list.add("No");
		return list;
	}
	
	public List<String> feaElevatorValue() {
		List<String> list = new LinkedList<>();
		list.add("-- --");
		list.add("Yes");
		list.add("No");
		return list;
	}
	
	public List<String> feaHumidityValue() {
		List<String> list = new LinkedList<>();
		list.add("-- --");
		list.add("Yes");
		list.add("No");
		return list;
	}
	
	public List<String> feaDoorValue() {
		List<String> list = new LinkedList<>();
		list.add("-- --");
		list.add("Rollup");
		list.add("Swing");
		list.add("Double");
		list.add("Chain Link");
		list.add("Other");
		return list;
	}
	
	public String feaFlHeader = "Floor";
	
	public List<String> feaFloorValue() {
		List<String> list = new LinkedList<>();
		list.add("-- --");
		list.add("Ground");
		list.add("Upper");
		return list;
	}
	
}

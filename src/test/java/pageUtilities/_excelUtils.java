package pageUtilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class _excelUtils {
	
	
	static String filePath = "test-input/storesList.xls";
	public static HashMap<String, String> exStore;
	public static FileInputStream fis;
	public static FileOutputStream fos;
	public static Workbook wb;
	public static Sheet sh;
	public static Row row;
	public static Cell cell;
	static int random;

	public static void openExcel() { 
		
		try {
			fis = new FileInputStream(filePath);
			wb = WorkbookFactory.create(fis);
			
		} catch (Exception e) {
			System.out.println("Couldn't able to read Excel file"+e);
		}
	}
	
	public static void getMarket() {
		if(_testData.marketTypeId==1) {
			getStoreAddress();
		} else if(_testData.marketTypeId==3) {
			getStateCity();
		}
	}
	
	public static void getStoreAddress() {
		sh = wb.getSheet("address");
		String isProcessed = "";
		DataFormatter format = new DataFormatter();
		exStore = new HashMap<String, String>();
		do {
			random = _utils.getRandNumber(sh.getLastRowNum());
			System.out.println(random);
			isProcessed = format.formatCellValue(sh.getRow(random).getCell(sh.getRow(0).getLastCellNum()-1));
			System.out.println(isProcessed);
		} while(isProcessed.isEmpty()!=true);
		
		for(int i=0; i<=sh.getRow(0).getLastCellNum()-2; i++) {
			String header_cell = sh.getRow(0).getCell(i).getStringCellValue();
			String value_cell = format.formatCellValue(sh.getRow(random).getCell(i));
			exStore.put(header_cell, value_cell);
		}
		
		String rad = exStore.get("radius");
		if(rad.isEmpty()) 
			exStore.replace("radius", "0");
		System.out.println(exStore);
	
		_testData.setMarketAddress(exStore);
	}
	
	public static void getStateCity() {
		sh = wb.getSheet("cityState");
		String isProcessed = "";
		DataFormatter format = new DataFormatter();
		exStore = new HashMap<String, String>();
		do {
			random = _utils.getRandNumber(sh.getLastRowNum());
			System.out.println(random);
			isProcessed = format.formatCellValue(sh.getRow(random).getCell(sh.getRow(0).getLastCellNum()-1));
			System.out.println(isProcessed);
		} while(isProcessed.isEmpty()!=true);
		
		for(int i=0; i<=sh.getRow(0).getLastCellNum()-2; i++) {
			String header_cell = sh.getRow(0).getCell(i).getStringCellValue();
			String value_cell = format.formatCellValue(sh.getRow(random).getCell(i));
			exStore.put(header_cell, value_cell);
		}
		
		System.out.println(exStore);
	
		_testData.setMarketCityState(exStore);
	}

	public static void setStoreProcessed() {
		String updateSheet = "";
		if(_testData.marketTypeId==1) {
			updateSheet = "address";
		} else if (_testData.marketTypeId==3) {
			updateSheet = "cityState";
		}
		
		try {
			sh = wb.getSheet(updateSheet);
			row = sh.getRow(random);
			cell = row.createCell(sh.getRow(0).getLastCellNum()-1);
			cell.setCellValue("Yes");
			fos = new FileOutputStream(filePath);
			wb.write(fos);
			fos.flush();
		} catch (Exception e) {
        e.printStackTrace();
		}
	}
	
	
	public static void closeExcel() { 
		
		try {
			wb.close();
			fos.close();
		} catch (Exception e) {
			System.out.println("No workbook instance open "+e);
		}
	}
}

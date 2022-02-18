package pageMethods;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import objRepository._knownDevPage;
import pageUtilities._base;
import pageUtilities._databaseUtils;
import pageUtilities._testData;
import pageUtilities._utils;

public class _knownDevelopments extends _knownDevPage {
	
	ExtentTest test = _base.test;
	ExtentTest node;
	
	public _knownDevelopments() {
		super();
		_utils.waitForElementInVisibleByLocator(loader);
		_helperClass.closeOpenWidgets();
		_utils.clickJs($knowDevLink);
		_utils.waitForElementInVisibleByLocator(loader);
		_utils.clickJs($showWidget);
		_utils.waitForElementInVisibleByLocator(loader);
	}
	
	public void knownDevelopment() {
		
			node = test.log(Status.INFO, "Known Developments");
			System.out.println();
			
			try {
				HashMap<String, String> gridValues = new HashMap<>();
				int firstRowCellCount = $firstRowCell.size();
				int totalDevUi = $noOfDevelopments.size();
				String totalDevDb = _databaseUtils.getStringValue(_testData.queryIns.knowDevNoOfDevs());
				if(firstRowCellCount!=1) {
					_helperClass.compareUiDb("Total Known Developments", String.valueOf(totalDevUi), totalDevDb, node);
					int noOfStoresToCheck = (totalDevUi>3) ? 3 : totalDevUi;
					for(int i=1; i<=noOfStoresToCheck; i++) {
						String address = $address(_base.driver, i).getText();
						gridValues = _databaseUtils.getColumnValues(_testData.queryIns.knownDevGridVal(address));
						for(int j=1; j<=firstRowCellCount; j++) {
							String header = $headerLabel(_base.driver, j).getText();
							String uiValue = ($rowValues(_base.driver, i, j).getText()).replace("--", "N/A");
							String dbValue = (gridValues.get(header)==null) ? "N/A" : gridValues.get(header).replace("amp;", "").replace("; ", " ");
							
							/* To skip replacing special characters in compare function */
							List<String> headerListToSkip = Arrays.asList( "STORE NAME", "OWNER NAME", "OPERATOR/MANAGEMENT", "ADDRESS", "SOURCE URL", "NOTES");
							for(String a : headerListToSkip) {
								if(header.equals(a)) {
									header = header+"@skip@";
									break;
								}
							}
							_helperClass.compareUiDb(header, uiValue, dbValue, node);
						}
					}
					
				} else {
					_helperClass.compareUiDb("Total Known Developments", "N/A", totalDevDb, node);
				}
			} catch(Exception e) {
				node.log(Status.ERROR, "Exception: "+e);
			}
	}
}

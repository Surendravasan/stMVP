package pageMethods;

import objRepository._marketTypePage;
import pageUtilities._utils;

public class _marketType extends _marketTypePage {
	
	public _marketType() {
		super();
	}
	
	public void selectGeoLocation() {
		_utils.click($geoLocationRadio);
	}
	
	public void selectCityList() {
		_utils.click($cityListRadio);
	}
	
	public void saveMarketType() {
		_utils.waitClick($saveBtn);
	}

}

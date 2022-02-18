package tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeTest;

public class _toExplore {

	WebDriver driver;

	@BeforeTest
	public void intitateBrowser() {
		int dupes[] = { 1 };

		int[] wodupes = new int[dupes.length];

		for (int a = 0; a < dupes.length; a++) {
			if (wodupes.length == 0) {

				System.out.println(dupes[a]);
				wodupes[0] = dupes[a];
			}

		}
		System.out.println(wodupes[0]);
	}
}
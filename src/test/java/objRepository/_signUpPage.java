package objRepository;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pageUtilities._base;

public class _signUpPage {
	
	public _signUpPage() {
		PageFactory.initElements(_base.driver, this);
	}
	
	protected By loader = By.cssSelector("div.loading");
	
	@FindBy(xpath="//div[contains(text(),'National')]/../../button")
	protected WebElement $nationalSubs;

	@FindBy(css="div.user-details input[name='Login']")
	protected WebElement $emailAddress;
	
	@FindBy(css="div.user-details input[name='Password']")
	protected WebElement $password;
	
	@FindBy(css="div.user-details input[name='ConfirmPassword']")
	protected WebElement $confirmPassword;
	
	@FindBy(css="div.user-details input[name='FirstName']")
	protected WebElement $firstName;
	
	@FindBy(css="div.user-details input[name='LastName']")
	protected WebElement $lastName;
	
	@FindBy(css="div.user-details input[name='Company']")
	protected WebElement $company;
	
	@FindBy(css="div.user-details input[name='phone' i]")
	protected WebElement $phone;
	
	@FindBy(css="div.user-details button[class*='next-btn']")
	protected WebElement $userDetailsNext;
	
	@FindBy(css="div[class*='billing-content'] input[name='BillingAddress']")
	protected WebElement $address1;
	
	@FindBy(css="div[class*='billing-content'] input[name='BillingCity']")
	protected WebElement $city;
	
	@FindBy(css="div[class*='billing-content'] select[name='BillingState']")
	protected WebElement $state;
	
	@FindBy(css="div[class*='billing-content'] input[name='BillingZipCode']")
	protected WebElement $zipCode;
	
	@FindBy(xpath="//div[contains(@class,'billing-content')]//button[contains(@class,'btn')][contains(text(),'Pay')]")
	protected WebElement $billingPayNow;
	
	@FindBy(css="div[class='payment-details'] input[name='cardName']")
	protected WebElement $cardName;
	
	@FindBy(css="div[class='payment-details'] input[name='cardNumber']")
	protected WebElement $cardNumber;
	
	@FindBy(css="div[class='payment-details'] input[name='expiry']")
	protected WebElement $cardExpiry;
	
	@FindBy(css="div[class='payment-details'] input[name='SecurityCode']")
	protected WebElement $cardSecurityCode;
	
	@FindBy(css="div[class='payment-details'] input[name='checkItOut']")
	protected WebElement $agreeCheck;
	
	@FindBy(css="div[class*='payment-details'] button[class*='next-btn']")
	protected WebElement $paymentNow;
	
	@FindBy(css="div[class*='last-step'] button[class*='btn']")
	protected WebElement $gotoDash;
	
}

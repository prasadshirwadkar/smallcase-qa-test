package pageobjects;

import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Flipkart {
	WebDriver driver;
	
	@CacheLookup
	@FindBy (xpath="//button[@class='_2KpZ6l _2doB4z']")
	private WebElement cancelLoginButton;
	
	@CacheLookup
	@FindBy (xpath="//input[@class='_3704LK']")
	private WebElement searchBar;
	
	@CacheLookup
	@FindBy(xpath = "//div[@class='_13oc-S']/div")
	private List<WebElement> resultLinks;
	
	@CacheLookup
	@FindBy (xpath="//div[@class='_30jeq3 _16Jk6d']")
	private WebElement priceInPage;
	
	@CacheLookup
	@FindBy (xpath="//span[@class='B_NuCI']")
	private WebElement productTitle;
	
	@CacheLookup
	@FindBy (xpath="//button[@class='_2KpZ6l _2U9uOA _3v1-ww']")
	private WebElement addToCartButon;
	
	@CacheLookup
	@FindBy (xpath="//button[@class='_23FHuj'][2]")
	private WebElement increaseQuantityButton;
	
	@CacheLookup
	@FindBy (xpath="//span[@class='_2-ut7f _1WpvJ7']")
	private WebElement priceInCart;
	
	@CacheLookup
	@FindBy (xpath="//div[@class='_2sKwjB']")
	private WebElement quantityIncreasePopup;
	

	public Flipkart(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public WebDriver searchItemAndClickOnFirst(WebDriver driver, String itemName, WebDriverWait wait) {
		wait.until(ExpectedConditions.visibilityOfAllElements(cancelLoginButton));
		cancelLoginButton.click();
		searchBar.sendKeys(itemName, Keys.ENTER);
		resultLinks.get(0).click();
		return driver;
	}

	public WebElement getCancelLoginButton() {
		return cancelLoginButton;
	}

	public WebElement getSearchBar() {
		return searchBar;
	}

	public List<WebElement> getResultLinks() {
		return resultLinks;
	}

	public WebElement getProductTitle() {
		return productTitle;
	}

	public WebElement getPriceInPage() {
		return priceInPage;
	}

	public WebElement getAddToCartButon() {
		return addToCartButon;
	}

	public WebElement getIncreaseQuantityButton() {
		return increaseQuantityButton;
	}

	public WebElement getPriceInCart() {
		return priceInCart;
	}
	

	public WebElement getQuantityIncreasePopup() {
		return quantityIncreasePopup;
	}
	
}

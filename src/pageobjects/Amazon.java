package pageobjects;

import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Amazon {
	WebDriver driver;
	
	@CacheLookup
	@FindBy (xpath="//input[@id='twotabsearchtextbox']")
	private WebElement searchBar;

	@CacheLookup
	@FindBy (xpath="//span[contains(@class, 'widgetId=search-results')]//span[contains(@class,'a-text-normal')]")
	private List<WebElement> resultLinks;
	
	@CacheLookup
	@FindBy (xpath="//td[@class='a-span12']/span[1]")
	private WebElement priceInPage;
	
	@CacheLookup
	@FindBy (xpath="//input[@id='add-to-cart-button']")
	private WebElement addToCartButton;
	
	@CacheLookup
	@FindBy (xpath="//span[contains(@class,'nav-cart-count')]")
	private WebElement countInCart;
	
	@CacheLookup
	@FindBy (xpath="//div[@id='nav-cart-text-container']")
	private WebElement cartButton;
	
	@CacheLookup
	@FindBy (xpath="//span[contains(@class, 'sc-product-price')]")
	private WebElement priceInCart;
	
	public Amazon(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public WebDriver searchItem(WebDriver driver, String itemName, WebDriverWait wait) {
		searchBar.sendKeys(itemName, Keys.ENTER);
		return driver;
	}

	public WebElement getSearchBar() {
		return searchBar;
	}

	public List<WebElement> getResultLinks() {
		return resultLinks;
	}

	public WebElement getPriceInPage() {
		return priceInPage;
	}

	public WebElement getAddToCartButton() {
		return addToCartButton;
	}

	public WebElement getCountInCart() {
		return countInCart;
	}

	public WebElement getPriceInCart() {
		return priceInCart;
	}

	public WebElement getCartButton() {
		return cartButton;
	}
	
	
}

package tests;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import pageobjects.Amazon;
import pageobjects.Flipkart;

public class TestScenarios extends TestSetup {
	WebDriver driver;
	WebDriverWait wait;
	static String flipkartURL;
	static String amazonURL;
	Flipkart flipkart;
	Amazon amazon;
	String itemName;
	
	@BeforeMethod
	public void initialiseTest(){
		flipkartURL = properties.getProperty("flipkartURL");
		amazonURL = properties.getProperty("amazonURL");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		flipkart = new Flipkart(driver);
		amazon = new Amazon(driver);
	}
	
	@AfterMethod
	public void closeWebDriver() {
		driver.quit();
	}
	
	@Test
	@Parameters({"item"})
	public void scenario1(String item) {
		logger = extent.createTest("Scenario 1");
		
		//Search item on Flipkart
		driver.get(flipkartURL);
		flipkart.searchItemAndClickOnFirst(driver, item, wait);
		logger.log(Status.PASS, "Go to "+flipkartURL+" and search for '"+item+"'");
		
		//Print price on product page
		ArrayList<String> windowHandles = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(windowHandles.get(1));
		Double priceOnProductPage = Double.parseDouble(formatPrice(flipkart.getPriceInPage().getText()));
		logger.log(Status.PASS, "Product price on product page: " +flipkart.getPriceInPage().getText().substring(1));
		
		//Add to cart and increase quantity
		flipkart.getAddToCartButon().click();
		flipkart.getIncreaseQuantityButton().click();
		
		//Validate if price increase is correct
		wait.until(ExpectedConditions.visibilityOfAllElements(flipkart.getQuantityIncreasePopup()));
		Double priceAfterQuantityIncrease = Double.parseDouble(formatPrice(flipkart.getPriceInCart().getText()));
		logger.log(Status.PASS, "Product price in cart after the quantity was increased by 1: "+flipkart.getPriceInCart().getText().substring(1));
		Assert.assertTrue(priceOnProductPage*2 == priceAfterQuantityIncrease, "Price increase does not match");
		logger.log(Status.PASS, "Price after quantity increase is correct.");
		
	}
	
	@Test
	@Parameters({"item"})
	public void scenario2(String item) {
		logger = extent.createTest("Scenario 2");
		
		//Search item on Flipkart
		driver.get(flipkartURL);
		flipkart.searchItemAndClickOnFirst(driver, item, wait);
		logger.log(Status.PASS, "Go to "+flipkartURL+" and search for '"+item+"'");
		
		//Print price on product page
		ArrayList<String> windowHandles = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(windowHandles.get(1));
		String productTitleFlipkart = flipkart.getProductTitle().getText();
		Double priceOnFlipkart = Double.parseDouble(formatPrice(flipkart.getPriceInPage().getText()));
		logger.log(Status.PASS, "Product name on Flipkart: " +productTitleFlipkart);
		logger.log(Status.PASS, "Product price on Flipkart product page: " +flipkart.getPriceInPage().getText().substring(1));
		
		//Add to cart and print price
		flipkart.getAddToCartButon().click();
		logger.log(Status.PASS, "Product price in Flipkart cart: "+flipkart.getPriceInCart().getText().substring(1));
		
		//Search for the same item on Amazon
		driver.switchTo().newWindow(WindowType.TAB);
		driver.get(amazonURL);
		amazon.searchItem(driver, item, wait);
		logger.log(Status.PASS, "Go to "+amazonURL+" and search for '"+item+"'");
		List<WebElement> amazonResutLinks = amazon.getResultLinks();
		
		//Check if the first page contains matching product
		//Matching logic is explained in the method comment
		WebElement matchingAmazonProduct = getMatchingProduct(productTitleFlipkart, amazonResutLinks);
		Assert.assertNotNull(matchingAmazonProduct, "Amazon does not have a matching product.");
		logger.log(Status.PASS,"Amazon contains matching product");
		
		//Click on matching product and print price
		logger.log(Status.PASS, "Product name on Amazon: " +matchingAmazonProduct.getText());
		matchingAmazonProduct.click();
		windowHandles = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(windowHandles.get(3));
		logger.log(Status.PASS, "Product price on Amazon product page: " +amazon.getPriceInPage().getText().substring(1));
		Double priceOnAmazon = Double.parseDouble(formatPrice(amazon.getPriceInPage().getText()));
		
		//Add to cart and check the price
		amazon.getAddToCartButton().click();
		wait.until(ExpectedConditions.textToBePresentInElement(amazon.getCountInCart(), "1"));
		driver.navigate().refresh();
		amazon.getCartButton().click();
		wait.until(ExpectedConditions.visibilityOfAllElements(amazon.getPriceInCart()));
		logger.log(Status.PASS, "Product price in Amazon cart: " +amazon.getPriceInCart().getText().substring(1));
		
		//Comparing prices on Flipkart and Amazon and printing which is lower.
		if(priceOnFlipkart<priceOnAmazon) {
			logger.log(Status.PASS, "Price on Flipkart is lower (" +priceOnFlipkart+")");
		} else if(priceOnFlipkart>priceOnAmazon){
			logger.log(Status.PASS, "Price on Amazon is lower (" +priceOnAmazon+")");
		} else {
			logger.log(Status.PASS, "Price on Amazon and Flipkart is same");
		}
	}
	
	public String formatPrice(String price) {
		return price.substring(1).replace(",", "");
	}
	
	
	/*
	 * The logic written in the below method for matching the product is -
	 * -- Remove special characters from Flipkart product name and split it into individual words
	 * -- For each product on Amazon's search result list, check how many words it cotains from Flipkart's product name
	 * -- If matching words are more than half, return that search result
	 * -- If no search result contains more than half matching words, return null
	 */
	public WebElement getMatchingProduct(String flipkartName, List<WebElement> amazonResutLinks) {
		WebElement matchingAmazonproduct = null;
		flipkartName = flipkartName.replaceAll("[^a-zA-Z0-9 ]", "");
		String[] flipkartNameSplit = flipkartName.split(" ");
		String productTitleAmazon;
		for(WebElement resultLink : amazonResutLinks) {
			int matchingCount = 0;
			productTitleAmazon = resultLink.getText();
			for(int i=0;i<flipkartNameSplit.length;i++) {
				if(productTitleAmazon.toLowerCase().contains(flipkartNameSplit[i].toLowerCase())) {
					matchingCount++;
				}
			}
			
			if(matchingCount>flipkartNameSplit.length/2) {
				matchingAmazonproduct = resultLink;
				break;
			}
		}
		
		return matchingAmazonproduct;
	}
}

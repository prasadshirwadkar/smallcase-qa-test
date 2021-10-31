package tests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestSetup {
	static ExtentReports extent;
	static ExtentSparkReporter spark;
	static ExtentTest logger;
	static Properties properties;
	
	@BeforeSuite
	public void setupReport() {
		extent = new ExtentReports();
		spark = new ExtentSparkReporter(".\\reports\\smallcase-report.html");
		extent.attachReporter(spark);
		WebDriverManager.chromedriver().setup();
	}
	
	@BeforeTest
	public void loadProperties() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(".\\config.properties"));
		properties = new Properties();
		properties.load(reader);
		reader.close();
	}
	
	@AfterMethod
	public void getResult(ITestResult result){
		if (result.getStatus() == ITestResult.FAILURE){
			logger.log(Status.FAIL, result.getThrowable());
		} else if(result.getStatus() == ITestResult.SKIP){
			logger.log(Status.SKIP, "Test Case Skipped is "+result.getName());
		}
	}
	
	@AfterSuite
	public void teardownReport() {
		extent.flush();
	}
}

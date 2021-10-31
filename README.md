## Pre-requisites
Java, Maven and Chrome browser must be installed on the system.

## Running the tests
1. Clone the repo into your local system.
2. The product to be searched for both the tests is given as a parameter in testng.xml. Change the value if required.
3. Run the tests using ```mvn test``` command.
4. After the tests have finished running, the report will be generated as reports\smallcase-report.html. I have used ExtentReports for test reporting.

## POM and test structure
1. The page objects for both Amazon and Flipkart are present in src\pageobjects folder. I have used Selenium PageFactory mechanism.
2. The tests are present in src\tests folder. 
3. The TestSetup.java class contains methods for setting up report, WebDriver and properties file. The test cases are writen in TestScenarios.java class.

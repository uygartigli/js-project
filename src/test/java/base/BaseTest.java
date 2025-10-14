package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import org.testng.ITestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import utils.ConfigReader;
import utils.WebDriverFactory;
import utils.ExtentReportManager;

public class BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected static ExtentReports extent;
    protected static ExtentTest test;

    @BeforeSuite
    public void setupReport() {
        logger.info("Setting up ExtentReports in @BeforeSuite");
        extent = ExtentReportManager.getInstance();
        logger.info("ExtentReports initialized successfully");
    }
    
    @BeforeClass
    public void setupClass() {
        
        // Initialize driver at class level to ensure it's available
        if (driver == null) {
            logger.info("Driver is null, initializing WebDriver...");
            driver = WebDriverFactory.getDriver();
            logger.info("WebDriver created successfully");
            driver.get(ConfigReader.get("baseUrl"));
            logger.info("Navigated to base URL: {}", ConfigReader.get("baseUrl"));
        } else {
            logger.info("Driver already initialized");
        }
        logger.info("Class setup completed");
    }
    
    protected ExtentTest createTest(String testName) {
        return extent.createTest(testName);
    }

    @AfterSuite
    public void tearDownReport() {
        if (extent != null) {
            extent.flush();
        }
    }
    
    @BeforeMethod
    public void setUp(ITestResult result) {
        logger.info("Setting up method: {}", result.getMethod().getMethodName());
        
        // Just clear cookies to ensure clean state for each test
        driver.manage().deleteAllCookies();
        logger.info("Cookies cleared for test: {}", result.getMethod().getMethodName());
        
        // Test will be created manually in each test method with custom name
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (test != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                test.fail(result.getThrowable());
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                test.pass("Test passed");
            } else if (result.getStatus() == ITestResult.SKIP) {
                test.skip("Test skipped");
            }
        }

        // Don't quit driver here - let it be shared between tests
        // Driver will be closed in @AfterClass or @AfterSuite
    }
    
    @AfterClass
    public void tearDownClass() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}

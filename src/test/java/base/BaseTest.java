package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import utils.WebDriverFactory;
import utils.ExtentReportManager;

public class BaseTest {
    protected WebDriver driver;
    protected ExtentReports extent;
    protected ExtentTest test;

    @BeforeSuite
    public void setupReport() {
        extent = ExtentReportManager.getInstance();
    }

    @AfterSuite
    public void tearDownReport() {
        if (extent != null) {
            extent.flush();
        }
    }
    
    @BeforeMethod
    public void setUp(ITestResult result) {
        driver = WebDriverFactory.createDriver("chrome");
        driver.manage().window().maximize();
        
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

        if (driver != null) {
            driver.quit();
        }
    }
}

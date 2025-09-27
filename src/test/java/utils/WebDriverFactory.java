package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class WebDriverFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(WebDriverFactory.class);
    private static final String[] SUPPORTED_BROWSERS = {"chrome", "firefox", "edge", "safari"};
    
    public static WebDriver getDriver() {
        String browser = getBrowserFromConfig();
        logger.info("Initializing WebDriver for browser: {}", browser);
        
        WebDriver driver = createDriver(browser);
        configureDriver(driver);
        
        logger.info("WebDriver successfully initialized for browser: {}", browser);
        return driver;
    }
    
    private static String getBrowserFromConfig() {
        String browser = ConfigReader.get("browser");
        if (browser == null || browser.trim().isEmpty()) {
            logger.warn("No browser specified in config, defaulting to chrome");
            return "chrome";
        }
        
        String normalizedBrowser = browser.toLowerCase().trim();
        if (!isValidBrowser(normalizedBrowser)) {
            logger.warn("Unsupported browser '{}', defaulting to chrome. Supported browsers: {}", 
                       browser, String.join(", ", SUPPORTED_BROWSERS));
            return "chrome";
        }
        
        return normalizedBrowser;
    }
    
    private static boolean isValidBrowser(String browser) {
        for (String supportedBrowser : SUPPORTED_BROWSERS) {
            if (supportedBrowser.equals(browser)) {
                return true;
            }
        }
        return false;
    }
    
    private static WebDriver createDriver(String browser) {
        String seleniumUrl = System.getenv("SELENIUM_REMOTE_URL");
        
        if (seleniumUrl != null && !seleniumUrl.trim().isEmpty()) {
            logger.info("Using remote WebDriver with Selenium Grid URL: {}", seleniumUrl);
            return createRemoteDriver(browser, seleniumUrl);
        } else {
            logger.info("Using local WebDriver");
            return createLocalDriver(browser);
        }
    }
    
    private static WebDriver createRemoteDriver(String browser, String seleniumUrl) {
        int maxRetries = 3;
        int retryDelay = 5000; // 5 seconds
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                URL gridUrl = new URL(seleniumUrl);
                logger.info("Attempting to connect to Selenium Grid (attempt {}/{})", attempt, maxRetries);
                
                WebDriver driver = createRemoteDriverWithOptions(browser, gridUrl);
                logger.info("Successfully connected to Selenium Grid on attempt {}", attempt);
                return driver;
                
            } catch (MalformedURLException e) {
                logger.error("Invalid Selenium Grid URL: {}", seleniumUrl, e);
                throw new RuntimeException("Invalid Selenium Grid URL: " + seleniumUrl, e);
            } catch (Exception e) {
                logger.warn("Failed to create remote WebDriver for browser: {} (attempt {}/{})", browser, attempt, maxRetries, e);
                
                if (attempt == maxRetries) {
                    logger.error("Failed to create remote WebDriver after {} attempts", maxRetries);
                    throw new RuntimeException("Failed to create remote WebDriver for browser: " + browser + " after " + maxRetries + " attempts", e);
                }
                
                try {
                    logger.info("Waiting {}ms before retry...", retryDelay);
                    Thread.sleep(retryDelay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while waiting for retry", ie);
                }
            }
        }
        
        throw new RuntimeException("Failed to create remote WebDriver after " + maxRetries + " attempts");
    }
    
    private static WebDriver createRemoteDriverWithOptions(String browser, URL gridUrl) {
        switch (browser) {
            case "firefox":
                logger.debug("Creating remote Firefox driver");
                return new RemoteWebDriver(gridUrl, new FirefoxOptions());
                
            case "edge":
                logger.debug("Creating remote Edge driver");
                return new RemoteWebDriver(gridUrl, new EdgeOptions());
                
            case "safari":
                logger.debug("Creating remote Safari driver");
                return new RemoteWebDriver(gridUrl, new SafariOptions());
                
            case "chrome":
            default:
                logger.debug("Creating remote Chrome driver");
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless=new");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--disable-plugins");
                chromeOptions.addArguments("--disable-images");
                chromeOptions.addArguments("--disable-javascript");
                return new RemoteWebDriver(gridUrl, chromeOptions);
        }
    }
    
    private static WebDriver createLocalDriver(String browser) {
        try {
            switch (browser) {
                case "firefox":
                    logger.debug("Creating local Firefox driver");
                    return new FirefoxDriver();
                    
                case "edge":
                    logger.debug("Creating local Edge driver");
                    return new EdgeDriver();
                    
                case "safari":
                    logger.debug("Creating local Safari driver");
                    return new SafariDriver();
                    
                case "chrome":
                default:
                    logger.debug("Creating local Chrome driver");
                    return new ChromeDriver();
            }
        } catch (Exception e) {
            logger.error("Failed to create local WebDriver for browser: {}", browser, e);
            throw new RuntimeException("Failed to create local WebDriver for browser: " + browser, e);
        }
    }
    
    private static void configureDriver(WebDriver driver) {
        try {
            // Configure implicit wait
            String implicitWaitStr = ConfigReader.get("implicitWait");
            if (implicitWaitStr != null && !implicitWaitStr.trim().isEmpty()) {
                try {
                    int implicitWaitSeconds = Integer.parseInt(implicitWaitStr.trim());
                    if (implicitWaitSeconds > 0) {
                        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitSeconds));
                        logger.debug("Set implicit wait to {} seconds", implicitWaitSeconds);
                    } else {
                        logger.warn("Invalid implicitWait value: {} (must be positive)", implicitWaitSeconds);
                    }
                } catch (NumberFormatException e) {
                    logger.warn("Invalid implicitWait value in config.properties: '{}'. Using default timeout.", implicitWaitStr);
                }
            } else {
                logger.warn("No implicitWait value found in config.properties. Using default timeout.");
            }
            
            // Maximize window
            driver.manage().window().maximize();
            logger.debug("Window maximized");
            
        } catch (Exception e) {
            logger.error("Failed to configure WebDriver settings", e);
            throw new RuntimeException("Failed to configure WebDriver settings", e);
        }
    }
}

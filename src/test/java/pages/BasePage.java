package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    // Constructor
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Common navigation locators (available on all pages)
    private By loginNavButton = By.id("item-0");
    private By bookStoreNavButton = By.id("item-2");
    private By profileNavButton = By.id("item-3");

    // Common navigation methods
    public void clickLoginNavigation() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(loginNavButton));
        element.click();
    }

    public void clickBookStoreNavigation() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(bookStoreNavButton));
        element.click();
    }

    public void clickProfileNavigation() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(profileNavButton));
        element.click();
    }
}

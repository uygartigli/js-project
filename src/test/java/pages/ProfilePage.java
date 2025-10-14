package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProfilePage extends BasePage {
    
    private WebDriverWait wait;
    
    // Constructor
    public ProfilePage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    // Profile page locators
    private By logoutButton = By.id("submit");
    
    // Profile page methods
    public boolean isLogoutButtonPresent() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(logoutButton));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    // Constructor
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // Locators
    private By usernameInput = By.id("userName");
    private By passwordInput = By.id("password");
    private By loginButton = By.id("login");
    private By errorMessage = By.id("name");
    private By loggedInMessage = By.id("loading-label");

    // Actions
    public void enterUsername(String username) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(usernameInput));
        element.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(passwordInput));
        element.sendKeys(password);
    }

    public void clickLogin() {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        element.click();
    }

    public String getErrorMessage() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        return element.getText();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public boolean isLoggedInMessagePresent() {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(loggedInMessage));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
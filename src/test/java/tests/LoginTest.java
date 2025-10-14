package tests;

import org.testng.annotations.Test;
import org.testng.Assert;
import pages.LoginPage;
import pages.ProfilePage;
import base.BaseTest;

import static testdata.TestData.*;

public class LoginTest extends BaseTest {

    @Test(groups = {"login", "smoke"})
    public void tryToLoginWithInvalidCredentials() {
        test = createTest("Invalid Login Test");
        
        driver.get(LOGIN_URL);
        test.info("Navigated to login page: " + LOGIN_URL);

        LoginPage loginPage = new LoginPage(driver);
        test.info("LoginPage initialized.");

        loginPage.login(INVALID_USERNAME, INVALID_PASSWORD);
        test.info("Login attempt with invalid credentials.");
        
        String error = loginPage.getErrorMessage();
        test.info("Error message: " + error);

        Assert.assertNotNull(error, "Error message should not be null.");
        Assert.assertFalse(error.isEmpty(), "Error message should not be empty.");
        Assert.assertTrue(error.contains(EXPECTED_ERROR_MESSAGE), "Expected an error message for invalid credentials.");
        test.pass("Test passed: Error message contains expected text: " + EXPECTED_ERROR_MESSAGE);

    }

    @Test(groups = {"login", "smoke"})
    public void tryToLoginWithValidCredentials() {
        test = createTest("Valid Login Test");
        
        driver.get(LOGIN_URL);
        test.info("Navigated to login page: " + LOGIN_URL);

        LoginPage loginPage = new LoginPage(driver);
        test.info("LoginPage initialized.");

        loginPage.login(VALID_USERNAME, VALID_PASSWORD);
        test.info("Login attempt with valid credentials.");
        
        ProfilePage profilePage = new ProfilePage(driver);
        test.info("ProfilePage initialized.");
        
        // Verify that the logout button is present
        boolean isLogoutButtonPresent = profilePage.isLogoutButtonPresent();
        test.info("Checking if logout button is present: " + isLogoutButtonPresent);
        
        Assert.assertTrue(isLogoutButtonPresent, "Logout button should be present after successful login");
        test.pass("Test passed: Logout button is present after successful login");
    }
}

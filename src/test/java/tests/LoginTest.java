package tests;

import org.testng.annotations.Test;
import org.testng.Assert;
import pages.LoginPage;
import base.BaseTest;

import static testdata.TestData.*;

public class LoginTest extends BaseTest {

    @Test
    public void invalidLoginShouldShowErrorMessage() {
        driver.get(LOGIN_URL);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(INVALID_USERNAME, INVALID_PASSWORD);

        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains(EXPECTED_ERROR_MESSAGE), "Expected an error message for invalid credentials.");
    }
}

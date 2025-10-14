package testdata;

public class TestData {
    public static final String BASE_URL = "https://demoqa.com";
    public static final String LOGIN_URL = BASE_URL + "/login";

    // Login Test Data
    public static final String INVALID_USERNAME = "wrongUser";
    public static final String INVALID_PASSWORD = "wrongPassword";
    public static final String VALID_USERNAME = "test_user";
    public static final String VALID_PASSWORD = "Testuser123!";

    public static final String EXPECTED_ERROR_MESSAGE = "Invalid";
}

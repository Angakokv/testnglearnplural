package unit;

import app.DuplicateUserException;
import app.UserManager;
import baseclasses.UnitTestBaseClass;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

@Test(dependsOnGroups = "sanity")
public class FirstTestNgClass extends UnitTestBaseClass {
    UserManager um;

    @BeforeMethod
    public void localBeforeMethodSetup(Method testMethod) {
        String desc = testMethod.getAnnotation(Test.class).description();
        System.out.println("Starting test: " + testMethod.getName() + " with description: " + desc);

        um = new UserManager();
    }

    @Parameters({"baseUrl"})
    @Step("Add user using {baseUrl} link")
    @Test(description = "Verify that addUser method returns true when successful")
    public void successfulAddUserReturnsTrue(String baseUrl) throws DuplicateUserException {
        // Arrange
        System.out.println(baseUrl);
        // Act
        boolean result = um.addUser("test@mailinator.com");

        // Assert
        Assert.assertTrue(result);
    }

    @Test(enabled = false, priority = -1, description = "Verify that getUser method retrieves the correct existing user")
    public void getUserReturnsExistingSavedUser() throws DuplicateUserException  {
        // Arrange
        um.addUser("test@mailinator.com");

        // Act
        String user = um.getUser("test@mailinator.com");

        // Assert
        Assert.assertEquals(user, "test@mailinator.com");

    }

    @Test(enabled = false, description = "Verify that getUser method returns null if the user doesn't exist")
    public void getNonExistingUserReturnsNull() {
        // Arrange

        // Act
        String user = um.getUser("test@mailinator.com");

        // Assert
        Assert.assertNull(user, "The method should return null if it doesn't find a user");
    }

    @Test(enabled = false, expectedExceptions = DuplicateUserException.class,
            expectedExceptionsMessageRegExp = ".* already .*")
    public void addDuplicateThrowsException() throws DuplicateUserException {
        // Act
        um.addUser("user@mailinator.com");
        um.addUser("user@mailinator.com");
    }

    @DataProvider
    protected static Object[][] invalidEmailProvider() {
        return new Object[][] {
                {""},
                {"tesmailinator.com"},
                {"test@mailinatorcom"}
        };
    }

    @Test(enabled = false, dataProvider = "invalidEmailProvider", expectedExceptions = IllegalArgumentException.class)
    public void emptyUserThrowsException(String invalidEmail) throws DuplicateUserException {
        // Act
        boolean result = um.addUser(invalidEmail);

        // Assert
        Assert.assertTrue(result);
    }
}

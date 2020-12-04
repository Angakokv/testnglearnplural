package unit.sanity;

import app.UserManager;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "sanity")
public class UnitSanityTests {
    public void sanityStorageGetsCreatedAndIsEmpty() {
        UserManager um = new UserManager();
        Assert.assertTrue(um.getAllUsers().isEmpty());
        System.out.println("1st sanity test");
    }

    public void sanityTest2() {
        System.out.println("2nd sanity test");
    }
}

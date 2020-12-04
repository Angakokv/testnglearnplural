package api;

import app.CommonApiDataProviders;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.apache.http.entity.ContentType.getOrDefault;
import static org.testng.Assert.assertEquals;

public class WebServiceTestSoftAssert {
    CloseableHttpClient client;
    CloseableHttpResponse response;

    @BeforeMethod(timeOut = 2000) // enough time - will pass
    public void setup() throws IOException {
        client = HttpClientBuilder.create().build();
    }

    @AfterMethod
    public void cleanup() throws IOException {
        client.close();
        response.close();
    }

    @Test
    public void testEnvVars(Method testMethod) {
        System.out.println("Starting test: " + testMethod.getName());
        String ENV_NAME = StringUtils.isEmpty(System.getProperty("envName"))
                ? System.getenv("envName")
                : System.getProperty("envName");
        System.out.println("envName = " + ENV_NAME);
    }
    @Test
    public void statusIs200(Method testMethod) throws IOException {
        System.out.println("Starting test: " + testMethod.getName());
        response = client.execute(new HttpGet("https://api.github.com"));
        assertEquals(response.getStatusLine().getStatusCode(), 200);
    }

    @Test
    public void typeIsJson(Method testMethod) throws IOException {
        System.out.println("Starting test: " + testMethod.getName());
        response = client.execute(new HttpGet("https://api.github.com"));
        assertEquals(getOrDefault(response.getEntity()).getMimeType(), "application/json");
    }

    @Test
    public void charsetIsUtf8(Method testMethod) throws IOException {
        System.out.println("Starting test: " + testMethod.getName());
        response = client.execute(new HttpGet("https://api.github.com"));
        assertEquals(getOrDefault(response.getEntity()).getCharset().toString(), "UTF-8");
    }

    @Test(timeOut = 200, enabled = false) // not enough time - will fail
    public void testIsTooSlow() throws IOException {
        // Act
        response = client.execute(new HttpGet("https://api.github.com"));

        // Assert
        assertEquals(response.getStatusLine().getStatusCode(), 200);
    }

    @Test(enabled = false)
    public void hardAssertStopsImmediately() throws IOException {
        // Arrange
        CloseableHttpClient client = HttpClientBuilder.create().build();

        // Act
        CloseableHttpResponse response = client.execute(new HttpGet("https://api.github.com"));

        // Assert
        System.out.println("First assert");
        assertEquals(response.getStatusLine().getStatusCode(), 404);

        System.out.println("Second assert");
        assertEquals(getOrDefault(response.getEntity()).getMimeType(), "application/xml");

        System.out.println("Third assert");
        assertEquals(getOrDefault(response.getEntity()).getCharset().toString(), "UTF-8");

        client.close();
        response.close();
    }

    @Test(enabled = false)
    public void softAssertContinuesToTheEnd() throws IOException {
        // Arrange
        CloseableHttpClient client = HttpClientBuilder.create().build();
        SoftAssert sa = new SoftAssert();

        // Act
        CloseableHttpResponse response = client.execute(new HttpGet("https://api.github.com"));

        // Assert
        System.out.println("First assert");
        sa.assertEquals(response.getStatusLine().getStatusCode(), 404);

        System.out.println("Second assert");
        sa.assertEquals(getOrDefault(response.getEntity()).getMimeType(), "application/xml");

        System.out.println("Third assert");
        sa.assertEquals(getOrDefault(response.getEntity()).getCharset().toString(), "UTF-8");

        client.close();
        response.close();

        sa.assertAll();
    }

    @Test(dataProvider = "endpointRequiringAuthentication", dataProviderClass = CommonApiDataProviders.class)
    public void userEndpointReturns401(String endpoint, Method testMethod) throws IOException {
        System.out.println("Starting test: " + testMethod.getName());
        // Act
        response = client.execute(new HttpGet("https://api.github.com/" + endpoint));
        int actualStatusCode = response.getStatusLine().getStatusCode();

        // Assert
        assertEquals(actualStatusCode, 401);
    }
}

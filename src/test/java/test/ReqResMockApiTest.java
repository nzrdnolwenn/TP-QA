package test;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReqResMockApiTest {

    private static Playwright playwright;
    private static Browser browser;

    @BeforeAll
    static void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );
    }

    @AfterAll
    static void teardown() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @Test
    void mockUsersList() {
        BrowserContext context = browser.newContext();
        Page page = context.newPage();

        page.route("**/api/users**", route -> {
            String body = "{ \"data\": [" +
                    "{ \"id\": 1, \"first_name\": \"Jean\", \"last_name\": \"Dupont\", \"email\": \"jean.dupont@example.com\" }," +
                    "{ \"id\": 2, \"first_name\": \"Claire\", \"last_name\": \"Martin\", \"email\": \"claire.martin@example.com\" }" +
                    "] }";

            route.fulfill(
                    new Route.FulfillOptions()
                            .setStatus(200)
                            .setContentType("application/json")
                            .setBody(body)
            );
        });

        page.navigate("https://reqres.in/");

        page.click("text=List Users");

        page.pause();

        assertTrue(page.getByText("Jean").isVisible());
        assertTrue(page.getByText("Claire").isVisible());


        page.unroute("**/api/users**");


        context.close();
    }

    @Test
    void mockSwaggerEndpoints() {
        BrowserContext context = browser.newContext();
        Page page = context.newPage();

        page.route("**/api/users**", route -> {
            String body = "{ \"data\": [" +
                    "{ \"id\": 10, \"first_name\": \"Mocky\", \"last_name\": \"McMock\", \"email\": \"mock@example.com\" }" +
                    "] }";

            route.fulfill(
                    new Route.FulfillOptions()
                            .setStatus(200)
                            .setContentType("application/json")
                            .setBody(body)
            );
        });

        page.navigate("https://reqres.in/api-docs/");

        page.click("text=/users");
        page.click("text=Try it out");
        page.click("text=Execute");
        page.pause();
        assertTrue(page.getByText("mock@example.com").isVisible());


        page.unroute("**api/users**");

        context.close();
    }
}
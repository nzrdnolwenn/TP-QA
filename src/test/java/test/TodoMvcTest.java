package test;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TodoMvcTest {

    private static Playwright playwright;
    private static Browser browser;

    @BeforeAll
    static void globalSetUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @AfterAll
    static void globalTearDown() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @Test
    void ajouterUneTacheTODO() {
        BrowserContext context = browser.newContext();
        Page page = context.newPage();

        page.navigate("https://demo.playwright.dev/todomvc");


        page.getByPlaceholder("What needs to be done?")
                .fill("Implémenter un test E2E");

        page.keyboard().press("Enter");

        boolean visible = page.getByText("Implémenter un test E2E").isVisible();
        assertTrue(visible, "La tâche devrait être visible dans la liste");

        context.close();
    }

    @Test
    void multipleTasksScenario() {
        BrowserContext context = browser.newContext();
        Page page = context.newPage();

        page.navigate("https://demo.playwright.dev/todomvc");

        Locator input = page.getByPlaceholder("What needs to be done?");

        input.fill("Acheter du pain");
        page.keyboard().press("Enter");

        input.fill("Aller courir");
        page.keyboard().press("Enter");

        Locator firstItem = page.getByText("Acheter du pain");
        firstItem.hover();
        firstItem.locator("xpath=..").locator("button.destroy").click();

        assertFalse(page.getByText("Acheter du pain").isVisible(),
                "\"Acheter du pain\" ne devrait plus être visible");
        assertTrue(page.getByText("Aller courir").isVisible(),
                "\"Aller courir\" devrait être toujours visible");

        page.pause();

        context.close();
    }
}
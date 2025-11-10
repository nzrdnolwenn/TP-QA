package test;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

public class TodoTest {

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
    void ajouterEtCompleterUneTache() {
        BrowserContext context = browser.newContext();
        Page page = context.newPage();

        TodoPage todoPage = new TodoPage(page);

        todoPage.gotoPage();
        todoPage.addTask("Acheter du café");
        todoPage.isTaskVisible("Acheter du café");

        todoPage.completeTask("Acheter du café");
        todoPage.isTaskCompleted("Acheter du café");

        context.close();
    }

    @Test
    void scenarioMultipleTaches() {
        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        TodoPage todoPage = new TodoPage(page);

        todoPage.gotoPage();

        todoPage.addTask("Acheter du pain");
        todoPage.addTask("Aller courir");

        todoPage.isTaskVisible("Acheter du pain");
        todoPage.isTaskVisible("Aller courir");

        todoPage.deleteTask("Acheter du pain");

        todoPage.isTaskInvisible("Acheter du pain");
        todoPage.isTaskVisible("Aller courir");

        context.close();
    }
}
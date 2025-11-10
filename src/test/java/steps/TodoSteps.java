package steps;

import com.microsoft.playwright.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.*;

public class TodoSteps {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @Before
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions()
                .setHeadless(false)
        );
        context = browser.newContext();
        page = context.newPage();
    }

    @After
    public void tearDown() {
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @Given("je suis sur la page TodoMVC")
    public void jeSuisSurLaPageTodoMVC() {
        page.navigate("https://demo.playwright.dev/todomvc");
    }

    @When("j’ajoute la tâche {string}")
    public void jAjouteLaTache(String task) {
        page.getByPlaceholder("What needs to be done?").fill(task);
        page.keyboard().press("Enter");
    }

    @Then("la tâche {string} est visible dans la liste")
    public void laTacheEstVisibleDansLaListe(String task) {
        assertTrue(
            page.getByText(task).isVisible(),
            "La tâche '" + task + "' devrait être visible"
        );
    }

    @When("je supprime la tâche {string}")
    public void jeSupprimeLaTache(String task) {
        Locator todoItem = page.locator(
            String.format("xpath=//label[text()=\"%s\"]/..", task)
        );
        todoItem.hover();
        todoItem.locator(".destroy").click();
    }

    @Then("la tâche {string} n’est plus visible dans la liste")
    public void laTacheNEstPlusVisibleDansLaListe(String task) {
        int count = page.getByText(task).count();
        assertEquals(
            0, count,
            "La tâche '" + task + "' ne devrait plus être visible (count == 0)"
        );
    }

    @When("je coche la tâche {string}")
    public void jeCocheLaTache(String task) {
        Locator checkbox = page.locator(
            String.format(
                "xpath=//label[text()=\"%s\"]/../input[@class='toggle']",
                task
            )
        );
        checkbox.check();
    }

    @Then("la tâche {string} apparaît comme terminée")
    public void laTacheApparaitCommeTerminee(String task) {
        Locator todoItem = page.locator(
            String.format("xpath=//label[text()=\"%s\"]/../..", task)
        );
        String classAttr = todoItem.getAttribute("class");
        assertNotNull(classAttr, "L'attribut class ne doit pas être null");
        assertTrue(
            classAttr.contains("completed"),
            "La tâche '" + task + "' devrait avoir la classe CSS 'completed'"
        );
    }
}
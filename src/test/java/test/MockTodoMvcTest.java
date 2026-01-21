package test;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MockTodoMvcTest {

    private static Playwright playwright;
    private static Browser browser;

    @BeforeAll
    static void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true)
        );
    }

    @AfterAll
    static void teardown() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @Test
    void mockLocalStorageWithExistingTasks() {
        BrowserContext context = browser.newContext();
        Page page = context.newPage();

        page.addInitScript(
                """
                (() => {
                  const mockedTodos = [
                    { title: 'Acheter du pain', completed: false },
                    { title: 'Préparer le repas', completed: true },
                    { title: 'Lire la documentation Playwright', completed: false }
                  ];
                  window.localStorage.setItem('react-todos', JSON.stringify(mockedTodos));
                })();
                """
        );

        page.navigate("https://demo.playwright.dev/todomvc");

        assertTrue(page.getByText("Acheter du pain").isVisible());
        assertTrue(page.getByText("Préparer le repas").isVisible());
        assertTrue(page.getByText("Lire la documentation Playwright").isVisible());

        context.close();
    }

    @Test
    void mockLocalStorageExercise() {
        BrowserContext context = browser.newContext();
        Page page = context.newPage();

        page.addInitScript(
                """
                (() => {
                  const todos = [
                    { title: 'Tâche 1', completed: false },
                    { title: 'Tâche 2', completed: false },
                    { title: 'Tâche 3', completed: false },
                    { title: 'Tâche 4', completed: false }
                  ];
                  todos[2].completed = true;
                  todos.shift();
                  window.localStorage.setItem('react-todos', JSON.stringify(todos));
                })();
                """
        );

        page.navigate("https://demo.playwright.dev/todomvc");


        assertFalse(page.getByText("Tâche 1").isVisible(),
                "Tâche 1 ne devrait plus être visible");

        assertTrue(page.getByText("Tâche 2").isVisible(),
                "Tâche 2 devrait être visible");
        assertTrue(page.getByText("Tâche 3").isVisible(),
                "Tâche 3 devrait être visible");
        assertTrue(page.getByText("Tâche 4").isVisible(),
                "Tâche 4 devrait être visible");

        Locator todo3 = page.locator("xpath=//label[text()='Tâche 3']/../..");
        String classAttr = todo3.getAttribute("class");
        assertNotNull(classAttr);
        assertTrue(classAttr.contains("completed"),
                "Tâche 3 devrait être complétée (classe 'completed')");

        context.close();
    }
}
package test;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static org.junit.jupiter.api.Assertions.*;

public class TodoPage {

    private final Page page;

    public TodoPage(Page page) {
        this.page = page;
    }

    public void gotoPage() {
        page.navigate("https://demo.playwright.dev/todomvc");
    }

    public void addTask(String task) {
        String todoInputSelector = "input[placeholder=\"What needs to be done?\"]";
        page.fill(todoInputSelector, task);
        page.keyboard().press("Enter");
    }

    public void deleteTask(String task) {
        Locator todoItem = page.locator(
            String.format("xpath=//label[text()=\"%s\"]/..", task)
        );
        todoItem.hover();
        todoItem.locator(".destroy").click();
    }

    public void completeTask(String task) {
        Locator toggle = page.locator(
            String.format("xpath=//label[text()=\"%s\"]/..//input[@class='toggle']", task)
        );
        toggle.check();
    }

    public void isTaskVisible(String task) {
        assertTrue(page.getByText(task).isVisible(),
                "Task '" + task + "' should be visible");
    }

    public void isTaskInvisible(String task) {
        assertFalse(page.getByText(task).isVisible(),
                "Task '" + task + "' should be invisible");
    }

    public void isTaskCompleted(String task) {
        Locator todoItem = page.locator(
            String.format("xpath=//label[text()=\"%s\"]/../..", task)
        );
        String classAttr = todoItem.getAttribute("class");
        assertTrue(classAttr != null && classAttr.contains("completed"),
                "Task '" + task + "' should be marked completed (class contains 'completed')");
    }

}
package test;

import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.*;

import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.*;

@UsePlaywright
public class TestExample {
    @Test
    void test(Page page) {
        page.navigate("https://demo.playwright.dev/todomvc/#/");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).fill("Test123");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("What needs to be done?")).press("Enter");
        assertThat(page.getByTestId("todo-title")).isVisible();
    }
}
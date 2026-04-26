package e2e;

import org.junit.Test;
import play.test.WithBrowser;

import static org.junit.Assert.assertTrue;

public class FacultyE2ETest extends WithBrowser {

    @Test
    public void happyPathUserCanOpenFacultyDirectoryAndSearch() {
        browser.goTo("http://localhost:" + port + "/faculty");

        assertTrue(browser.pageSource().contains("Faculty Directory"));

        browser.$("#search").text("Ada");
        browser.$("button[type='submit']").click();

        assertTrue(browser.pageSource().contains("Faculty Directory"));
    }

    @Test
    public void errorScenarioInvalidFacultyProfileShowsNotFoundMessage() {
        browser.goTo("http://localhost:" + port + "/faculty/999999999");

        assertTrue(
                browser.pageSource().contains("Faculty member not found")
                || browser.pageSource().contains("not found")
                || browser.pageSource().contains("Not Found")
        );
    }
}
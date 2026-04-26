package e2e;

import org.junit.Test;
import play.test.WithBrowser;

import static org.junit.Assert.assertTrue;

public class FacultyE2ETest extends WithBrowser {

    @Test
    public void happyPathUserCanOpenFacultyDirectoryAndSearch() {
        // E2E test hitting the backend API directly
        browser.goTo("http://localhost:" + port + "/faculty");
        assertTrue(browser.pageSource().contains("items"));
        assertTrue(browser.pageSource().contains("total"));

        // Simulate a search query directly via the URL since this is an API
        browser.goTo("http://localhost:" + port + "/faculty?search=Ada");
        assertTrue(browser.pageSource().contains("items"));
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
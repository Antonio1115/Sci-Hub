package controllers;

import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Result;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

public class FacultyIntegrationTest {

    private final Application app = new GuiceApplicationBuilder().build();

    @Test
    public void facultyDirectorySearchHappyPathReturnsOk() {
        Result result = route(app, fakeRequest(GET, "/faculty?search=Ada"));

        assertEquals(OK, result.status());
    }

    @Test
    public void facultyDirectoryDepartmentFilterReturnsOk() {
        Result result = route(app, fakeRequest(GET, "/faculty?department=Computer%20Science"));

        assertEquals(OK, result.status());
    }

    @Test
    public void facultyDirectoryResearchAreaFilterReturnsOk() {
        Result result = route(app, fakeRequest(GET, "/faculty?researchArea=Machine%20Learning"));

        assertEquals(OK, result.status());
    }
}
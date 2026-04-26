package controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Result;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class FacultyApiIntegrationTest {

    private Application app;

    @Before
    public void setUp() {
        app = new GuiceApplicationBuilder().build();
        Helpers.start(app);
    }

    @After
    public void tearDown() {
        Helpers.stop(app);
    }

    @Test
    public void facultyListRouteReturnsOk() {
        Result result = route(app, fakeRequest(GET, "/faculty"));

        assertEquals(OK, result.status());
    }

    @Test
    public void facultySearchRouteReturnsOk() {
        Result result = route(app, fakeRequest(GET, "/faculty?search=Ada"));

        assertEquals(OK, result.status());
    }

    @Test
    public void facultyDepartmentFilterRouteReturnsOk() {
        Result result = route(app, fakeRequest(GET, "/faculty?department=Computer%20Science"));

        assertEquals(OK, result.status());
    }

    @Test
    public void facultyResearchAreaFilterRouteReturnsOk() {
        Result result = route(app, fakeRequest(GET, "/faculty?researchArea=Machine%20Learning"));

        assertEquals(OK, result.status());
    }

    @Test
    public void facultyInvalidProfileReturnsNotFound() {
        Result result = route(app, fakeRequest(GET, "/faculty/999999999"));

        assertEquals(NOT_FOUND, result.status());
    }
}
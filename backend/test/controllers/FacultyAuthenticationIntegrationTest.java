package controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Result;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;
import play.test.Helpers;

public class FacultyAuthenticationIntegrationTest {

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
    public void publicVisitorCanAccessFacultyListWithoutLogin() {
        Result result = route(app, fakeRequest(GET, "/faculty"));

        assertEquals(OK, result.status());
    }

    @Test
    public void publicVisitorCanUseFacultyFiltersWithoutLogin() {
        Result result = route(app, fakeRequest(GET, "/faculty?search=Ada&department=Computer%20Science&researchArea=AI"));

        assertEquals(OK, result.status());
    }
}
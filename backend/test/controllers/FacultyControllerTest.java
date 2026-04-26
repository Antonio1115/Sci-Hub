package controllers;

import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.NOT_FOUND;
import static play.test.Helpers.*;

public class FacultyControllerTest {

    private Application app = new GuiceApplicationBuilder().build();

    @Test
    public void testFacultyPageLoadsSuccessfully() {
        Result result = route(app, fakeRequest(GET, "/faculty"));

        assertEquals(OK, result.status());
        String content = contentAsString(result);
        assertTrue(content.contains("items"));
        assertTrue(content.contains("total"));
    }

    @Test
    public void testFacultySearchByNameLoadsSuccessfully() {
        Result result = route(app, fakeRequest(GET, "/faculty?search=Smith"));

        assertEquals(OK, result.status());
        String content = contentAsString(result);
        assertTrue(content.contains("items"));
    }

    @Test
    public void testFacultyFilterByDepartmentLoadsSuccessfully() {
        Result result = route(app, fakeRequest(GET, "/faculty?department=Computer%20Science"));

        assertEquals(OK, result.status());
        String content = contentAsString(result);
        assertTrue(content.contains("items"));
    }

    @Test
    public void testFacultyFilterByResearchInterestLoadsSuccessfully() {
        Result result = route(app, fakeRequest(GET, "/faculty?researchArea=AI"));

        assertEquals(OK, result.status());
        String content = contentAsString(result);
        assertTrue(content.contains("items"));
    }

    @Test
    public void testFacultyProfilePageReturnsNotFoundForInvalidId() {
        Result result = route(app, fakeRequest(GET, "/faculty/999999"));

        assertEquals(NOT_FOUND, result.status());
        assertTrue(contentAsString(result).contains("Faculty member not found"));
    }
}
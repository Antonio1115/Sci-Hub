package controllers;

import play.mvc.*;
import views.html.*;

/**
 * Controller for the Faculty Directory feature.
 */
public class FacultyController extends Controller {

    /**
     * Renders the faculty directory page.
     */
    public Result facultyList() {
        return ok(facultyList.render());
    }

    /**
     * Renders the details page for a specific faculty member.
     */
    public Result facultyDetail(Long id) {
        return ok(facultyDetail.render(id));
    }
}

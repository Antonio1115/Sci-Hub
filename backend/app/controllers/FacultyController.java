package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.ResearcherInfo;
import models.User;
import models.rest.RESTResponse;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import services.FacultyService;
import utils.Common;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FacultyController extends Controller {

    private static final String DEFAULT_SORT = "lastName";

    private final FacultyService facultyService;

    @Inject
    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    /**
     * Lists all faculty members with name, department, research interests, and up to 5 recent publications.
     */
    public Result listFaculty(Optional<Integer> pageLimit, Optional<Integer> offset,
                              Optional<String> sortCriteria) {
        String sortOrder = Common.getSortCriteria(sortCriteria, DEFAULT_SORT);
        try {
            List<ResearcherInfo> facultyList = new ArrayList<>();
            for (ResearcherInfo info : ResearcherInfo.find.query().findList()) {
                User user = info.getUser();
                if (user != null && user.isResearcher()) {
                    facultyList.add(info);
                }
            }
            RESTResponse response = facultyService.paginateResults(facultyList, offset, pageLimit, sortOrder);
            return ok(response.response());
        } catch (Exception e) {
            Logger.error("FacultyController.listFaculty exception", e);
            return internalServerError("Failed to retrieve faculty list.");
        }
    }

    /**
     * Returns the detailed profile for a single faculty member, including full publication data.
     *
     * @param id the user id of the faculty member
     */
    public Result facultyDetail(Long id) {
        if (id == null) {
            return Common.badRequestWrapper("Faculty id is required.");
        }
        try {
            ResearcherInfo info = ResearcherInfo.find.query().where().eq("user_id", id).findOne();
            if (info == null || info.getUser() == null || !info.getUser().isResearcher()) {
                return notFound("Faculty member not found with id: " + id);
            }
            ObjectNode result = facultyService.toFacultyJson(info, true);
            return ok(result);
        } catch (Exception e) {
            Logger.error("FacultyController.facultyDetail exception", e);
            return notFound("Faculty member not found with id: " + id);
        }
    }
}

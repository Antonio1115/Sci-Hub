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
     * Supports optional filtering by name keyword (search), department, and research area.
     */
    public Result listFaculty(Optional<Integer> pageLimit, Optional<Integer> offset,
                              Optional<String> sortCriteria, Optional<String> search,
                              Optional<String> department, Optional<String> researchArea) {
        String sortOrder = Common.getSortCriteria(sortCriteria, DEFAULT_SORT);
        try {
            List<ResearcherInfo> facultyList = new ArrayList<>();
            for (ResearcherInfo info : ResearcherInfo.find.query().findList()) {
                User user = info.getUser();
                if (user != null && user.isResearcher() && matchesFilters(info, user, search, department, researchArea)) {
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

    private boolean matchesFilters(ResearcherInfo info, User user,
                                   Optional<String> search, Optional<String> department,
                                   Optional<String> researchArea) {
        if (search.isPresent() && !search.get().isEmpty()) {
            String q = search.get().toLowerCase();
            String fullName = ((user.getFirstName() != null ? user.getFirstName() : "") + " "
                    + (user.getLastName() != null ? user.getLastName() : "")).toLowerCase();
            String email = user.getEmail() != null ? user.getEmail().toLowerCase() : "";
            if (!fullName.contains(q) && !email.contains(q)) return false;
        }
        if (department.isPresent() && !department.get().isEmpty()) {
            String dept = info.getDepartment() != null ? info.getDepartment().toLowerCase() : "";
            if (!dept.contains(department.get().toLowerCase())) return false;
        }
        if (researchArea.isPresent() && !researchArea.get().isEmpty()) {
            String fields = info.getResearchFields() != null ? info.getResearchFields().toLowerCase() : "";
            if (!fields.contains(researchArea.get().toLowerCase())) return false;
        }
        return true;
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

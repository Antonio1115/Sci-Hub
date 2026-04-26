package controllers;

import com.fasterxml.jackson.databind.JsonNode;
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
            List<ResearcherInfo> facultyList = buildFacultyList("", "", "");
            RESTResponse response = facultyService.paginateResults(facultyList, offset, pageLimit, sortOrder);
            return ok(response.response());
        } catch (Exception e) {
            Logger.error("FacultyController.listFaculty exception", e);
            return internalServerError("Failed to retrieve faculty list.");
        }
    }

    /**
     * Searches faculty by name, department, and research area with pagination and sorting.
     */
    public Result searchFaculty(Optional<Integer> pageLimit, Optional<Integer> offset,
                                Optional<String> sortCriteria) {
        String sortOrder = Common.getSortCriteria(sortCriteria, DEFAULT_SORT);
        try {
            JsonNode body = request().body().asJson();
            String name = (body != null && body.has("name")) ? body.get("name").asText("") : "";
            String department = (body != null && body.has("department")) ? body.get("department").asText("") : "";
            String researchArea = (body != null && body.has("researchArea")) ? body.get("researchArea").asText("") : "";

            List<ResearcherInfo> facultyList = buildFacultyList(name, department, researchArea);
            RESTResponse response = facultyService.paginateResults(facultyList, offset, pageLimit, sortOrder);
            return ok(response.response());
        } catch (Exception e) {
            Logger.error("FacultyController.searchFaculty exception", e);
            return internalServerError("Failed to search faculty.");
        }
    }

    private List<ResearcherInfo> buildFacultyList(String name, String department, String researchArea) {
        List<ResearcherInfo> result = new ArrayList<>();
        for (ResearcherInfo info : ResearcherInfo.find.query().findList()) {
            User user = info.getUser();
            if (user == null || !user.isResearcher()) continue;
            if (!matchesSearch(user, info, name, department, researchArea)) continue;
            result.add(info);
        }
        return result;
    }

    private boolean matchesSearch(User user, ResearcherInfo info, String name, String dept, String area) {
        if (!name.isEmpty()) {
            String first = user.getFirstName() != null ? user.getFirstName() : "";
            String last = user.getLastName() != null ? user.getLastName() : "";
            String fullName = (first + " " + last).toLowerCase();
            if (!fullName.contains(name.toLowerCase())) return false;
        }
        if (!dept.isEmpty()) {
            String d = info.getDepartment() != null ? info.getDepartment().toLowerCase() : "";
            if (!d.contains(dept.toLowerCase())) return false;
        }
        if (!area.isEmpty()) {
            String fields = info.getResearchFields() != null ? info.getResearchFields().toLowerCase() : "";
            if (!fields.contains(area.toLowerCase())) return false;
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

package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import play.Logger;
import play.mvc.*;
import utils.RESTfulCalls;
import views.html.*;

import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class FacultyController extends Controller {

    private static final String FACULTY_LIST_API = "/faculty";
    private static final String FACULTY_DETAIL_API = "/faculty/";

    private final Config config;

    @Inject
    public FacultyController(Config config) {
        this.config = config;
    }

    public Result facultyList(Optional<String> search, Optional<String> department,
                              Optional<String> researchArea) {
        String searchVal = search.orElse("");
        String deptVal = department.orElse("");
        String researchVal = researchArea.orElse("");

        String baseUrl = RESTfulCalls.getBackendAPIUrl(config, FACULTY_LIST_API);
        Map<String, String> params = new LinkedHashMap<>();
        if (!searchVal.isEmpty()) params.put("search", searchVal);
        if (!deptVal.isEmpty()) params.put("department", deptVal);
        if (!researchVal.isEmpty()) params.put("researchArea", researchVal);

        JsonNode items = null;
        try {
            JsonNode response = params.isEmpty()
                    ? RESTfulCalls.getAPI(baseUrl)
                    : RESTfulCalls.getAPIWithParams(baseUrl, params);
            if (response != null && response.has("items")) {
                items = response.get("items");
            }
        } catch (Exception e) {
            Logger.error("FacultyController.facultyList error", e);
        }

        // Always fetch unfiltered faculty for the word cloud
        JsonNode allItems = null;
        try {
            JsonNode allResponse = RESTfulCalls.getAPI(baseUrl);
            if (allResponse != null && allResponse.has("items")) {
                allItems = allResponse.get("items");
            }
        } catch (Exception e) {
            Logger.error("FacultyController.facultyList allItems error", e);
        }

        return ok(facultyList.render(items, allItems, searchVal, deptVal, researchVal));
    }

    public Result facultyDetail(Long id) {
        JsonNode faculty = null;
        try {
            faculty = RESTfulCalls.getAPI(RESTfulCalls.getBackendAPIUrl(config, FACULTY_DETAIL_API + id));
        } catch (Exception e) {
            Logger.error("FacultyController.facultyDetail error", e);
        }
        return ok(facultyDetail.render(id, faculty));
    }
}

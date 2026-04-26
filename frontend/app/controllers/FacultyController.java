package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import play.Logger;
import play.mvc.*;
import utils.RESTfulCalls;
import views.html.*;

import javax.inject.Inject;
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

        StringBuilder url = new StringBuilder(RESTfulCalls.getBackendAPIUrl(config, FACULTY_LIST_API));
        url.append("?");
        if (!searchVal.isEmpty()) url.append("search=").append(encode(searchVal)).append("&");
        if (!deptVal.isEmpty()) url.append("department=").append(encode(deptVal)).append("&");
        if (!researchVal.isEmpty()) url.append("researchArea=").append(encode(researchVal)).append("&");

        JsonNode items = null;
        try {
            JsonNode response = RESTfulCalls.getAPI(url.toString());
            if (response != null && response.has("items")) {
                items = response.get("items");
            }
        } catch (Exception e) {
            Logger.error("FacultyController.facultyList error", e);
        }

        return ok(facultyList.render(items, searchVal, deptVal, researchVal));
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

    private String encode(String value) {
        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            return value;
        }
    }
}

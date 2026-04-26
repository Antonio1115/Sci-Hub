package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.Config;
import models.FacultyMember;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constants;
import utils.RESTfulCalls;
import views.html.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utils.Common.beginIndexForPagination;
import static utils.Common.endIndexForPagination;

public class FacultyController extends Controller {

    @Inject
    Config config;

    private static final int PAGE_LIMIT = Integer.parseInt(Constants.PAGINATION_NUMBER_ITEM_TWENTY);

    public Result facultyListPage(Integer pageNum, String sortCriteria) {
        try {
            int offset = PAGE_LIMIT * (pageNum - 1);
            JsonNode responseJson = RESTfulCalls.getAPI(RESTfulCalls.getBackendAPIUrl(config,
                    Constants.FACULTY_LIST + "?offset=" + offset + "&pageLimit=" + PAGE_LIMIT
                            + "&sortCriteria=" + sortCriteria));
            return renderFacultyList(responseJson, "all", "", "", "", pageNum, sortCriteria);
        } catch (Exception e) {
            Logger.error("FacultyController.facultyListPage exception", e);
            return ok(generalError.render());
        }
    }

    public Result facultySearchPOST(Integer pageNum, String sortCriteria) {
        try {
            Map<String, String[]> form = request().body().asFormUrlEncoded();
            String name = getFormValue(form, "name");
            String department = getFormValue(form, "department");
            String researchArea = getFormValue(form, "researchArea");

            ObjectNode searchJson = Json.newObject();
            searchJson.put("name", name);
            searchJson.put("department", department);
            searchJson.put("researchArea", researchArea);

            int offset = PAGE_LIMIT * (pageNum - 1);
            JsonNode responseJson = RESTfulCalls.postAPI(RESTfulCalls.getBackendAPIUrl(config,
                    Constants.FACULTY_SEARCH + "?offset=" + offset + "&pageLimit=" + PAGE_LIMIT
                            + "&sortCriteria=" + sortCriteria), searchJson);
            return renderFacultyList(responseJson, "search", name, department, researchArea, pageNum, sortCriteria);
        } catch (Exception e) {
            Logger.error("FacultyController.facultySearchPOST exception", e);
            return ok(generalError.render());
        }
    }

    private Result renderFacultyList(JsonNode responseJson, String listType,
                                     String searchName, String searchDept, String searchArea,
                                     int pageNum, String sortCriteria) {
        if (responseJson == null || responseJson.has("error")) {
            Logger.warn("FacultyController: empty or error response from backend");
            return renderEmptyList(listType, searchName, searchDept, searchArea, sortCriteria);
        }

        JsonNode items = responseJson.path("items");
        List<FacultyMember> faculty = new ArrayList<>();
        if (items.isArray()) {
            for (JsonNode item : items) {
                faculty.add(FacultyMember.deserialize(item));
            }
        }

        int total = responseJson.path("total").asInt(0);
        int count = responseJson.path("count").asInt(faculty.size());
        int offset = responseJson.path("offset").asInt(0);
        String retSort = responseJson.path("sort").asText(sortCriteria);
        int page = offset / PAGE_LIMIT + 1;
        int beginIdx = beginIndexForPagination(PAGE_LIMIT, total, page);
        int endIdx = endIndexForPagination(PAGE_LIMIT, total, page);

        return ok(facultyList.render(faculty, listType, page, retSort, offset, total, count,
                PAGE_LIMIT, searchName, searchDept, searchArea, beginIdx, endIdx));
    }

    private Result renderEmptyList(String listType, String name, String dept, String area, String sort) {
        return ok(facultyList.render(new ArrayList<>(), listType, 1, sort, 0, 0, 0,
                PAGE_LIMIT, name, dept, area, 1, 1));
    }

    public Result facultyDetailPage(Long facultyId) {
        return redirect(routes.AuthorController.authorDetailPage(facultyId));
    }

    private String getFormValue(Map<String, String[]> form, String key) {
        if (form == null) return "";
        String[] vals = form.get(key);
        return (vals != null && vals.length > 0) ? vals[0].trim() : "";
    }
}

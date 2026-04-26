package services;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Author;
import models.Paper;
import models.ResearcherInfo;
import models.User;
import models.rest.RESTResponse;
import play.libs.Json;
import utils.Common;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FacultyService {

    private static final int MAX_RECENT_PUBLICATIONS = 5;

    public RESTResponse paginateResults(List<ResearcherInfo> facultyList, Optional<Integer> offset,
                                        Optional<Integer> pageLimit, String sortCriteria) {
        RESTResponse response = new RESTResponse();
        int maxRows = facultyList.size();
        if (pageLimit.isPresent()) maxRows = pageLimit.get();
        int startIndex = 0;
        if (offset.isPresent()) startIndex = offset.get();

        if (!facultyList.isEmpty() && startIndex >= facultyList.size())
            startIndex = pageLimit.get() * ((facultyList.size() - 1) / pageLimit.get());

        List<ResearcherInfo> paginated = Common.paginate(startIndex, maxRows, facultyList);
        response.setTotal(facultyList.size());
        response.setSort(sortCriteria);
        response.setOffset(startIndex);
        response.setItems(toJsonArray(paginated, false));
        return response;
    }

    public ObjectNode toFacultyJson(ResearcherInfo info, boolean fullPublications) {
        User user = info.getUser();
        ObjectNode node = Json.newObject();
        node.put("id", user.getId());
        node.put("firstName", user.getFirstName() != null ? user.getFirstName() : "");
        node.put("lastName", user.getLastName() != null ? user.getLastName() : "");
        node.put("email", user.getEmail() != null ? user.getEmail() : "");
        node.put("avatar", user.getAvatar() != null ? user.getAvatar() : "");
        node.put("department", info.getDepartment() != null ? info.getDepartment() : "");
        node.put("school", info.getSchool() != null ? info.getSchool() : "");
        node.put("researchFields", info.getResearchFields() != null ? info.getResearchFields() : "");
        node.put("highestDegree", info.getHighestDegree() != null ? info.getHighestDegree() : "");
        node.put("orcid", info.getOrcid() != null ? info.getOrcid() : "");

        node.set("recentPublications", buildPublications(info, fullPublications));
        return node;
    }

    private ArrayNode buildPublications(ResearcherInfo info, boolean fullPublications) {
        ArrayNode publications = Json.newArray();
        Author author = info.getAuthor();
        if (author == null || author.getPapersByAuthor() == null) return publications;

        List<Paper> recent = author.getPapersByAuthor().stream()
                .sorted((a, b) -> {
                    String ya = a.getYear() != null ? a.getYear() : "";
                    String yb = b.getYear() != null ? b.getYear() : "";
                    return yb.compareTo(ya);
                })
                .limit(MAX_RECENT_PUBLICATIONS)
                .collect(Collectors.toList());

        for (Paper paper : recent) {
            if (fullPublications) {
                publications.add(Json.toJson(paper));
            } else {
                ObjectNode pub = Json.newObject();
                pub.put("id", paper.getId());
                pub.put("title", paper.getTitle() != null ? paper.getTitle() : "");
                pub.put("year", paper.getYear() != null ? paper.getYear() : "");
                publications.add(pub);
            }
        }
        return publications;
    }

    private ArrayNode toJsonArray(List<ResearcherInfo> list, boolean fullPublications) {
        ArrayNode array = Json.newArray();
        for (ResearcherInfo info : list) array.add(toFacultyJson(info, fullPublications));
        return array;
    }
}

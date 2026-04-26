package services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.ResearcherInfo;
import models.User;
import models.rest.RESTResponse;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class FacultyServiceTest {

    @Test
    public void toFacultyJsonIncludesBasicFacultyFields() {
        FacultyService service = new FacultyService();

        User user = new User();
        user.setId(1L);
        user.setFirstName("Ada");
        user.setLastName("Lovelace");
        user.setEmail("ada.lovelace@smu.edu");
        user.setAvatar("");

        ResearcherInfo info = new ResearcherInfo(
                user,
                "Artificial Intelligence, Machine Learning",
                "PhD",
                "0000-0001-2345-6789",
                "SMU",
                "Computer Science"
        );

        ObjectNode json = service.toFacultyJson(info, false);

        assertEquals(1L, json.get("id").asLong());
        assertEquals("Ada", json.get("firstName").asText());
        assertEquals("Lovelace", json.get("lastName").asText());
        assertEquals("ada.lovelace@smu.edu", json.get("email").asText());
        assertEquals("Computer Science", json.get("department").asText());
        assertEquals("SMU", json.get("school").asText());
        assertEquals("Artificial Intelligence, Machine Learning", json.get("researchFields").asText());
        assertEquals("PhD", json.get("highestDegree").asText());
        assertEquals("0000-0001-2345-6789", json.get("orcid").asText());
        assertTrue(json.has("recentPublications"));
    }

    @Test
    public void toFacultyJsonReplacesNullFieldsWithEmptyStrings() {
        FacultyService service = new FacultyService();

        User user = new User();
        user.setId(2L);
        user.setFirstName(null);
        user.setLastName(null);
        user.setEmail(null);
        user.setAvatar(null);

        ResearcherInfo info = new ResearcherInfo(
                user,
                null,
                null,
                null,
                null,
                null
        );

        ObjectNode json = service.toFacultyJson(info, false);

        assertEquals("", json.get("firstName").asText());
        assertEquals("", json.get("lastName").asText());
        assertEquals("", json.get("email").asText());
        assertEquals("", json.get("avatar").asText());
        assertEquals("", json.get("department").asText());
        assertEquals("", json.get("school").asText());
        assertEquals("", json.get("researchFields").asText());
        assertEquals("", json.get("highestDegree").asText());
        assertEquals("", json.get("orcid").asText());
    }

    @Test
    public void paginateResultsReturnsCorrectTotalOffsetAndSort() {
        FacultyService service = new FacultyService();

        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("Ada");
        user1.setLastName("Lovelace");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Alan");
        user2.setLastName("Turing");

        ResearcherInfo info1 = new ResearcherInfo(user1, "AI", "PhD", "", "SMU", "CS");
        ResearcherInfo info2 = new ResearcherInfo(user2, "Security", "PhD", "", "SMU", "CS");

        List<ResearcherInfo> faculty = Arrays.asList(info1, info2);

        RESTResponse response = service.paginateResults(
                faculty,
                Optional.of(0),
                Optional.of(10),
                "lastName"
        );

        assertEquals(2, response.getTotal());
        assertEquals(0, response.getOffset());
        assertEquals("lastName", response.getSort());
        assertNotNull(response.response());
    }

    @Test
    public void paginateResultsHandlesEmptyFacultyList() {
        FacultyService service = new FacultyService();

        RESTResponse response = service.paginateResults(
                Arrays.asList(),
                Optional.of(0),
                Optional.of(10),
                "lastName"
        );

        assertEquals(0, response.getTotal());
        assertEquals(0, response.getOffset());
        assertEquals("lastName", response.getSort());
    }
}
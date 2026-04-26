package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Author;
import models.ResearcherInfo;
import models.User;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Collections;

import static org.junit.Assert.*;

public class ResearcherAuthorLinkingUnitTest {

    @Test
    public void setAuthorStoresAuthorId() {
        User user = new User("Test User", "test@smu.edu");
        ResearcherInfo researcherInfo = new ResearcherInfo(user, "AI", "PhD", "", "SMU", "CS");

        Author author = new Author("Test User", "test@smu.edu");
        author.setId(42L);

        researcherInfo.setAuthor(author);

        assertEquals(Long.valueOf(42L), researcherInfo.getAuthorId());
    }

    @Test
    public void setAuthorNullClearsAuthorId() {
        User user = new User("Test User", "test@smu.edu");
        ResearcherInfo researcherInfo = new ResearcherInfo(user, "AI", "PhD", "", "SMU", "CS");

        researcherInfo.setAuthorId(77L);
        researcherInfo.setAuthor(null);

        assertNull(researcherInfo.getAuthorId());
    }

    @Test
    public void getAuthorReturnsNullWhenAuthorIdMissing() {
        User user = new User("Test User", "test@smu.edu");
        ResearcherInfo researcherInfo = new ResearcherInfo(user, "AI", "PhD", "", "SMU", "CS");

        assertNull(researcherInfo.getAuthor());
    }

    @Test
    public void buildResearcherDisplayNameUsesNameParts() throws Exception {
        User user = new User();
        user.setFirstName("Ada");
        user.setMiddleInitial("M");
        user.setLastName("Lovelace");
        user.setUserName("fallback_username");

        Method method = AuthorController.class.getDeclaredMethod(
                "buildResearcherDisplayName",
                User.class
        );
        method.setAccessible(true);

        String displayName = (String) method.invoke(null, user);

        assertEquals("Ada M Lovelace", displayName);
    }

    @Test
    public void ambiguousEntryContainsCandidateMetadata() throws Exception {
        User user = new User();
        user.setId(5L);
        user.setUserName("facultyUser");
        user.setEmail("faculty@smu.edu");

        Author candidate = new Author("Faculty User", "faculty@smu.edu");
        candidate.setId(9L);

        Method method = AuthorController.class.getDeclaredMethod(
                "createAmbiguousMatchEntry",
                User.class,
                String.class,
                String.class,
                java.util.List.class
        );
        method.setAccessible(true);

        ObjectNode entry = (ObjectNode) method.invoke(
                null,
                user,
                "email",
                "faculty@smu.edu",
                Collections.singletonList(candidate)
        );

        assertEquals(5L, entry.get("userId").asLong());
        assertEquals("facultyUser", entry.get("userName").asText());
        assertEquals("faculty@smu.edu", entry.get("email").asText());
        assertEquals("email", entry.get("matchingField").asText());
        assertEquals("faculty@smu.edu", entry.get("matchingValue").asText());
        assertTrue(entry.get("candidates").isArray());
        assertEquals(1, entry.get("candidates").size());
        assertEquals(9L, entry.get("candidates").get(0).get("authorId").asLong());
    }
}
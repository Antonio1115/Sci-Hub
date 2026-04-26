package models;

import com.fasterxml.jackson.databind.JsonNode;

public class FacultyMember {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String school;
    private String researchFields;

    public static FacultyMember deserialize(JsonNode json) {
        FacultyMember m = new FacultyMember();
        m.id = json.path("id").asLong();
        m.firstName = json.path("firstName").asText("");
        m.lastName = json.path("lastName").asText("");
        m.email = json.path("email").asText("");
        m.department = json.path("department").asText("");
        m.school = json.path("school").asText("");
        m.researchFields = json.path("researchFields").asText("");
        return m;
    }

    public long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getDepartment() { return department; }
    public String getSchool() { return school; }
    public String getResearchFields() { return researchFields; }

    public String getFullName() {
        return (firstName + " " + lastName).trim();
    }
}

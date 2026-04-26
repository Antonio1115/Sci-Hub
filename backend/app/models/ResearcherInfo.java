package models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.ebean.Finder;
import io.ebean.Model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"organizations"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "user", scope = ResearcherInfo.class)
@ToString
public class ResearcherInfo extends Model {

    private String highestDegree;

    private String orcid;

    private String researchFields;

    private String school;
    
    private String department;

    @Transient
    private Long authorId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public ResearcherInfo(User user, String researchFields, String highestDegree, String orcid, String school, String department) {
        this.user = user;
        this.researchFields = researchFields;
        this.highestDegree = highestDegree;
        this.orcid = orcid;
        this.school = school;
        this.department = department;
    }

    public Author getAuthor() {
        if (authorId == null) {
            return null;
        }
        return Author.find.byId(authorId);
    }

    public void setAuthor(Author author) {
        this.authorId = author == null ? null : author.getId();
    }

    /************************************* End of Constructors ********************************************************/

    public static Finder<Long, ResearcherInfo> find = new Finder<Long, ResearcherInfo>(ResearcherInfo.class);


    /*************************************** Utility functions ********************************************************/
}

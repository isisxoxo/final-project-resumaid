package nus.iss.edu.sg.final_project_backend_resumaid.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "resume")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resume {

    @Id
    private String id;

    private String userId;
    private String title;
    private String fullName;
    private String phone;
    private String email;
    private List<Education> education;
    private List<Work> work;
    private List<Cca> cca;
    private String additional;
    private String url;
    private Date creationTime;
    private Date lastUpdateTime;

}

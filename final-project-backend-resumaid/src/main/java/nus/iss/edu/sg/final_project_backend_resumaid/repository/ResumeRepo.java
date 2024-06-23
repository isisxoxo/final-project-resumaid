package nus.iss.edu.sg.final_project_backend_resumaid.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import nus.iss.edu.sg.final_project_backend_resumaid.model.Cca;
import nus.iss.edu.sg.final_project_backend_resumaid.model.Education;
import nus.iss.edu.sg.final_project_backend_resumaid.model.Resume;
import nus.iss.edu.sg.final_project_backend_resumaid.model.Work;

@Repository
public class ResumeRepo {

    @Autowired
    private MongoTemplate mongoTemplate;

    public String saveNewResume(String userId, String fullName, String phone, String email, List<Education> education,
            List<Work> work, List<Cca> cca, String additional, String url) {

        String id = UUID.randomUUID().toString().substring(0, 8);
        Resume resume = new Resume(id, userId, fullName, phone, email, education, work, cca, additional, url);
        Resume result = mongoTemplate.save(resume);
        return result.getId();
    }

}

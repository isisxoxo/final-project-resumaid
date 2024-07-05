package nus.iss.edu.sg.final_project_backend_resumaid.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import nus.iss.edu.sg.final_project_backend_resumaid.model.Cca;
import nus.iss.edu.sg.final_project_backend_resumaid.model.Education;
import nus.iss.edu.sg.final_project_backend_resumaid.model.Resume;
import nus.iss.edu.sg.final_project_backend_resumaid.model.Work;

@Repository
public class ResumeRepo {

    @Autowired
    private MongoTemplate mongoTemplate;

    // C
    public String saveNewResume(String title, String userId, String fullName, String phone, String email,
            List<Education> education, List<Work> work, List<Cca> cca, String additional, String url) {

        Date currTime = new Date();
        String id = UUID.randomUUID().toString().substring(0, 8);
        Resume resume = new Resume(id, userId, title, fullName, phone, email, education, work, cca, additional, url,
                currTime, currTime);
        Resume result = mongoTemplate.save(resume);
        return result.getId();
    }

    // R
    public Resume getResumeUserById(String id, String userId) {
        Criteria criteria = Criteria.where("id").is(id).and("userId").is(userId);
        Query query = Query.query(criteria);
        Resume result = mongoTemplate.find(query, Resume.class, "resume").get(0);
        return result;
    }

    public List<Resume> getAllResumeUser(String userId) {

        MatchOperation matchOperation = Aggregation.match(Criteria.where("userId").is(userId));
        SortOperation sortOperation = Aggregation.sort(Sort.by(Direction.DESC, "lastUpdateTime"));

        Aggregation pipeline = Aggregation.newAggregation(matchOperation, sortOperation);

        AggregationResults<Resume> aggregationResults = mongoTemplate.aggregate(pipeline, "resume", Resume.class);

        return aggregationResults.getMappedResults();
    }

    // U
    public Boolean updateResumeUserById(String id, String title, String userId, String fullName, String phone,
            String email, List<Education> education, List<Work> work, List<Cca> cca, String additional, String url) {

        Date currTime = new Date();

        Query query = Query.query(Criteria.where("id").is(id).and("userId").is(userId));
        Update updateOps = new Update()
                .set("title", title)
                .set("fullName", fullName)
                .set("phone", phone)
                .set("email", email)
                .set("education", education)
                .set("work", work)
                .set("cca", cca)
                .set("additional", additional)
                .set("lastUpdateTime", currTime)
                .set("url", url);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, updateOps, Resume.class, "resume");
        return updateResult.getModifiedCount() > 0 ? true : false;
    }

    // D
    public Boolean deleteResumeById(String id) {

        Query query = Query.query(Criteria.where("id").is(id));
        Resume deleted = mongoTemplate.findAndRemove(query, Resume.class, "resume");
        return deleted != null ? true : false;
    }

}

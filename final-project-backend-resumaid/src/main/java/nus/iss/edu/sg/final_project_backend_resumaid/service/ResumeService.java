package nus.iss.edu.sg.final_project_backend_resumaid.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import nus.iss.edu.sg.final_project_backend_resumaid.model.Cca;
import nus.iss.edu.sg.final_project_backend_resumaid.model.Education;
import nus.iss.edu.sg.final_project_backend_resumaid.model.Work;
import nus.iss.edu.sg.final_project_backend_resumaid.repository.ResumeRepo;
import nus.iss.edu.sg.final_project_backend_resumaid.repository.S3Repo;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepo resumeRepo;

    @Autowired
    private S3Repo s3Repo;

    public String saveNewResume(String userId, String fullName, String phone, String email, List<Education> education,
            List<Work> work, List<Cca> cca, String additional, MultipartFile file) throws IOException {

        String url = "";

        System.out.println();

        if (file != null) {
            url = s3Repo.upload(file, userId);
        }

        String id = resumeRepo.saveNewResume(userId, fullName, phone, email, education, work, cca, additional, url);
        return id;
    }
}

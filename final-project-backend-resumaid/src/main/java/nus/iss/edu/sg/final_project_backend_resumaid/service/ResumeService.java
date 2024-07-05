package nus.iss.edu.sg.final_project_backend_resumaid.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import nus.iss.edu.sg.final_project_backend_resumaid.model.Cca;
import nus.iss.edu.sg.final_project_backend_resumaid.model.Education;
import nus.iss.edu.sg.final_project_backend_resumaid.model.Resume;
import nus.iss.edu.sg.final_project_backend_resumaid.model.Work;
import nus.iss.edu.sg.final_project_backend_resumaid.repository.ResumeRepo;
import nus.iss.edu.sg.final_project_backend_resumaid.repository.S3Repo;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepo resumeRepo;

    @Autowired
    private S3Repo s3Repo;

    // C
    public String saveNewResume(String title, String userId, String fullName, String phone, String email,
            List<Education> education, List<Work> work, List<Cca> cca, String additional, MultipartFile file)
            throws IOException {

        String url = "";

        System.out.println("FILE: " + file);
        System.out.println("USERID: " + userId);

        if (file != null) {
            url = s3Repo.upload(file, userId);
        }

        String id = resumeRepo.saveNewResume(title, userId, fullName, phone, email, education, work, cca, additional,
                url);
        return id;
    }

    // R
    public Resume getResumeUserById(String id, String userId) {
        return resumeRepo.getResumeUserById(id, userId);
    }

    public List<Resume> getAllResumeUser(String userId) {
        return resumeRepo.getAllResumeUser(userId);
    }

    // U
    public Boolean updateResumeUserById(String id, String title, String userId, String fullName, String phone,
            String email, List<Education> education, List<Work> work, List<Cca> cca, String additional,
            MultipartFile file) throws IOException {

        String url = "";
        Resume resume = resumeRepo.getResumeUserById(id, userId);

        System.out.println("FILE: " + file);
        System.out.println("USERID: " + userId);

        if (file != null) {
            // If got new photo: Save new and delete old
            System.out.println("GOT NEW PHOTO");
            url = s3Repo.upload(file, userId);
            s3Repo.delete(resume.getUrl());
        } else {
            // If no new photo
            System.out.println("NO NEW PHOTO");
            url = resume.getUrl();
        }

        Boolean result = resumeRepo.updateResumeUserById(id, title, userId, fullName, phone, email, education, work,
                cca, additional, url);

        return result;
    }

    // D
    public Boolean deleteResumeById(String id, String userId) {

        Resume resume = resumeRepo.getResumeUserById(id, userId);

        System.out.println("IN DELETE RESUME SERVICE: " + resume);

        if (!resume.getUrl().equals("")) {
            s3Repo.delete(resume.getUrl()); // Delete photo from DO
        }
        return resumeRepo.deleteResumeById(id); // Delete from MongoDB
    }
}

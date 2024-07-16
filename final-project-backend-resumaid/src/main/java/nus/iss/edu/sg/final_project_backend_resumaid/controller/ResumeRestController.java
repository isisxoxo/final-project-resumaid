package nus.iss.edu.sg.final_project_backend_resumaid.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import nus.iss.edu.sg.final_project_backend_resumaid.model.Cca;
import nus.iss.edu.sg.final_project_backend_resumaid.model.Education;
import nus.iss.edu.sg.final_project_backend_resumaid.model.Resume;
import nus.iss.edu.sg.final_project_backend_resumaid.model.Work;
import nus.iss.edu.sg.final_project_backend_resumaid.service.ResumeService;
import nus.iss.edu.sg.final_project_backend_resumaid.service.UserService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(path = "/api")
public class ResumeRestController {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private UserService userService;

    // C
    @PostMapping(path = "/create/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> postNewResume(@PathVariable String userId,
            @RequestPart String title,
            @RequestPart String fullName,
            @RequestPart String phone,
            @RequestPart String email,
            @RequestPart String educationJson,
            @RequestPart String workJson,
            @RequestPart String ccaJson,
            @RequestPart(required = false) String additional,
            @RequestPart(required = false) MultipartFile mypic,
            @RequestPart(required = false) String imageUrl,
            HttpServletRequest request) throws JsonMappingException, JsonProcessingException {

        System.out.println("title " + title);
        System.out.println("fullName " + fullName);
        System.out.println("phone " + phone);
        System.out.println("email " + email);
        System.out.println("mypic " + mypic);
        System.out.println("educationJson " + educationJson);
        System.out.println("workJson " + workJson);
        System.out.println("ccaJson " + ccaJson);
        System.out.println("additional " + additional);
        System.out.println("imageUrl " + imageUrl);

        ObjectMapper objectMapper = new ObjectMapper();

        Education[] educationArray = objectMapper.readValue(educationJson, Education[].class);
        List<Education> education = Arrays.asList(educationArray);

        Work[] workArray = objectMapper.readValue(workJson, Work[].class);
        List<Work> work = Arrays.asList(workArray);

        Cca[] ccaArray = objectMapper.readValue(ccaJson, Cca[].class);
        List<Cca> cca = Arrays.asList(ccaArray);

        String jwt = userService.getJwtFromHeader(request);

        JsonObject payload;

        try {
            String id;
            if (imageUrl != null) {
                id = resumeService.saveNewResumeImage(title, userId, fullName, phone, email, education, work, cca,
                        additional, imageUrl);
            } else {
                id = resumeService.saveNewResume(title, userId, fullName, phone, email, education, work, cca,
                        additional, mypic);
            }
            payload = Json.createObjectBuilder().add("id", id).build();
            return ResponseEntity.status(201).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .body(payload.toString());
        } catch (IOException e) {
            payload = Json.createObjectBuilder().add("message", e.getMessage()).build();
            return ResponseEntity.status(404).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .body(payload.toString()); // 404
        }
    }

    // R
    @GetMapping("/view/{userId}/all")
    public ResponseEntity<List<Resume>> getAllResume(@PathVariable String userId, HttpServletRequest request) {

        String jwt = userService.getJwtFromHeader(request);

        try {
            List<Resume> resumeAll = resumeService.getAllResumeUser(userId);
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).body(resumeAll);
        } catch (Exception e) {
            return ResponseEntity.status(404).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).body(null);
        }
    }

    @GetMapping("/view/{userId}")
    public ResponseEntity<Resume> getResumeById(@PathVariable String userId, @RequestParam String id,
            HttpServletRequest request) {

        System.out.println("IN GET MAPPING");

        String jwt = userService.getJwtFromHeader(request);

        try {
            Resume resumeResult = resumeService.getResumeUserById(id, userId);
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).body(resumeResult);
        } catch (Exception e) {
            return ResponseEntity.status(404).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).body(null);
        }
    }

    // U
    @PutMapping("/create/{userId}")
    public ResponseEntity<Boolean> putResumeById(@PathVariable String userId,
            @RequestParam String id,
            @RequestPart String title,
            @RequestPart String fullName,
            @RequestPart String phone,
            @RequestPart String email,
            @RequestPart String educationJson,
            @RequestPart String workJson,
            @RequestPart String ccaJson,
            @RequestPart(required = false) String additional,
            @RequestPart(required = false) MultipartFile mypic,
            HttpServletRequest request) throws JsonMappingException,
            JsonProcessingException {

        System.out.println("title " + title);
        System.out.println("fullName " + fullName);
        System.out.println("phone " + phone);
        System.out.println("email " + email);
        System.out.println("mypic " + mypic);
        System.out.println("educationJson " + educationJson);
        System.out.println("workJson " + workJson);
        System.out.println("ccaJson " + ccaJson);
        System.out.println("additional " + additional);

        ObjectMapper objectMapper = new ObjectMapper();

        Education[] educationArray = objectMapper.readValue(educationJson,
                Education[].class);
        List<Education> education = Arrays.asList(educationArray);

        Work[] workArray = objectMapper.readValue(workJson, Work[].class);
        List<Work> work = Arrays.asList(workArray);

        Cca[] ccaArray = objectMapper.readValue(ccaJson, Cca[].class);
        List<Cca> cca = Arrays.asList(ccaArray);

        String jwt = userService.getJwtFromHeader(request);

        try {
            Boolean updateResult = resumeService.updateResumeUserById(id, title, userId,
                    fullName, phone, email, education, work, cca, additional, mypic);
            return ResponseEntity.status(201).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).body(updateResult);
        } catch (IOException e) {
            return ResponseEntity.status(404).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).body(false); // 404
        }
    }

    // D
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Boolean> deleteResumeById(@PathVariable String userId,
            @RequestParam String id,
            HttpServletRequest request) {

        String jwt = userService.getJwtFromHeader(request);

        try {
            Boolean deleteResult = resumeService.deleteResumeById(id, userId);
            System.out.println("DELETE RESULT: " + deleteResult);
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).body(deleteResult);
        } catch (Exception e) {
            return ResponseEntity.status(404).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).body(false);
        }
    }

}

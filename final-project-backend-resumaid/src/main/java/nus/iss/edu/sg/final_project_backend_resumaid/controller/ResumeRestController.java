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
import nus.iss.edu.sg.final_project_backend_resumaid.model.Work;
import nus.iss.edu.sg.final_project_backend_resumaid.service.ResumeService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping(path = "/api/create")
public class ResumeRestController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping(path = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> postNewResume(@PathVariable String userId,
            @RequestPart String fullName,
            @RequestPart String phone,
            @RequestPart String email,
            @RequestPart String educationJson,
            @RequestPart String workJson,
            @RequestPart String ccaJson,
            @RequestPart(required = false) String additional,
            @RequestPart(required = false) MultipartFile mypic,
            HttpServletRequest request) throws JsonMappingException, JsonProcessingException {

        System.out.println("fullName " + fullName);
        System.out.println("phone " + phone);
        System.out.println("email " + email);
        System.out.println("mypic " + mypic);
        System.out.println("educationJson " + educationJson);
        // [{"eName":"NUS","eCountry":"Singapore","eCert":"BAC","eFrom":"2024-06-12","eCurrent":false,
        // "eTo":"2024-06-20","ePoints":"IloveNUS\nSpecialise in BA"}]
        System.out.println("workJson " + workJson);
        // [{"wName":"Deutsche
        // Bank","wCountry":"Singapore","wRole":"Trainee","wFrom":"2024-06-06","wCurrent":true,"wPoints":"IBF"}]
        System.out.println("ccaJson " + ccaJson);
        // [{"cName":"NIL","cCountry":"Singapore","cTitle":"NIL","cFrom":"2024-06-13","cCurrent":false,"cTo":"","cPoints":"NIL"}]
        System.out.println("additional " + additional);

        ObjectMapper objectMapper = new ObjectMapper();

        Education[] educationArray = objectMapper.readValue(educationJson, Education[].class);
        List<Education> education = Arrays.asList(educationArray);

        Work[] workArray = objectMapper.readValue(workJson, Work[].class);
        List<Work> work = Arrays.asList(workArray);

        Cca[] ccaArray = objectMapper.readValue(ccaJson, Cca[].class);
        List<Cca> cca = Arrays.asList(ccaArray);

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwt = authHeader.substring(7);

        JsonObject payload;

        try {
            String id = resumeService.saveNewResume(userId, fullName, phone, email, education, work, cca, additional,
                    mypic);
            payload = Json.createObjectBuilder().add("id", id).build();
            return ResponseEntity.status(201).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .body(payload.toString());
        } catch (IOException e) {
            payload = Json.createObjectBuilder().add("message", e.getMessage()).build();
            return ResponseEntity.status(404).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .body(payload.toString()); // 404
        }
    }

}

package nus.iss.edu.sg.final_project_backend_resumaid.controller;

import java.util.Map;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import nus.iss.edu.sg.final_project_backend_resumaid.exceptions.EmailExistsException;
import nus.iss.edu.sg.final_project_backend_resumaid.model.User;
import nus.iss.edu.sg.final_project_backend_resumaid.service.EmailService;
import nus.iss.edu.sg.final_project_backend_resumaid.service.UserService;

@RestController
@RequestMapping(path = "/api")
public class LoginRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    // Registration
    @PostMapping("/register")
    public ResponseEntity<String> postRegisterNewUserAccount(@RequestBody User user) {

        try {
            Map<String, String> result = userService.registerNewUserAccount(user);

            // Send email upon registration
            emailService.sendRegistrationEmail(user.getEmail(), user.getUsername());

            return ResponseEntity.status(201)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("jwt")) // Set JWT in response header
                    .body(Json.createObjectBuilder().add("id", result.get("id")).build().toString());
        } catch (EmailExistsException e) {
            return ResponseEntity.status(400).body(
                    Json.createObjectBuilder().add("message", e.getMessage()).build().toString());
        }
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<String> postLoginAccount(@RequestBody User user) {

        try {
            Map<String, String> result = userService.getUserByEmail(user.getEmail(), user.getPassword());
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("jwt")) // Set JWT in response header
                    .body(Json.createObjectBuilder().add("id", result.get("id")).build().toString());
        } catch (LoginException e) {
            return ResponseEntity.status(400).body(
                    Json.createObjectBuilder().add("message", e.getMessage()).build().toString());
        }
    }

}

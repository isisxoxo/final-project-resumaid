package nus.iss.edu.sg.final_project_backend_resumaid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.http.HttpServletRequest;
import nus.iss.edu.sg.final_project_backend_resumaid.service.OllamaService;
import nus.iss.edu.sg.final_project_backend_resumaid.service.UserService;
import nus.iss.edu.sg.final_project_backend_resumaid.utils.Prompts;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "/api/ollama")
public class OllamaRestController implements Prompts {

    @Autowired
    private OllamaService ollamaService;

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<String> postOllamaReponse(@RequestBody(required = true) String payload,
            HttpServletRequest request) {

        System.out.println(">>>>> PAYLOAD: " + payload);

        JsonObject result = null;

        try {
            System.out.println("IN HERE");
            String prompt = IMPROVE_MESSAGE + payload;
            String response = ollamaService.chatWithOllama(prompt);
            System.out.println(">>>>> RESPONSE: " + response);

            JsonObjectBuilder bld = Json.createObjectBuilder()
                    .add("message", response);
            result = bld.build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(500)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getMessage());
        }

        System.out.println(">>>>> RESULT: " + result.toString());

        String jwt = userService.getJwtFromHeader(request);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.toString());

        // SPLIT BY \n\n

        // PAYLOAD: {"message":"Took charge of fraud defences in local markets mainly in
        // Singapore, Mexico and Thailand by creating online rule engine and offline
        // fraud defences, eventually reducing fraud loss by > 20%\nCollaborated with
        // the data science team for end-to-end development of 4 types of models
        // concurrently, established SOPs and instituted the implementation to all
        // markets for DS models to be a key fraud defence\nSpearheaded a data security
        // project, managing various stakeholders to assess data protection and external
        // data leakage risks across all operational functions under tight
        // deadlines\nUse of Presto SQL to deliver actionable insights and propose
        // data-driven business decisions to management"}

        // RESULT: {"message": "Improved Resume Points:\n\n1. Led fraud defences in
        // local markets, including Singapore, Mexico, and Thailand, by creating an
        // online rule engine and implementing offline fraud defences. Successfully
        // reduced fraud loss by over 20%.\n2. Collaborated with the data science team
        // to develop four types of models concurrently, while establishing SOPs and
        // implementing DS models as a key fraud defence across all markets.\n3.
        // Spearheaded a data security project, managing various stakeholders to assess
        // data protection and external data leakage risks for all operational functions
        // under tight deadlines. Utilized Presto SQL to deliver actionable insights and
        // propose data-driven business decisions to management."}
    }

}

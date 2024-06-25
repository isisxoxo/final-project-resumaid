package nus.iss.edu.sg.final_project_backend_resumaid.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.amithkoujalgi.ollama4j.core.OllamaAPI;
import io.github.amithkoujalgi.ollama4j.core.exceptions.OllamaBaseException;
import io.github.amithkoujalgi.ollama4j.core.models.OllamaResult;
import io.github.amithkoujalgi.ollama4j.core.types.OllamaModelType;
import io.github.amithkoujalgi.ollama4j.core.utils.OptionsBuilder;

@Service
public class OllamaService {

    @Value("${ollama.url}")
    private String host;

    @Value("${ollama.request.timeout}")
    private String timeout;

    public String chatWithOllama(String message) throws OllamaBaseException, IOException, InterruptedException {
        OllamaAPI ollamaAPI = new OllamaAPI(host);
        ollamaAPI.setVerbose(true);
        ollamaAPI.setRequestTimeoutSeconds(Long.parseLong(timeout));

        OllamaResult result = ollamaAPI.generate(OllamaModelType.LLAVA, message, new OptionsBuilder().build());
        System.out.println(result.getResponse()); // TO REMOVE
        return result.getResponse();
    }
}
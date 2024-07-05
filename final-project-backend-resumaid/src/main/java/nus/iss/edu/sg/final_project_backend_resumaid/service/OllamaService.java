package nus.iss.edu.sg.final_project_backend_resumaid.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.amithkoujalgi.ollama4j.core.OllamaAPI;
import io.github.amithkoujalgi.ollama4j.core.exceptions.OllamaBaseException;
import io.github.amithkoujalgi.ollama4j.core.models.Model;
import io.github.amithkoujalgi.ollama4j.core.models.OllamaResult;
import io.github.amithkoujalgi.ollama4j.core.types.OllamaModelType;
import io.github.amithkoujalgi.ollama4j.core.utils.OptionsBuilder;

@Service
public class OllamaService {

    @Value("${ollama.host}")
    private String host;

    @Value("${ollama.request.timeout}")
    private String timeout;

    public String chatWithOllama(String message)
            throws OllamaBaseException, IOException, InterruptedException, URISyntaxException {
        OllamaAPI ollamaAPI = new OllamaAPI(host);
        System.err.println(">>>>HOST: " + ollamaAPI);

        ollamaAPI.setVerbose(true);
        ollamaAPI.setRequestTimeoutSeconds(Long.parseLong(timeout));

        // CHECK + PULL MODEL FOR RAILWAY:
        boolean isOllamaServerReachable = ollamaAPI.ping();
        System.out.println("Is Ollama server alive: " + isOllamaServerReachable);
        // ollamaAPI.pullModel(OllamaModelType.LLAVA);
        List<Model> models = ollamaAPI.listModels();
        System.out.println("MODELS AVAILABLE: " + models);

        OllamaResult result = ollamaAPI.generate(OllamaModelType.LLAVA, message, new OptionsBuilder().build());

        System.out.println(">>>>> GET RESPONSE: " + result.getResponse()); // TO REMOVE
        return result.getResponse();
    }
}
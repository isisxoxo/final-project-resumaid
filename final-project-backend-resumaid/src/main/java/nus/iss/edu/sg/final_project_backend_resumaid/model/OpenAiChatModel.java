package nus.iss.edu.sg.final_project_backend_resumaid.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class OpenAiChatModel {

    private final String apiKey;

    public OpenAiChatModel(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String sendMessage(String message) throws IOException, URISyntaxException {
        String url = "https://api.openai.com/v1/engines/davinci/completions";

        URL obj = new URI(url).toURL();
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Set the request method and headers
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + apiKey);

        // Send post request
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = ("{\"prompt\":\"" + message + "\",\"max_tokens\":150}").getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Read response
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        return response.toString();
    }

    // Other methods for interacting with OpenAI API can be similarly implemented
}

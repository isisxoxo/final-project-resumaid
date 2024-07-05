// package nus.iss.edu.sg.final_project_backend_resumaid.config;

// import java.io.File;
// import java.io.FileInputStream;
// import java.io.InputStream;
// import java.util.Collections;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
// import com.google.api.client.http.HttpRequestInitializer;
// import com.google.api.client.http.HttpTransport;
// import com.google.api.client.json.JsonFactory;
// import com.google.api.client.json.gson.GsonFactory;
// import com.google.api.services.calendar.Calendar;
// import com.google.api.services.calendar.CalendarScopes;
// import com.google.auth.http.HttpCredentialsAdapter;
// import com.google.auth.oauth2.GoogleCredentials;

// import nus.iss.edu.sg.final_project_backend_resumaid.exceptions.GoogleCalException;

// @Configuration
// public class GoogleCalConfig {

//     @Value("${spring.application.name}")
//     private String applicationName;

//     @Value("${google.credentials.filepath}")
//     private String credentialsFilePath;

//     private JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

//     private final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

//     @Bean
//     // public Calendar buildGoogleCal() {
//     public Calendar buildGoogleCal() throws GoogleCalException {

//         GoogleCredentials credential;
//         Calendar calendar;

//         try {
//             InputStream credentialsStream = getClass().getResourceAsStream(credentialsFilePath);
//             credential = GoogleCredentials
//                     .fromStream(credentialsStream)
//                     .createScoped(SCOPES);

//             credential.refreshIfExpired();

//             System.out.println("CREDENTIAL: " + credential);

//             HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credential);
//             HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

//             calendar = new Calendar.Builder(httpTransport, jsonFactory, requestInitializer)
//                     .setApplicationName(applicationName).build();

//         } catch (Exception e) {
//             // e.printStackTrace();
//             throw new GoogleCalException("Cannot find Google Calendar");
//         }
//         return calendar;
//     }
// }


package nus.iss.edu.sg.final_project_backend_resumaid.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Configuration
public class GoogleCalConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${google.credentials.filepath}")
    private String credentialsFilePath;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

    @Bean
    public Calendar googleCalendar() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = getCredentials(HTTP_TRANSPORT);
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(applicationName)
                .build();
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {

        InputStream in = getClass().getResourceAsStream(credentialsFilePath);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + credentialsFilePath);
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}

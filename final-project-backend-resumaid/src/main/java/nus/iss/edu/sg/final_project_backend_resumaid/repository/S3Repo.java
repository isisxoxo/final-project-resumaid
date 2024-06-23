package nus.iss.edu.sg.final_project_backend_resumaid.repository;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Repository
public class S3Repo {

    @Autowired
    private AmazonS3 s3;

    // UPLOAD METHOD
    public String upload(MultipartFile file, String userId) throws IOException {

        // User metadata (String)
        Map<String, String> userData = new HashMap<>();
        userData.put("upload-timestamp", (new Date()).toString());
        userData.put("user-id", userId);
        userData.put("filename", file.getOriginalFilename());

        // Metadata for file
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        metadata.setUserMetadata(userData);

        String key = UUID.randomUUID().toString().substring(0, 8);

        // Bucket name (OceanDigital), key, input stream, metadata
        PutObjectRequest putReq = new PutObjectRequest("final-project-resumaid", key, file.getInputStream(), metadata);

        // Make the object publicly available
        putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);

        // PUT THE OBJECT
        s3.putObject(putReq);

        // Return the Digital Ocean URL of the image uploaded
        return s3.getUrl("final-project-resumaid", key).toString(); // DO key = UUID
    }

    // DELETE METHOD
    public void delete(String profileUrl) {
        String key = profileUrl.split(".com/")[1]; // PathVariable
        System.out.println(">>> KEY:" + key);
        s3.deleteObject("final-project-resumaid", key);
    }
}

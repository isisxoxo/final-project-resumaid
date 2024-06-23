package nus.iss.edu.sg.final_project_backend_resumaid.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {

    @Value("${s3.secret.key}")
    private String s3SecretKey;

    @Value("${s3.access.key}")
    private String s3AccessKey;

    @Bean
    public AmazonS3 getS3Client() {
        BasicAWSCredentials cred = new BasicAWSCredentials(s3AccessKey, s3SecretKey);
        // https://final-project-resumaid.sgp1.digitaloceanspaces.com
        EndpointConfiguration endpoint = new EndpointConfiguration("sgp1.digitaloceanspaces.com", "sgp1");
        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(cred))
                .build();
    }
}

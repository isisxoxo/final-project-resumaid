package nus.iss.edu.sg.final_project_backend_resumaid.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

@Configuration
public class StripeConfig {

    @Value("${stripe.api.key}")
    private String secretKey;

    @Bean
    public Stripe initStripe() {
        Stripe.apiKey = secretKey;
        return new Stripe() {};
    }
}

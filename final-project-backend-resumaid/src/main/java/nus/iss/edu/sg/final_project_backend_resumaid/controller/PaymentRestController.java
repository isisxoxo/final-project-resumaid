package nus.iss.edu.sg.final_project_backend_resumaid.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.servlet.http.HttpServletRequest;
import nus.iss.edu.sg.final_project_backend_resumaid.model.Payment;
import nus.iss.edu.sg.final_project_backend_resumaid.service.UserService;

@RestController
@RequestMapping(path = "/api/payment")
public class PaymentRestController {

    private static String stripeSecretKey;

    @Value("${stripe.api.key}")
    public void setStripeSecretKey(String key) {
        PaymentRestController.stripeSecretKey = key;
    }

    private static void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @Autowired
    private UserService userService;

    // create a Gson object
    private static Gson gson = new Gson();

    @PostMapping()
    public ResponseEntity<String> paymentWithCheckoutPage(@RequestBody Payment payment, HttpServletRequest request)
            throws StripeException {

        String jwt = userService.getJwtFromHeader(request);

        init();

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD) // Credit card
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(payment.getSuccessUrl())
                .setCancelUrl(payment.getCancelUrl())
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setQuantity(payment.getQuantity())
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(payment.getCurrency()).setUnitAmount(payment.getAmount())
                                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData
                                                        .builder().setName(payment.getName()).build())
                                                .build())
                                .build())
                .build();

        Session session = Session.create(params);
        Map<String, String> responseData = new HashMap<>();
        responseData.put("id", session.getId());

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).body(gson.toJson(responseData));
    }

}

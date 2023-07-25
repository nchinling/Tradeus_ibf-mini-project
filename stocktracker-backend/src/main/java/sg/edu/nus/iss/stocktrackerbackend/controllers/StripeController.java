package sg.edu.nus.iss.stocktrackerbackend.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import sg.edu.nus.iss.stocktrackerbackend.models.CheckoutPayment;

@RestController
@RequestMapping(value = "/api")
// @CrossOrigin(origins = "*")
public class StripeController {

@Value("${stripe.secret.key}")
private String stripeSecretKey;

@Value("${app.success-url}")
private String successUrl;

@Value("${app.cancel-url}")
private String cancelUrl;




  @PostMapping("/payment")
  public ResponseEntity<String> paymentWithCheckoutPage(@RequestBody CheckoutPayment payment) throws StripeException {
    
    System.out.println(">>>>>I am inside payment");
    
    Stripe.apiKey = stripeSecretKey;

    // String successUrl = "http://www.google.com/";
    // String cancelUrl = "http://www.ngchinling.com/";
    // String successUrl = "http://localhost:8080/success.html";
    // String cancelUrl = "http://localhost:8080/cancel.html";

    System.out.println(">>>the successUrl is>>>" + successUrl);
    
    System.out.println(">>>the cancelUrl is>>>" + cancelUrl);
    
    SessionCreateParams params = SessionCreateParams.builder()
        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
        .setMode(SessionCreateParams.Mode.PAYMENT)
       
        .setSuccessUrl(successUrl)
        .setCancelUrl(cancelUrl)
        .addLineItem(SessionCreateParams.LineItem.builder()
            .setQuantity(payment.getQuantity())
            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(payment.getCurrency())
                .setUnitAmount(payment.getAmount())
                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                    .setName(payment.getName())
                    .build())
                .build())
            .build())
        .build();

    // Create the Stripe session
    Session session = Session.create(params);

    System.out.println(">>>>I am returning the key>>>>" + session.getId());
    
    // Return the session ID as the response
      return ResponseEntity.ok().body(session.getId());
  }


 @GetMapping("/checkout/{sessionId}")
  public ResponseEntity<String> redirectToCheckout(@PathVariable String sessionId) {

  
    System.out.println(">>>the sessionId in checkout is >>>>" + sessionId);
    System.out.println(">>>I am inside checkout/sessionId");
    try {
      // Retrieve the session using the session ID
      Session session = Session.retrieve(sessionId);
      
      // Get the checkout URL from the session
      String checkoutUrl = session.getUrl();
      System.out.println("The checkout Url is >>>>>" + checkoutUrl);

      return ResponseEntity.ok().body(checkoutUrl);
      
   
    } catch (StripeException e) {
      // Handle errors 
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error retrieving session");
    }
  }


}


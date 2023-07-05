package sg.edu.nus.iss.stocktrackerbackend.controllers;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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

  @PostMapping("/payment")
  public ResponseEntity<String> paymentWithCheckoutPage(@RequestBody CheckoutPayment payment) throws StripeException {
    // Initialize Stripe and create session parameters
    System.out.println(">>>>>I am inside payment");
    
    Stripe.apiKey = stripeSecretKey;
    
    SessionCreateParams params = SessionCreateParams.builder()
        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setSuccessUrl(payment.getSuccessUrl())
        .setCancelUrl(payment.getCancelUrl())
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

    // Stripe.apiKey = stripeSecretKey;

    System.out.println(">>>the sessionId in checkout is >>>>" + sessionId);
    System.out.println(">>>I am inside checkout/sessionId");
    try {
      // Retrieve the session using the session ID
      Session session = Session.retrieve(sessionId);
      
      // Get the checkout URL from the session
      String checkoutUrl = session.getUrl();
      System.out.println("The checkout Url is >>>>>" + checkoutUrl);
      
      // Redirect to the checkout URL
      return ResponseEntity.status(HttpStatus.FOUND)
          .header(HttpHeaders.LOCATION, checkoutUrl)
          .build();
    } catch (StripeException e) {
      // Handle any errors that occur during the retrieval of the session
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error retrieving session");
    }
  }
    

// @GetMapping("/stripe-success")
// public String stripeSuccess() {
//     System.out.println(">>I am inside /stripe-success");

//     return "Your payment was successful. Thank you."; 
// }

@GetMapping("/stripe-success")
public String stripeSuccess() {
    System.out.println(">> I am inside /stripe-success");

    return "<p>Your payment was successful. Thank you.</p><p>You may close this page.</p>";
}


@GetMapping("/stripe-cancelled")
public String stripeCancelled() {
    System.out.println(">>I am inside /stripe-cancelled");

    return "<p>You have cancelled the transaction.</p><p>You may close this page</p>";
}



}


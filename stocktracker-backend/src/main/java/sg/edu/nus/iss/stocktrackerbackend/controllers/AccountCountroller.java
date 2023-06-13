package sg.edu.nus.iss.stocktrackerbackend.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.stocktrackerbackend.models.Account;
import sg.edu.nus.iss.stocktrackerbackend.models.Stock;
import sg.edu.nus.iss.stocktrackerbackend.services.AccountException;
import sg.edu.nus.iss.stocktrackerbackend.services.AccountService;
import sg.edu.nus.iss.stocktrackerbackend.services.StockService;

@Controller
@RequestMapping(path="/api")
@CrossOrigin(origins="*")
public class AccountCountroller {
    
    @Autowired
    private AccountService accSvc;

    @Autowired
    private StockService stockSvc;

	@PostMapping(path="/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	public ResponseEntity<String> login(@RequestBody MultiValueMap<String, String> form) {

        String username = form.getFirst("username");
        String password = form.getFirst("password");

        System.out.printf(">>> I am inside Controller Login >>>>>\n");
        JsonObject resp = null;

            Account loggedInAccount;
            try {
                loggedInAccount = accSvc.loginAccount(username, password);
                resp = Json.createObjectBuilder()
                .add("account_id", loggedInAccount.getAccountId())
                .add("username", loggedInAccount.getUsername())
                .add("timestamp", (new Date()).toString())
                .build();
            } catch (AccountNotFoundException | IOException e) {
                String errorMessage = e.getMessage();
                System.out.printf(">>>Account Exception occured>>>>>\n");   
                resp = Json.createObjectBuilder()
                .add("error", errorMessage)
                .build();

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(resp.toString());
            } 

        System.out.printf(">>>Successfully logged in>>>>>\n");   

        return ResponseEntity.ok(resp.toString());

    }


    @PostMapping(path="/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	public ResponseEntity<String> register(@RequestBody MultiValueMap<String, String> form) {

        System.out.printf(">>> I am inside Controller Register>>>>>\n");

        String name = form.getFirst("name");
        String username = form.getFirst("username");
        String password = form.getFirst("password");
        String mobileNo = form.getFirst("mobile_no");
        String nationality = form.getFirst("nationality");
        String address = form.getFirst("address");
        String dob = form.getFirst("date_of_birth");


        String pattern = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate dateOfBirth = LocalDate.parse(dob, formatter);
        System.out.println(dateOfBirth);

        Account account = new Account(name, username, mobileNo, password, nationality,address,dateOfBirth);
 
        JsonObject resp = null;

        try {
            Account registeredAccount = accSvc.createAccount(account);
                resp = Json.createObjectBuilder()
                .add("account_id", registeredAccount.getAccountId())
                .add("username", registeredAccount.getUsername())
                .add("timestamp", (new Date()).toString())
                .add("status", "registered")
                .build();

         System.out.printf(">>>Successfully registered>>>>>\n");   

        } catch (AccountException e) {
            String errorMessage = e.getMessage();
            //  String errorMessage = "Email has been taken";
             System.out.printf(">>>Account Exception occured>>>>>\n");   
            resp = Json.createObjectBuilder()
            .add("error", errorMessage)
            .build();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(resp.toString());
        }

        return ResponseEntity.ok(resp.toString());

    }


}

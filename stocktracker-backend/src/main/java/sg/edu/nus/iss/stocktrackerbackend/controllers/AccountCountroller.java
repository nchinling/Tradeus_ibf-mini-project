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
import org.springframework.web.bind.annotation.GetMapping;
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


@Controller
@RequestMapping(path="/api")
@CrossOrigin(origins="*")
public class AccountCountroller {
    
    @Autowired
    private AccountService accSvc;

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

        String accountId = form.getFirst("account_id");
        String name = form.getFirst("name");
        String username = form.getFirst("username");
        String password = form.getFirst("password");
        String mobileNo = form.getFirst("mobile_no");
        String nationality = form.getFirst("nationality");
        String address = form.getFirst("address");
        String dob = form.getFirst("date_of_birth");

         System.out.println(">>> The accountId for update is >>>>>" + accountId);
        System.out.println(">>> The username for update is >>>>>" + username);
        System.out.println(">>> The password for update is >>>>>" + password);
        System.out.println(">>> The date for update is >>>>>" + dob);


        String pattern = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate dateOfBirth = LocalDate.parse(dob, formatter);
        System.out.println(dateOfBirth);

        //For updating account
       if (!accountId.isEmpty()) {
            Account account = new Account(accountId, name, username, mobileNo, password, nationality,address,dateOfBirth);
            JsonObject resp = null;

      
            Account registeredAccount;
            try {
                registeredAccount = accSvc.updateAccount(account);
                resp = Json.createObjectBuilder()
                .add("account_id", registeredAccount.getAccountId())
                .add("username", registeredAccount.getUsername())
                .add("timestamp", (new Date()).toString())
                .add("status", "updated")
                .build();

            System.out.printf(">>>Successfully updated>>>>>\n");   
            return ResponseEntity.ok(resp.toString());

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
        }

        

        // For creation of new account
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


    @GetMapping(path="/getuser")
    @ResponseBody
    public ResponseEntity<String> getUserData(@RequestParam(required=true) String username) throws IOException{
        // Integer num = weatherSvc.getWeather(city);
        System.out.println("The getuser username is" + username);
        


        JsonObject resp = null;

        try {
            Account retrievedAccount = accSvc.retrieveAccount(username);
            System.out.println("The getuser name is " + retrievedAccount.getName());
             System.out.println("The getuser password is " + retrievedAccount.getPassword());
              System.out.println("The getuser username is " + retrievedAccount.getUsername());
                resp = Json.createObjectBuilder()
                .add("name", retrievedAccount.getName())
                .add("username", retrievedAccount.getUsername())
                .add("mobile_no", retrievedAccount.getMobileNo())
                .add("password", retrievedAccount.getPassword())
                .add("nationality", retrievedAccount.getNationality())
                .add("address", retrievedAccount.getAddress())
                .add("account_id", retrievedAccount.getAccountId())
                .add("date_of_birth", retrievedAccount.getDateOfBirth().toString())
                .build();

         System.out.printf(">>>Successfully retrieved user account>>>>>\n");   

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

package sg.edu.nus.iss.stocktrackerbackend.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.stocktrackerbackend.models.Account;

import sg.edu.nus.iss.stocktrackerbackend.models.Trade;
import sg.edu.nus.iss.stocktrackerbackend.models.Watchlist;
import sg.edu.nus.iss.stocktrackerbackend.services.AccountException;
import sg.edu.nus.iss.stocktrackerbackend.services.AccountService;
import sg.edu.nus.iss.stocktrackerbackend.services.EmailService;
import sg.edu.nus.iss.stocktrackerbackend.services.WebSocketService;


@Controller
@RequestMapping(path="/api")
@CrossOrigin(origins="*")
public class AccountController {
    
    @Autowired
    private AccountService accSvc;

    @Autowired
    private WebSocketService webSocketSvc;

    @Autowired
    private EmailService emailSvc;

    @Value("${twelve.data.websocket.key}")
    private String twelveDataWebSocketApiKey;

	@PostMapping(path="/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	public ResponseEntity<String> login(@RequestBody MultiValueMap<String, String> form) throws Exception {

        String username = form.getFirst("username");
        String password = form.getFirst("password");

        System.out.printf(">>> I am inside Controller Login >>>>>\n");
        System.out.printf(">>> The key is >>>>>\n" + twelveDataWebSocketApiKey);
        JsonObject resp = null;

        // webSocketSvc.getWebSocketData();

            Account loggedInAccount;
            try {
                loggedInAccount = accSvc.loginAccount(username, password);
                resp = Json.createObjectBuilder()
                .add("account_id", loggedInAccount.getAccountId())
                .add("username", loggedInAccount.getUsername())
                .add("key", twelveDataWebSocketApiKey)
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

        String to = "nchinling@gmail.com";
        String subject = "Tradeus: Login on " + new Date().toString();
        String body = "Hi " + loggedInAccount.getName() + ", You have logged in to Tradeus on " +new Date().toString() +". Please contact us immediately if it is not made by you.";
       
        emailSvc.sendEmail(to, subject, body);

        return ResponseEntity.ok(resp.toString());

    }

    
    // @PostMapping(path="/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    // @RequestMapping(path = "/register", method = {RequestMethod.POST, RequestMethod.PUT}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
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

    
    @PutMapping(path="/update", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
	public ResponseEntity<String> update(@RequestParam MultiValueMap<String, String> form) {

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

     

	@PostMapping(path="/savetoportfolio", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
	public ResponseEntity<String> saveToPortfolio(@RequestBody MultiValueMap<String, String> form) {

        System.out.printf(">>> I am inside saveToPortfolio>>>>>\n");

        String accountId = form.getFirst("account_id");
        String username = form.getFirst("username");
        String exchange = form.getFirst("exchange");
        String stockName = form.getFirst("stockName");
        String symbol = form.getFirst("symbol");
        String currency = form.getFirst("currency");
        Double units = Double.parseDouble(form.getFirst("units"));
        Double price = Double.parseDouble(form.getFirst("price"));
        Double fee = Double.parseDouble(form.getFirst("fee"));
        String date = form.getFirst("date");

        System.out.println(">>> The accountId for update is >>>>>" + accountId);
        System.out.println(">>> The username for update is >>>>>" + username);
        System.out.println(">>> The price for update is >>>>>" + price);
        System.out.println(">>> The date for update is >>>>>" + date);


        String pattern = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate loggedDate = LocalDate.parse(date, formatter);
        System.out.println(">>>>>>>>The loggedDate is >>>>>>> " + loggedDate);

        // For creation of new trade
        Trade trade = new Trade(accountId, username, exchange, stockName, symbol, units, price, currency, fee, loggedDate);
 
        //         this.accountId = accountId;
        // this.username = username;
        // this.exchange = exchange;
        // this.stockName = stockName;
        // this.symbol = symbol;
        // this.units = units;
        // this.price = price;
        // this.currency = currency;
        // this.fee = fee;
        // this.date = date;
        // this.total = total;
        JsonObject resp = null;

        try {
            Trade savedTrade = accSvc.saveToPortfolio(trade);
                resp = Json.createObjectBuilder()
                .add("account_id", savedTrade.getAccountId())
                .add("username", savedTrade.getUsername())
                .add("exchange", savedTrade.getExchange())
                .add("stockName", savedTrade.getStockName())
                .add("symbol", savedTrade.getSymbol())
                .add("units", savedTrade.getUnits())
                .add("price", savedTrade.getPrice())
                .add("fee", savedTrade.getFee())
                .add("date", savedTrade.getDate().toString())
                .build();

         System.out.printf(">>>Successfully saved to portfolio>>>>>\n");   

        } catch (AccountException e) {
            String errorMessage = e.getMessage();
            //  String errorMessage = "Email has been taken";
             System.out.printf(">>>Portfolio Exception occured>>>>>\n");   
            resp = Json.createObjectBuilder()
            .add("error", errorMessage)
            .build();
            
     
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(resp.toString());
        }

        System.out.printf(">>>Sending back to portfolio client>>>>>\n");   
        return ResponseEntity.ok(resp.toString());

    }


    
    @GetMapping(path="/portfolio", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getPortfolioList(@RequestParam(required=true) String accountId) throws IOException{
     
        System.out.println("I am in getPortfolio server");
        System.out.println(">>>>>>>>accountId in controller>>>>>" + accountId);
       
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		List<String> userPortfolioList = accSvc.getPortfolioList(accountId);
		userPortfolioList.stream()
			.map(each -> Json.createObjectBuilder()
						.add("symbol", each)

						.build()
			)
			.forEach(json -> arrBuilder.add(json));

		return ResponseEntity.ok(arrBuilder.build().toString());
       
    }


    @DeleteMapping(path="/portfolioList", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    // public ResponseEntity<String> saveWatchlist(@RequestBody String[] symbols) {
        public ResponseEntity<String> deleteFromPortfolio(@RequestParam("symbol") String symbol, 
                                                        @RequestParam("accountId") String accountId) {

        System.out.println(">>>>The symbol received is>>>" + symbol);
        System.out.println(">>>>The accountId received is>>>" + accountId);
        String deletedSymbol = accSvc.deleteFromPortfolio(symbol, accountId);
        JsonObject resp = Json.createObjectBuilder()
                .add("symbol", deletedSymbol)
                .build();
    
            return ResponseEntity.ok(resp.toString());

	}


    @DeleteMapping(path="/tradesList", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    // public ResponseEntity<String> saveWatchlist(@RequestBody String[] symbols) {
        public ResponseEntity<String> deleteFromAnnualisedPortfolio(@RequestParam("symbol") String symbol, 
            @RequestParam("date") String stringDate,@RequestParam("accountId") String accountId) {

        
        System.out.println(">>>>The symbol received is>>>" + symbol);
        System.out.println(">>>>The accountId received is>>>" + accountId);
        System.out.println(">>>>The date received is>>>" + stringDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        JsonObject resp= null;
        try {
            date = dateFormat.parse(stringDate);
            String deletedSymbol = accSvc.deleteFromTrades(symbol, date, accountId);
            resp = Json.createObjectBuilder()
                .add("symbol", deletedSymbol)
                .build();
    
            return ResponseEntity.ok(resp.toString());
        } catch (ParseException e) {
           
            e.printStackTrace();
        }

            // Handle the case when an exception occurs
        resp = Json.createObjectBuilder()
            .add("error", "An error occurred")
            .build();

        return ResponseEntity.ok(resp.toString());

	}



}

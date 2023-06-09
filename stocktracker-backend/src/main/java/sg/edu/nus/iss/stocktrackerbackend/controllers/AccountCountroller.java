package sg.edu.nus.iss.stocktrackerbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.stocktrackerbackend.services.AccountService;

@Controller
@RequestMapping(path="/")
@CrossOrigin(origins="*")
public class AccountCountroller {
    
    @Autowired
    private AccountService accSvc;

	@PostMapping(path="/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	public ResponseEntity<String> register(@RequestBody MultiValueMap<String, String> form) {

    System.out.printf(">>> I am inside register >>>>>");
    JsonObject resp = null;
      
		// System.out.printf(">>> title: %s\n", title);
        // System.out.printf(">>> comments: %s\n", comments);


       
            resp = Json.createObjectBuilder()
            .add("postId", 1)
            .add("title", "registration")
            .add("status", "registered")
            .build();

             //save initial vote count
            // LikesResponse firstlike = new LikesResponse(postId, "0", "0");
            // postSvc.save(firstlike);
            
            // return ResponseEntity.ok(resp.toString());
    
        return ResponseEntity.ok(resp.toString());

	}


}

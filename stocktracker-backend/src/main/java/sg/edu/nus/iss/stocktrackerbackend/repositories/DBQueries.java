package sg.edu.nus.iss.stocktrackerbackend.repositories;

public class DBQueries {
      public static final String INSERT_REGISTRATION = """
        
      insert into accounts(account_id, name, username, password, 
                            address, mobile_no, nationality, date_of_birth)
                        values (?, ?, ?, ? ,?, ?,?,?);

    """;

     public static final String SELECT_ACCOUNT_BY_USERNAME ="select * from accounts where username = ?";
            
}

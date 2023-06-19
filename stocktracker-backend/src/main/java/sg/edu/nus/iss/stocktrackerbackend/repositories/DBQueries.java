package sg.edu.nus.iss.stocktrackerbackend.repositories;

public class DBQueries {
      public static final String INSERT_REGISTRATION = """
        
      insert into accounts(account_id, name, username, password, 
                            address, mobile_no, nationality, date_of_birth)
                        values (?, ?, ?, ? ,?, ?,?,?);

    """;

    public static final String UPDATE_ACCOUNT = """
        
      update accounts
        set name = ?, username = ?, password = ?, address = ?, 
        mobile_no = ?, nationality = ?, date_of_birth = ?
        where account_id = ?

    """;

     public static final String SELECT_ACCOUNT_BY_USERNAME ="select * from accounts where username = ?";
    
     public static final String CHECK_ACCOUNTID_EXISTS = "SELECT COUNT(*) FROM accounts WHERE account_id = ?";
}

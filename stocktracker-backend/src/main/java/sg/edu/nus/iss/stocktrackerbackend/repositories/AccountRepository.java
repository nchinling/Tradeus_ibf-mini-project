package sg.edu.nus.iss.stocktrackerbackend.repositories;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.stocktrackerbackend.models.Account;
import static sg.edu.nus.iss.stocktrackerbackend.repositories.DBQueries.*;

import java.util.List;
import java.util.Optional;

@Repository
public class AccountRepository {
    
    @Autowired 
    JdbcTemplate jdbcTemplate;

    //insert into order table
    public boolean createAccount(Account account){

        return jdbcTemplate.update(INSERT_REGISTRATION, account.getAccountId(), account.getName(), 
                                account.getUsername(), account.getPassword(), account.getAddress(),
                                account.getMobileNo(), account.getNationality(), account.getDateOfBirth()) > 0;

    }

    public Optional<Account> getAccountByUsername(String username){
        List<Account> accounts = jdbcTemplate.query(SELECT_ACCOUNT_BY_USERNAME, 
        new AccountRowMapper() , new Object[]{username});
        
        if (!accounts.isEmpty()) {
            return Optional.of(accounts.get(0));
        } else {
            return Optional.empty();
        }
        
    }

    
}

package sg.edu.nus.iss.stocktrackerbackend.repositories;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sg.edu.nus.iss.stocktrackerbackend.models.Account;

public class AccountRowMapper implements RowMapper<Account> {
    
    @Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {

        Account account = new Account();

        account.setAccountId(rs.getString("account_id"));
        account.setName(rs.getString("name"));
        account.setUsername(rs.getString("username"));
        account.setPassword(rs.getString("password"));
        account.setMobileNo(rs.getString("mobile_no"));
        account.setNationality(rs.getString("nationality"));
        Date dateOfBirth = rs.getDate("date_of_birth");
        account.setDateOfBirth(dateOfBirth.toLocalDate());
        account.setAddress(rs.getString("address"));

        return account;

    }
}

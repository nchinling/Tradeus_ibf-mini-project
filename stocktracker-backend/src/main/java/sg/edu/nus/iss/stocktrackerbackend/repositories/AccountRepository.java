package sg.edu.nus.iss.stocktrackerbackend.repositories;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.nus.iss.stocktrackerbackend.models.Account;
import sg.edu.nus.iss.stocktrackerbackend.models.Trade;
import sg.edu.nus.iss.stocktrackerbackend.services.AccountException;

import static sg.edu.nus.iss.stocktrackerbackend.repositories.DBQueries.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AccountRepository {
    
    @Autowired 
    JdbcTemplate jdbcTemplate;

    //insert into accounts table
    public boolean createAccount(Account account){

        // Update if account already exist
        System.out.println(">>>>>>>>AccountId is>>>>>>" + account.getAccountId());
        if (isAccountIdExists(account.getAccountId())) {
        return jdbcTemplate.update(UPDATE_ACCOUNT, account.getName(), 
                                account.getPassword(), account.getAddress(),
                                account.getMobileNo(), account.getNationality(), account.getDateOfBirth(), account.getAccountId()) > 0;

        
        }

        // Else create new account
        return jdbcTemplate.update(INSERT_REGISTRATION, account.getAccountId(), account.getName(), 
                                account.getUsername(), account.getPassword(), account.getAddress(),
                                account.getMobileNo(), account.getNationality(), account.getDateOfBirth()) > 0;

    }

    private boolean isAccountIdExists(String accountId) {
    
    int count = jdbcTemplate.queryForObject(CHECK_ACCOUNTID_EXISTS, Integer.class, accountId);
    return count > 0; 

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

    public Optional<Trade> getTradeData(String accountId, String symbol){
        List<Trade> trades = jdbcTemplate.query(SELECT_TRADE_BY_ACCOUNTID_AND_SYMBOL, 
        new TradeRowMapper() , new Object[]{accountId, symbol});
        
        if (!trades.isEmpty()) {
            return Optional.of(trades.get(0));
        } else {
            return Optional.empty();
        }
        
    }


    //insert into portfolio and trades table
    @Transactional(rollbackFor = AccountException.class)
    public Trade saveToPortfolio(Trade trade){

        // No need to insert new symbol into portfolio if symbol already exist
        System.out.println(">>>>>>>>trade.getDatenew is>>>>>>" + trade.getDate());
        
     
        jdbcTemplate.update(INSERT_INTO_PORTFOLIO, trade.getAccountId(), trade.getSymbol());

        // String articleId = UUID.randomUUID().toString().substring(0, 8);
        String portfolioId = jdbcTemplate.queryForObject(SELECT_PORTFOLIO_ID, String.class, trade.getAccountId(), trade.getSymbol());
        //Insert into trades
        jdbcTemplate.update(INSERT_TRADE, portfolioId, trade.getAccountId(), trade.getUsername(), 
                                trade.getExchange(), trade.getSymbol(), trade.getStockName(),
                                trade.getUnits(), trade.getDate(), trade.getPrice(), trade.getCurrency(),trade.getFee(), trade.getTotal()); 

        return trade;
    }


    public List<String> getPortfolioList(String accountId) {
    List<String> portfolioSymbols=jdbcTemplate.queryForList(SELECT_SYMBOLS_BY_ACCOUNTID,String.class, accountId);
    
    return portfolioSymbols;
    }


    public String deleteFromPortfolio(String symbol, String accountId) {
        jdbcTemplate.update(DELETE_SYMBOL_BY_ACCOUNTID, symbol, accountId);
    
        return symbol;
    }

  

}

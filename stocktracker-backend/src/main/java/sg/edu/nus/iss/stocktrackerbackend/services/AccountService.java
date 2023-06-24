package sg.edu.nus.iss.stocktrackerbackend.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.nus.iss.stocktrackerbackend.models.Account;
import sg.edu.nus.iss.stocktrackerbackend.models.Trade;
import sg.edu.nus.iss.stocktrackerbackend.repositories.AccountRepository;

@Service
public class AccountService {
    
    
    @Autowired
    private AccountRepository accountRepo;
    

        @Transactional(rollbackFor = AccountException.class)
        public Account createAccount(Account account) throws AccountException {
            try {
        
                //create Account id. 
                String accountId = UUID.randomUUID().toString().substring(0, 8);

                account.setAccountId(accountId);
                accountRepo.createAccount(account);

                return account;
            } catch (DataIntegrityViolationException ex) {
                String errorMessage = "Email has been taken.";
                throw new AccountException(errorMessage);
            }
        }

        public Account updateAccount(Account account) throws AccountException {
            try {

                System.out.println("xxxxxxxxThe accountId in updateAccount is:xxxxx" + account.getAccountId());
        
         
                accountRepo.createAccount(account);

                return account;
            } catch (DataIntegrityViolationException ex) {
                String errorMessage = "Email has been taken.";
                throw new AccountException(errorMessage);
            }
        }

        
        @Transactional(rollbackFor = AccountException.class)
        public Account retrieveAccount(String username) throws AccountException {
            // try {
        
                Optional<Account> retrievedAccount = accountRepo.getAccountByUsername(username);

                return retrievedAccount.get();

            // } catch (DataIntegrityViolationException ex) {
            //     String errorMessage = "Error in retrieving account.";
            //     throw new AccountException(errorMessage);
            // }
        }


        public Account loginAccount(String username, String password) throws IOException, AccountNotFoundException {
            Optional<Account> optAccount = accountRepo.getAccountByUsername(username);

            if (optAccount.isPresent()) {
                Account loggedInAccount = optAccount.get();
                System.out.printf(">>>String Password is >>>" + password);
                System.out.printf(">>>loggedInAccountPassword is >>>" + loggedInAccount.getPassword());   
                if (loggedInAccount.getPassword().equals(password)){
                    return loggedInAccount;
                }
                else{
                    throw new AccountNotFoundException("Password is incorrect");
                }

            } else {
                throw new AccountNotFoundException("Account not found for username: " + username);
            }
        }


        @Transactional(rollbackFor = AccountException.class)
        public Trade saveToPortfolio(Trade trade) throws AccountException {
            try {

                Double total = trade.getUnits()*trade.getPrice() + trade.getFee();
                trade.setTotal(total);
        
                return accountRepo.saveToPortfolio(trade);

            } catch (DataIntegrityViolationException ex) {
                String errorMessage = "An error occurred while saving. Please try again.";
                throw new AccountException(errorMessage);
            }
        }


    public List<String> getPortfolioList(String accountId) {
        System.out.println(">>>>>>>> I am in Service >>> getUserPortfolioList");
        return accountRepo.getPortfolioList(accountId);
    }


    public String deleteFromPortfolio(String symbol, String accountId) {
        System.out.println(">>>>>>>> I am in Service >>> deleteFromPortfolio");
        return accountRepo.deleteFromPortfolio(symbol, accountId);
    }



    
}









    


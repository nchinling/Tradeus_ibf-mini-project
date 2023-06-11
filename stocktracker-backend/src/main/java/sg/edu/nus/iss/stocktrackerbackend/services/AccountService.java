package sg.edu.nus.iss.stocktrackerbackend.services;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.nus.iss.stocktrackerbackend.models.Account;
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


    
}









    


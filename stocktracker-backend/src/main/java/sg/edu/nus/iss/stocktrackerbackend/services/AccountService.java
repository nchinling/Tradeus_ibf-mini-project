package sg.edu.nus.iss.stocktrackerbackend.services;

import java.util.UUID;

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
    
    // @Transactional(rollbackFor = AccountException.class)
    // public Account createAccount(Account account) throws AccountException{

    //     //create Account id. 
    //     String accountId = UUID.randomUUID().toString().substring(0, 8);

    //     account.setAccountId(accountId);


    //     //insert account info
    //    accountRepo.createAccount(account);

    //    return account;

    // }

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
}









    


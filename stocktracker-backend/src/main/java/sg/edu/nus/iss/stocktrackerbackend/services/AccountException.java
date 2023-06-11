package sg.edu.nus.iss.stocktrackerbackend.services;

public class AccountException extends Exception{
      
    public AccountException(){
        super();
    }
    public AccountException(String message){
        super(message);
    }

    // public AccountException(String message, Throwable cause) {
    // super(message, cause);
    // }

    
}



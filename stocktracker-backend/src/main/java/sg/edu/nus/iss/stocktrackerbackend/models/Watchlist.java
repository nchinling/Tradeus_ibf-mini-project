package sg.edu.nus.iss.stocktrackerbackend.models;

public class Watchlist {
    private String[] symbols;
    private String username;

    public Watchlist() {
    }

    public Watchlist(String[] symbols, String username) {
        this.symbols = symbols;
        this.username = username;
    }

    public String[] getSymbols() {
        return symbols;
    }

    public void setSymbols(String[] symbols) {
        this.symbols = symbols;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}

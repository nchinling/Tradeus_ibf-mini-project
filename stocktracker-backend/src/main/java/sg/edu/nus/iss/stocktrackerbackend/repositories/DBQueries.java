package sg.edu.nus.iss.stocktrackerbackend.repositories;

public class DBQueries {
      public static final String INSERT_REGISTRATION = """
        
      insert into accounts(account_id, name, username, password, 
                            address, mobile_no, nationality, date_of_birth)
                        values (?, ?, ?, ? ,?, ?,?,?);

    """;

    public static final String UPDATE_ACCOUNT = """
        
      update accounts
        set name = ?, password = ?, address = ?, 
        mobile_no = ?, nationality = ?, date_of_birth = ?
        where account_id = ?

    """;

     public static final String SELECT_ACCOUNT_BY_USERNAME ="select * from accounts where username = ?";
    
     public static final String CHECK_ACCOUNTID_EXISTS = "SELECT COUNT(*) FROM accounts WHERE account_id = ?";

    public static final String INSERT_INTO_PORTFOLIO = "insert ignore into portfolio(account_id, symbol) values (?, ?)";

    public static final String INSERT_TRADE = """
    
                insert into trades(portfolio_id,account_id, username, exchange, symbol,  
                                  stock_name, units, buy_date, buy_price, currency, fee, total)
                        values (?,?, ?, ?, ? ,?, ?,?,?,?,?,?);

            """;
    public static final String SELECT_SYMBOLS_BY_ACCOUNTID = "SELECT symbol FROM portfolio WHERE account_id = ?";

    public static final String SELECT_PORTFOLIO_ID ="SELECT id FROM portfolio WHERE account_id = ? AND symbol = ?";


    public static final String SELECT_TRADE_BY_ACCOUNTID_AND_SYMBOL = """
            SELECT t.account_id, t.symbol, t.username, t.exchange, t.stock_name,
            t.currency, SUM(t.units*t.buy_price)/SUM(t.units) as buy_price, 
            SUM(t.total) AS total_sum, SUM(t.units) AS total_units, 
            SUM(t.fee) AS total_fee
            FROM trades AS t
            RIGHT JOIN portfolio AS p ON t.portfolio_id = p.id
            WHERE t.account_id = ? AND t.symbol = ?
            GROUP BY t.account_id, t.symbol, t.username, t.exchange, t.stock_name, t.currency
            ORDER BY t.symbol
        """;
    
    public static final String SELECT_TRADES_BY_ACCOUNTID_AND_SYMBOL_ANNUALISED = """
        SELECT * FROM trades AS t WHERE t.account_id = ?
    """;


    public static final String DELETE_SYMBOL_BY_ACCOUNTID ="DELETE FROM portfolio WHERE symbol = ? AND account_id = ?";

    public static final String DELETE_TRADE_BY_ACCOUNTID_AND_DATE ="DELETE FROM trades WHERE symbol = ? AND buy_date =? AND account_id = ?";

    public static final String FIND_TOTAL_BY_ACCOUNTID_AND_SYMBOL ="SELECT COUNT(*) FROM trades WHERE symbol = ? AND account_id = ?";

    public static final String DELETE_FROM_PORTFOLIO_BY_ACCOUNTID_AND_SYMBOL ="DELETE FROM portfolio WHERE symbol = ? AND account_id = ?";
   

}
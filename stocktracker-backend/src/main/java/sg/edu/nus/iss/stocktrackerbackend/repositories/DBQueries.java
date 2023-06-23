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

    // public static final String SELECT_TRADE_BY_ACCOUNTID_AND_SYMBOL ="""
    //     SELECT t.*
    //     FROM portfolio AS p
    //     JOIN trades AS t ON p.account_id = t.account_id AND p.symbol = t.symbol
    //     WHERE p.account_id = ? and p.symbol=?
    //     ORDER BY p.symbol
    // """;


    //next best working
    // public static final String SELECT_TRADE_BY_ACCOUNTID_AND_SYMBOL = """
    //     SELECT p.account_id, p.symbol, t.username, t.exchange, t.stock_name, t.buy_date,
    //     t.currency, SUM(t.units*t.buy_price)/SUM(t.units) as buy_price, 
    //     SUM(t.total) AS total_sum, SUM(t.units) AS total_units, 
    //     SUM(t.fee) AS total_fee
    //     FROM portfolio AS p
    //     JOIN trades AS t ON p.account_id = t.account_id AND p.symbol = t.symbol
    //     WHERE p.account_id = ? and p.symbol=?
    //     GROUP BY p.account_id, p.symbol, t.username, t.exchange, t.stock_name, t.buy_date, t.currency
    //     ORDER BY p.symbol
    // """;

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



    // public static final String SELECT_TRADE_BY_ACCOUNTID_AND_SYMBOL ="""
    //     SELECT p.account_id, p.symbol, t.username, t.exchange, t.stock_name, t.buy_date,
    //     t.currency, SUM(t.units*t.buy_price)/SUM(t.units) as buy_price, 
    //     SUM(t.total) AS total, SUM(t.units) AS units, 
    //     SUM(t.fee) AS fee, SUM(t.total) AS total
    //     FROM portfolio AS p
    //     JOIN trades AS t ON p.account_id = t.account_id AND p.symbol = t.symbol
    //     WHERE p.account_id = ? and p.symbol=?
    //     GROUP BY p.account_id, p.symbol, t.username, t.exchange, t.stock_name, t.buy_date, t.currency
    //     ORDER BY p.symbol
    // """;

 
    // public static final String SELECT_TRADE_BY_ACCOUNTID ="""

    //         select p.account_id, p.symbol
    //         from portfolio as p 
    //         join trades as t on p.account_id = t.account_id and p.symbol = t.symbol
    //         where p.account_id = ?
    //         ORDER BY p.symbol


    //     """;

    // public static final String SELECT_TRADE_BY_ACC ="""

    //     select s.id, s.cat_id, s.style_name, count(*) as beercount 
    //     from styles as s 
    //     join categories as c on s.cat_id = c.id 
    //     join beers as b on b.cat_id = c.id and 
    //     b.style_id = s.id 
    //     GROUP BY s.id, s.style_name 
    //     ORDER BY beercount DESC, s.style_name ASC

    //     """;

        


}

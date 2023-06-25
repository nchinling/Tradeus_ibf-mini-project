package sg.edu.nus.iss.stocktrackerbackend.repositories;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;


import org.springframework.jdbc.core.RowMapper;


import sg.edu.nus.iss.stocktrackerbackend.models.Trade;

public class TradeRowMapper implements RowMapper<Trade> {
    @Override
    public Trade mapRow(ResultSet rs, int rowNum) throws SQLException {

        Trade trade = new Trade();
        trade.setAccountId(rs.getString("account_id"));
        trade.setUsername(rs.getString("username"));
        trade.setExchange(rs.getString("exchange"));
        trade.setSymbol(rs.getString("symbol"));
        trade.setStockName(rs.getString("stock_name"));
        trade.setUnits(rs.getDouble("total_units"));
        trade.setPrice(rs.getDouble("buy_price"));
        // Date buyDate = rs.getDate("buy_date");
        // trade.setDate(buyDate.toLocalDate());
        trade.setCurrency(rs.getString("currency"));
        trade.setFee(rs.getDouble("total_fee"));
        trade.setTotal(rs.getDouble("total_sum"));
        return trade;
    }
}


// public class TradeRowMapper implements RowMapper<Trade> {
//     @Override
//     public Trade mapRow(ResultSet rs, int rowNum) throws SQLException {

//         Trade trade = new Trade();
//         trade.setAccountId(rs.getString("account_id"));
//         trade.setUsername(rs.getString("username"));
//         trade.setExchange(rs.getString("exchange"));
//          trade.setSymbol(rs.getString("symbol"));
//         trade.setStockName(rs.getString("stock_name"));
//         trade.setUnits(Double.parseDouble(rs.getString("total_units")));
//         trade.setPrice(Double.parseDouble(rs.getString("buy_price")));
//         trade.setCurrency(rs.getString("currency"));
//         trade.setFee(Double.parseDouble(rs.getString("total_fee")));
//         Date date = rs.getDate("buy_date");
//         trade.setDate(date.toLocalDate());
//         trade.setTotal(Double.parseDouble(rs.getString("total_sum")));
//         return trade;

//     }
// }

 
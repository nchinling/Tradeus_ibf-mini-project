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
        trade.setUnits(Double.parseDouble(rs.getString("units")));
        trade.setPrice(Double.parseDouble(rs.getString("buy_price")));
        trade.setCurrency(rs.getString("currency"));
        trade.setFee(Double.parseDouble(rs.getString("fee")));
        Date date = rs.getDate("buy_date");
        trade.setDate(date.toLocalDate());
        trade.setTotal(Double.parseDouble(rs.getString("total")));
        return trade;

    }
}

 
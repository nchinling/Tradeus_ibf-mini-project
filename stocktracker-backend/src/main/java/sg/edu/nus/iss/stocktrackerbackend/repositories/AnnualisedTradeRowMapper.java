package sg.edu.nus.iss.stocktrackerbackend.repositories;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sg.edu.nus.iss.stocktrackerbackend.models.Trade;

public class AnnualisedTradeRowMapper implements RowMapper<Trade> {

    @Override
    public Trade mapRow(ResultSet rs, int rowNum) throws SQLException {

        Trade trade = new Trade();
        trade.setAccountId(rs.getString("account_id"));
        trade.setUsername(rs.getString("username"));
        trade.setExchange(rs.getString("exchange"));
        trade.setSymbol(rs.getString("symbol"));
        trade.setStockName(rs.getString("stock_name"));
        trade.setUnits(rs.getDouble("units"));
        trade.setPrice(rs.getDouble("buy_price"));
        Date buyDate = rs.getDate("buy_date");
        trade.setDate(buyDate.toLocalDate());
        trade.setCurrency(rs.getString("currency"));
        trade.setFee(rs.getDouble("fee"));
        trade.setTotal(rs.getDouble("total"));
        return trade;
    }
    
}

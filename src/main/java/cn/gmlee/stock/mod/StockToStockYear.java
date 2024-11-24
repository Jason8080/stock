package cn.gmlee.stock.mod;


import cn.gmlee.stock.dao.entity.Stock2024;
import cn.gmlee.tools.base.util.BoolUtil;
import cn.gmlee.tools.base.util.TimeUtil;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.Date;

/**
 * The interface Stock to stock year.
 */
@Mapper(componentModel = "spring")
public abstract class StockToStockYear {
    /**
     * String to date.
     *
     * @param timestamp the timestamp
     * @return the date
     */
    public Date stringToDate(String timestamp) {
        return TimeUtil.parseTime(timestamp);
    }

    /**
     * String to big decimal big decimal.
     *
     * @param value the value
     * @return the big decimal
     */
    public BigDecimal stringToBigDecimal(String value) {
        if (BoolUtil.isEmpty(value.trim())) {
            return null;
        }
        return new BigDecimal(value);
    }

    /**
     * To entity stock 2024.
     *
     * @param stock the stock
     * @return the stock 2024
     */
    public abstract Stock2024 toEntity(Stock stock);

    /**
     * To object stock.
     *
     * @param stock2024 the stock 2024
     * @return the stock
     */
    public abstract Stock toObject(Stock2024 stock2024);
}

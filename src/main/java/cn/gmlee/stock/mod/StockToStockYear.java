package cn.gmlee.stock.mod;


import cn.gmlee.stock.dao.entity.Stock2024;
import cn.gmlee.tools.base.util.TimeUtil;
import org.mapstruct.Mapper;

import java.util.Date;
import java.util.Timer;

/**
 * The interface Stock to stock year.
 */
@Mapper(componentModel = "spring")
public interface StockToStockYear {
    /**
     * String to date.
     *
     * @param timestamp the timestamp
     * @return the date
     */
    default Date stringToDate(String timestamp) {
        return TimeUtil.parseTime(timestamp);
    }

    /**
     * To entity stock 2024.
     *
     * @param stock the stock
     * @return the stock 2024
     */
    Stock2024 toEntity(Stock stock);
}

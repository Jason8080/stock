package cn.gmlee.stock.mod;


import cn.gmlee.stock.dao.entity.Stock2024;
import org.mapstruct.Mapper;

/**
 * The interface Stock to stock year.
 */
@Mapper(componentModel = "spring")
public interface StockToStockYear {
    /**
     * To entity stock 2024.
     *
     * @param stock the stock
     * @return the stock 2024
     */
    Stock2024 toEntity(Stock stock);
}

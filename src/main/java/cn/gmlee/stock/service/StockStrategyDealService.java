package cn.gmlee.stock.service;

import cn.gmlee.stock.dao.entity.StockStrategyDeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Jas °
 * @since 2024 -11-23
 */
public interface StockStrategyDealService extends IService<StockStrategyDeal> {
    /**
     * Insert or update batch.
     *
     * @param dealLis the deal lis
     */
    void insertOrUpdateBatch(List<StockStrategyDeal> dealLis);

    /**
     * Last day string.
     *
     * @param strategyId the strategy id
     * @return the string
     */
    String lastDay(Long strategyId);
}

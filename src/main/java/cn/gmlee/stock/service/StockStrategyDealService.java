package cn.gmlee.stock.service;

import cn.gmlee.stock.dao.entity.StockStrategyDeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
     * Stats map.
     *
     * @param start the start
     * @param end   the end
     * @param sold  the sold
     * @param ids   the ids
     * @return the map
     */
    Map<String, Object> stats(Date start, Date end, Boolean sold, Integer... ids);
}

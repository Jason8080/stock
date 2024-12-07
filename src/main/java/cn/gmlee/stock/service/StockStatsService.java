package cn.gmlee.stock.service;

import cn.gmlee.stock.dao.entity.StockStats;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Jas °
 * @since 2024 -12-06
 */
public interface StockStatsService extends IService<StockStats> {
    /**
     * Stats map.
     *
     * @param start the start
     * @param end   the end
     * @param sold  the sold
     * @param ids   the ids
     * @return the map
     */
    List<StockStats> stats(Date start, Date end, Boolean sold, Integer... ids);

    /**
     * Insert or update batch.
     *
     * @param entities the entities
     */
    void insertOrUpdateBatch(Collection<StockStats> entities);
}

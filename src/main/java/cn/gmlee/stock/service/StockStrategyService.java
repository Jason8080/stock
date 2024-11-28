package cn.gmlee.stock.service;

import cn.gmlee.stock.dao.entity.StockStrategy;
import cn.gmlee.stock.mod.Stats;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Jas °
 * @since 2024 -11-23
 */
public interface StockStrategyService extends IService<StockStrategy> {
    /**
     * Calculate list.
     *
     * @param start the start
     * @param end   the end
     * @param ids   the ids
     * @return the list
     */
    List<Stats> listStats(Date start, Date end, Integer... ids);
}

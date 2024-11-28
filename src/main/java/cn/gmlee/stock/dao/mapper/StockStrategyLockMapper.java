package cn.gmlee.stock.dao.mapper;

import cn.gmlee.stock.dao.entity.StockStrategyLock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Jas °
 * @since 2024 -11-24
 */
public interface StockStrategyLockMapper extends BaseMapper<StockStrategyLock> {
    List<StockStrategyLock> stats(Date start, Date end, Integer... ids);
}

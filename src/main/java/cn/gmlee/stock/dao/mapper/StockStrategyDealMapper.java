package cn.gmlee.stock.dao.mapper;

import cn.gmlee.stock.dao.entity.StockStrategyDeal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Jas °
 * @since 2024 -11-24
 */
public interface StockStrategyDealMapper extends BaseMapper<StockStrategyDeal> {
    /**
     * Insert or update batch.
     *
     * @param entities the entities
     */
    void insertOrUpdateBatch(@Param("entities") List<StockStrategyDeal> entities);

    /**
     * Last day string.
     *
     * @return the string
     */
    String lastDay();
}

package cn.gmlee.stock.dao.mapper;

import cn.gmlee.stock.dao.entity.StockStrategyDeal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
     * Stats map.
     *
     * @param start the start
     * @param end   the end
     * @param ids   the ids
     * @return the map
     */
    Map<String, Object> stats(@Param("start") Date start, @Param("end") Date end, @Param("sold") Boolean sold, @Param("ids") Integer... ids);
}

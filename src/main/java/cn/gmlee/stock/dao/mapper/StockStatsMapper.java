package cn.gmlee.stock.dao.mapper;

import cn.gmlee.stock.dao.entity.StockStats;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Jas °
 * @since 2024 -12-06
 */
public interface StockStatsMapper extends BaseMapper<StockStats> {
    /**
     * Stats map.
     *
     * @param start the start
     * @param end   the end
     * @param sold  the sold
     * @param ids   the ids
     * @return the map
     */
    List<StockStats> stats(@Param("start") Date start, @Param("end") Date end, @Param("sold") Boolean sold, @Param("ids") Integer... ids);

    /**
     * Insert or update batch.
     *
     * @param entities the entities
     */
    void insertOrUpdateBatch(@Param("entities") Collection<StockStats> entities);
}

package cn.gmlee.stock.dao.mapper;

import cn.gmlee.stock.dao.entity.StockStats;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jas°
 * @since 2024-12-06
 */
public interface StockStatsMapper extends BaseMapper<StockStats> {
    /**
     * Stats map.
     *
     * @param start the start
     * @param end   the end
     * @param ids   the ids
     * @return the map
     */
    StockStats stats(@Param("start") Date start, @Param("end") Date end, @Param("sold") Boolean sold, @Param("ids") Integer... ids);
}

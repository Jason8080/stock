package cn.gmlee.stock.dao.mapper;

import cn.gmlee.stock.dao.entity.StockStrategy;
import cn.gmlee.stock.mod.Stats;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Jas °
 * @since 2024 -11-23
 */
public interface StockStrategyMapper extends BaseMapper<StockStrategy> {
    /**
     * List stats list.
     *
     * @param start the start
     * @param end   the end
     * @param ids   the ids
     * @return the list
     */
    List<Stats> listStats(@Param("start") Date start, @Param("end") Date end, @Param("ids") Integer... ids);
}

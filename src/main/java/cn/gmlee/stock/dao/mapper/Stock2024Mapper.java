package cn.gmlee.stock.dao.mapper;

import cn.gmlee.stock.dao.entity.Stock2024;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Jas °
 * @since 2024 -11-23
 */
public interface Stock2024Mapper extends BaseMapper<Stock2024> {
    /**
     * Gets last day.
     *
     * @return the last day
     */
    String lastDay();

    /**
     * Insert or update batch.
     *
     * @param entities the entities
     */
    void insertOrUpdateBatch(@Param("entities") List<Stock2024> entities);
}

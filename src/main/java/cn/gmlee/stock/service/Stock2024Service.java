package cn.gmlee.stock.service;

import cn.gmlee.stock.dao.entity.Stock2024;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Jas °
 * @since 2024 -11-23
 */
public interface Stock2024Service extends IService<Stock2024> {
    /**
     * Insert or update batch.
     *
     * @param entities the entities
     */
    void insertOrUpdateBatch(List<Stock2024> entities);
}

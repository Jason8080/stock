package cn.gmlee.stock.service.impl;

import cn.gmlee.stock.dao.entity.StockStats;
import cn.gmlee.stock.dao.mapper.StockStatsMapper;
import cn.gmlee.stock.service.StockStatsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jas°
 * @since 2024-12-06
 */
@Service
public class StockStatsServiceImpl extends ServiceImpl<StockStatsMapper, StockStats> implements StockStatsService {
    @Override
    public List<StockStats> stats(Date start, Date end, Boolean sold, Integer... ids) {
        return baseMapper.stats(start, end, sold, ids);
    }

    @Override
    public void insertOrUpdateBatch(Collection<StockStats> entities) {
        baseMapper.insertOrUpdateBatch(entities);
    }
}

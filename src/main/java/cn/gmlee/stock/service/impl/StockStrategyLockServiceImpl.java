package cn.gmlee.stock.service.impl;

import cn.gmlee.stock.dao.entity.StockStrategyLock;
import cn.gmlee.stock.dao.mapper.StockStrategyLockMapper;
import cn.gmlee.stock.service.StockStrategyLockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Jas°
 * @since 2024-11-23
 */
@Service
public class StockStrategyLockServiceImpl extends ServiceImpl<StockStrategyLockMapper, StockStrategyLock> implements StockStrategyLockService {
    @Override
    public List<StockStrategyLock> stats(Date start, Date end, Integer... ids) {
        return baseMapper.stats(start, end, ids);
    }
}

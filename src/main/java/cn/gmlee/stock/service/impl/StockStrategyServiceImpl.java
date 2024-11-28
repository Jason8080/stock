package cn.gmlee.stock.service.impl;

import cn.gmlee.stock.dao.entity.StockStrategy;
import cn.gmlee.stock.dao.mapper.StockStrategyMapper;
import cn.gmlee.stock.mod.Stats;
import cn.gmlee.stock.service.StockStrategyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jas°
 * @since 2024-11-23
 */
@Service
public class StockStrategyServiceImpl extends ServiceImpl<StockStrategyMapper, StockStrategy> implements StockStrategyService {

    @Override
    public List<Stats> listStats(Date start, Date end, Integer... ids) {
        return baseMapper.listStats(start, end, ids);
    }
}

package cn.gmlee.stock.service.impl;

import cn.gmlee.stock.dao.entity.StockStrategyDeal;
import cn.gmlee.stock.dao.mapper.StockStrategyDealMapper;
import cn.gmlee.stock.service.StockStrategyDealService;
import cn.gmlee.tools.base.util.BoolUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
public class StockStrategyDealServiceImpl extends ServiceImpl<StockStrategyDealMapper, StockStrategyDeal> implements StockStrategyDealService {

    @Override
    public void insertOrUpdateBatch(List<StockStrategyDeal> entities) {
        if (BoolUtil.isEmpty(entities)) {
            return;
        }
        baseMapper.insertOrUpdateBatch(entities);
    }

    @Override
    public String lastDay(Long strategyId) {
        return baseMapper.lastDay(strategyId);
    }
}

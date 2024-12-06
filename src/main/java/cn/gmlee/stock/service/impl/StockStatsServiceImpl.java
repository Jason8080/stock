package cn.gmlee.stock.service.impl;

import cn.gmlee.stock.dao.entity.StockStats;
import cn.gmlee.stock.controller.vo.StockStatsVo;
import cn.gmlee.stock.dao.mapper.StockStatsMapper;
import cn.gmlee.stock.service.StockStatsService;

import cn.gmlee.tools.base.util.BeanUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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

    @Resource
    StockStatsMapper stockStatsMapper;

    @Override
    public void saveBatch(List<StockStatsVo> list) {
        list.stream().mapToInt(stockStatsVo -> {
            StockStats stockStats = BeanUtil.convert(stockStatsVo, StockStats.class);
            return stockStatsMapper.insert(stockStats);
        }).sum();
    }

    @Override
    public StockStats modify(StockStatsVo stockStatsVo) {
        StockStats stockStats = BeanUtil.convert(stockStatsVo, StockStats.class);
        if (Objects.isNull(stockStats.getId())) {
            stockStatsMapper.insert(stockStats);
        } else {
            stockStatsMapper.updateById(stockStats);
        }
        return stockStats;
    }

    @Override
    public void updateBatch(List<StockStatsVo> list) {
        list.stream().mapToInt(stockStatsVo -> {
            StockStats stockStats = BeanUtil.convert(stockStatsVo, StockStats.class);
            return stockStatsMapper.updateById(stockStats);
        }).sum();
    }

    @Override
    public void logicDelById(Long id) {
        stockStatsMapper.update(null,
            Wrappers.<StockStats>lambdaUpdate()
                .eq(StockStats::getId, id)
                .set(StockStats::getDel, 1)
        );
    }

    @Override
    public void logicDelByIds(Collection<Long> ids) {
        ids.forEach(id -> logicDelById(id));
    }

    @Override
    public List<StockStats> listBy(StockStatsVo stockStatsVo) {
        StockStats stockStats = BeanUtil.convert(stockStatsVo, StockStats.class);
        return stockStatsMapper.selectList(new QueryWrapper(stockStats));
    }

    @Override
    public IPage listPageBy(Page page, StockStatsVo stockStatsVo) {
        StockStats stockStats = BeanUtil.convert(stockStatsVo, StockStats.class);
        return stockStatsMapper.selectPage(page, new QueryWrapper(stockStats));
    }
}

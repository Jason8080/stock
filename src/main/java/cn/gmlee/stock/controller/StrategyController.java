package cn.gmlee.stock.controller;

import cn.gmlee.stock.controller.vo.ListStrategyDealVo;
import cn.gmlee.stock.controller.vo.ListStrategyVo;
import cn.gmlee.stock.dao.entity.StockStats;
import cn.gmlee.stock.dao.entity.StockStrategy;
import cn.gmlee.stock.dao.entity.StockStrategyDeal;
import cn.gmlee.stock.service.Stock2024Service;
import cn.gmlee.stock.service.StockStatsService;
import cn.gmlee.stock.service.StockStrategyDealService;
import cn.gmlee.stock.service.StockStrategyService;
import cn.gmlee.tools.base.entity.Key;
import cn.gmlee.tools.base.entity.Status;
import cn.gmlee.tools.base.mod.PageRequest;
import cn.gmlee.tools.base.mod.PageResponse;
import cn.gmlee.tools.base.mod.R;
import cn.gmlee.tools.base.util.BeanUtil;
import cn.gmlee.tools.base.util.BigDecimalUtil;
import cn.gmlee.tools.base.util.BoolUtil;
import cn.gmlee.tools.base.util.QuickUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 策略接口.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("strategy")
public class StrategyController {

    private final Stock2024Service stock2024Service;

    private final StockStatsService stockStatsService;

    private final StockStrategyService stockStrategyService;

    private final StockStrategyDealService stockStrategyDealService;

    /**
     * 策略列表.
     *
     * @param pr  the pr
     * @param key the key
     * @return the r
     */
    @GetMapping("list")
    public R<PageResponse> list(PageRequest pr, Key key) {
        IPage page = new Page(pr.current, pr.size);
        IPage<StockStrategy> iPage = stockStrategyService.page(page, Wrappers.<StockStrategy>lambdaQuery()
                .and(BoolUtil.notEmpty(key.uniqueKey), wrapper -> wrapper
                        .like(StockStrategy::getName, key.uniqueKey)
                        .or()
                        .like(StockStrategy::getAuthor, key.uniqueKey)
                )
                .eq(StockStrategy::getStatus, 1)
                .orderByAsc(StockStrategy::getId, StockStrategy::getV)
        );
        List<ListStrategyVo> vos = iPage.getRecords().stream().map(this::toListStrategy).collect(Collectors.toList());
        return R.OK.newly(PageResponse.of(pr, iPage.getTotal(), vos));
    }

    /**
     * 交易列表.
     *
     * @param pr  the pr
     * @param key the key
     * @return the r
     */
    @GetMapping("deal")
    public R<PageResponse> deal(PageRequest pr, Key key, @Valid Status status) {
        IPage page = new Page(pr.current, pr.size);
        String lastDay = stock2024Service.lastDay();
        IPage<StockStrategyDeal> iPage = stockStrategyDealService.page(page, Wrappers.<StockStrategyDeal>lambdaQuery()
                .and(BoolUtil.notEmpty(key.uniqueKey), wrapper -> wrapper
                        .like(StockStrategyDeal::getName, key.uniqueKey)
                        .or()
                        .like(StockStrategyDeal::getCode, key.uniqueKey)
                )
                .and(BoolUtil.notNull(status.status), wrapper -> wrapper
                        .or(w -> w.eq(StockStrategyDeal::getDate, lastDay).eq(StockStrategyDeal::getSold, false))
                        .or(w -> w.eq(StockStrategyDeal::getCurrentDate, lastDay).eq(StockStrategyDeal::getSold, true))
                )
                .eq(BoolUtil.notNull(status.status), StockStrategyDeal::getSold, status.status)
                .eq(StockStrategyDeal::getStrategyId, status.id)
                .orderByDesc(StockStrategyDeal::getDate)
        );
        List<ListStrategyDealVo> vos = iPage.getRecords().stream().map(x -> toListStrategyDeal(x, lastDay)).collect(Collectors.toList());
        return R.OK.newly(PageResponse.of(pr, iPage.getTotal(), vos));
    }

    private ListStrategyDealVo toListStrategyDeal(StockStrategyDeal sd, String lastDay) {
        ListStrategyDealVo vo = BeanUtil.convert(sd, ListStrategyDealVo.class);
        vo.setDiffPrice(BigDecimalUtil.subtract(sd.getCurrentPrice(), sd.getPrice()));
        QuickUtil.isTrue(sd.getSold(), () -> vo.setSoldCn("卖出"));
        QuickUtil.isTrue(!sd.getSold() && BoolUtil.eq(lastDay, sd.getDate()), () -> vo.setSoldCn("买入"));
        return vo;
    }

    private ListStrategyVo toListStrategy(StockStrategy ss) {
        ListStrategyVo vo = BeanUtil.convert(ss, ListStrategyVo.class);
        ListStrategyVo.Props props = new ListStrategyVo.Props();
        List<StockStats> soldStats = stockStatsService.stats(null, null, true, ss.getId());
        List<StockStats> lockStats = stockStatsService.stats(null, null, false, ss.getId());
        BigDecimal sumRate = BigDecimal.ZERO;
        BigDecimal sumAvgRate = BigDecimal.ZERO;
        BigDecimal winRate = BigDecimal.ZERO;
        if (BoolUtil.notEmpty(soldStats)) {
            StockStats stockStats = soldStats.get(0);
            props.setTotal(stockStats.getTotal());
            props.setSold(stockStats.getQty());
            props.setRate(stockStats.getRate());
            props.setAvgRate(stockStats.getAvgRate());
            props.setSoldStats(stockStats);
            sumRate = sumRate.add(stockStats.getRate());
            sumAvgRate = sumAvgRate.add(stockStats.getAvgRate());
            winRate = winRate.add(stockStats.getWinRate());
        }
        if (BoolUtil.notEmpty(lockStats)) {
            StockStats stockStats = lockStats.get(0);
            props.setLock(stockStats.getQty());
            props.setLockStats(stockStats);
            sumRate = sumRate.add(stockStats.getRate());
            sumAvgRate = sumAvgRate.add(stockStats.getAvgRate());
            winRate = winRate.add(stockStats.getWinRate());
        }
        props.setRate(sumRate);
        props.setProportion(BigDecimal.valueOf(100));
        props.setAvgRate(BigDecimalUtil.divide(sumAvgRate, 2).setScale(2, RoundingMode.HALF_UP));
        props.setWinRate(BigDecimalUtil.divide(winRate, 2).setScale(2, RoundingMode.HALF_UP));
        vo.setProps(props);
        return vo;
    }
}

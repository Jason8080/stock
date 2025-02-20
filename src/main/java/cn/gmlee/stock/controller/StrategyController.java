package cn.gmlee.stock.controller;

import cn.gmlee.stock.controller.vo.ListStrategyDealVo;
import cn.gmlee.stock.controller.vo.ListStrategyVo;
import cn.gmlee.stock.dao.entity.StockStats;
import cn.gmlee.stock.dao.entity.StockStrategy;
import cn.gmlee.stock.dao.entity.StockStrategyDeal;
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
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 策略接口.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("strategy")
public class StrategyController {

    private AtomicBoolean odd = new AtomicBoolean(true);

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
        String lastDay = stockStrategyDealService.lastDay();
        boolean oddBoolean = odd.getAndSet(!odd.get());
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
                .orderByAsc(pr.current==1&&!oddBoolean, StockStrategyDeal::getDate, StockStrategyDeal::getRiseRatio)
                .orderByDesc(pr.current==1&&oddBoolean, StockStrategyDeal::getRiseRatio, StockStrategyDeal::getDate)
                .orderByAsc(pr.current!=1&&!oddBoolean, StockStrategyDeal::getDate, StockStrategyDeal::getRiseRatio)
                .orderByDesc(pr.current!=1&&oddBoolean, StockStrategyDeal::getRiseRatio, StockStrategyDeal::getDate)
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
        List<StockStats> fullStats = stockStatsService.stats(null, null, null, ss.getId());
        if (BoolUtil.notEmpty(soldStats)) {
            StockStats stockStats = soldStats.get(0);
            props.setTotal(stockStats.getTotal());
            props.setSold(stockStats.getQty());
            props.setRate(stockStats.getRate());
            props.setAvgRate(stockStats.getAvgRate());
            props.setSoldStats(stockStats);
        }
        if (BoolUtil.notEmpty(lockStats)) {
            StockStats stockStats = lockStats.get(0);
            props.setLock(stockStats.getQty());
            props.setLockStats(stockStats);
        }
        StockStats stockStats = fullStats.get(0);
        props.setRate(stockStats.getRate());
        props.setProportion(stockStats.getProportion());
        props.setAvgRate(stockStats.getAvgRate());
        props.setWinRate(stockStats.getWinRate());
        vo.setProps(props);
        return vo;
    }
}

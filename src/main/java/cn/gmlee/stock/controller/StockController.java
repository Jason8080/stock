package cn.gmlee.stock.controller;

import cn.gmlee.stock.dao.entity.*;
import cn.gmlee.stock.mod.Deal;
import cn.gmlee.stock.mod.Stock;
import cn.gmlee.stock.mod.StockToStockYear;
import cn.gmlee.stock.service.*;
import cn.gmlee.stock.util.DealKit;
import cn.gmlee.tools.base.entity.Code;
import cn.gmlee.tools.base.entity.Key;
import cn.gmlee.tools.base.mod.PageRequest;
import cn.gmlee.tools.base.mod.PageResponse;
import cn.gmlee.tools.base.mod.R;
import cn.gmlee.tools.base.util.BoolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 股票接口.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("stock")
public class StockController {

    private final StockToStockYear stockToStockYear;

    private final StockStatsService stockStatsService;

    private final Stock2024Service stock2024Service;

    private final StockStrategyService stockStrategyService;

    private final StockStrategyRuleService stockStrategyRuleService;

    private final StockStrategyDealService stockStrategyDealService;

    /**
     * 股票列表.
     *
     * @param pr  the pr
     * @param key the key
     * @return the r
     */
    @GetMapping("list")
    public R<PageResponse> list(PageRequest pr, Key key) {
        IPage page = new Page(pr.current, pr.size);
        String lastDay = stock2024Service.lastDay();
        IPage<Stock2024> iPage = stock2024Service.page(page, Wrappers.<Stock2024>lambdaQuery()
                .and(BoolUtil.notEmpty(key.uniqueKey), wrapper -> wrapper
                        .like(Stock2024::getName, key.uniqueKey)
                        .or()
                        .like(Stock2024::getCode, key.uniqueKey)
                )
                .eq(Stock2024::getDate, lastDay)
                .orderByAsc(Stock2024::getCurrentPrice)
        );
        return R.OK.newly(PageResponse.of(pr, iPage.getTotal(), iPage.getRecords()));
    }

    /**
     * 交易列表.
     *
     * @param pr  the pr
     * @param key the key
     * @return the r
     */
    @GetMapping("deal")
    public R<PageResponse> deal(PageRequest pr, Key key, @Valid Code code) {
        IPage<StockStrategy> iPage = stockStrategyService.page(new Page(pr.current, pr.size), Wrappers.<StockStrategy>lambdaQuery()
                .and(BoolUtil.notEmpty(key.uniqueKey), wrapper -> wrapper
                        .like(StockStrategy::getName, key.uniqueKey)
                        .or()
                        .like(StockStrategy::getAuthor, key.uniqueKey)
                )
                .eq(StockStrategy::getStatus, 1)
                .orderByAsc(StockStrategy::getId, StockStrategy::getV)
        );
        // 策略规则准备
        List<Integer> strategyIds = iPage.getRecords().stream().map(StockStrategy::getId).collect(Collectors.toList());
        strategyIds.add(-1); // 确保没有异常
        List<StockStrategyRule> rules = stockStrategyRuleService.list(Wrappers.<StockStrategyRule>lambdaQuery()
                .in(StockStrategyRule::getStrategyId, strategyIds)
                .eq(StockStrategyRule::getStatus, true)
        );
        Map<Integer, List<StockStrategyRule>> ruleMap = rules.stream().collect(Collectors.groupingBy(StockStrategyRule::getStrategyId));
        // 行情数据准备
        Stock2024 stock2024 = stock2024Service.getOne(Wrappers.<Stock2024>lambdaQuery()
                .eq(Stock2024::getCode, code.code)
                .orderByDesc(Stock2024::getDate)
                .groupBy(Stock2024::getCode)
        , false);
        Stock stock = stockToStockYear.toObject(stock2024);
        // 持仓数据准备
        List<StockStrategyDeal> deals = stockStrategyDealService.list(Wrappers.<StockStrategyDeal>lambdaQuery()
                .in(StockStrategyDeal::getStrategyId, strategyIds)
                .eq(StockStrategyDeal::getSold, false)
                .orderByAsc(StockStrategyDeal::getDate)
        );
        Map<Integer, List<StockStrategyDeal>> dealsMap = deals.stream().collect(Collectors.groupingBy(StockStrategyDeal::getStrategyId));
        // 统计数据准备
        List<StockStats> soldStats = stockStatsService.stats(null, null, true, iPage.getRecords().stream().map(StockStrategy::getId).toArray(Integer[]::new));
        List<StockStats> lockStats = stockStatsService.stats(null, null, false, iPage.getRecords().stream().map(StockStrategy::getId).toArray(Integer[]::new));
        Map<Integer, StockStats> soldStatsMap = soldStats.stream().collect(Collectors.toMap(StockStats::getStrategyId, Function.identity(), (k1, k2) -> k1));
        Map<Integer, StockStats> lockStatsMap = lockStats.stream().collect(Collectors.toMap(StockStats::getStrategyId, Function.identity(), (k1, k2) -> k1));
        List<Deal> vos = iPage.getRecords().stream().map(x -> DealKit.toDeal(x, ruleMap, dealsMap, soldStatsMap, lockStatsMap, stock)).collect(Collectors.toList());
        return R.OK.newly(PageResponse.of(pr, iPage.getTotal(), vos));
    }

}

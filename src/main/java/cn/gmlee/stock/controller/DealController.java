package cn.gmlee.stock.controller;

import cn.gmlee.stock.controller.vo.ViewStockDealVo;
import cn.gmlee.stock.dao.entity.StockStats;
import cn.gmlee.stock.dao.entity.StockStrategy;
import cn.gmlee.stock.dao.entity.StockStrategyDeal;
import cn.gmlee.stock.dao.entity.StockStrategyRule;
import cn.gmlee.stock.mod.Deal;
import cn.gmlee.stock.mod.Stock;
import cn.gmlee.stock.service.StockStatsService;
import cn.gmlee.stock.service.StockStrategyDealService;
import cn.gmlee.stock.service.StockStrategyRuleService;
import cn.gmlee.stock.service.StockStrategyService;
import cn.gmlee.stock.util.DealKit;
import cn.gmlee.stock.util.MarketKit;
import cn.gmlee.stock.util.TencentKit;
import cn.gmlee.tools.base.mod.R;
import cn.gmlee.tools.base.util.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 交易接口.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("deal")
public class DealController {

    private final StockStatsService stockStatsService;

    private final StockStrategyService stockStrategyService;

    private final StockStrategyRuleService stockStrategyRuleService;

    private final StockStrategyDealService stockStrategyDealService;

    /**
     * 查看信号.
     *
     * @return the r
     */
    @GetMapping("view")
    public R<List> view(@RequestParam("code") String code, @RequestParam("id") String id) {
        // 参数解析
        List<String> strategyIds = Arrays.asList(id.split(","));
        List<String> stockCodes = Arrays.asList(code.split(","));
        // 策略查询
        List<StockStrategy> strategies = stockStrategyService.list(Wrappers.<StockStrategy>lambdaQuery()
                .in(StockStrategy::getId, strategyIds)
        );
        // 行情查询
        String[] codes = stockCodes.stream().distinct().map(x -> MarketKit.getMarket(x).concat(x)).toArray(String[]::new);
        List<Stock> stocks = TencentKit.getStocks(codes);
        // 规则查询
        List<StockStrategyRule> rules = stockStrategyRuleService.list(Wrappers.<StockStrategyRule>lambdaQuery()
                .in(StockStrategyRule::getStrategyId, strategyIds)
        );
        Map<Integer, List<StockStrategyRule>> ruleMap = rules.stream().collect(Collectors.groupingBy(StockStrategyRule::getStrategyId));
        // 持仓查询
        List<StockStrategyDeal> deals = stockStrategyDealService.list(Wrappers.<StockStrategyDeal>lambdaQuery()
                .in(StockStrategyDeal::getStrategyId, strategyIds)
                .eq(StockStrategyDeal::getSold, false)
                .orderByAsc(StockStrategyDeal::getDate)
        );
        Map<Integer, List<StockStrategyDeal>> dealsMap = deals.stream().collect(Collectors.groupingBy(StockStrategyDeal::getStrategyId));
        // 统计数据
        List<StockStats> soldStats = stockStatsService.stats(null, null, true, strategyIds.stream().map(Integer::valueOf).toArray(Integer[]::new));
        Map<Integer, StockStats> soldStatsMap = soldStats.stream().collect(Collectors.toMap(StockStats::getStrategyId, Function.identity(), (k1, k2) -> k1));
        List<StockStats> lockStats = stockStatsService.stats(null, null, false, strategyIds.stream().map(Integer::valueOf).toArray(Integer[]::new));
        Map<Integer, StockStats> lockStatsMap = lockStats.stream().collect(Collectors.toMap(StockStats::getStrategyId, Function.identity(), (k1, k2) -> k1));
        // 信号收集
        return R.OK.newly(collectSignal(stocks, strategies, ruleMap, dealsMap, soldStatsMap, lockStatsMap));
    }

    private List<ViewStockDealVo> collectSignal(List<Stock> stocks, List<StockStrategy> strategies, Map<Integer, List<StockStrategyRule>> ruleMap, Map<Integer, List<StockStrategyDeal>> dealsMap, Map<Integer, StockStats> soldStatsMap, Map<Integer, StockStats> lockStatsMap) {
        List<ViewStockDealVo> vos = new ArrayList<>();
        for (Stock stock : stocks) {
            ViewStockDealVo vo = BeanUtil.convert(stock, ViewStockDealVo.class);
            for (StockStrategy strategy : strategies) {
                Deal deal = DealKit.toDeal(strategy, ruleMap, dealsMap, soldStatsMap, lockStatsMap, stock);
                vo.getDeals().add(deal);
            }
            vos.add(vo);
        }
        return vos;
    }


}

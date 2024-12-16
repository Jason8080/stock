package cn.gmlee.stock.controller;

import cn.gmlee.stock.controller.vo.ViewStockDealVo;
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
import cn.gmlee.tools.base.enums.XTime;
import cn.gmlee.tools.base.mod.R;
import cn.gmlee.tools.base.util.BeanUtil;
import cn.gmlee.tools.base.util.TimeUtil;
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
                .lt(StockStrategyDeal::getDate, TimeUtil.getCurrentDatetime(XTime.DAY_NONE))
                .orderByAsc(StockStrategyDeal::getDate)
        );
        Map<Integer, List<StockStrategyDeal>> dealsMap = deals.stream().collect(Collectors.groupingBy(StockStrategyDeal::getStrategyId));
        // 信号收集
        return R.OK.newly(collectSignal(stocks, strategies, ruleMap, dealsMap));
    }

    private List<ViewStockDealVo> collectSignal(List<Stock> stocks, List<StockStrategy> strategies, Map<Integer, List<StockStrategyRule>> ruleMap, Map<Integer, List<StockStrategyDeal>> dealsMap) {
        List<ViewStockDealVo> vos = new ArrayList<>();
        for (Stock stock : stocks) {
            ViewStockDealVo vo = BeanUtil.convert(stock, ViewStockDealVo.class);
            for (StockStrategy strategy : strategies) {
                Deal deal = DealKit.toDeal(strategy, ruleMap, dealsMap, null, null, stock);
                deal.setStock(null);
                if (deal.getSold() == null) {
                    vo.getLook().add(deal);
                } else if (deal.getSold()) {
                    vo.getSell().add(deal);
                } else if (!deal.getSold()) {
                    vo.getBuy().add(deal);
                }
            }
            vos.add(vo);
        }
        return vos;
    }


}

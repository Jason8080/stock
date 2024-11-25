package cn.gmlee.stock.server;

import cn.gmlee.stock.dao.entity.*;
import cn.gmlee.stock.mod.Stock;
import cn.gmlee.stock.mod.StockToStockYear;
import cn.gmlee.stock.service.Stock2024Service;
import cn.gmlee.stock.service.StockStrategyDealService;
import cn.gmlee.stock.service.StockStrategyRuleService;
import cn.gmlee.stock.service.StockStrategyService;
import cn.gmlee.stock.util.CustomVariableKit;
import cn.gmlee.stock.util.FeiShuReader;
import cn.gmlee.stock.util.MarketKit;
import cn.gmlee.stock.util.TencentKit;
import cn.gmlee.tools.base.enums.XTime;
import cn.gmlee.tools.base.util.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Strategy server.
 */
@Component
@RequiredArgsConstructor
public class StrategyServer {

    private final StockToStockYear stockToStockYear;
    private final Stock2024Service stock2024Service;
    private final StockStrategyService stockStrategyService;
    private final StockStrategyRuleService stockStrategyRuleService;
    private final StockStrategyDealService stockStrategyDealService;

    /**
     * Deal handle.
     *
     * @return
     */
    public boolean dealHandle() {
        // 策略数据准备
        List<StockStrategy> list = stockStrategyService.list(Wrappers.<StockStrategy>lambdaQuery()
                .eq(StockStrategy::getStatus, 1)
        );
        Map<Integer, StockStrategy> strategyMap = list.stream().collect(Collectors.toMap(StockStrategy::getId, Function.identity()));
        if(BoolUtil.isEmpty(strategyMap)){
            return false;
        }
        // 持仓数据准备
        List<StockStrategyDeal> deals = stockStrategyDealService.list(Wrappers.<StockStrategyDeal>lambdaQuery()
                .in(StockStrategyDeal::getStrategyId, strategyMap.keySet())
                .isNull(StockStrategyDeal::getSellPrice)
                .isNull(StockStrategyDeal::getSellDate)
                .orderByAsc(StockStrategyDeal::getDate)
        );
        Map<String, StockStrategyDeal> dealMap = deals.stream().collect(Collectors.toMap(x -> String.format("%s_%s", x.getStrategyId(), x.getCode()), Function.identity(), (k1, k2) -> k1));
        // 单个策略处理
        strategyMap.forEach((k, v) -> ExceptionUtil.sandbox(() -> strategyHandlerOne(v, strategyMap, dealMap)));
        return true;
    }

    private void strategyHandlerOne(StockStrategy strategy, Map<Integer, StockStrategy> strategyMap, Map<String, StockStrategyDeal> dealMap) {
        List<StockStrategyRule> rules = stockStrategyRuleService.list(Wrappers.<StockStrategyRule>lambdaQuery()
                .in(StockStrategyRule::getStrategyId, strategyMap.keySet())
                .eq(StockStrategyRule::getStatus, true)
        );
        if (BoolUtil.isEmpty(rules)) {
            return;
        }
        Map<Integer, List<StockStrategyRule>> ruleMap = rules.stream().collect(Collectors.groupingBy(StockStrategyRule::getTransType));
        // 买入规则准备
        List<StockStrategyRule> buyRule = ruleMap.get(1);
        List<StockStrategyRule> excludeBuyRule = ruleMap.get(2);
        // 卖出规则准备
        List<StockStrategyRule> sellRule = ruleMap.get(-1);
        List<StockStrategyRule> excludeSellRule = ruleMap.get(-2);
        // 股票数据准备
        IPage<Stock2024> page = new Page<>(1, 1000);
        LambdaQueryWrapper<Stock2024> qw = Wrappers.<Stock2024>lambdaQuery()
                .eq(Stock2024::getDate, TimeUtil.getCurrentDatetime(XTime.DAY_NONE));
        PageUtil.nextPage(() -> stock2024Service.page(page, qw), (List<Stock2024> stock2024s) -> {
            if (BoolUtil.isEmpty(stock2024s)) {
                return;
            }
            List<StockStrategyDeal> dealLis = stock2024s.stream().map(
                    x -> ExceptionUtil.sandbox(() -> deal(x, dealMap, strategy, buyRule, excludeBuyRule, sellRule, excludeSellRule))
            ).filter(Objects::nonNull).collect(Collectors.toList());
            stockStrategyDealService.saveOrUpdateBatch(dealLis);
        });

    }

    /**
     * 交易对象封装
     *
     * @param stock2024       股票行情
     * @param dealMap         持仓数据
     * @param strategy
     * @param buyRule         买入规则
     * @param excludeBuyRule  排除买入规则
     * @param sellRule        卖出规则
     * @param excludeSellRule 排除卖出规则
     * @return 返回null表示不需要交易(买入 / 卖出)
     */
    private StockStrategyDeal deal(
            Stock2024 stock2024, Map<String, StockStrategyDeal> dealMap, StockStrategy strategy,
            List<StockStrategyRule> buyRule, List<StockStrategyRule> excludeBuyRule,
            List<StockStrategyRule> sellRule, List<StockStrategyRule> excludeSellRule
    ) {
        Stock stock = stockToStockYear.toObject(stock2024);
        Map<String, Object> stockMap = ClassUtil.generateCurrentMap(stock);
        CustomVariableKit.add(stock, stockMap);
        StockStrategyDeal deal = dealMap.get(stock2024.getCode());// 可能没有持仓: null
        // 交易规则 and 关系、(排除)交易规则 or 关系
        boolean buy = isDeal(stockMap, buyRule, excludeBuyRule);
        boolean sell = isDeal(stockMap, sellRule, excludeSellRule);
        if (deal == null && buy) {
            StockStrategyDeal entity = new StockStrategyDeal();
            entity.setStrategyId(strategy.getId());
            entity.setDate(stock2024.getDate());
            entity.setCode(stock2024.getCode());
            entity.setName(stock2024.getName());
            entity.setPrice(stock2024.getCurrentPrice());
            entity.setSellPrice(null);
            entity.setSellDate(null);
            return entity;
        }
        if (deal != null && sell) {
            StockStrategyDeal entity = new StockStrategyDeal();
            entity.setStrategyId(strategy.getId());
            entity.setDate(deal.getDate());
            entity.setCode(deal.getCode());
            entity.setSellPrice(stock2024.getCurrentPrice());
            entity.setSellDate(stock2024.getDate());
            return entity;
        }
        return null;
    }

    private boolean isDeal(Map<String, Object> stockMap, List<StockStrategyRule> rules, List<StockStrategyRule> excludeRules) {
        for (StockStrategyRule rule : NullUtil.get(rules)) {
            String javascript = substitutionVariable(rule.getRule(), stockMap);
            Object eval = ScriptUtil.eval(javascript, true);
            if (!(eval instanceof Boolean)) { // 非布尔值结果规则忽略
                continue;
            }
            if (!(Boolean) eval) { // 只要1个条件不符合即不交易
                return false;
            }
        }
        for (StockStrategyRule rule : NullUtil.get(excludeRules)) {
            String javascript = substitutionVariable(rule.getRule(), stockMap);
            Object eval = ScriptUtil.eval(javascript, true);
            if (!(eval instanceof Boolean)) { // 非布尔值结果规则忽略
                continue;
            }
            if ((Boolean) eval) { // 只要满足1个排除条件即不交易
                return false;
            }
        }
        return true;
    }

    private String substitutionVariable(String rule, Map<String, Object> map) {
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> next = it.next();
            String key = next.getKey();
            Object value = next.getValue();
            if (value != null) {
                rule = rule.replace(key, value.toString());
            }
        }
        return rule;
    }

    /**
     * Deal inform.
     *
     * @return
     */
    public boolean dealInform() {
        // 获取订阅
        Map<String, List<String>> subscribeMap = FeiShuReader.getSubscribeMap();
        List<StockList> all = subscribeMap.values().stream().flatMap(List::stream)
                .filter(BoolUtil::notEmpty).distinct().map(MarketKit::newStockList)
                .collect(Collectors.toList());
        // 查询行情
        List<List<Stock>> lists = QuickUtil.batch(all, 100, (cn.gmlee.tools.base.enums.Function.P2r<List<StockList>, List<Stock>>) TencentKit::getStocks);
        List<Stock2024> entities = lists.stream().flatMap(List::stream).map(stockToStockYear::toEntity).collect(Collectors.toList());
        Map<String, Stock2024> codeMap = entities.stream().collect(Collectors.toMap(Stock2024::getCode, Function.identity()));

        return true;
    }
}

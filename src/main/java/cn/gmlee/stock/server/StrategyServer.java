package cn.gmlee.stock.server;

import cn.gmlee.stock.dao.entity.*;
import cn.gmlee.stock.mod.Stock;
import cn.gmlee.stock.mod.StockToStockYear;
import cn.gmlee.stock.service.Stock2024Service;
import cn.gmlee.stock.service.StockStrategyDealService;
import cn.gmlee.stock.service.StockStrategyRuleService;
import cn.gmlee.stock.service.StockStrategyService;
import cn.gmlee.stock.util.*;
import cn.gmlee.tools.base.enums.XTime;
import cn.gmlee.tools.base.util.*;
import cn.gmlee.util.ConsoleKit;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Strategy server.
 */
@Component
@SuppressWarnings("all")
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
     * @return boolean
     */
    public boolean dealHandle() {
        // 策略数据准备
        List<StockStrategy> list = stockStrategyService.list(Wrappers.<StockStrategy>lambdaQuery()
                .eq(BoolUtil.notEmpty(ConsoleKit.getStrategyId()), StockStrategy::getId, ConsoleKit.getStrategyId())
                .eq(BoolUtil.isEmpty(ConsoleKit.getStrategyId()), StockStrategy::getStatus, 1)
        );
        Map<Integer, StockStrategy> strategyMap = list.stream().collect(Collectors.toMap(StockStrategy::getId, Function.identity()));
        if (BoolUtil.isEmpty(strategyMap)) {
            return false;
        }
        // 策略规则准备
        List<StockStrategyRule> rules = stockStrategyRuleService.list(Wrappers.<StockStrategyRule>lambdaQuery()
                .in(StockStrategyRule::getStrategyId, strategyMap.keySet())
                .eq(BoolUtil.isEmpty(ConsoleKit.getStrategyId()), StockStrategyRule::getStatus, true)
        );
        Map<Integer, List<StockStrategyRule>> ruleMap = rules.stream().collect(Collectors.groupingBy(StockStrategyRule::getStrategyId));
        // 持仓数据清理
        if (BoolUtil.notEmpty(ConsoleKit.getStrategyId())) {
            stockStrategyDealService.remove(Wrappers.<StockStrategyDeal>lambdaQuery().eq(StockStrategyDeal::getStrategyId, ConsoleKit.getStrategyId()));
        }
        // 单个策略处理
        strategyMap.forEach((k, v) -> ExceptionUtil.sandbox(() -> strategyHandlerOne(v, ruleMap)));
        return true;
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
            entity.setName(deal.getName());
            entity.setPrice(deal.getPrice());
            entity.setSellPrice(stock2024.getCurrentPrice());
            entity.setSellDate(stock2024.getDate());
            entity.setRiseRatio(RatioKit.calculate(deal.getPrice(), stock2024.getCurrentPrice()));
            entity.setDays((int) LocalDateTimeUtil.between(deal.getDate(), stock2024.getDate()).toDays());
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
            if (value == null) {
                return "false";
            }
            rule = rule.replace(key, value.toString());
        }
        return rule;
    }

    /**
     * Send message boolean.
     *
     * @return boolean
     */
    public boolean sendMessage() {
        // 策略数据准备
        List<StockStrategy> list = stockStrategyService.list(Wrappers.<StockStrategy>lambdaQuery()
                .eq(BoolUtil.notEmpty(ConsoleKit.getStrategyId()), StockStrategy::getId, ConsoleKit.getStrategyId())
                .eq(StockStrategy::getStatus, 1)
        );
        Map<Integer, StockStrategy> strategyMap = list.stream().collect(Collectors.toMap(StockStrategy::getId, Function.identity()));
        if (BoolUtil.isEmpty(strategyMap)) {
            return false;
        }
        // 策略规则准备
        List<StockStrategyRule> rules = stockStrategyRuleService.list(Wrappers.<StockStrategyRule>lambdaQuery()
                .in(StockStrategyRule::getStrategyId, strategyMap.keySet())
                .eq(StockStrategyRule::getStatus, true)
        );
        Map<Integer, List<StockStrategyRule>> ruleMap = rules.stream().collect(Collectors.groupingBy(StockStrategyRule::getStrategyId));
        // 获取订阅
        Map<String, List<String>> subscribeMap = FeiShuReader.getSubscribeMap();
        List<StockList> all = subscribeMap.values().stream().flatMap(List::stream)
                .filter(BoolUtil::notEmpty).distinct().map(MarketKit::newStockList)
                .collect(Collectors.toList());
        // 查询行情
        List<List<Stock>> lists = QuickUtil.batch(all, 100, (cn.gmlee.tools.base.enums.Function.P2r<List<StockList>, List<Stock>>) TencentKit::getStocks);
        List<Stock> entities = lists.stream().flatMap(List::stream).collect(Collectors.toList());
        Map<String, Stock> codeMap = entities.stream().collect(Collectors.toMap(Stock::getCode, Function.identity()));
        // 策略处理
        strategyMap.forEach((k, v) -> ExceptionUtil.sandbox(() -> strategyHandlerOne(v, ruleMap, subscribeMap, codeMap)));
        return true;
    }

    private boolean strategyHandlerOne(StockStrategy strategy, Map<Integer, List<StockStrategyRule>> ruleMap) {
        if (BoolUtil.isEmpty(ruleMap) || BoolUtil.isEmpty(ruleMap.get(strategy.getId()))) {
            return false;
        }
        List<StockStrategyRule> rules = ruleMap.get(strategy.getId());
        Map<Integer, List<StockStrategyRule>> groupMap = rules.stream().collect(Collectors.groupingBy(StockStrategyRule::getTransType));
        // 买入规则准备
        List<StockStrategyRule> buyRule = groupMap.get(1);
        List<StockStrategyRule> excludeBuyRule = groupMap.get(2);
        // 卖出规则准备
        List<StockStrategyRule> sellRule = groupMap.get(-1);
        List<StockStrategyRule> excludeSellRule = groupMap.get(-2);
        // 数据储备日期
        List<Stock2024> list = stock2024Service.list(Wrappers.<Stock2024>lambdaQuery().select(Stock2024::getDate).groupBy(Stock2024::getDate));
        if (BoolUtil.isEmpty(ConsoleKit.getStrategyId())) {
            return oneDayHandle(TimeUtil.getCurrentDatetime(XTime.DAY_NONE), strategy, buyRule, excludeBuyRule, sellRule, excludeSellRule);
        }
        list.stream().map(Stock2024::getDate).filter(BoolUtil::notEmpty).forEach(date -> oneDayHandle(date, strategy, buyRule, excludeBuyRule, sellRule, excludeSellRule));
        return true;
    }

    private boolean oneDayHandle(String date, StockStrategy strategy, List<StockStrategyRule> buyRule, List<StockStrategyRule> excludeBuyRule, List<StockStrategyRule> sellRule, List<StockStrategyRule> excludeSellRule) {
        // 持仓数据准备
        List<StockStrategyDeal> deals = stockStrategyDealService.list(Wrappers.<StockStrategyDeal>lambdaQuery()
                .in(StockStrategyDeal::getStrategyId, strategy.getId())
                .isNull(StockStrategyDeal::getRiseRatio)
                .orderByAsc(StockStrategyDeal::getDate)
        );
        Map<String, StockStrategyDeal> dealMap = deals.stream().collect(Collectors.toMap(StockStrategyDeal::getCode, Function.identity(), (k1, k2) -> k1));
        // 股票数据准备
        paging(date, strategy, buyRule, excludeBuyRule, sellRule, excludeSellRule, dealMap);
        return true;
    }

    @SneakyThrows
    private void paging(String date, StockStrategy strategy, List<StockStrategyRule> buyRule, List<StockStrategyRule> excludeBuyRule, List<StockStrategyRule> sellRule, List<StockStrategyRule> excludeSellRule, Map<String, StockStrategyDeal> dealMap) {
        // 数据准备
        LambdaQueryWrapper<Stock2024> qw = Wrappers.<Stock2024>lambdaQuery().eq(Stock2024::getDate, date);
        int count = stock2024Service.count(qw);
        int size = 1000;
        int pages = (int) Math.ceil(count / size);
        // 并发处理
        CountDownLatch latch = new CountDownLatch(pages);
        for (int i = 0; i < pages; i++) {
            int current = i + 1;
            ThreadUtil.execute(() -> {
                IPage<Stock2024> page = new Page<>(current, size);
                IPage<Stock2024> iPage = stock2024Service.page(page, qw);
                List<Stock2024> stock2024s = iPage.getRecords();
                if (BoolUtil.isEmpty(stock2024s)) {
                    return;
                }
                // 交易处理
                List<StockStrategyDeal> dealLis = stock2024s.stream().map(
                        x -> ExceptionUtil.sandbox(() -> deal(x, dealMap, strategy, buyRule, excludeBuyRule, sellRule, excludeSellRule))
                ).filter(Objects::nonNull).collect(Collectors.toList());
                stockStrategyDealService.insertOrUpdateBatch(dealLis);
                latch.countDown();
            });
        }
        latch.await();
    }

    private void strategyHandlerOne(StockStrategy strategy, Map<Integer, List<StockStrategyRule>> ruleMap, Map<String, List<String>> subscribeMap, Map<String, Stock> codeMap) {
        if (BoolUtil.isEmpty(ruleMap) || BoolUtil.isEmpty(subscribeMap) || BoolUtil.isEmpty(codeMap) || BoolUtil.isEmpty(ruleMap.get(strategy.getId()))) {
            return;
        }
        List<StockStrategyRule> rules = ruleMap.get(strategy.getId());
        Map<Integer, List<StockStrategyRule>> groupMap = rules.stream().collect(Collectors.groupingBy(StockStrategyRule::getTransType));
        subscribeMap.forEach((k, v) -> ExceptionUtil.sandbox(() -> deal(k, v, strategy, groupMap, codeMap)));
    }

    private void deal(String uid, List<String> subscribes, StockStrategy strategy, Map<Integer, List<StockStrategyRule>> groupMap, Map<String, Stock> codeMap) {
        // 买入规则准备
        List<StockStrategyRule> buyRule = groupMap.get(1);
        List<StockStrategyRule> excludeBuyRule = groupMap.get(2);
        // 卖出规则准备
        List<StockStrategyRule> sellRule = groupMap.get(-1);
        List<StockStrategyRule> excludeSellRule = groupMap.get(-2);
        // 准备飞书消息
        Map map = new HashMap();
        map.put("winRate", "100");
        map.put("profitRate", "100");
        map.put("zjzf", "100");
        map.put("zjly", "100");
        map.put("zjyz", "100");
        map.put("strategyName", String.format("%sv%s", strategy.getName(), strategy.getV()));
        map.put("strategyAuthor", strategy.getAuthor());
        List<Map> list = subscribes.stream().map(code ->
                ExceptionUtil.sandbox(() -> getVariablesMap(strategy, codeMap.get(code), buyRule, excludeBuyRule, sellRule, excludeSellRule))
        ).filter(Objects::nonNull).collect(Collectors.toList());
        map.put("list", list);
        FeiShuSender.send(uid, map);
    }

    private Map getVariablesMap(StockStrategy strategy, Stock stock, List<StockStrategyRule> buyRule, List<StockStrategyRule> excludeBuyRule, List<StockStrategyRule> sellRule, List<StockStrategyRule> excludeSellRule) {
        Map<String, Object> stockMap = ClassUtil.generateCurrentMap(stock);
        CustomVariableKit.add(stock, stockMap);
        // 交易规则 and 关系、(排除)交易规则 or 关系
        boolean buy = isDeal(stockMap, buyRule, excludeBuyRule);
        boolean sell = isDeal(stockMap, sellRule, excludeSellRule);
        Map variableMap = JsonUtil.convert(stock, Map.class);
        // 添加颜色和地址
        variableMap.put("color", buy ? "red" : sell ? "green" : "gray");
        variableMap.put("url", String.format("https://gushitong.baidu.com/stock/ab-%s", stock.getCode()));
        return variableMap;
    }
}

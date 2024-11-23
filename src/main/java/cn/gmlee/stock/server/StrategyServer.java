package cn.gmlee.stock.server;

import cn.gmlee.stock.dao.entity.StockStrategy;
import cn.gmlee.stock.dao.entity.StockStrategyRule;
import cn.gmlee.stock.service.Stock2024Service;
import cn.gmlee.stock.service.StockStrategyDealService;
import cn.gmlee.stock.service.StockStrategyRuleService;
import cn.gmlee.stock.service.StockStrategyService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Strategy server.
 */
@Component
@RequiredArgsConstructor
public class StrategyServer {

    private final Stock2024Service stock2024Service;
    private final StockStrategyService stockStrategyService;
    private final StockStrategyRuleService stockStrategyRuleService;
    private final StockStrategyDealService stockStrategyDealService;

    /**
     * Deal handle.
     */
    public void dealHandle() {
        // 策略数据准备
        List<StockStrategy> list = stockStrategyService.list(Wrappers.<StockStrategy>lambdaUpdate()
                .eq(StockStrategy::getStatus, 1)
        );
        Map<Integer, StockStrategy> strategyMap = list.stream().collect(Collectors.toMap(StockStrategy::getId, Function.identity()));
        List<StockStrategyRule> rules = stockStrategyRuleService.list(Wrappers.<StockStrategyRule>lambdaUpdate()
                .in(StockStrategyRule::getStrategyId, strategyMap.keySet())
        );
        Map<Integer, List<StockStrategyRule>> ruleMap = rules.stream().collect(Collectors.groupingBy(StockStrategyRule::getTransType));
        // 买入数据准备
        List<StockStrategyRule> buyRule = ruleMap.get(1);
        List<StockStrategyRule> excludeBuyRule = ruleMap.get(2);
        // 卖出数据准备
        List<StockStrategyRule> sellRule = ruleMap.get(-1);
        List<StockStrategyRule> excludeSellRule = ruleMap.get(-2);

    }

    /**
     * Deal inform.
     */
    public void dealInform() {

    }
}

package cn.gmlee.stock.util;

import cn.gmlee.stock.dao.entity.StockStats;
import cn.gmlee.stock.dao.entity.StockStrategy;
import cn.gmlee.stock.dao.entity.StockStrategyDeal;
import cn.gmlee.stock.dao.entity.StockStrategyRule;
import cn.gmlee.stock.mod.Deal;
import cn.gmlee.stock.mod.Stock;
import cn.gmlee.tools.base.util.BeanUtil;
import cn.gmlee.tools.base.util.QuickUtil;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Deal kit.
 */
public class DealKit {

    /**
     * To stock deal strategy deal vo.
     *
     * @param strategy     the strategy
     * @param ruleMap      the rule map
     * @param dealsMap     the deals map
     * @param soldStatsMap the sold stats map
     * @param lockStatsMap the lock stats map
     * @param stock        the stock
     * @return the strategy deal vo
     */
    public static Deal toDeal(StockStrategy strategy,
                              Map<Integer, List<StockStrategyRule>> ruleMap,
                              Map<Integer, List<StockStrategyDeal>> dealsMap,
                              Map<Integer, StockStats> soldStatsMap,
                              Map<Integer, StockStats> lockStatsMap,
                              Stock stock) {
        Deal vo = BeanUtil.convert(strategy, Deal.class);
        vo.setStock(stock);
        // 策略规则准备
        List<StockStrategyRule> rules = ruleMap.get(strategy.getId());
        Map<Integer, List<StockStrategyRule>> groupMap = rules.stream().collect(Collectors.groupingBy(StockStrategyRule::getTransType));
        QuickUtil.notEmpty(soldStatsMap, map -> vo.setSoldStats(map.get(strategy.getId())));
        QuickUtil.notEmpty(lockStatsMap, map -> vo.setLockStats(map.get(strategy.getId())));
        // 交易信号准备
        List<StockStrategyDeal> deals = dealsMap.get(strategy.getId());
        Map<String, StockStrategyDeal> dealMap = deals.stream().collect(Collectors.toMap(StockStrategyDeal::getCode, Function.identity(), (k1, k2) -> k1));
        vo.setSold(SoldKit.sold(stock, dealMap, groupMap));
        QuickUtil.notNull(vo.getSold(), x -> vo.setSoldCn(x ? "买入" : "卖出"));
        return vo;
    }
}

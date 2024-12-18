package cn.gmlee.stock.util;

import cn.gmlee.stock.dao.entity.StockStrategyDeal;
import cn.gmlee.stock.dao.entity.StockStrategyRule;
import cn.gmlee.stock.mod.Stock;
import cn.gmlee.tools.base.util.BoolUtil;
import cn.gmlee.tools.base.util.ClassUtil;
import cn.gmlee.tools.base.util.NullUtil;
import cn.gmlee.tools.base.util.ScriptUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The type Sold kit.
 */
public class SoldKit {

    /**
     * Sold boolean.
     *
     * @param stock                 the stock
     * @param dealMap               the deal map
     * @param sellMap
     * @param groupTransTypeRuleMap the group trans type rule map
     * @return the boolean
     */
    public static Boolean sold(Stock stock, Map<String, StockStrategyDeal> dealMap, Map<String, StockStrategyDeal> sellMap, Map<Integer, List<StockStrategyRule>> groupTransTypeRuleMap) {
        Map<String, Object> stockMap = ClassUtil.generateCurrentMap(stock);
        CustomVariableKit.add(stock, stockMap); // 添加自定义变量
        StockStrategyDeal deal = dealMap.get(stock.getCode());// 可能没有持仓: null
        CustomVariableKit.add(deal, stockMap);// 添加自定义变量
        // 买入规则准备
        List<StockStrategyRule> buyRule = groupTransTypeRuleMap.get(1);
        List<StockStrategyRule> excludeBuyRule = groupTransTypeRuleMap.get(2);
        // 卖出规则准备
        List<StockStrategyRule> sellRule = groupTransTypeRuleMap.get(-1);
        List<StockStrategyRule> excludeSellRule = groupTransTypeRuleMap.get(-2);
        // 止盈止损准备
        List<StockStrategyRule> stopRule = groupTransTypeRuleMap.get(3);
        List<StockStrategyRule> excludeStopRule = groupTransTypeRuleMap.get(-3);
        boolean buy = isDeal(stockMap, buyRule, excludeBuyRule);
        boolean sell = isDeal(stockMap, sellRule, excludeSellRule);
        boolean stop = isDeal(stockMap, stopRule, excludeStopRule);
        if (deal == null && buy) {
            return false;// 买入
        }
        if (sellMap.containsKey(stock.getCode()) || (deal != null && (sell || stop))) {
            return true;// 卖出
        }
        return null;
    }

    private static boolean isDeal(Map<String, Object> stockMap, List<StockStrategyRule> rules, List<StockStrategyRule> excludeRules) {
        if (BoolUtil.isEmpty(rules)) {
            return false;
        }
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

    private static String substitutionVariable(String rule, Map<String, Object> map) {
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

}

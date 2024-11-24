package cn.gmlee.stock.util;

import cn.gmlee.stock.mod.Stock;
import cn.gmlee.tools.base.util.BigDecimalUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * The type Custom variable kit.
 */
public class CustomVariableKit {

    /**
     * Add.
     *
     * @param stock    the stock
     * @param stockMap the stock map
     */
    public static void add(Stock stock, Map<String, Object> stockMap) {
        if (stock == null || stockMap == null) {
            return;
        }
        // 添加实心
        addSxl(stock, stockMap);
    }

    private static void addSxl(Stock stock, Map<String, Object> stockMap) {
        // 实心率 = |当前 - 今开 / 最高 - 最低|
        BigDecimal zc = BigDecimalUtil.subtract(stock.getCurrentPrice(), stock.getOpenPrice()); // 柱长
        BigDecimal xc = BigDecimalUtil.subtract(stock.getHighestPrice(), stock.getLowestPrice()); // 线长
        BigDecimal sx = BigDecimalUtil.divide(zc, xc); // 实长
        BigDecimal result = BigDecimalUtil.multiply(sx, 100).abs();
        stockMap.put("实心", result.setScale(2, RoundingMode.HALF_UP));
    }
}

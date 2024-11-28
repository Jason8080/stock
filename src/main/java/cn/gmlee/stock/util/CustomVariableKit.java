package cn.gmlee.stock.util;

import cn.gmlee.stock.mod.Stock;
import cn.gmlee.tools.base.util.BigDecimalUtil;
import cn.gmlee.tools.base.util.BoolUtil;

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
        // 添加离天
        addLtl(stock, stockMap);
    }

    private static void addLtl(Stock stock, Map<String, Object> stockMap) {
        // 离天率 = 最高 - (今开 | 价格) / 最高 - 最低
        BigDecimal openPrice = BigDecimalUtil.get(stock.getOpenPrice());
        BigDecimal highestPrice = BigDecimalUtil.get(stock.getHighestPrice());
        BigDecimal price = BigDecimalUtil.get(stock.getCurrentPrice());
        double min = Math.min(openPrice.doubleValue(), price.doubleValue()); // 最小
        double max = Math.max(openPrice.doubleValue(), price.doubleValue()); // 最大
        boolean ge = BoolUtil.gt(price, openPrice); // 是否红线
        BigDecimal xc = BigDecimalUtil.subtract(stock.getHighestPrice(), stock.getLowestPrice()); // 线长
        BigDecimal sx = BigDecimalUtil.divide(ge ? BigDecimalUtil.subtract(highestPrice, min) : BigDecimalUtil.subtract(highestPrice, max), xc); // 距离
        BigDecimal result = BigDecimalUtil.multiply(sx, 100).abs();
        stockMap.put("离天", result.setScale(2, RoundingMode.HALF_UP));
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

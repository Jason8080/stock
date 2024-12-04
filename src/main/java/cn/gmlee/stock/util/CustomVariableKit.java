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
        // 添加委率
        addWbl(stock, stockMap);
        // 添加开幅
        addKfl(stock, stockMap);
        // 添加离天
        addLtl(stock, stockMap);
        // 添加离地
        addLdl(stock, stockMap);
        // 添加年位
        addNwz(stock, stockMap);
    }

    private static void addWbl(Stock stock, Map<String, Object> stockMap) {
        // 委比率 = 买盘 - 买盘 / 买盘 + 卖盘
        BigDecimal buyVolume = BigDecimalUtil.get(stock.getBuyVolume());
        BigDecimal sellVolume = BigDecimalUtil.get(stock.getSellVolume());
        BigDecimal jl = BigDecimalUtil.subtract(buyVolume, sellVolume); // 距离
        BigDecimal xc = BigDecimalUtil.add(buyVolume, buyVolume); // 线长
        BigDecimal result = BigDecimalUtil.multiply(BigDecimalUtil.divide(jl, xc), 100);
        stockMap.put("委率", result.setScale(2, RoundingMode.HALF_UP));
    }

    private static void addKfl(Stock stock, Map<String, Object> stockMap) {
        // 开幅率 = 今开 - 昨收 / 涨停 - 昨收
        BigDecimal openPrice = BigDecimalUtil.get(stock.getOpenPrice());
        BigDecimal previousClose = BigDecimalUtil.get(stock.getPreviousClose());
        BigDecimal upperPrice = BigDecimalUtil.get(stock.getUpperPrice());
        BigDecimal jl = BigDecimalUtil.subtract(openPrice, previousClose); // 距离
        BigDecimal xc = BigDecimalUtil.subtract(upperPrice, previousClose); // 线长
        BigDecimal result = BigDecimalUtil.multiply(BigDecimalUtil.divide(jl, xc), 100);
        stockMap.put("开幅", result.setScale(2, RoundingMode.HALF_UP));
    }

    private static void addNwz(Stock stock, Map<String, Object> stockMap) {
        // 年位置 = 价格 - 年底 / 年 高 - 年底
        BigDecimal currentPrice = BigDecimalUtil.get(stock.getCurrentPrice());
        BigDecimal topYear = BigDecimalUtil.get(stock.getTopYear());
        BigDecimal bottomYear = BigDecimalUtil.get(stock.getBottomYear());
        BigDecimal jl = BigDecimalUtil.subtract(currentPrice, bottomYear); // 距离
        BigDecimal xc = BigDecimalUtil.subtract(topYear, bottomYear); // 线长
        BigDecimal result = BigDecimalUtil.multiply(BigDecimalUtil.divide(jl, xc), 100).abs();
        stockMap.put("年位", result.setScale(2, RoundingMode.HALF_UP));
    }

    private static void addLdl(Stock stock, Map<String, Object> stockMap) {
        // 离地率 = 最低 - (今开 | 价格) / 最高 - 最低
        BigDecimal openPrice = BigDecimalUtil.get(stock.getOpenPrice());
        BigDecimal highestPrice = BigDecimalUtil.get(stock.getHighestPrice());
        BigDecimal lowestPrice = BigDecimalUtil.get(stock.getLowestPrice());
        BigDecimal price = BigDecimalUtil.get(stock.getCurrentPrice());
        BigDecimal xc = BigDecimalUtil.subtract(highestPrice, lowestPrice); // 线长
        double max = Math.max(openPrice.doubleValue(), price.doubleValue()); // 最小
        BigDecimal jl = BigDecimalUtil.divide(BigDecimalUtil.subtract(lowestPrice, max), xc); // 距离
        BigDecimal result = BigDecimalUtil.multiply(jl, 100).abs();
        stockMap.put("离地", result.setScale(2, RoundingMode.HALF_UP));
    }

    private static void addLtl(Stock stock, Map<String, Object> stockMap) {
        // 离天率 = 最高 - (今开 | 价格) / 最高 - 最低
        BigDecimal openPrice = BigDecimalUtil.get(stock.getOpenPrice());
        BigDecimal highestPrice = BigDecimalUtil.get(stock.getHighestPrice());
        BigDecimal lowestPrice = BigDecimalUtil.get(stock.getLowestPrice());
        BigDecimal price = BigDecimalUtil.get(stock.getCurrentPrice());
        BigDecimal xc = BigDecimalUtil.subtract(highestPrice, lowestPrice); // 线长
        double min = Math.min(openPrice.doubleValue(), price.doubleValue()); // 最小
        BigDecimal jl = BigDecimalUtil.divide(BigDecimalUtil.subtract(highestPrice, min), xc); // 距离
        BigDecimal result = BigDecimalUtil.multiply(jl, 100).abs();
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

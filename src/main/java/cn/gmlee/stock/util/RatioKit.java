package cn.gmlee.stock.util;

import cn.gmlee.tools.base.util.BigDecimalUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * The type Ratio kit.
 */
public class RatioKit {
    /**
     * Calculate big decimal.
     *
     * @param oldPrice the old price
     * @param newPrice the new price
     * @return the big decimal
     */
    public static BigDecimal calculate(Object oldPrice, Object newPrice) {
        BigDecimal subtract = BigDecimalUtil.subtract(newPrice, oldPrice);
        BigDecimal divide = BigDecimalUtil.divide(subtract, oldPrice);
        BigDecimal result = BigDecimalUtil.multiply(divide, 100).abs();
        return result.setScale(2, RoundingMode.HALF_UP);
    }
}

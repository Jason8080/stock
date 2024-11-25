package cn.gmlee.stock.util;

import cn.gmlee.stock.dao.entity.StockList;
import cn.gmlee.tools.base.util.BoolUtil;

/**
 * The type Market kit.
 */
public class MarketKit {

    /**
     * Gets market.
     *
     * @param code the code
     * @return the market
     */
    public static String getMarket(String code) {
        if (code.startsWith("60") || code.startsWith("68")) {
            return "sh";
        }
        if (code.startsWith("00") || code.startsWith("30")) {
            return "sz";
        }
        if (code.startsWith("43") || code.startsWith("82") || code.startsWith("83") || code.startsWith("87") || code.startsWith("88")) {
            return "bj";
        }
        return "";
    }

    /**
     * Add market string.
     *
     * @param code the code
     * @return the string
     */
    public static String addMarket(String code) {
        if (BoolUtil.isEmpty(code)) {
            return null;
        }
        return getMarket(code).concat(code);
    }

    /**
     * New stock list stock list.
     *
     * @param code the code
     * @return the stock list
     */
    public static StockList newStockList(String code) {
        StockList stockList = new StockList();
        stockList.setCode(code);
        stockList.setMarket(getMarket(code));
        return stockList;
    }
}

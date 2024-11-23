package cn.gmlee.stock.util;

import cn.gmlee.stock.dao.entity.StockList;
import cn.gmlee.stock.mod.Stock;
import cn.gmlee.tools.base.mod.HttpResult;
import cn.gmlee.tools.base.util.AssertUtil;
import cn.gmlee.tools.base.util.BoolUtil;
import cn.gmlee.tools.base.util.HttpUtil;

import java.util.Collections;
import java.util.List;

/**
 * The type Tencent kit.
 */
public class TencentKit {

    private static final String getMarketApi = "https://qt.gtimg.cn/q=";

    /**
     * Get stocks list.
     *
     * @param stockLists the stock lists
     * @return the list
     */
    public static List<Stock> getStocks(List<StockList> stockLists) {
        String[] codes = stockLists.stream().map(x -> x.getMarket() + x.getCode()).toArray(String[]::new);
        return getStocks(codes);
    }

    /**
     * Get stocks list.
     *
     * @param codes the codes
     * @return the list
     */
    public static List<Stock> getStocks(String... codes){
        if (BoolUtil.isEmpty(codes)){
            return Collections.emptyList();
        }
        HttpResult httpResult = HttpUtil.get(getMarketApi.concat(String.join(",", codes)));
        String response = httpResult.byteResponseBody2String("GBK");
        AssertUtil.isTrue(httpResult.isOk(), String.format("腾讯行情接口异常: %s", response));
        return TencentParser.parse(response);
    }
}

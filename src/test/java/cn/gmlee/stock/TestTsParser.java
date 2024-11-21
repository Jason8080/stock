package cn.gmlee.stock;

import cn.gmlee.stock.mod.Stock;
import cn.gmlee.stock.util.TencentStockParser;
import cn.gmlee.tools.base.mod.HttpResult;
import cn.gmlee.tools.base.util.ClassUtil;
import cn.gmlee.tools.base.util.HttpUtil;

import java.util.List;

/**
 * 股票接口.
 */
public class TestTsParser {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        HttpResult httpResult = HttpUtil.get("https://qt.gtimg.cn/q=sz300059");
        String response = httpResult.byteResponseBody2String("GBK");
        List<Stock> stockList = TencentStockParser.parse(response);
        for (Stock stock : stockList) {
            System.out.println(ClassUtil.generateCurrentMap(stock));
        }
    }
}

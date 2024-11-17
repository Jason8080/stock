package cn.gmlee.stock;

import cn.gmlee.stock.util.TencentStockParser;
import cn.gmlee.tools.base.mod.HttpResult;
import cn.gmlee.tools.base.util.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.List;

public class App {
    public static void main(String[] args) {
        HttpResult httpResult = HttpUtil.get("https://qt.gtimg.cn/q=sz300059,sz002008");
        String response = httpResult.byteResponseBody2String("GBK");
        System.out.println(response);
        List<TencentStockParser.StockInfo> stockList = TencentStockParser.parse(response);
        for (TencentStockParser.StockInfo stock : stockList) {
            System.out.println("股票代码: " + stock.getCode());
            System.out.println("股票名称: " + stock.getName());
            System.out.println("当前价格: " + stock.getCurrentPrice());
            System.out.println("昨日收盘价: " + stock.getPreviousClose());
            System.out.println("今日开盘价: " + stock.getOpenPrice());
            System.out.println("最高价: " + stock.getHighestPrice());
            System.out.println("最低价: " + stock.getLowestPrice());
            System.out.println("市盈率(动态): " + stock.getPeRatio());
            System.out.println("市净率: " + stock.getPbRatio());
            System.out.println("振幅: " + stock.getAmplitude());
            System.out.println("量比: " + stock.getVolumeRatio());
            System.out.println("换手率: " + stock.getTurnoverRate());
            System.out.println("涨速: " + stock.getPriceChangeSpeed());
            System.out.println("时间: " + (stock.getTimestamp() != null
                    ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(stock.getTimestamp())
                    : "无效时间"));
            System.out.println("----");
        }
    }
}

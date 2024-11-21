package cn.gmlee.stock;

import cn.gmlee.stock.util.TencentStockParser;
import cn.gmlee.tools.base.enums.XTime;
import cn.gmlee.tools.base.mod.HttpResult;
import cn.gmlee.tools.base.util.HttpUtil;
import cn.gmlee.tools.base.util.TimeUtil;

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
        HttpResult httpResult = HttpUtil.get("https://qt.gtimg.cn/q=sz300059,sz002008");
        String response = httpResult.byteResponseBody2String("GBK");
        List<TencentStockParser.StockInfo> stockList = TencentStockParser.parse(response);
        for (TencentStockParser.StockInfo stock : stockList) {
            System.out.println("代码: " + stock.getCode());
            System.out.println("名称: " + stock.getName());
            System.out.println("价格: " + stock.getCurrentPrice());
            System.out.println("昨收: " + stock.getPreviousClose());
            System.out.println("今开: " + stock.getOpenPrice());
            System.out.println("最高: " + stock.getHighestPrice());
            System.out.println("最低: " + stock.getLowestPrice());
            System.out.println("均价: " + stock.getAvgPrice());
            System.out.println("振幅: " + stock.getAmplitude());
            System.out.println("涨停: " + stock.getUpperPrice());
            System.out.println("跌停: " + stock.getLowerPrice());
            System.out.println("量比: " + stock.getVolumeRatio());
            System.out.println("换手: " + stock.getTurnoverRate());
            System.out.println("涨速: " + stock.getPriceChangeSpeed());
            System.out.println("成量: " + stock.getVolume());
            System.out.println("成额: " + stock.getTurnover());
            System.out.println("卖盘: " + stock.getSellVolume());
            System.out.println("买盘: " + stock.getBuyVolume());
            System.out.println("委比: " + stock.getTcRatio());
            System.out.println("市盈: " + stock.getPeRatio());
            System.out.println("市净: " + stock.getPbRatio());
            System.out.println("股息: " + stock.getDyRatio());
            System.out.println("市值: " + stock.getMcTotal());
            System.out.println("流通: " + stock.getMcCirculate());
            System.out.println("年顶: " + stock.getTopYear());
            System.out.println("年底: " + stock.getBottomYear());
            System.out.println("时间: " + TimeUtil.format(stock.getTimestamp(), XTime.SECOND_MINUS_BLANK_COLON));
        }
    }
}

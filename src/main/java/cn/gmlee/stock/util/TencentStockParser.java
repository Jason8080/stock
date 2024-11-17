package cn.gmlee.stock.util;

import cn.gmlee.tools.base.util.TimeUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * https://qt.gtimg.cn/q=sz300059,sz002008
 * v_sz300059="51~东方财富~300059~25.63~27.41~27.34~11396456~5312319~6084137~25.63~68713~25.62~25790~25.61~7568~25.60~18653~25.59~4616~25.64~2321~25.65~5152~25.66~3310~25.67~1843~25.68~6329~~20241115161439~-1.78~-6.49~27.55~25.59~25.63/11396456/30195067880~11396456~3019507~8.53~50.41~~27.55~25.59~7.15~3423.98~4045.83~5.26~32.89~21.93~1.02~106385~26.50~50.22~49.38~~~1.95~3019506.7880~782.8607~3054~ A~GP-A-CYB~83.07~-9.24~0.16~10.43~2.46~31.00~9.87~11.43~11.68~140.66~13359278683~15785542475~73.73~92.71~13359278683~~~68.07~-0.50~~CNY~0~~25.58~5377"; v_sz002008="51~大族激光~002008~28.20~26.68~26.59~1315257~675234~640023~28.20~2940~28.19~476~28.18~2663~28.17~109~28.16~111~28.21~249~28.22~621~28.23~670~28.24~418~28.25~974~~20241115161403~1.52~5.70~29.35~26.48~28.20/1315257/3742572101~1315257~374257~13.43~18.43~~29.35~26.48~10.76~276.16~296.72~1.87~29.35~24.01~2.19~3367~28.46~15.60~36.18~~~1.42~374257.2101~0.0000~0~ ~GP-A~37.41~3.45~0.70~10.13~4.99~29.35~14.25~12.85~17.60~40.79~979283962~1052193000~36.47~57.61~979283962~~~25.21~-0.28~~CNY~0~~28.30~-1248";
 */
public class TencentStockParser {

    @Data
    public static class StockInfo {
        private String name;           // 股票名称
        private String code;           // 股票代码
        private double currentPrice;   // 当前价格
        private double previousClose;  // 昨日收盘价
        private double openPrice;      // 今日开盘价
        private double highestPrice;   // 最高价
        private double lowestPrice;    // 最低价
        private long volume;           // 成交量（手）
        private long turnover;         // 成交额
        private double peRatio;        // 市盈率（动态）
        private double pbRatio;        // 市净率
        private double amplitude;      // 振幅
        private double volumeRatio;    // 量比
        private double turnoverRate;   // 换手率
        private double priceChangeSpeed; // 涨速
        private Date timestamp;        // 时间戳
    }

    public static List<StockInfo> parse(String response) {
        List<StockInfo> stockList = new ArrayList<>();
        String[] entries = response.split(";");
        for (String entry : entries) {
            if (!entry.contains("=")) continue;

            String data = entry.split("=")[1].replace("\"", "");
            String[] fields = data.split("~");

            StockInfo stock = new StockInfo();
            stock.setName(fields[1]);
            stock.setCode(fields[2]);
            stock.setCurrentPrice(Double.parseDouble(fields[3]));
            stock.setPreviousClose(Double.parseDouble(fields[4]));
            stock.setOpenPrice(Double.parseDouble(fields[5]));
            stock.setVolume(Long.parseLong(fields[6]));
            stock.setTurnover(Long.parseLong(fields[37]));
            stock.setHighestPrice(Double.parseDouble(fields[32]));
            stock.setLowestPrice(Double.parseDouble(fields[33]));
            stock.setPeRatio(Double.parseDouble(fields[39]));     // 市盈率
            stock.setPbRatio(Double.parseDouble(fields[46]));     // 市净率
            stock.setAmplitude(Double.parseDouble(fields[43]));   // 振幅
            stock.setVolumeRatio(Double.parseDouble(fields[7]));  // 量比
            stock.setTurnoverRate(Double.parseDouble(fields[38]));// 换手率
            stock.setPriceChangeSpeed(Double.parseDouble(fields[41])); // 涨速
            stock.setTimestamp(TimeUtil.parseTime(fields[30]));

            stockList.add(stock);
        }
        return stockList;
    }
}

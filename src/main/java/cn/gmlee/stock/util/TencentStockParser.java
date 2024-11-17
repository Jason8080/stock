package cn.gmlee.stock.util;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            stock.setTimestamp(parseTimestamp(fields[30]));

            stockList.add(stock);
        }
        return stockList;
    }

    private static Date parseTimestamp(String timestamp) {
        try {
            return new SimpleDateFormat("yyyyMMddHHmmss").parse(timestamp);
        } catch (Exception e) {
            return null;
        }
    }
}

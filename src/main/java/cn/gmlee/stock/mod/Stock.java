package cn.gmlee.stock.mod;

import lombok.Data;

@Data
public class Stock {
    private String name;           // 名称
    private String code;           // 代码
    private double currentPrice;   // 价格
    private double previousClose;  // 昨收
    private double openPrice;      // 今开
    private double highestPrice;   // 最高
    private double lowestPrice;    // 最低
    private double avgPrice;       // 均价
    private double upperPrice;     // 涨停
    private double lowerPrice;     // 跌停
    private long volume;           // 成交
    private long turnover;         // 成交
    private long sellVolume;       // 卖盘
    private long buyVolume;        // 买盘
    private double tcRatio;        // 委比
    private double peRatio;        // 市盈
    private double pbRatio;        // 市净
    private double dyRatio;        // 股息
    private double amplitude;      // 振幅
    private double volumeRatio;    // 量比
    private double turnoverRate;   // 换手
    private double riseRate;       // 涨速
    private double mcTotal;         // 市值
    private double mcCirculate;     // 流通
    private double topYear;         // 年高
    private double bottomYear;      // 年低
    private String timestamp;         // 时间
}
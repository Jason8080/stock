package cn.gmlee.stock.mod;

import cn.gmlee.tools.base.anno.Column;
import lombok.Data;


@Data
public class Stock {
    @Column("代码")
    
    private String code;
    @Column("名称")
    
    private String name;
    @Column("价格")
    
    private String currentPrice;
    @Column("昨收")
    
    private String previousClose;
    @Column("今开")
    
    private String openPrice;
    @Column("最高")
    
    private String highestPrice;
    @Column("最低")
    
    private String lowestPrice;
    @Column("均价")
    
    private String avgPrice;
    @Column("涨停")
    
    private String upperPrice;
    @Column("跌停")
    
    private String lowerPrice;
    @Column("成量")
    
    private String volume;
    @Column("成额")
    
    private String turnover;
    @Column("卖盘")
    
    private String sellVolume;
    @Column("买盘")
    
    private String buyVolume;
    @Column("委比")
    
    private String tcRatio;
    @Column("市盈")
    
    private String peRatio;
    @Column("市净")
    
    private String pbRatio;
    @Column("股息")
    
    private String dyRatio;
    @Column("振幅")
    
    private String amplitude;
    @Column("量比")
    
    private String volumeRatio;
    @Column("换手")
    
    private String turnoverRate;
    @Column("涨速")
    
    private String riseRate;
    @Column("市值")
    
    private String mcTotal;
    @Column("流通")
    
    private String mcCirculate;
    @Column("年高")
    
    private String topYear;
    @Column("年低")
    
    private String bottomYear;
    @Column("时间")

    private String timestamp;
}
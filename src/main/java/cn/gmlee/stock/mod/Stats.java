package cn.gmlee.stock.mod;

import cn.gmlee.tools.base.anno.Column;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Stats {
    private Integer strategyId;

    @Column("策略")
    private String strategyName;
    @Column("总量(只)")
    private Long total;

    @Column("收益(%)")
    private BigDecimal profit;
    @Column("盈量(只)")
    private BigDecimal profitTotal;
    @Column("胜率(%)")
    private BigDecimal profitRatio;
}

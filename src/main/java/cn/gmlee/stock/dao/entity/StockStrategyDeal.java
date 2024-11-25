package cn.gmlee.stock.dao.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Jas°
 * @since 2024-11-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("stock_strategy_deal")
@KeySequence("SEQ_STOCK_STRATEGY_DEAL")
public class StockStrategyDeal implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 买入日期
     */
    @IsKey
    @TableId(type = IdType.INPUT)
    private String date;

    @IsKey
    @TableField
    private String code;

    @Column
    private String name;

    @Column
    private BigDecimal price;

    @Column
    private BigDecimal sellPrice;

    /**
     * 卖出日期
     */
    @Column
    private String sellDate;

    @Column
    private BigDecimal riseRatio;

    @IsKey
    @TableField
    private Integer strategyId;
}

package cn.gmlee.stock.dao.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
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
    @TableId(type = IdType.INPUT)
    private String date;

    @TableField
    private String code;

    private String name;

    private BigDecimal price;

    private BigDecimal sellPrice;

    /**
     * 卖出日期
     */
    private Date sellDate;

    private Integer strategyId;


}

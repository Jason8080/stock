package cn.gmlee.stock.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

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
@TableName("stock_strategy_lock")
@KeySequence("SEQ_STOCK_STRATEGY_LOCK")
public class StockStrategyLock implements Serializable {

    private static final long serialVersionUID = 1L;

    @IsKey
    @TableId(type = IdType.INPUT)
    private String date;

    @Column
    private Long total;

    @Column
    private BigDecimal profit;
    @Column
    private Long profitTotal;
    @Column
    private BigDecimal profitRatio;

    @Column
    private BigDecimal lock;
    @Column
    private Long lockTotal;
    @Column
    private BigDecimal lockRatio;

    @IsKey
    @Column
    private Integer strategyId;

    @Column
    private String strategyName;
}

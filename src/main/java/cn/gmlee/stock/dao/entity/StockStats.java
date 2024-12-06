package cn.gmlee.stock.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Jas°
 * @since 2024-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("stock_stats")
@KeySequence("SEQ_STOCK_STATS")
public class StockStats implements Serializable {

    private static final long serialVersionUID = 1L;

    @IsKey
    @TableId(type = IdType.INPUT)
    private Date date;

    @IsKey
    @TableField
    private Integer strategyId;

    @IsKey
    @TableField
    private Boolean sold;

    @Column(comment = "总量(只)")
    private Long total;

    @Column(comment = "数量(只)")
    private Long qty;

    @Column(comment = "盈数(只)")
    private Long profitNum;

    @Column(comment = "亏数(只)")
    private Long lossNum;

    @Column(comment = "占比(%)")
    private BigDecimal proportion;

    @Column(comment = "盈亏(%)")
    private BigDecimal rate;

    @Column(comment = "均盈(%)")
    private BigDecimal avgRate;

    @Column(comment = "胜率(%)")
    private BigDecimal winRate;

    @Column(comment = "败率(%)")
    private BigDecimal lossRate;

}

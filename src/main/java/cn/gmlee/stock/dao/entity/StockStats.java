package cn.gmlee.stock.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private Integer sold;

    @Column(comment = "策略")
    private String strategyName;

    @Column(comment = "总数(只)")
    private Long total;

    @Column(comment = "占比(%)")
    private BigDecimal proportion;

    @Column(comment = "盈亏(%)")
    private BigDecimal rate;

    @Column(comment = "盈数(只)")
    private Long num;

    @Column(comment = "胜率(%)")
    private BigDecimal winRate;

}

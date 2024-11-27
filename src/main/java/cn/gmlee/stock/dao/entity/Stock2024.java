package cn.gmlee.stock.dao.entity;

import cn.gmlee.tools.base.enums.XTime;
import cn.gmlee.tools.base.util.BoolUtil;
import cn.gmlee.tools.base.util.TimeUtil;
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
@TableName("stock_2024")
@KeySequence("SEQ_STOCK")
public class Stock2024 implements Serializable {

    private static final long serialVersionUID = 1L;

    @IsKey
    @TableId(type = IdType.INPUT)
    private String date;

    @IsKey
    @TableField
    private String code;

    @Column
    private String name;

    @Column
    private BigDecimal currentPrice;

    @Column
    private BigDecimal previousClose;

    @Column
    private BigDecimal openPrice;

    @Column
    private BigDecimal highestPrice;

    @Column
    private BigDecimal lowestPrice;

    @Column
    private BigDecimal avgPrice;

    @Column
    private BigDecimal riseRatio;

    @Column
    private BigDecimal upperPrice;

    @Column
    private BigDecimal lowerPrice;

    @Column
    private BigDecimal volume;

    @Column
    private BigDecimal turnover;

    @Column
    private BigDecimal sellVolume;

    @Column
    private BigDecimal buyVolume;

    @Column
    private BigDecimal tcRatio;

    @Column
    private BigDecimal peRatio;

    @Column
    private BigDecimal pbRatio;

    @Column
    private BigDecimal dyRatio;

    @Column
    private BigDecimal amplitude;

    @Column
    private BigDecimal volumeRatio;

    @Column
    private BigDecimal turnoverRate;

    @Column
    private BigDecimal riseRate;

    @Column
    private BigDecimal mcTotal;

    @Column
    private BigDecimal mcCirculate;

    @Column
    private BigDecimal topYear;

    @Column
    private BigDecimal bottomYear;

    @Column
    private Date timestamp;

    public String getDate() {
        if(BoolUtil.notEmpty(date)){
            return date;
        }
        if(timestamp == null){
            return null;
        }
        return TimeUtil.format(timestamp, XTime.DAY_NONE);
    }
}

package cn.gmlee.stock.dao.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2024-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("stock_2024")
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    private String date;

    private String code;

    private String name;

    private BigDecimal currentPrice;

    private BigDecimal previousClose;

    private BigDecimal openPrice;

    private BigDecimal highestPrice;

    private BigDecimal lowestPrice;

    private BigDecimal avgPrice;

    private BigDecimal upperPrice;

    private BigDecimal lowerPrice;

    private BigDecimal volume;

    private BigDecimal turnover;

    private BigDecimal sellVolume;

    private BigDecimal buyVolume;

    private BigDecimal tcRatio;

    private BigDecimal peRatio;

    private BigDecimal pbRatio;

    private BigDecimal dyRatio;

    private BigDecimal amplitude;

    private BigDecimal volumeRatio;

    private BigDecimal turnoverRate;

    private BigDecimal riseRate;

    private BigDecimal mcTotal;

    private BigDecimal mcCirculate;

    private BigDecimal topYear;

    private BigDecimal bottomYear;

    private Date timestamp;


}

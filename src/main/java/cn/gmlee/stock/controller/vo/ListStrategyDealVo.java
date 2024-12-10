package cn.gmlee.stock.controller.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author Jas°
 * @since 2024-11-23
 */
@Data
public class ListStrategyDealVo implements Serializable {

    private String date;

    private String code;

    private String name;

    private BigDecimal price;

    private BigDecimal currentPrice;

    private String currentDate;

    private BigDecimal diffPrice;

    private BigDecimal riseRatio;

    private Integer days;

    private Boolean sold;

    private Integer strategyId;
}

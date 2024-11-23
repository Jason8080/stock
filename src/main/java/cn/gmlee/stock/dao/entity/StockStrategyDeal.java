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
@TableName("stock_strategy_deal")
public class StockStrategyDeal implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer strategyId;

    private String code;

    private String name;

    private BigDecimal buyPrice;

    private Date buyDate;

    private BigDecimal sellPrice;

    private Date sellDate;


}

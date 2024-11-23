package cn.gmlee.stock.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("stock_strategy_rule")
public class StockStrategyRule implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer strategyId;

    /**
     * -2(排除)卖出 -1卖出 0观望 1买入 2(排除)买入
     */
    private Integer transType;

    private String rule;

    private Boolean status;


}

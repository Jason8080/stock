package cn.gmlee.stock.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2024-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("stock_strategy_rule")
public class StockStrategyRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @IsKey
    private Long id;

    @Column
    private Integer strategyId;

    /**
     * -2(排除)卖出 -1卖出 0观望 1买入 2(排除)买入
     */
    @Column
    private Integer transType;

    @Column
    private String rule;

    @Column
    private Boolean status;


}

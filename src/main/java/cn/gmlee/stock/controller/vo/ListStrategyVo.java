package cn.gmlee.stock.controller.vo;

import cn.gmlee.stock.dao.entity.StockStats;
import lombok.Data;

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
public class ListStrategyVo implements Serializable {

    private Integer id;

    private String name;

    private String author;

    private Integer v;

    private String remark;

    private Integer status;

    private Date createdAt;

    private Props props;

    @Data
    public static class Props {
        private Long total;// 总量
        private Long sold;// 卖出
        private Long lock;// 持仓
        private BigDecimal rate;// 涨幅
        private StockStats soldStats;// 卖出统计
        private StockStats lockStats;// 持仓统计
    }
}

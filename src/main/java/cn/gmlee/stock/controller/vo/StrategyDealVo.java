package cn.gmlee.stock.controller.vo;

import cn.gmlee.stock.dao.entity.StockStats;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Jas°
 * @since 2024-11-23
 */
@Data
public class StrategyDealVo implements Serializable {
    private Integer id;
    private String name;
    private String author;
    private Integer v;
    private String sold = "观望"; // 买入、卖出
    private StockStats soldStats = new StockStats();// 卖出统计
    private StockStats lockStats = new StockStats();// 持仓统计
}
